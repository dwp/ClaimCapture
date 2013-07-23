package controllers.s10_consent_and_declaration

import play.api.mvc.Controller
import models.view.CachedClaim
import models.domain._
import models.domain.Claim

object G5Submit extends Controller with CachedClaim{
  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(models.domain.ConsentAndDeclaration)

  def present = claiming { implicit claim => implicit request =>
    Ok(views.html.s7_consent_and_declaration.g5_submit(completedQuestionGroups))
  }

  def submit = claiming { implicit claim => implicit request =>
    Redirect("/submit")
  }
}