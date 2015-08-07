package controllers.s_self_employment

import play.api.mvc._
import models.view.CachedClaim
import models.domain._
import models.view.Navigable
import play.api.i18n.Lang
import models.view.ClaimHandling.ClaimResult
import controllers.mappings.Mappings._

object SelfEmployment extends Controller with CachedClaim with Navigable {
  def completed = claimingWithCheck {implicit claim =>  implicit request =>  lang =>
    redirect(lang)
  }

  def completedSubmit = claimingWithCheck { implicit claim =>  implicit request =>  lang =>
    redirect(lang)
  }

  def presentConditionally(c: => ClaimResult, lang: Lang)(implicit claim: Claim, request: Request[AnyContent]): ClaimResult = {
    val beenInPreview = claim.previouslySavedClaim.isDefined
    val emp = claim.questionGroup[Employment].getOrElse(Employment())
    //Lazy because they are going to be lazily evaluated on usage only if we've been in preview.
    lazy val previousEmp = claim.previouslySavedClaim.get.questionGroup[Employment]
    lazy val previousSEValue = previousEmp.get.beenSelfEmployedSince1WeekBeforeClaim
    val SEValue = emp.beenSelfEmployedSince1WeekBeforeClaim

    if (models.domain.SelfEmployment.visible && (!beenInPreview || beenInPreview && SEValue == yes && previousSEValue == no)) c
    else redirect(lang)
  }

  private def redirect(lang: Lang)(implicit claim: Claim, request: Request[AnyContent]): ClaimResult =
    claim -> Redirect(controllers.s_employment.routes.GBeenEmployed.present())
}