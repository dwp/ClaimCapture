package controllers.s3_your_partner

import play.api.mvc.Controller
import controllers.Routing
import models.view.CachedClaim
import play.api.data.Form
import play.api.data.Forms._
import controllers.Mappings._
import models.domain.YourPartnerPersonalDetails


object G1YourPartnerPersonalDetails extends Controller with Routing with CachedClaim {

  override val route = YourPartnerPersonalDetails.id -> routes.G1YourPartnerPersonalDetails.present

  val form = Form(
    mapping(
      "title" -> nonEmptyText,
      "firstName" -> nonEmptyText(maxLength = sixty),
      "middleName" -> optional(text(maxLength = sixty)),
      "surname" -> nonEmptyText(maxLength = sixty),
      "otherNames" -> optional(text(maxLength = sixty)),
      "nationalInsuranceNumber" -> optional(nino.verifying(validNinoOnly)),
      "dateOfBirth" -> dayMonthYear.verifying(validDate),
      "nationality" -> optional(text(maxLength = sixty)),
      "liveAtSameAddress" -> nonEmptyText
    )(YourPartnerPersonalDetails.apply)(YourPartnerPersonalDetails.unapply))


  def present = claiming {
    implicit claim => implicit request =>
      Ok("present")
  }

  def submit = claiming {
    implicit claim => implicit request =>
      Ok("submit")
  }

}
