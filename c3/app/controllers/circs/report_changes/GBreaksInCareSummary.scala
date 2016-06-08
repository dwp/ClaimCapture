package controllers.circs.report_changes

import play.api.Play._
import play.api.mvc.Controller
import models.view.{CachedChangeOfCircs, Navigable}
import scala.language.postfixOps
import play.api.data.{FormError, Form}
import play.api.data.Forms._
import utils.helpers.CarersForm._
import controllers.mappings.Mappings._
import models.yesNo.YesNoWithText
import models.domain.{CircumstancesBreaksInCareSummary, CircumstancesBreaksInCare}
import controllers.CarersForms._
import play.api.i18n._

object GBreaksInCareSummary extends Controller with CachedChangeOfCircs with Navigable with I18nSupport {
  override val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]

  val backCall = controllers.circs.start_of_process.routes.GReportChangeReason.present()

  val additionalBreaksMapping =
    "additionalBreaks" -> mapping(
      "answer" -> nonEmptyText.verifying(validYesNo),
      "text" -> optional(carersNonEmptyText(maxLength = 300))
    )(YesNoWithText.apply)(YesNoWithText.unapply)
      .verifying("additionalBreaks.text.required", YesNoWithText.validateOnYes _)

  val form = Form(mapping(
    additionalBreaksMapping
  )(CircumstancesBreaksInCareSummary.apply)(CircumstancesBreaksInCareSummary.unapply)
  )

  def present = claiming { implicit circs => implicit request => implicit request2lang =>
    track(CircumstancesBreaksInCareSummary) {
      implicit circs => Ok(views.html.circs.report_changes.breaksInCareSummary(form.fill(CircumstancesBreaksInCareSummary), circs.questionGroup[CircumstancesBreaksInCare].getOrElse(new CircumstancesBreaksInCare()), backCall))
    }
  }

  def submit = claiming { implicit circs => implicit request => implicit request2lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val formWithErrorsUpdate = formWithErrors
          .replaceError("additionalBreaks", "additionalBreaks.text.required", FormError("additionalBreaks.text", errorRequired))
        BadRequest(views.html.circs.report_changes.breaksInCareSummary(formWithErrorsUpdate, circs.questionGroup[CircumstancesBreaksInCare].getOrElse(new CircumstancesBreaksInCare()), backCall))
      },
      f => circs.update(f) -> Redirect(controllers.circs.your_details.routes.GYourDetails.present())
    )
  }

}
