package controllers.s3_your_partner

import play.api.mvc.Controller
import controllers.Routing
import models.view.CachedClaim
import models.domain.YourPartnerContactDetails

object G2YourPartnerContactDetails extends Controller with Routing with CachedClaim {

  override val route = YourPartnerContactDetails.id -> routes.G2YourPartnerContactDetails.present

  def present = claiming {
    implicit claim => implicit request =>
      Ok("present")
  }

  def submit = claiming {
    implicit claim => implicit request =>
      Ok("submit")
  }

}
