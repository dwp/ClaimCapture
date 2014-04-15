package controllers.s7_employment

import language.reflectiveCalls
import play.api.mvc.Controller
import play.api.data.Form
import play.api.data.Forms._
import models.view.{Navigable, CachedClaim}
import models.domain.{Jobs, JobDetails}
import utils.helpers.CarersForm._
import controllers.Mappings._
import Employment._
import controllers.CarersForms._
import utils.helpers.PastPresentLabelHelper._
import play.api.data.FormError
import scala.Some

object G3JobDetails extends Controller with CachedClaim with Navigable {
  val form = Form(mapping(
    "jobID" -> nonEmptyText,
    "employerName"-> carersNonEmptyText(maxLength = 60),
    "jobStartDate" -> dayMonthYear.verifying(validDate),
    "finishedThisJob" -> nonEmptyText,
    "lastWorkDate" -> optional(dayMonthYear.verifying(validDate)),
    "p45LeavingDate" -> optional(dayMonthYear.verifying(validDateOnly)),
    "hoursPerWeek" -> optional(text(maxLength = 2).verifying(validNumber)),
    "payrollEmployeeNumber" -> optional(carersText)
  )(JobDetails.apply)(JobDetails.unapply)
    .verifying("lastWorkDate", JobDetails.validateLastWorkDate _)
  )

  def job(jobID: String) = claimingWithCheck { implicit claim => implicit request => implicit lang =>
    claim.questionGroup(Jobs) match {
      case Some(js: Jobs) if js.job(jobID).isDefined =>
        track(JobDetails) { implicit claim => Ok(views.html.s7_employment.g3_jobDetails(form.fillWithJobID(JobDetails, jobID))) }
      case _ =>
        Redirect(routes.G2BeenEmployed.present())
    }
  }

  def present(jobID: String) = claimingWithCheck { implicit claim => implicit request => implicit lang =>
    track(JobDetails) { implicit claim => Ok(views.html.s7_employment.g3_jobDetails(form.fillWithJobID(JobDetails, jobID))) }
  }

  def submit = claimingWithCheckInJob { jobID => implicit claim => implicit request => implicit lang =>
    form.bindEncrypted.fold(
      formWithErrors =>{
        val form = formWithErrors.replaceError("", "lastWorkDate", FormError("lastWorkDate", "error.required"))
        .replaceError("hoursPerWeek","number.invalid",FormError("hoursPerWeek","number.invalid", Seq(labelForEmployment(claim, lang, "hoursPerWeek", jobID))))
        BadRequest(views.html.s7_employment.g3_jobDetails(form))
      },jobDetails => claim.update(jobs.update(jobDetails)) -> Redirect(routes.G4EmployerContactDetails.present(jobID)))
  }
}