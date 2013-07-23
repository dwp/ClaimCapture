package controllers.s6_education

import language.reflectiveCalls
import controllers.Mappings._
import models.domain.AddressOfSchoolCollegeOrUniversity
import models.domain.Claim
import models.view.CachedClaim
import play.api.data.Form
import play.api.data.Forms.mapping
import play.api.data.Forms._
import play.api.mvc.Controller
import utils.helpers.CarersForm.formBinding
import Education._

object G2AddressOfSchoolCollegeOrUniversity extends Controller with CachedClaim {
  val form = Form(
    mapping(
      call(routes.G2AddressOfSchoolCollegeOrUniversity.present()),
      "nameOfSchoolCollegeOrUniversity" -> optional(text),
      "nameOfMainTeacherOrTutor" -> optional(text),
      "address" -> optional(address),
      "postcode" -> optional(text verifying validPostcode),
      "phoneNumber" -> optional(text verifying validPhoneNumber),
      "faxNumber" -> optional(text verifying validPhoneNumber)
    )(AddressOfSchoolCollegeOrUniversity.apply)(AddressOfSchoolCollegeOrUniversity.unapply))

  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(AddressOfSchoolCollegeOrUniversity)

  def present = claiming { implicit claim => implicit request =>
    whenVisible(claim)(() => Ok(views.html.s6_education.g2_addressOfSchoolCollegeOrUniversity(form.fill(AddressOfSchoolCollegeOrUniversity), completedQuestionGroups)))
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s6_education.g2_addressOfSchoolCollegeOrUniversity(formWithErrors, completedQuestionGroups)),
      f => claim.update(f) -> Redirect(routes.Education.completed()))
  }
}