package controllers.s7_employment

import scala.language.reflectiveCalls
import models.view.{Navigable, CachedClaim}
import play.api.mvc.Controller
import play.api.data.{FormError, Form}
import play.api.data.Forms._
import models.domain.AboutExpenses
import utils.helpers.CarersForm._
import Employment._
import utils.helpers.PastPresentLabelHelper._

object G8AboutExpenses extends Controller with CachedClaim with Navigable {
  val form = Form(mapping(
    "jobID" -> nonEmptyText,
    "payForAnythingNecessary" -> nonEmptyText,
    "payAnyoneToLookAfterChildren" -> nonEmptyText,
    "payAnyoneToLookAfterPerson" -> nonEmptyText
  )(AboutExpenses.apply)(AboutExpenses.unapply))

  def present(jobID: String) = claiming { implicit claim => implicit request =>
    track(AboutExpenses) { implicit claim => Ok(views.html.s7_employment.g8_aboutExpenses(form.fillWithJobID(AboutExpenses, jobID))) }
  }

  def submit = claimingInJob { jobID => implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val pastPresent = pastPresentLabelForEmployment(claim, didYou, doYou , jobID)
        val formWithErrorsUpdate = formWithErrors
          .replaceError("payForAnythingNecessary", "error.required", FormError("payForAnythingNecessary", "error.required", Seq(pastPresent)))
          .replaceError("payAnyoneToLookAfterChildren", "error.required", FormError("payAnyoneToLookAfterChildren", "error.required", Seq(pastPresent.toLowerCase)))
          .replaceError("payAnyoneToLookAfterPerson", "error.required", FormError("payAnyoneToLookAfterPerson", "error.required", Seq(pastPresent.toLowerCase)))
        BadRequest(views.html.s7_employment.g8_aboutExpenses(formWithErrorsUpdate))
      },
      aboutExpenses => claim.update(jobs.update(aboutExpenses)) -> Redirect(routes.G9NecessaryExpenses.present(jobID)))
  }
}