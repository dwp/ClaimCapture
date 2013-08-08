package controllers.s10_pay_details

import play.api.mvc.{Result, SimpleResult, Controller}
import models.view.CachedClaim
import models.domain.Claim
import play.api.templates.Html

object PayDetails extends Controller with CachedClaim {

  def whenSectionVisible(f: => Either[play.api.mvc.Result,(models.domain.Claim, play.api.mvc.Result)])(implicit claim: Claim):Either[play.api.mvc.Result,(models.domain.Claim, play.api.mvc.Result)] = {
    if (claim.isSectionVisible(models.domain.PayDetails)) f
    else Redirect(claim.nextSection(models.domain.PayDetails).firstPage)
  }

  def completed = claiming { implicit claim => implicit request =>
    whenSectionVisible(Ok(views.html.s10_pay_details.g3_completed(claim.completedQuestionGroups(models.domain.PayDetails))))
  }


  def completedSubmit = claiming { implicit claim => implicit request =>
    Redirect(claim.nextSection(models.domain.PayDetails).firstPage)
  }

}