package controllers.circs.s2_report_changes

import play.api.mvc.Controller
import models.view.{CachedChangeOfCircs, Navigable}
import scala.language.postfixOps
import play.api.data.{FormError, Form}
import play.api.data.Forms._
import utils.helpers.CarersForm._
import controllers.mappings.Mappings._
import models.yesNo.YesNoWithText
import models.domain.{CircumstancesBreaksInCareSummary,CircumstancesBreaksInCare}
import controllers.CarersForms._
import play.api.data.FormError
import utils.helpers.PastPresentLabelHelper._
import play.api.data.FormError

object G8BreaksInCareSummary extends Controller with CachedChangeOfCircs with Navigable {
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

  def present = claiming {implicit circs =>  implicit request =>  lang =>
    track(CircumstancesBreaksInCareSummary) {
      implicit circs => Ok(views.html.circs.s2_report_changes.g8_breaksInCareSummary(form.fill(CircumstancesBreaksInCareSummary), circs.questionGroup[CircumstancesBreaksInCare].getOrElse(new CircumstancesBreaksInCare()))(lang))
    }
  }

  def submit = claiming {implicit circs =>  implicit request =>  lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val formWithErrorsUpdate = formWithErrors
          .replaceError("additionalBreaks", "additionalBreaks.text.required", FormError("additionalBreaks.text", "error.required"))
        BadRequest(views.html.circs.s2_report_changes.g8_breaksInCareSummary(formWithErrorsUpdate, circs.questionGroup[CircumstancesBreaksInCare].getOrElse(new CircumstancesBreaksInCare()))(lang))
      },
      f => circs.update(f) -> Redirect(controllers.circs.s3_consent_and_declaration.routes.G1Declaration.present())
    )
  }

}
