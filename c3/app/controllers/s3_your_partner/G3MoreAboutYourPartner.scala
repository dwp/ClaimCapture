package controllers.s3_your_partner

import controllers.Mappings.dayMonthYear
import controllers.Mappings.nino
import controllers.Mappings.sixty
import controllers.Mappings.validDate
import controllers.Mappings.validNinoOnly
import controllers.Routing
import models.domain.MoreAboutYourPartner
import models.domain.YourPartnerPersonalDetails
import models.view.CachedClaim
import play.api.data.Form
import play.api.data.Forms.mapping
import play.api.data.Forms.nonEmptyText
import play.api.data.Forms.optional
import play.api.data.Forms.text
import play.api.mvc.Controller

object G3MoreAboutYourPartner extends Controller with Routing with CachedClaim {

  override val route = MoreAboutYourPartner.id -> routes.G3MoreAboutYourPartner.present

  val form = Form(
    mapping(
      "dateStartedLivingTogether" -> dayMonthYear.verifying(validDate),
      "separatedFromPartner" -> nonEmptyText
    )(MoreAboutYourPartner.apply)(MoreAboutYourPartner.unapply))

  def present = claiming {
    implicit claim => implicit request =>

      val currentForm: Form[MoreAboutYourPartner] = claim.questionGroup(MoreAboutYourPartner.id) match {
        case Some(t: MoreAboutYourPartner) => form.fill(t)
        case _ => form
      }

      Ok(views.html.s3_your_partner.g3_moreAboutYourPartner(currentForm))
  }

  def submit = claiming {
    implicit claim => implicit request =>

      Ok("submit")
  }

}
