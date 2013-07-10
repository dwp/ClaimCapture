package controllers.s7_consent_and_declaration

import play.api.mvc.Controller
import controllers.Routing
import models.view.CachedClaim
import models.domain._
import models.domain.Claim

object G6Error extends Controller with Routing with CachedClaim{

  override val route = AdditionalInfo.id -> controllers.s7_consent_and_declaration.routes.G5Submit.present


  def present = claiming {
    implicit claim => implicit request =>


      Ok(views.html.s7_consent_and_declaration.g6_error())
  }


}
