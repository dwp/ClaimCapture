package controllers.s7_employment

import language.reflectiveCalls
import play.api.mvc.Controller
import play.api.data.{FormError, Form}
import play.api.data.Forms._
import models.view.{Navigable, CachedClaim}
import models.domain.{Jobs, JobDetails}
import utils.helpers.CarersForm._
import controllers.Mappings._
import Employment._
import controllers.CarersForms._
import scala.Some
import utils.helpers.PastPresentLabelHelper._
import play.api.data.FormError
import scala.Some
import play.api.Logger

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

  def job(jobID: String) = claiming { implicit claim => implicit request =>
    claim.questionGroup(Jobs) match {
      case Some(js: Jobs) if js.job(jobID).isDefined =>
        track(JobDetails) { implicit claim => Ok(views.html.s7_employment.g3_jobDetails(form.fillWithJobID(JobDetails, jobID))) }
      case _ =>
        Redirect(routes.G2BeenEmployed.present())
    }
  }

  def present(jobID: String) = claiming { implicit claim => implicit request =>
    track(JobDetails) { implicit claim => Ok(views.html.s7_employment.g3_jobDetails(form.fillWithJobID(JobDetails, jobID))) }
  }

  def submit = claimingInJob { jobID => implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors =>{
        val pastPresent = formWithErrors("finishedThisJob").value match {
          case Some(v) => if (v.toLowerCase == no) "do you" else "did you"
          case _ => "do you"
        }
        val form = formWithErrors.replaceError("", "lastWorkDate", FormError("lastWorkDate", "error.required"))
        .replaceError("hoursPerWeek","number.invalid",FormError("hoursPerWeek","number.invalid", Seq(pastPresent)))
        BadRequest(views.html.s7_employment.g3_jobDetails(form))
      },jobDetails => claim.update(jobs.update(jobDetails)) -> Redirect(routes.G4EmployerContactDetails.present(jobID)))
  }
}