package controllers.s7_employment

import scala.language.reflectiveCalls
import models.view.CachedClaim
import play.api.mvc.Controller
import play.api.data.Form
import play.api.data.Forms._
import models.domain.MoneyOwedbyEmployer
import utils.helpers.CarersForm._
import controllers.Mappings._
import Employment._

object G6MoneyOwedbyEmployer extends Controller with CachedClaim {
  val form = Form(
    mapping(
      "jobID" -> nonEmptyText,
      "howMuch" -> optional(text),
      "owedPeriod" -> optional(periodFromTo),
      "owedFor" -> optional(text),
      "shouldBeenPaidBy" -> optional(dayMonthYear),
      "whenWillGetIt" -> optional(text)
    )(MoneyOwedbyEmployer.apply)(MoneyOwedbyEmployer.unapply))

  def present(jobID: String) = claiming { implicit claim => implicit request =>
    Ok(views.html.s7_employment.g6_moneyOwedByEmployer(form.fillWithJobID(MoneyOwedbyEmployer, jobID), completedQuestionGroups(MoneyOwedbyEmployer, jobID)))
  }

  def submit = claimingInJob { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s7_employment.g6_moneyOwedByEmployer(formWithErrors, completedQuestionGroups(MoneyOwedbyEmployer, formWithErrors("jobID").value.get))),
      moneyowed => claim.update(jobs.update(moneyowed)) -> Redirect(routes.G7PensionSchemes.present(moneyowed.jobID)))
  }
}