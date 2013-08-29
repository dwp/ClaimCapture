package controllers.s2_about_you

import play.api.mvc._
import models.view.{Navigable, CachedClaim}
import models.domain._
import controllers.Mappings.no

object AboutYou extends Controller with CachedClaim with Navigable {
  def completed = claiming { implicit claim => implicit request =>
    track(models.domain.AboutYou) { implicit claim => Ok(views.html.s2_about_you.g8_completed()) }
  }

  def completedSubmit = claiming { implicit claim => implicit request =>
    val yourDetailsVisible = claim.questionGroup(YourDetails) match {
      case Some(y: YourDetails) => y.alwaysLivedUK == no
      case _ => true
    }

    val nrOfCompletedQuestionGroups = claim.completedQuestionGroups(models.domain.AboutYou).distinct.size

    if (yourDetailsVisible && nrOfCompletedQuestionGroups == 6) Redirect(controllers.s3_your_partner.routes.G1YourPartnerPersonalDetails.present())
    else if (!yourDetailsVisible && nrOfCompletedQuestionGroups == 5) Redirect(controllers.s3_your_partner.routes.G1YourPartnerPersonalDetails.present())
    else Redirect(routes.G1YourDetails.present())
  }
}