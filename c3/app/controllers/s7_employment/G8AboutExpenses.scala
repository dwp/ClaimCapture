package controllers.s7_employment

import scala.language.reflectiveCalls
import play.api.mvc.Controller
import play.api.data.Form
import play.api.data.Forms._
import models.view.{Navigable, CachedClaim}
import models.domain.AboutExpenses
import utils.helpers.CarersForm._
import Employment._
import utils.helpers.PastPresentLabelHelper._
import controllers.Mappings._
import play.api.data.FormError

object G8AboutExpenses extends Controller with CachedClaim with Navigable {
  val form = Form(mapping(
    "jobID" -> nonEmptyText,
    "payForAnythingNecessary" -> nonEmptyText.verifying(validYesNo),
    "payAnyoneToLookAfterChildren" -> nonEmptyText.verifying(validYesNo),
    "payAnyoneToLookAfterPerson" -> nonEmptyText.verifying(validYesNo)
  )(AboutExpenses.apply)(AboutExpenses.unapply))

  def present(jobID: String) = claimingWithCheck { implicit claim => implicit request => implicit lang =>
    track(AboutExpenses) { implicit claim => Ok(views.html.s7_employment.g8_aboutExpenses(form.fillWithJobID(AboutExpenses, jobID))) }
  }

  def submit = claimingWithCheckInJob { jobID => implicit claim => implicit request => implicit lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val formWithErrorsUpdate = formWithErrors
          .replaceError("payForAnythingNecessary", "error.required", FormError("payForAnythingNecessary", "error.required", Seq(labelForEmployment(claim, lang, "payForAnythingNecessary", jobID))))
          .replaceError("payAnyoneToLookAfterChildren", "error.required", FormError("payAnyoneToLookAfterChildren", "error.required", Seq(labelForEmployment(claim, lang, "payAnyoneToLookAfterChildren", jobID))))
          .replaceError("payAnyoneToLookAfterPerson", "error.required", FormError("payAnyoneToLookAfterPerson", "error.required", Seq(labelForEmployment(claim, lang, "payAnyoneToLookAfterPerson", jobID))))
        BadRequest(views.html.s7_employment.g8_aboutExpenses(formWithErrorsUpdate))
      },
      aboutExpenses => claim.update(jobs.update(aboutExpenses)) -> Redirect(routes.G9NecessaryExpenses.present(jobID)))
  }
}