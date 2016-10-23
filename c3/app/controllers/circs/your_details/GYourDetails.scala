package controllers.circs.your_details

import controllers.CarersForms._
import controllers.mappings.Mappings
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
import play.api.mvc.Controller
import utils.CommonValidation
import utils.helpers.CarersForm._

import scala.language.{postfixOps, reflectiveCalls}

object GYourDetails extends Controller with CachedChangeOfCircs with Navigable with I18nSupport {
  override val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]

  val form = Form(mapping(
    "firstName" -> carersNonEmptyText(maxLength = 17),
    "surname" -> carersNonEmptyText(maxLength = CommonValidation.NAME_MAX_LENGTH),
    "nationalInsuranceNumber" -> nino.verifying(stopOnFirstFail (filledInNino,validNino)),
    "dateOfBirth" -> dayMonthYear.verifying(validDate),
    "theirFirstName" -> carersNonEmptyText(maxLength = 17),
    "theirSurname" -> carersNonEmptyText(maxLength = CommonValidation.NAME_MAX_LENGTH),
    "theirRelationshipToYou" -> carersNonEmptyText(maxLength = 35),
    "furtherInfoContact" -> optional(carersNonEmptyText.verifying(validPhoneNumberRequired)),
    "wantsEmailContactCircs" -> carersNonEmptyText.verifying(validYesNo),
    "mail" -> optional(carersEmailValidation.verifying(Constraints.maxLength(254))),
    "mailConfirmation" -> optional(text(maxLength = 254))
  )(CircumstancesYourDetails.apply)(CircumstancesYourDetails.unapply)
    .verifying("error.email.match", emailConfirmation _)
    .verifying("error.email.required", emailRequired _)
    .verifying("error.wants.required", wantsEmailRequired _)
  )

  def present = claiming ({ implicit circs => implicit request2lang => implicit request =>
      track(CircumstancesYourDetails) {
        implicit circs => Ok(views.html.circs.your_details.yourDetails(form.fill(CircumstancesYourDetails)))
      }
  },checkCookie=true)


  def submit = claiming ({ implicit circs => implicit request2lang => implicit request =>
      form.bindEncrypted.fold(
        formWithErrors => {
          val formWithErrorsUpdate = formWithErrors
            .replaceError("","error.email.match",FormError("mailConfirmation","error.email.match"))
            .replaceError("","error.email.required",FormError("mail",errorRequired))
            .replaceError("","error.wants.required",FormError("wantsEmailContactCircs",errorRequired))

          BadRequest(views.html.circs.your_details.yourDetails(formWithErrorsUpdate))
        },
        yourDetailsChange => circs.update(formatEmailAndPostCode(yourDetailsChange)) -> Redirect(controllers.circs.consent_and_declaration.routes.GCircsDeclaration.present())
      )
  },checkCookie=true)

  private def formatEmailAndPostCode(circumstancesReportChange: CircumstancesYourDetails): CircumstancesYourDetails = {
    circumstancesReportChange.copy(
      email = circumstancesReportChange.email.getOrElse("").trim match { case y if y.isEmpty => None case x => Some(x) },
      emailConfirmation = circumstancesReportChange.emailConfirmation.getOrElse("").trim match { case y if y.isEmpty => None case x => Some(x) })
  }
}
