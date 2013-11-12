package controllers.s6_education

import language.reflectiveCalls
import play.api.mvc.{AnyContent, Request, Controller}
import play.api.data.Form
import play.api.data.Forms._
import models.view.{Navigable, CachedClaim}
import models.domain.{Claim, YourCourseDetails}
import utils.helpers.CarersForm._
import controllers.Mappings._
import controllers.CarersForms._

object G1YourCourseDetails extends Controller with CachedClaim with Navigable {
  val form = Form(mapping(
    "courseType" -> optional(carersText(maxLength = 50)),
    "courseTitle" -> optional(carersText(maxLength = 50)),
    "startDate" -> optional(dayMonthYear.verifying(validDateOnly)),
    "expectedEndDate" -> optional(dayMonthYear.verifying(validDateOnly)),
    "finishedDate" -> optional(dayMonthYear.verifying(validDateOnly)),
    "studentReferenceNumber" -> optional(text(maxLength = sixty))
  )(YourCourseDetails.apply)(YourCourseDetails.unapply))

  def present = claiming { implicit claim => implicit request =>
    presentConditionally {
      track(YourCourseDetails) { implicit claim => Ok(views.html.s6_education.g1_yourCourseDetails(form.fill(YourCourseDetails))) }
    }
  }

  def presentConditionally(c: => ClaimResult)(implicit claim: Claim, request: Request[AnyContent]): ClaimResult = {
    if (models.domain.Education.visible) c
    else redirect
  }

  def redirect(implicit claim: Claim, request: Request[AnyContent]): ClaimResult =
    claim -> Redirect("/employment/been-employed")

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s6_education.g1_yourCourseDetails(formWithErrors)),
      yourCourseDetails => claim.update(yourCourseDetails) -> Redirect(routes.G2AddressOfSchoolCollegeOrUniversity.present()))
  }
}