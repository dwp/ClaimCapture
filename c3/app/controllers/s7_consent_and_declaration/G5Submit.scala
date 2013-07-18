package controllers.s7_consent_and_declaration

import play.api.mvc.Controller
import controllers.Routing
import models.view.CachedClaim
import models.domain._
import models.domain.Claim

object G5Submit extends Controller with Routing with CachedClaim{

  override val route = Submit.id -> controllers.s7_consent_and_declaration.routes.G5Submit.present

  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(models.domain.ConsentAndDeclaration.id)

  def present = claiming {
    implicit claim => implicit request =>
      Ok(views.html.s7_consent_and_declaration.g5_submit(completedQuestionGroups))
  }

  def submit = claiming { implicit claim => implicit request =>
    Redirect("/submit")
  }
}
