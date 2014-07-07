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
    "phoneNumber" -> optional(text verifying validPhoneNumber),
    "payrollEmployeeNumber" -> optional(carersText),
    "address" -> address.verifying(requiredAddress),
    "postcode" -> optional(text verifying validPostcode),
    "jobStartDate" -> dayMonthYear.verifying(validDate),
    "finishedThisJob" -> nonEmptyText.verifying(validYesNo),
    "lastWorkDate" -> optional(dayMonthYear.verifying(validDate)),
    "p45LeavingDate" -> optional(dayMonthYear.verifying(validDateOnly)),
    "hoursPerWeek" -> optional(nonEmptyText(maxLength = 2).verifying(validNumber))
  )(JobDetails.apply)(JobDetails.unapply)
    .verifying("lastWorkDate.required", JobDetails.validateLastWorkDate _)
    .verifying("hoursPerWeek.required", JobDetails.validateHoursPerWeek _)
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
        val form = formWithErrors
          .replaceError("", "lastWorkDate.required", FormError("lastWorkDate", "error.required", Seq(labelForEmployment(claim, lang, "lastWorkDate", jobID))))
          .replaceError("", "hoursPerWeek.required", FormError("hoursPerWeek", "error.required", Seq(labelForEmployment(claim, lang, "hoursPerWeek", jobID))))
          .replaceError("hoursPerWeek","number.invalid",FormError("hoursPerWeek","number.invalid", Seq(labelForEmployment(claim, lang, "hoursPerWeek", jobID))))
        BadRequest(views.html.s7_employment.g3_jobDetails(form))
      },jobDetails => claim.update(jobs.update(jobDetails)) -> Redirect(routes.G5LastWage.present(jobID)))
  }
}