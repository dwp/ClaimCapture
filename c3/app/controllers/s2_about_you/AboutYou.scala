package controllers.s2_about_you

import play.api.mvc._
import models.view.CachedClaim
import models.domain._
import scala.collection.immutable.ListMap
import play.api.mvc.Call
import controllers.Mappings.no

object AboutYou extends Controller with CachedClaim {
  val route: ListMap[String, Call] = ListMap(
    G1YourDetails,
    G2ContactDetails,
    G3TimeOutsideUK,
    G4ClaimDate,
    G5MoreAboutYou,
    G6Employment,
    G7PropertyAndRent)

  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(models.domain.AboutYou.id)

  def completed = claiming { implicit claim => implicit request =>
    val showYourPartnerSection = claim.isSectionVisible(YourPartner.id)

    Ok(views.html.s2_about_you.g8_completed(completedQuestionGroups, showYourPartnerSection))
  }

  def completedSubmit = claiming { implicit claim => implicit request =>
    val showYourPartnerSection = claim.isSectionVisible(YourPartner.id)

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