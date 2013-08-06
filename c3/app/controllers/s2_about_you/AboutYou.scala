package controllers.s2_about_you

import play.api.mvc._
import models.view.CachedClaim
import models.domain._
import controllers.Mappings.no

object AboutYou extends Controller with CachedClaim {
  def completedQuestionGroups(questionGroupIdentifier: QuestionGroup.Identifier)(implicit claim: Claim, request: Request[AnyContent]): List[(QuestionGroup, Call)] = {
    claim.completedQuestionGroups(questionGroupIdentifier).map(qg => qg -> route(qg))
  }

  def completed = claiming { implicit claim => implicit request =>
    Ok(views.html.s2_about_you.g8_completed(claim.completedQuestionGroups(models.domain.AboutYou).map(qg => qg -> route(qg))))
  }

  def completedSubmit = claiming { implicit claim => implicit request =>
    val yourDetailsVisible = claim.questionGroup(YourDetails) match {
      case Some(y: YourDetails) => y.alwaysLivedUK == no
      case _ => true
    }

    val nrOfCompletedQuestionGroups = claim.completedQuestionGroups(models.domain.AboutYou).distinct.size

    if(yourDetailsVisible && nrOfCompletedQuestionGroups == 7) Redirect(claim.nextSection(models.domain.AboutYou).firstPage)
    else if (!yourDetailsVisible && nrOfCompletedQuestionGroups == 6) Redirect(claim.nextSection(models.domain.AboutYou).firstPage)
    else Redirect(routes.G1YourDetails.present())
  }

  private def route(qg: QuestionGroup) = qg.identifier match {
    case YourDetails => routes.G1YourDetails.present()
    case ContactDetails => routes.G2ContactDetails.present()
    case TimeOutsideUK => routes.G3TimeOutsideUK.present()
    case ClaimDate => routes.G4ClaimDate.present()
    case MoreAboutYou => routes.G5MoreAboutYou.present()
    case Employment => routes.G6Employment.present()
    case PropertyAndRent => routes.G7PropertyAndRent.present()
  }
}