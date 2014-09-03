package controllers.s6_education

import language.reflectiveCalls
import play.api.mvc.{AnyContent, Request, Controller}
import play.api.data.Form
import play.api.data.Forms._
import models.view.{Navigable, CachedClaim}
import models.domain.YourCourseDetails
import utils.helpers.CarersForm._
import controllers.Mappings._
import models.view.CachedClaim.ClaimResult
import play.api.data.FormError
import models.domain.Claim
import controllers.CarersForms._


object G1YourCourseDetails extends Controller with CachedClaim with Navigable {
  val form = Form(mapping(
    "beenInEducationSinceClaimDate" -> nonEmptyText.verifying(validYesNo),
    "courseTitle" -> optional(carersNonEmptyText(maxLength = 50)),
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

  def present = claimingWithCheck { implicit claim => implicit request => implicit lang =>
    presentConditionally {
      track(YourCourseDetails) { implicit claim => Ok(views.html.s6_education.g1_yourCourseDetails(form.fill(YourCourseDetails))) }
    }
  }

  def presentConditionally(c: => ClaimResult)(implicit claim: Claim, request: Request[AnyContent]): ClaimResult = {
    if (models.domain.Education.visible) c
    else redirect
  }

  def redirect(implicit claim: Claim, request: Request[AnyContent]): ClaimResult =
    claim -> Redirect("/employment/employment")

  def submit = claimingWithCheck { implicit claim => implicit request => implicit lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val formWithErrorsUpdate = formWithErrors
          .replaceError("beenInEducationSinceClaimDate", "error.required", FormError("beenInEducationSinceClaimDate", "error.required"))
          .replaceError("", "courseTitle.required", FormError("courseTitle", "error.required"))
          .replaceError("", "nameOfSchoolCollegeOrUniversity.required", FormError("nameOfSchoolCollegeOrUniversity", "error.required"))
          .replaceError("", "nameOfMainTeacherOrTutor.required", FormError("nameOfMainTeacherOrTutor", "error.required"))
          .replaceError("", "startDate.required", FormError("startDate", "error.required"))
          .replaceError("", "expectedEndDate.required", FormError("expectedEndDate", "error.required"))
        BadRequest(views.html.s6_education.g1_yourCourseDetails(formWithErrorsUpdate))
      },
      yourCourseDetails => claim.update(yourCourseDetails) -> Redirect(controllers.s7_employment.routes.G1Employment.present()))
  }
}