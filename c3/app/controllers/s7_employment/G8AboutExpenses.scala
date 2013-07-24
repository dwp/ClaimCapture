package controllers.s7_employment

import scala.language.reflectiveCalls
import models.view.CachedClaim
import play.api.mvc.Controller
import play.api.data.Form
import play.api.data.Forms._
import models.domain.AboutExpenses
import utils.helpers.CarersForm._
import controllers.Mappings._
import Employment._

object G8AboutExpenses extends Controller with CachedClaim {
  val form = Form(
    mapping(
      "jobID" -> nonEmptyText,
      "payForAnythingNecessary" -> nonEmptyText,
      "payAnyoneToLookAfterChildren" -> nonEmptyText,
      "payAnyoneToLookAfterPerson" -> nonEmptyText,
      call(routes.G8AboutExpenses.present())
    )(AboutExpenses.apply)(AboutExpenses.unapply))

  def present = claiming { implicit claim => implicit request =>
    Ok(views.html.s7_employment.g8_aboutExpenses(form, completedQuestionGroups(AboutExpenses)))
  }

  def submit = claimingInJob { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s7_employment.g8_aboutExpenses(formWithErrors, completedQuestionGroups(AboutExpenses))),
      aboutExpenses => claim.update(jobs.update(aboutExpenses)) -> Redirect(routes.G8AboutExpenses.present()))
  }
}