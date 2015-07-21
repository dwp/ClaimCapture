package controllers.s8_employment

import language.reflectiveCalls
import play.api.mvc.Controller
import play.api.data.Form
import play.api.data.Forms._
import models.view.{Navigable, CachedClaim}
import models.domain.{Jobs, JobDetails}
import utils.helpers.CarersForm._
import controllers.mappings.Mappings._
import controllers.mappings.AddressMappings._
import Employment._
import controllers.CarersForms._
import utils.helpers.PastPresentLabelHelper._
import play.api.data.FormError
import scala.language.postfixOps
import utils.helpers.HtmlLabelHelper.displayPlaybackDatesFormat

object G3JobDetails extends Controller with CachedClaim with Navigable {
  val form = Form(mapping(
    "iterationID" -> carersNonEmptyText,
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

  def job(iterationID: String) = claimingWithCheck { implicit claim =>  implicit request =>  lang =>
    claim.questionGroup(Jobs) match {
      case Some(js: Jobs) if js.job(iterationID).isDefined =>
        track(JobDetails) { implicit claim => Ok(views.html.s8_employment.g3_jobDetails(form.fillWithJobID(JobDetails, iterationID))(lang)) }
      case _ =>
        Redirect(routes.G2BeenEmployed.present())
    }
  }

  def present(iterationID: String) = claimingWithCheck { implicit claim =>  implicit request =>  lang =>
    track(JobDetails) { implicit claim => Ok(views.html.s8_employment.g3_jobDetails(form.fillWithJobID(JobDetails, iterationID))(lang)) }
  }

  def submit = claimingWithCheckInIteration { iterationID => implicit claim =>  implicit request =>  lang =>
    form.bindEncrypted.fold(
      formWithErrors =>{
        val form = formWithErrors
          .replaceError("", "lastWorkDate.required", FormError("lastWorkDate", errorRequired, Seq(labelForEmployment(formWithErrors("finishedThisJob").value.getOrElse(""), lang, "lastWorkDate"))))
          .replaceError("hoursPerWeek","number.invalid",FormError("hoursPerWeek","number.invalid", Seq(labelForEmployment(formWithErrors("finishedThisJob").value.getOrElse(""), lang, "hoursPerWeek"))))
          .replaceError("hoursPerWeek","error.restricted.characters",FormError("hoursPerWeek","error.restricted.characters", Seq(labelForEmployment(formWithErrors("finishedThisJob").value.getOrElse(""), lang, "hoursPerWeek"))))
          .replaceError("", "jobStartDate.required", FormError("jobStartDate", errorRequired))
          .replaceError("startJobBeforeClaimDate", errorRequired, FormError("startJobBeforeClaimDate", errorRequired,Seq(claim.dateOfClaim.fold("")(dmy => displayPlaybackDatesFormat(lang,dmy - 1 months)))))
        BadRequest(views.html.s8_employment.g3_jobDetails(form)(lang))
      },jobDetails => claim.update(jobs.update(jobDetails)) -> Redirect(routes.G5LastWage.present(iterationID)))
  }
}