package controllers.circs.start_of_process

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

object GReportAChangeInYourCircumstances extends Controller with CachedChangeOfCircs with Navigable with I18nSupport {
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
    "wantsEmailContactCircs" -> carersNonEmptyText.verifying(validYesNo),
    "mail" -> optional(carersEmailValidation.verifying(Constraints.maxLength(254))),
    "mailConfirmation" -> optional(text(maxLength = 254))
  )(CircumstancesReportChange.apply)(CircumstancesReportChange.unapply)
    .verifying("error.email.match", emailConfirmation _)
    .verifying("error.email.required", emailRequired _)
    .verifying("error.wants.required", wantsEmailRequired _)
  )

  def present = claiming ({ implicit circs => implicit request2lang => implicit request =>
      track(CircumstancesReportChange) {
        implicit circs => Ok(views.html.circs.start_of_process.reportAChangeInYourCircumstances(form.fill(CircumstancesReportChange)))
      }
  },checkCookie=true)

  def submit = claiming ({ implicit circs => implicit request2lang => implicit request =>
      form.bindEncrypted.fold(
        formWithErrors => {
          val formWithErrorsUpdate = formWithErrors
            .replaceError("","error.email.match",FormError("mailConfirmation","error.email.match"))
            .replaceError("","error.email.required",FormError("mail",errorRequired))
            .replaceError("","error.wants.required",FormError("wantsEmailContactCircs",errorRequired))

          BadRequest(views.html.circs.start_of_process.reportAChangeInYourCircumstances(formWithErrorsUpdate))
        },
        circumstancesReportChange => circs.update(formatEmailAndPostCode(circumstancesReportChange)) -> getReportChangesRedirect(circs)
      )
  },checkCookie=true)

  private def getReportChangesRedirect(circs:Claim) = {
    val reportChanges = circs.questionGroup[ReportChanges].getOrElse(ReportChanges()).reportChanges

    // for qs groups under this section, if it is not reportedChange - delete
    val optSections = Stack(CircumstancesSelfEmployment,CircumstancesOtherInfo,CircumstancesStoppedCaring,
      CircumstancesPaymentChange, CircumstancesAddressChange, CircumstancesBreaksInCare, CircumstancesEmploymentChange)

    val selectedQG:(QuestionGroup.Identifier,Call) = {
      reportChanges match {
        case r.SelfEmployment.name => CircumstancesSelfEmployment -> controllers.circs.report_changes.routes.GSelfEmployment.present()
        case r.EmploymentChange.name => CircumstancesEmploymentChange -> controllers.circs.report_changes.routes.GEmploymentChange.present()
        case r.AddressChange.name => CircumstancesAddressChange  -> controllers.circs.report_changes.routes.GAddressChange.present()
        case r.StoppedCaring.name =>  CircumstancesStoppedCaring  -> controllers.circs.report_changes.routes.GPermanentlyStoppedCaring.present()
        case r.PaymentChange.name => CircumstancesPaymentChange  -> controllers.circs.report_changes.routes.GPaymentChange.present()
        case r.BreakFromCaring.name => CircumstancesBreaksInCare  -> controllers.circs.report_changes.routes.GBreaksInCare.present()
        case r.BreakFromCaringYou.name => CircumstancesBreaksInCare  -> controllers.circs.report_changes.routes.GBreaksInCare.present()
        case _ => CircumstancesOtherInfo -> controllers.circs.report_changes.routes.GOtherChangeInfo.present()
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

  private def formatEmailAndPostCode(circumstancesReportChange: CircumstancesReportChange): CircumstancesReportChange = {
    circumstancesReportChange.copy(
      email = Some(circumstancesReportChange.email.getOrElse("").trim),
      emailConfirmation = Some(circumstancesReportChange.emailConfirmation.getOrElse("").trim))
  }
}
