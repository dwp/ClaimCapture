package controllers.circs.your_details

import app.{ReportChange => r}
import controllers.CarersForms._
import controllers.mappings.Mappings._
import controllers.mappings.NINOMappings._
import models.domain.EMail._
import models.domain._
import models.view.{CachedChangeOfCircs, Navigable}
import play.api.Play._
import play.api.data.Forms._
import play.api.data.validation.Constraints
import play.api.data.{Form, FormError}
import play.api.i18n.{I18nSupport, MMessages, MessagesApi}
import play.api.mvc.{Call, Controller}
import gov.dwp.carers.xml.validation.CommonValidation
import utils.helpers.CarersForm._

import scala.language.{postfixOps, reflectiveCalls}

object GYourDetails extends Controller with CachedChangeOfCircs with Navigable with I18nSupport {
  override val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]

  val form = Form(mapping(
    "firstName" -> nonEmptyText(maxLength = CommonValidation.FIRSTNAME_MAX_LENGTH).verifying(YourDetails.validName),
    "surname" -> nonEmptyText(maxLength = CommonValidation.SURNAME_MAX_LENGTH).verifying(YourDetails.validName),
    "nationalInsuranceNumber" -> nino.verifying(stopOnFirstFail(filledInNino, validNino)),
    "dateOfBirth" -> dayMonthYear.verifying(validDateOfBirth),
    "wantsEmailContactCircs" -> carersNonEmptyText.verifying(validYesNo),
    "mail" -> optional(carersEmailValidation.verifying(Constraints.maxLength(254))),
    "mailConfirmation" -> optional(text(maxLength = 254)),
    "theirFirstName" -> nonEmptyText(maxLength = CommonValidation.FIRSTNAME_MAX_LENGTH).verifying(YourDetails.validName),
    "theirSurname" -> nonEmptyText(maxLength = CommonValidation.SURNAME_MAX_LENGTH).verifying(YourDetails.validName),
    "theirRelationshipToYou" -> carersNonEmptyText(maxLength = 35),
    "furtherInfoContact" -> optional(carersNonEmptyText.verifying(validPhoneNumberRequired))
  )(CircumstancesYourDetails.apply)(CircumstancesYourDetails.unapply)
    .verifying("error.email.match", emailConfirmation _)
    .verifying("error.email.required", emailRequired _)
    .verifying("error.wants.required", wantsEmailRequired _)
  )

  def present = claiming({ implicit circs => implicit request2lang => implicit request =>
    track(CircumstancesYourDetails) {
      implicit circs => Ok(views.html.circs.your_details.yourDetails(form.fill(CircumstancesYourDetails)))
    }
  }, checkCookie = true)


  def submit = claiming({ implicit circs => implicit request2lang => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val formWithErrorsUpdate = formWithErrors
          .replaceError("", "error.email.match", FormError("mailConfirmation", "error.email.match"))
          .replaceError("", "error.email.required", FormError("mail", errorRequired))
          .replaceError("", "error.wants.required", FormError("wantsEmailContactCircs", errorRequired))

        BadRequest(views.html.circs.your_details.yourDetails(formWithErrorsUpdate))
      },
      yourDetailsChange => {
        val circsUpdate = circs.update(formatEmailAndPostCode(yourDetailsChange))
        getReportChangesRedirect(circsUpdate)
      }
    )
  }, checkCookie = true)

  private def getReportChangesRedirect(circs: Claim) = {
    val reportChanges = circs.questionGroup[ReportChangeReason].getOrElse(ReportChangeReason()).reportChanges
    val breakInCare = circs.questionGroup[CircumstancesBreaksInCare].getOrElse(CircumstancesBreaksInCare())

    val selectedQG:(QGIdentifier, Call) = {
      reportChanges match {
        case r.EmploymentChange.name => CircumstancesEmploymentChange -> controllers.circs.report_changes.routes.GEmploymentChange.present()
        case r.AddressChange.name => CircumstancesAddressChange -> controllers.circs.report_changes.routes.GAddressChange.present()
        case r.StoppedCaring.name => CircumstancesStoppedCaring -> controllers.circs.report_changes.routes.GPermanentlyStoppedCaring.present()
        case r.PaymentChange.name => CircumstancesPaymentChange -> controllers.circs.report_changes.routes.GPaymentChange.present()
        case r.BreakFromCaring.name if (breakInCare.medicalCareDuringBreak != "") => CircumstancesBreaksInCare -> controllers.circs.report_changes.routes.GBreaksInCareSummary.present()
        case r.BreakFromCaring.name => CircumstancesBreaksInCare -> controllers.circs.report_changes.routes.GBreaksInCare.present()
        case r.BreakFromCaringYou.name if (breakInCare.medicalCareDuringBreak != "") => CircumstancesBreaksInCare -> controllers.circs.report_changes.routes.GBreaksInCareSummary.present()
        case r.BreakFromCaringYou.name => CircumstancesBreaksInCare -> controllers.circs.report_changes.routes.GBreaksInCare.present()
        case _ => CircumstancesOtherInfo -> controllers.circs.report_changes.routes.GOtherChangeInfo.present()
      }
    }
    circs -> Redirect(selectedQG._2)
  }

  private def formatEmailAndPostCode(circumstancesReportChange: CircumstancesYourDetails): CircumstancesYourDetails = {
    circumstancesReportChange.copy(
      email = circumstancesReportChange.email.getOrElse("").trim match {
        case y if y.isEmpty => None
        case x => Some(x)
      },
      emailConfirmation = circumstancesReportChange.emailConfirmation.getOrElse("").trim match {
        case y if y.isEmpty => None
        case x => Some(x)
      })
  }
}
