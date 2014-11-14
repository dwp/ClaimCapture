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
import scala.language.postfixOps

object G3JobDetails extends Controller with CachedClaim with Navigable {
  val form = Form(mapping(
    "jobID" -> carersNonEmptyText,
    "employerName"-> carersNonEmptyText(maxLength = 60),
    "phoneNumber" -> nonEmptyText.verifying(validPhoneNumberRequired),
    "address" -> address.verifying(requiredAddress),
    "postcode" -> optional(text verifying validPostcode),
    "startJobBeforeClaimDate" -> nonEmptyText.verifying(validYesNo),
    "jobStartDate" -> optional(dayMonthYear.verifying(validDate)),
    "finishedThisJob" -> nonEmptyText.verifying(validYesNo),
    "lastWorkDate" -> optional(dayMonthYear.verifying(validDate)),
    "p45LeavingDate" -> optional(dayMonthYear.verifying(validDateOnly)),
    "hoursPerWeek" -> optional(carersText(maxLength = 2).verifying(validNumber))
  )(JobDetails.apply)(JobDetails.unapply)
    .verifying("lastWorkDate.required", JobDetails.validateLastWorkDate _)
    .verifying("jobStartDate.required", JobDetails.validateJobStartDate _)
  )

  def job(jobID: String) = claimingWithCheck { implicit claim =>  implicit request =>  lang =>
    claim.questionGroup(Jobs) match {
      case Some(js: Jobs) if js.job(jobID).isDefined =>
        track(JobDetails) { implicit claim => Ok(views.html.s7_employment.g3_jobDetails(form.fillWithJobID(JobDetails, jobID))(lang)) }
      case _ =>
        Redirect(routes.G2BeenEmployed.present())
    }
  }

  def present(jobID: String) = claimingWithCheck { implicit claim =>  implicit request =>  lang =>
    track(JobDetails) { implicit claim => Ok(views.html.s7_employment.g3_jobDetails(form.fillWithJobID(JobDetails, jobID))(lang)) }
  }

  def submit = claimingWithCheckInJob { jobID => implicit claim =>  implicit request =>  lang =>
    form.bindEncrypted.fold(
      formWithErrors =>{
        val form = formWithErrors
          .replaceError("", "lastWorkDate.required", FormError("lastWorkDate", "error.required", Seq(labelForEmployment(claim, lang, "lastWorkDate", jobID))))
          .replaceError("hoursPerWeek","number.invalid",FormError("hoursPerWeek","number.invalid", Seq(labelForEmployment(claim, lang, "hoursPerWeek", jobID))))
          .replaceError("", "jobStartDate.required", FormError("jobStartDate", "error.required"))
          .replaceError("startJobBeforeClaimDate", "error.required", FormError("startJobBeforeClaimDate", "error.required",Seq(claim.dateOfClaim.fold("")(dmy => (dmy - 1 months).`dd month yyyy`))))
        BadRequest(views.html.s7_employment.g3_jobDetails(form)(lang))
      },jobDetails => claim.update(jobs.update(jobDetails)) -> Redirect(routes.G5LastWage.present(jobID)))
  }
}