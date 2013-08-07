package controllers.s2_about_you

import play.api.mvc._
import models.view.CachedClaim
import models.domain._
import controllers.Mappings.no
import controllers.Routing

object AboutYou extends Controller with CachedClaim with Routing {
  override def route(qgi: QuestionGroup.Identifier) = qgi match {
    case YourDetails => routes.G1YourDetails.present()
    case ContactDetails => routes.G2ContactDetails.present()
    case TimeOutsideUK => routes.G3TimeOutsideUK.present()
    case ClaimDate => routes.G4ClaimDate.present()
    case MoreAboutYou => routes.G5MoreAboutYou.present()
    case Employment => routes.G6Employment.present()
    case PropertyAndRent => routes.G7PropertyAndRent.present()
  }

  def completed = claiming { implicit claim => implicit request =>
    Ok(views.html.s2_about_you.g8_completed(completedQuestionGroups.map(qg => qg -> route(qg))))
  }

  def completedSubmit = claiming { implicit claim => implicit request =>
    val yourDetailsVisible = claim.questionGroup(YourDetails) match {
      case Some(y: YourDetails) => y.alwaysLivedUK == no
      case _ => true
    }

    val nrOfCompletedQuestionGroups = completedQuestionGroups.distinct.size

    if (yourDetailsVisible && nrOfCompletedQuestionGroups == 7) Redirect(claim.nextSection(models.domain.AboutYou).firstPage)
    else if (!yourDetailsVisible && nrOfCompletedQuestionGroups == 6) Redirect(claim.nextSection(models.domain.AboutYou).firstPage)
    else Redirect(routes.G1YourDetails.present())
  }

  private def completedQuestionGroups(implicit claim: Claim): List[QuestionGroup] = {
    claim.completedQuestionGroups(models.domain.AboutYou)
  }
}