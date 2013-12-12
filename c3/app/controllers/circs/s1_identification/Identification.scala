package controllers.circs.s1_identification

import play.api.mvc.Controller
import models.view.{Navigable, CachedChangeOfCircs}

object Identification extends Controller with CachedChangeOfCircs with Navigable {
  def completed = claiming { implicit circs => implicit request => implicit lang =>
    val completedQuestionGroups = circs.completedQuestionGroups(models.domain.CircumstancesIdentification)

    if (completedQuestionGroups.isEmpty) Redirect(routes.G1AboutYou.present())
    else track(models.domain.CircumstancesIdentification) {
      implicit circs => Ok(views.html.circs.s1_identification.g4_completed())
    }
  }

  def submit = claiming { implicit circs => implicit request => implicit lang =>
    Redirect(controllers.circs.s2_additional_info.routes.G1OtherChangeInfo.present())
  }
}
