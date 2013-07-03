package controllers.s3_your_partner

import models.domain.MoreAboutYourPartner
import play.api.mvc.Controller
import controllers.Routing
import models.view.CachedClaim

object G3MoreAboutYourPartner extends Controller with Routing with CachedClaim {

  override val route = MoreAboutYourPartner.id -> routes.G3MoreAboutYourPartner.present

  def present = claiming {
    implicit claim => implicit request =>

      Ok("present")
  }

  def submit = claiming {
    implicit claim => implicit request =>

      Ok("submit")
  }

}
