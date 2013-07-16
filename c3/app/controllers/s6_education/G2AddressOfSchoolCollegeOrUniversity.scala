package controllers.s6_education

import controllers.Mappings.address
import controllers.Mappings.validPhoneNumber
import controllers.Mappings.validPostcode
import controllers.Routing
import models.domain.AddressOfSchoolCollegeOrUniversity
import models.domain.Claim
import models.view.CachedClaim
import play.api.data.Form
import play.api.data.Forms.mapping
import play.api.data.Forms.optional
import play.api.data.Forms.text
import play.api.mvc.Controller
import utils.helpers.CarersForm.formBinding

object G2AddressOfSchoolCollegeOrUniversity extends Controller with Routing with CachedClaim {

  override val route = AddressOfSchoolCollegeOrUniversity.id -> routes.G2AddressOfSchoolCollegeOrUniversity.present

  val form = Form(
    mapping(
      "nameOfSchoolCollegeOrUniversity" -> optional(text),
      "nameOfMainTeacherOrTutor" -> optional(text),
      "address" -> optional(address),
      "postcode" -> optional(text verifying validPostcode),
      "phoneNumber" -> optional(text verifying validPhoneNumber),
      "faxNumber" -> optional(text verifying validPhoneNumber)
    )(AddressOfSchoolCollegeOrUniversity.apply)(AddressOfSchoolCollegeOrUniversity.unapply))

  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(AddressOfSchoolCollegeOrUniversity)

  def present = claiming {
    implicit claim => implicit request =>
      val currentForm: Form[AddressOfSchoolCollegeOrUniversity] = claim.questionGroup(AddressOfSchoolCollegeOrUniversity) match {
        case Some(m: AddressOfSchoolCollegeOrUniversity) => form.fill(m)
        case _ => form
      }

      Ok(views.html.s6_education.g2_addressOfSchoolCollegeOrUniversity(currentForm, completedQuestionGroups))
  }

  def submit = claiming {
    implicit claim => implicit request =>
      form.bindEncrypted.fold(
        formWithErrors => BadRequest(views.html.s6_education.g2_addressOfSchoolCollegeOrUniversity(formWithErrors, completedQuestionGroups)),
        f => claim.update(f) -> Redirect(routes.Education.completed()))
  }
}