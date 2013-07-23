package controllers.s6_education

import language.reflectiveCalls
import play.api.mvc.Controller
import models.view.CachedClaim
import play.api.data.Form
import play.api.data.Forms._
import models.domain.{Claim, YourCourseDetails}
import utils.helpers.CarersForm._
import controllers.Mappings._
import Education._

object G1YourCourseDetails extends Controller with CachedClaim {
  val form = Form(
    mapping(
      call(routes.G1YourCourseDetails.present()),
      "courseType" -> optional(text(maxLength = sixty)),
      "courseTitle" -> optional(text(maxLength = sixty)),
      "startDate" -> optional(dayMonthYear.verifying(validDateOnly)),
      "expectedEndDate" -> optional(dayMonthYear.verifying(validDateOnly)),
      "finishedDate" -> optional(dayMonthYear.verifying(validDateOnly)),
      "studentReferenceNumber" -> optional(text(maxLength = sixty))
    )(YourCourseDetails.apply)(YourCourseDetails.unapply))

  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(YourCourseDetails)

  def present = claiming { implicit claim => implicit request =>
    whenVisible(claim)(() => Ok(views.html.s6_education.g1_yourCourseDetails(form.fill(YourCourseDetails), completedQuestionGroups)))
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s6_education.g1_yourCourseDetails(formWithErrors, completedQuestionGroups)),
      yourCourseDetails => claim.update(yourCourseDetails) -> Redirect(routes.G2AddressOfSchoolCollegeOrUniversity.present()))
  }
}