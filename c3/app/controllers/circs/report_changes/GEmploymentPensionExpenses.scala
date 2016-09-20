package controllers.circs.report_changes

import controllers.CarersForms._
import controllers.mappings.Mappings._
import models.domain._
import models.view.{CachedChangeOfCircs, Navigable}
import models.yesNo.{YesNoWithText}
import play.api.Play._
import play.api.data.Forms._
import play.api.data.{Form, FormError}
import play.api.i18n._
import play.api.mvc.Controller
import utils.helpers.CarersForm._


object GEmploymentPensionExpenses extends Controller with CachedChangeOfCircs with Navigable with I18nSupport {
  override val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]

  val payIntoPension =
    "payIntoPension" -> mapping(
      "answer" -> text.verifying(validYesNo),
      "whatFor" -> optional(carersText(maxLength = CircumstancesEmploymentPensionExpenses.payIntoPensionMaxLength))
    )(YesNoWithText.apply)(YesNoWithText.unapply)
      .verifying("payIntoPension.text.required", YesNoWithText.validateOnYes _)

  val payForThings =
    "payForThings" -> mapping(
      "answer" -> text.verifying(validYesNo),
      "whatFor" -> optional(carersText(maxLength = CircumstancesEmploymentPensionExpenses.payForThingsMaxLength))
    )(YesNoWithText.apply)(YesNoWithText.unapply)
      .verifying("payForThings.text.required", YesNoWithText.validateOnYes _)

  val careCosts =
    "careCosts" -> mapping(
      "answer" -> text.verifying(validYesNo),
      "whatFor" -> optional(carersText(maxLength = CircumstancesEmploymentPensionExpenses.careCostsMaxLength))
    )(YesNoWithText.apply)(YesNoWithText.unapply)
      .verifying("careCosts.text.required", YesNoWithText.validateOnYes _)

  val form = Form(mapping(
    payIntoPension,
    payForThings,
    careCosts,
    "moreAboutChanges" -> optional(carersText(maxLength = CircumstancesEmploymentPensionExpenses.moreAboutChangesMaxLength))
  )(CircumstancesEmploymentPensionExpenses.apply)(CircumstancesEmploymentPensionExpenses.unapply))

  def present = claiming { implicit circs => implicit request => implicit request2lang =>
    track(CircumstancesEmploymentChange) {
      implicit circs => Ok(views.html.circs.report_changes.employmentPensionExpenses(form.fill(CircumstancesEmploymentPensionExpenses)))
    }
  }

  def submit = claiming { implicit circs => implicit request => implicit request2lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        // Note that this hacky error replacement requires none tense error message like pension={0} and then actual error-message of pension.past=The errror message.
        def errorMsgWithTense(errorKey: String) = {
          val tense = CircumstancesEmploymentPensionExpenses.presentPastOrFuture(circs)
          Seq(messagesApi(s"$errorKey.$tense"))
        }
        val formWithErrorsUpdate = formWithErrors
          .replaceError("payIntoPension.answer", "error.required", FormError("payIntoPension.answer", errorRequired, errorMsgWithTense("payIntoPension.answer")))
          .replaceError("payIntoPension", "payIntoPension.text.required", FormError("payIntoPension.whatFor", errorRequired, errorMsgWithTense("payIntoPension.whatFor")))
          .replaceError("payIntoPension.whatFor", "error.restricted.characters", FormError("payIntoPension.whatFor", errorRestrictedCharacters, errorMsgWithTense("payIntoPension.whatFor")))

          .replaceError("payForThings.answer", "error.required", FormError("payForThings.answer", errorRequired, errorMsgWithTense("payForThings.answer")))
          .replaceError("payForThings", "payForThings.text.required", FormError("payForThings.whatFor", errorRequired, errorMsgWithTense("payForThings.whatFor")))
          .replaceError("payForThings.whatFor", "error.restricted.characters", FormError("payForThings.whatFor", errorRestrictedCharacters, errorMsgWithTense("payForThings.whatFor")))

          .replaceError("careCosts.answer", "error.required", FormError("careCosts.answer", errorRequired, errorMsgWithTense("careCosts.answer")))
          .replaceError("careCosts", "careCosts.text.required", FormError("careCosts.whatFor", errorRequired, errorMsgWithTense("careCosts.whatFor")))
          .replaceError("careCosts.whatFor", "error.restricted.characters", FormError("careCosts.whatFor", errorRestrictedCharacters, errorMsgWithTense("careCosts.whatFor")))
        BadRequest(views.html.circs.report_changes.employmentPensionExpenses(formWithErrorsUpdate))
      },
      f => circs.update(f) -> Redirect(circsPathAfterFunction)
    )
  }
}
