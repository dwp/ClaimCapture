package controllers.s7_employment

import scala.language.reflectiveCalls
import models.view.{Navigable, CachedClaim}
import play.api.mvc.Controller
import play.api.data.Form
import play.api.data.Forms._
import models.domain.PensionSchemes
import utils.helpers.CarersForm._
import Employment._
import utils.helpers.PastPresentLabelHelper._
import controllers.Mappings._
import play.api.data.FormError

object G7PensionSchemes extends Controller with CachedClaim with Navigable {
  val form = Form(mapping(
    "jobID" -> nonEmptyText,
    "payOccupationalPensionScheme" -> nonEmptyText,
    "howMuchPension" -> optional(text verifying(validDecimalNumber)),
    "howOftenPension" -> optional(text),
    "payPersonalPensionScheme" -> nonEmptyText,
    "howMuchPersonal" -> optional(text verifying(validDecimalNumber)),
    "howOftenPersonal" -> optional(text)
  )(PensionSchemes.apply)(PensionSchemes.unapply))

  def present(jobID: String) = claiming { implicit claim => implicit request =>
    track(PensionSchemes) { implicit claim => Ok(views.html.s7_employment.g7_pensionSchemes(form.fillWithJobID(PensionSchemes, jobID))) }
  }

  def submit = claimingInJob { jobID => implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val pastPresent = pastPresentLabelForEmployment(claim, didYou, doYou , jobID)
        val formWithErrorsUpdate = formWithErrors
          .replaceError("howMuchPension", "decimal.invalid", FormError("howMuchPension", "decimal.invalid", Seq(pastPresent.toLowerCase)))
          .replaceError("howMuchPersonal", "decimal.invalid", FormError("howMuchPersonal", "decimal.invalid", Seq(pastPresent.toLowerCase)))
          .replaceError("payOccupationalPensionScheme", "error.required", FormError("payOccupationalPensionScheme", "error.required", Seq(pastPresent)))
          .replaceError("payPersonalPensionScheme", "error.required", FormError("payPersonalPensionScheme", "error.required", Seq(pastPresent)))
        BadRequest(views.html.s7_employment.g7_pensionSchemes(formWithErrorsUpdate))
      },
      schemes => claim.update(jobs.update(schemes)) -> Redirect(routes.G8AboutExpenses.present(jobID)))
  }
}