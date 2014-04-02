package controllers.s11_consent_and_declaration

import play.api.mvc._
import models.view.CachedClaim
import models.view.Navigable
import controllers.submission.{AsyncSubmissionController, ClaimSubmittable, AsyncClaimSubmissionController}
import models.view.CachedClaim._
import play.api.mvc.Call
import models.domain.Claim

object G5Submit extends Controller with CachedClaim with Navigable {

  val claimSubmission:ClaimSubmittable = if (AsyncSubmissionController.asyncCondition) new AsyncClaimSubmissionController else new ClaimSyncSubmit

  def present = claimingWithCheck { implicit claim => implicit request => implicit lang =>
    track(models.domain.Submit) { implicit claim => Ok(views.html.s11_consent_and_declaration.g5_submit()) }
  }

  def submit = claimingWithCheck { implicit claim => implicit request => implicit lang =>
    claimSubmission.submitAction(claim)
  }
}


class ClaimSyncSubmit extends Controller with CachedClaim with ClaimSubmittable{

  override def submitAction(claim:Claim): Either[Result, ClaimResult] = Redirect(routes.G7Submitting.present())
}




