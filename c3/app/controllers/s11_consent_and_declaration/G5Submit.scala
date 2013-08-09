package controllers.s11_consent_and_declaration

import play.api.mvc.Controller
import models.view.CachedClaim
import models.domain.Submit

object G5Submit extends Controller with ConsentAndDeclarationRouting with CachedClaim {
  def present = claiming { implicit claim => implicit request =>
    Ok(views.html.s11_consent_and_declaration.g5_submit(completedQuestionGroups(Submit)))
  }

  def submit = claiming { implicit claim => implicit request =>
    Redirect("/submit")
  }
}