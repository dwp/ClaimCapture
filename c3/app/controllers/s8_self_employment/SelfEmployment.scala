package controllers.s8_self_employment

import play.api.mvc._
import models.view._
import play.api.templates.Html
import models.domain.Claim

object SelfEmployment extends Controller with CachedClaim {

  def whenSectionVisible(f: => SimpleResult[Html])(implicit claim: Claim) = {
    if (claim.isSectionVisible(models.domain.SelfEmployment)) f
    else Redirect(controllers.s9_other_money.routes.G1AboutOtherMoney.present())
  }

  def completed = claiming { implicit claim => implicit request =>
    whenSectionVisible(Ok(views.html.s8_self_employment.g9_completed(claim.completedQuestionGroups(models.domain.SelfEmployment))))
  }

  def completedSubmit = claiming { implicit claim => implicit request =>
    Redirect(claim.nextSection(models.domain.SelfEmployment).firstPage)
  }
}