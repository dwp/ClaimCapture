package controllers.s7_consent_and_declaration

import play.api.mvc.Controller
import models.view.CachedClaim

object G6Error extends Controller with CachedClaim{

  //override val route = AdditionalInfo.id -> controllers.s7_consent_and_declaration.routes.G5Submit.present

  def present = claiming { implicit claim => implicit request =>
    Ok(views.html.s7_consent_and_declaration.g6_error())
  }
}