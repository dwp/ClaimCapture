package controllers.s6_education

import play.api.mvc.Controller
import controllers.Routing
import models.view.CachedClaim
import play.api.data.Form
import play.api.data.Forms._
import models.domain.{Claim, YourCourseDetails}
import utils.helpers.CarersForm._
import controllers.Mappings._

object G1YourCourseDetails extends Controller with Routing with CachedClaim {

  override val route = YourCourseDetails.id -> routes.G1YourCourseDetails.present

  val form = Form(
    mapping(
      "courseType" -> optional(text(maxLength = sixty)),
      "courseTitle" -> optional(text(maxLength = sixty)),
      "startDate" -> optional(dayMonthYear.verifying(validDateOnly)),
      "expectedEndDate" -> optional(dayMonthYear.verifying(validDateOnly)),
      "finishedDate" -> optional(dayMonthYear.verifying(validDateOnly)),
      "studentReferenceNumber" -> optional(text(maxLength = sixty))
    )(YourCourseDetails.apply)(YourCourseDetails.unapply))

  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(YourCourseDetails)

  def present = claiming {
    implicit claim => implicit request =>

      Education.whenVisible(claim)(() => {
        val preFilledForm: Form[YourCourseDetails] = claim.questionGroup(YourCourseDetails) match {
          case Some(t: YourCourseDetails) => form.fill(t)
          case _ => form
        }

        Ok(views.html.s6_education.g1_yourCourseDetails(preFilledForm, completedQuestionGroups))
      })
  }

  def submit = claiming {
    implicit claim => implicit request =>
      form.bindEncrypted.fold(
        formWithErrors => BadRequest(views.html.s6_education.g1_yourCourseDetails(formWithErrors, completedQuestionGroups)),
        yourCourseDetails => claim.update(yourCourseDetails) -> Redirect(routes.G2AddressOfSchoolCollegeOrUniversity.present()))
  }

}