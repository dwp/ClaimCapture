package controllers.s2_about_you

import play.api.mvc._
import models.view.CachedClaim
import models.domain._
import controllers.Mappings.no

object AboutYou extends Controller with CachedClaim {
  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(models.domain.AboutYou)

  def completed = claiming { implicit claim => implicit request =>
    val showYourPartnerSection = claim.isSectionVisible(YourPartner)

    Ok(views.html.s2_about_you.g8_completed(completedQuestionGroups, showYourPartnerSection))
  }

  def completedSubmit = claiming { implicit claim => implicit request =>
    val showYourPartnerSection = claim.isSectionVisible(YourPartner)

    claim.questionGroup(YourDetails) match {
      case Some(y: YourDetails) if y.alwaysLivedUK == no && completedQuestionGroups.distinct.size == 7 =>
        if (showYourPartnerSection) Redirect(controllers.s3_your_partner.routes.G1YourPartnerPersonalDetails.present())
        else Redirect(controllers.s4_care_you_provide.routes.G1TheirPersonalDetails.present())

      case Some(_: YourDetails) if completedQuestionGroups.distinct.size == 6 =>
        if (showYourPartnerSection) Redirect(controllers.s3_your_partner.routes.G1YourPartnerPersonalDetails.present())
        else Redirect(controllers.s4_care_you_provide.routes.G1TheirPersonalDetails.present())

      case _ => Redirect(routes.G1YourDetails.present())
    }
  }
}