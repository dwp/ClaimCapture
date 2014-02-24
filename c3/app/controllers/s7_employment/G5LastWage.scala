package controllers.s7_employment

import language.reflectiveCalls
import play.api.mvc.Controller
import play.api.data.Form
import play.api.data.Forms._
import models.view.{Navigable, CachedClaim}
import models.domain.LastWage
import utils.helpers.CarersForm._
import controllers.Mappings._
import Employment._
import controllers.CarersForms._

object G5LastWage extends Controller with CachedClaim with Navigable {
  val form = Form(mapping(
    "jobID" -> nonEmptyText,
    "lastPaidDate" -> optional(dayMonthYear.verifying(validDateOnly)),
    "grossPay" -> required(nonEmptyText.verifying(validDecimalNumberRequired)),
    "payInclusions" -> optional(carersText(maxLength = 500)),
    "sameAmountEachTime" -> optional(text.verifying(validYesNo))
  )(LastWage.apply)(LastWage.unapply))

  def present(jobID: String) = claimingWithCheck { implicit claim => implicit request => implicit lang =>
    track(LastWage) { implicit claim => Ok(views.html.s7_employment.g5_lastWage(form.fillWithJobID(LastWage, jobID))) }
  }

  def submit = claimingWithCheckInJob { jobID => implicit claim => implicit request => implicit lang =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s7_employment.g5_lastWage(formWithErrors)),
      lastWage => claim.update(jobs.update(lastWage)) -> Redirect(routes.G6AdditionalWageDetails.present(jobID)))
  }
}