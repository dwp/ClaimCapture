package controllers.circs.s1_identification

import play.api.mvc.Controller
import models.view.{Navigable, CachedCircs}

object Identification extends Controller with CachedCircs with Navigable {

  def completed = executeOnForm { implicit claim => implicit request =>
    val completedQuestionGroups = claim.completedQuestionGroups(models.domain.CircumstancesIdentification)
    if (completedQuestionGroups.isEmpty) Redirect(routes.G1AboutYou.present())
    else track(models.domain.CircumstancesIdentification) { implicit claim => Ok(views.html.circs.s1_identification.g4_completed()) }
  }

  def submit = executeOnForm { implicit claim => implicit request =>
    Redirect(controllers.circs.s2_additional_info.routes.G1OtherChangeInfo.present())
  }
}
