package controllers.s7_employment

import language.reflectiveCalls
import models.view.{Navigable, CachedClaim}
import play.api.mvc.Controller
import play.api.data.Form
import play.api.data.Forms._
import models.domain.LastWage
import utils.helpers.CarersForm._
import controllers.Mappings._
import Employment._

object G4LastWage extends Controller with CachedClaim with Navigable {
  val form = Form(mapping(
    "jobID" -> nonEmptyText,
    "lastPaidDate" -> optional(dayMonthYear.verifying(validDateOnly)),
    "grossPay" -> optional(nonEmptyText.verifying(validDecimalNumber)),
    "payInclusions" -> optional(text),
    "sameAmountEachTime" -> optional(text)
  )(LastWage.apply)(LastWage.unapply))

  def present(jobID: String) = claiming { implicit claim => implicit request =>
    track(LastWage) { implicit claim => Ok(views.html.s7_employment.g4_lastWage(form.fillWithJobID(LastWage, jobID))) }
  }

  def submit = claimingInJob { jobID => implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s7_employment.g4_lastWage(formWithErrors)),
      lastWage => claim.update(jobs.update(lastWage)) -> Redirect(routes.G5AdditionalWageDetails.present(jobID)))
  }
}