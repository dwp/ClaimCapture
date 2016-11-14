package controllers.s_employment

import controllers.mappings.AddressMappings
import play.api.Play._

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
import play.api.i18n._

object GJobDetails extends Controller with CachedClaim with Navigable with I18nSupport {
  override val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
  val form = Form(mapping(
    "iterationID" -> carersNonEmptyText,
    "employerName"-> carersNonEmptyText(maxLength = 60),
    "phoneNumber" -> nonEmptyText.verifying(validPhoneNumberRequired),
    "address" -> address(AddressMappings.EMPLOYMENT),
    "postcode" -> optional(text verifying(restrictedPostCodeAddressStringText, validPostcode)),
    "startJobBeforeClaimDate" -> nonEmptyText.verifying(validYesNo),
    "jobStartDate" -> optional(dayMonthYear.verifying(validDate)),
    "finishedThisJob" -> nonEmptyText.verifying(validYesNo),
    "lastWorkDate" -> optional(dayMonthYear.verifying(validDate)),
    "p45LeavingDate" -> optional(dayMonthYear.verifying(validDateOnly)),
    "hoursPerWeek" -> optional(carersText(maxLength = JobDetails.maxLengthHoursWorked).verifying(validDecimalNumber))
  )(JobDetails.apply)(JobDetails.unapply)
    .verifying("lastWorkDate.required", JobDetails.validateLastWorkDate _)
    .verifying("jobStartDate.required", JobDetails.validateJobStartDate _)
  )

  def job(iterationID: String) = claimingWithCheck { implicit claim => implicit request => implicit request2lang =>
    claim.questionGroup(Jobs) match {
      case Some(js: Jobs) if js.job(iterationID).isDefined =>
        track(JobDetails) { implicit claim => Ok(views.html.s_employment.g_jobDetails(form.fillWithJobID(JobDetails, iterationID))) }
      case _ =>
        Redirect(routes.GBeenEmployed.present())
    }
  }

  def present(iterationID: String) = claimingWithCheck { implicit claim => implicit request => implicit request2lang =>
    track(JobDetails) { implicit claim => Ok(views.html.s_employment.g_jobDetails(form.fillWithJobID(JobDetails, iterationID))) }
  }

  def submit = claimingWithCheckInIteration { iterationID => implicit claim => implicit request => implicit request2lang =>
    form.bindEncrypted.fold(
      formWithErrors =>{
        val form = formWithErrors
          .replaceError("", "jobStartDate.required", FormError("jobStartDate", errorRequired))
          .replaceError("startJobBeforeClaimDate", errorRequired, FormError("startJobBeforeClaimDate", errorRequired, Seq(claim.dateOfClaim.fold("")(dmy => displayPlaybackDatesFormat(request2lang, dmy - 1 months)))))
          .replaceError("", "lastWorkDate.required", FormError("lastWorkDate", errorRequired, Seq(labelForEmployment(formWithErrors("finishedThisJob").value.getOrElse(""), request2lang, "lastWorkDate"))))
          .replaceError("hoursPerWeek", "number.invalid", FormError("hoursPerWeek", "number.invalid", Seq(labelForEmployment(formWithErrors("finishedThisJob").value.getOrElse(""), request2lang, "hoursPerWeek"))))
          .replaceError("hoursPerWeek", "error.restricted.characters", FormError("hoursPerWeek", "error.restricted.characters", Seq(labelForEmployment(formWithErrors("finishedThisJob").value.getOrElse(""), request2lang, "hoursPerWeek"))))
          .replaceError("finishedThisJob", errorRequired, FormError("finishedThisJob", errorRequired))
        BadRequest(views.html.s_employment.g_jobDetails(form))
      },jobDetails => claim.update(jobs.update(jobDetails.copy(postcode = Some(formatPostCode(jobDetails.postcode.getOrElse("")))))) -> Redirect(routes.GLastWage.present(iterationID)))
  }
}
