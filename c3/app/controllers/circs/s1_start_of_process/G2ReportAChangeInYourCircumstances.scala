package controllers.circs.s1_start_of_process

import app.{ReportChange => r}
import controllers.CarersForms._
import controllers.mappings.Mappings._
import controllers.mappings.NINOMappings._
import models.domain.EMail._
import models.domain._
import models.view.{CachedChangeOfCircs, Navigable}
import play.api.Play._
import play.api.data.{FormError, Form}
import play.api.data.Forms._
import play.api.data.validation.Constraints
import play.api.mvc.{Call, Controller}
import utils.helpers.CarersForm._
import play.api.i18n.{MessagesApi, I18nSupport, MMessages}

import scala.annotation.tailrec
import scala.collection.immutable.Stack
import scala.language.reflectiveCalls
import scala.language.postfixOps

object G2ReportAChangeInYourCircumstances extends Controller with CachedChangeOfCircs with Navigable with I18nSupport {
  override val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]

  val fullName = "fullName"
  val nationalInsuranceNumber = "nationalInsuranceNumber"
  val dateOfBirth = "dateOfBirth"
  val theirFullName = "theirFullName"
  val theirRelationshipToYou = "theirRelationshipToYou"

  val form = Form(mapping(
    fullName -> carersNonEmptyText(maxLength = 35),
    nationalInsuranceNumber -> nino.verifying(filledInNino).verifying(validNino),
    dateOfBirth -> dayMonthYear.verifying(validDate),
    theirFullName -> carersNonEmptyText(maxLength = 35),
    theirRelationshipToYou -> carersNonEmptyText(maxLength = 35),

    "furtherInfoContact" -> optional(carersNonEmptyText.verifying(validPhoneNumberRequired)),
    "wantsEmailContactCircs" -> optional(carersNonEmptyText.verifying(validYesNo)),
    "mail" -> optional(email.verifying(Constraints.maxLength(254))),
    "mailConfirmation" -> optional(text(maxLength = 254))

  )(CircumstancesReportChange.apply)(CircumstancesReportChange.unapply)
    .verifying("error.email.match", emailConfirmation _)
    .verifying("error.email.required", emailRequired _)
    .verifying("error.wants.required", wantsEmailRequired _)

  )

  def present = claiming ({ implicit circs => implicit lang => implicit request => 
      track(CircumstancesReportChange) {
        implicit circs => Ok(views.html.circs.s1_start_of_process.g2_reportAChangeInYourCircumstances(form.fill(CircumstancesReportChange)))
      }
  },checkCookie=true)

  def submit = claiming ({ implicit circs => implicit lang => implicit request => 
      form.bindEncrypted.fold(
        formWithErrors => {
          val formWithErrorsUpdate = formWithErrors
            .replaceError("","error.email.match",FormError("mailConfirmation","error.email.match"))
            .replaceError("","error.email.required",FormError("mail",errorRequired))
            .replaceError("","error.wants.required",FormError("wantsEmailContactCircs",errorRequired))

          BadRequest(views.html.circs.s1_start_of_process.g2_reportAChangeInYourCircumstances(formWithErrorsUpdate))
        },
        f => circs.update(f) -> getReportChangesRedirect(circs)
      )
  },checkCookie=true)

  private def getReportChangesRedirect(circs:Claim) = {
    import controllers.circs.s2_report_changes.routes

    val reportChanges = circs.questionGroup[ReportChanges].getOrElse(ReportChanges()).reportChanges

    // for qs groups under this section, if it is not reportedChange - delete
    val optSections = Stack(CircumstancesSelfEmployment,CircumstancesOtherInfo,CircumstancesStoppedCaring,
      CircumstancesPaymentChange, CircumstancesAddressChange, CircumstancesBreaksInCare, CircumstancesEmploymentChange)

    val selectedQG:(QuestionGroup.Identifier,Call) = {
      reportChanges match {
        case r.SelfEmployment.name => CircumstancesSelfEmployment -> routes.G2SelfEmployment.present()
        case r.EmploymentChange.name => CircumstancesEmploymentChange -> routes.G9EmploymentChange.present()
        case r.AddressChange.name => CircumstancesAddressChange  -> routes.G6AddressChange.present()
        case r.StoppedCaring.name =>  CircumstancesStoppedCaring  -> routes.G3PermanentlyStoppedCaring.present()
        case r.PaymentChange.name => CircumstancesPaymentChange  -> routes.G5PaymentChange.present()
        case r.BreakFromCaring.name => CircumstancesBreaksInCare  -> routes.G7BreaksInCare.present()
        case r.BreakFromCaringYou.name => CircumstancesBreaksInCare  -> routes.G7BreaksInCare.present()
        case _ => CircumstancesOtherInfo      -> routes.G4OtherChangeInfo.present()
      }
    }

    val updatedCircs = popDeleteQG(circs,optSections.filter(_.id != selectedQG._1.id))

    Redirect(selectedQG._2)
  }

  @tailrec
  private def popDeleteQG(circs:Claim,optSections:Stack[QuestionGroup.Identifier]):Claim = {
    if (optSections.isEmpty) circs
    else popDeleteQG(circs delete(optSections top),optSections pop)
  }
}
