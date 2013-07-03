package controllers.s3_your_partner

import play.api.mvc.Controller
import controllers.Routing
import models.view.CachedClaim
import play.api.data.Form
import play.api.data.Forms._
import controllers.Mappings._

import models.domain._
import play.api.data.Form
import play.api.data.Forms._
import controllers.Mappings._
import play.api.mvc.Controller
import models.view.CachedClaim
import controllers.Routing
import utils.helpers.CarersForm._


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

      val currentForm: Form[YourPartnerPersonalDetails] = claim.questionGroup(YourPartnerPersonalDetails.id) match {
        case Some(t: YourPartnerPersonalDetails) => form.fill(t)
        case _ => form
      }

      Ok(views.html.s3_your_partner.g1_yourPartnerPersonalDetails(currentForm))
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s3_your_partner.g1_yourPartnerPersonalDetails(formWithErrors)),
      f => claim.update(f) -> Redirect(routes.G2YourPartnerContactDetails.present))
  }
}
