package controllers.s_education

import play.api.Play._

import language.reflectiveCalls
import play.api.mvc.{AnyContent, Request, Controller}
import play.api.data.Form
import play.api.data.Forms._
import models.view.{Navigable, CachedClaim}
import models.domain.YourCourseDetails
import utils.helpers.CarersForm._
import controllers.mappings.Mappings._
import models.view.ClaimHandling.ClaimResult
import play.api.data.FormError
import models.domain.Claim
import controllers.CarersForms._
import play.api.i18n._


object GYourCourseDetails extends Controller with CachedClaim with Navigable with I18nSupport {
  override val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
  val form = Form(mapping(
    "beenInEducationSinceClaimDate" -> nonEmptyText.verifying(validYesNo),
    "courseTitle" -> optional(carersNonEmptyText(maxLength = 75)),
    "nameOfSchoolCollegeOrUniversity" -> optional(carersNonEmptyText(maxLength = sixty)),
    "nameOfMainTeacherOrTutor" -> optional(carersNonEmptyText(maxLength = sixty)),
    "courseContactNumber" -> optional(text verifying validPhoneNumber),
    "startDate" -> optional(dayMonthYear.verifying(validDate)),
    "expectedEndDate" -> optional(dayMonthYear.verifying(validDate))
  )(YourCourseDetails.apply)(YourCourseDetails.unapply)
    .verifying("courseTitle.required", YourCourseDetails.validateTitle _)
    .verifying("nameOfSchoolCollegeOrUniversity.required", YourCourseDetails.validateNameOfSchool _)
    .verifying("nameOfMainTeacherOrTutor.required", YourCourseDetails.validateNameOfTeacher _)
    .verifying("startDate.required", YourCourseDetails.validateStartDate _)
    .verifying("expectedEndDate.required", YourCourseDetails.validateExpectedEndDate _)
  )

  def present = claimingWithCheck {implicit claim => implicit request => implicit lang => 
    presentConditionally {
      track(YourCourseDetails) { implicit claim => Ok(views.html.s_education.g_yourCourseDetails(form.fill(YourCourseDetails))) }
    }
  }

  private def presentConditionally(c: => ClaimResult)(implicit claim: Claim, request: Request[AnyContent]): ClaimResult = {
    if (models.domain.Education.visible) c
    else redirect(claim, request)
  }

  private def redirect( claim: Claim, request: Request[AnyContent]): ClaimResult =
    claim -> Redirect("/employment/employment")

  def submit = claimingWithCheck {implicit claim => implicit request => implicit lang => 
    form.bindEncrypted.fold(
      formWithErrors => {
        val formWithErrorsUpdate = formWithErrors
          .replaceError("beenInEducationSinceClaimDate", errorRequired, FormError("beenInEducationSinceClaimDate", errorRequired))
          .replaceError("", "courseTitle.required", FormError("courseTitle", errorRequired))
          .replaceError("", "nameOfSchoolCollegeOrUniversity.required", FormError("nameOfSchoolCollegeOrUniversity", errorRequired))
          .replaceError("", "nameOfMainTeacherOrTutor.required", FormError("nameOfMainTeacherOrTutor", errorRequired))
          .replaceError("", "startDate.required", FormError("startDate", errorRequired))
          .replaceError("", "expectedEndDate.required", FormError("expectedEndDate", errorRequired))
        BadRequest(views.html.s_education.g_yourCourseDetails(formWithErrorsUpdate))
      },
      yourCourseDetails => claim.update(yourCourseDetails) -> Redirect(controllers.s_self_employment.routes.GEmployment.present()))
  } withPreview()
}
