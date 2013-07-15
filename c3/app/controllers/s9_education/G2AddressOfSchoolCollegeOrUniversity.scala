package controllers.s9_education

import play.api.mvc.Controller
import controllers.Routing
import models.view.CachedClaim
import play.api.data.Form
import play.api.data.Forms._
import controllers.Mappings._
import utils.helpers.CarersForm._
import models.domain.{Claim}
import models.domain.AddressOfSchoolCollegeOrUniversity

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

  def present = claiming { implicit claim => implicit request =>
    Ok("Hello, world!")
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => ???,//BadRequest(views.html.s9_education.g2_addressOfSchoolCollegeOrUniversity(formWithErrors, completedQuestionGroups)),
      addressOfSchoolCollegeOrUniversity => claim.update(AddressOfSchoolCollegeOrUniversity) -> Redirect(routes.G2AddressOfSchoolCollegeOrUniversity.present()))
  }
}