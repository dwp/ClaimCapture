package controllers.s7_employment

import language.reflectiveCalls
import models.view.CachedClaim
import play.api.mvc.Controller
import play.api.data.Form
import play.api.data.Forms._
import models.domain.{Jobs, JobDetails}
import utils.helpers.CarersForm._
import controllers.Mappings._
import Employment._

object G2JobDetails extends Controller with CachedClaim {
  val form = Form(
    mapping(
      "jobID" -> nonEmptyText,
      "employerName"-> nonEmptyText,
      "jobStartDate" -> optional(dayMonthYear.verifying(validDateOnly)),
      "finishedThisJob" -> nonEmptyText,
      "lastWorkDate" -> optional(dayMonthYear.verifying(validDate)),
      "hoursPerWeek" -> optional(text),
      "jobTitle" -> optional(text),
      "payrollEmployeeNumber" -> optional(text)
    )(JobDetails.apply)(JobDetails.unapply))

  def job(jobID: String) = claiming { implicit claim => implicit request =>
    claim.questionGroup(Jobs) match {
      case Some(js: Jobs) if js.job(jobID).isDefined =>
        whenSectionVisible(Ok(views.html.s7_employment.g2_jobDetails(form.fillWithJobID(JobDetails, jobID))))
      case _ =>
        Redirect(routes.G1BeenEmployed.present())
    }
  }

  def present = claiming { implicit claim => implicit request =>
    whenSectionVisible(Ok(views.html.s7_employment.g2_jobDetails(form)))
  }

  def presentInJob(jobID: String) = claiming { implicit claim => implicit request =>
    whenSectionVisible(Ok(views.html.s7_employment.g2_jobDetails(form.fillWithJobID(JobDetails, jobID))))
  }

  def submit = claimingInJob {jobID =>  implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => whenSectionVisible(BadRequest(views.html.s7_employment.g2_jobDetails(formWithErrors))),
      jobDetails => claim.update(jobs.update(jobDetails)) -> Redirect(routes.G3EmployerContactDetails.present(jobID)))
  }
}