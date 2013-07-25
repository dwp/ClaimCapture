package controllers.s2_about_you

import play.api.mvc._
import models.view.CachedClaim
import models.domain._
import controllers.Mappings.no

object AboutYou extends Controller with CachedClaim {
  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(models.domain.AboutYou)

  def completed = claiming { implicit claim => implicit request =>
    Ok(views.html.s2_about_you.g8_completed(completedQuestionGroups))
  }

  def completedSubmit = claiming { implicit claim => implicit request =>

    val yourDetailsVisible = claim.questionGroup(YourDetails) match {
      case Some(y: YourDetails) => y.alwaysLivedUK == no
      case _ => true
    }

    val nrOfCompletedQuestionGroups = completedQuestionGroups.distinct.size

    if(yourDetailsVisible && nrOfCompletedQuestionGroups == 7) Redirect(claim.nextSection(models.domain.AboutYou).firstPage)
    else if (!yourDetailsVisible && nrOfCompletedQuestionGroups == 6) Redirect(claim.nextSection(models.domain.AboutYou).firstPage)
    else Redirect(routes.G1YourDetails.present())
  }
}