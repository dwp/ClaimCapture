package controllers.submission

import play.api.mvc.{AnyContent, Action}
import models.view.CachedClaim
import services.ClaimTransactionComponent
import services.submission.AsyncClaimSubmissionService
import services.async.AsyncActors
import models.domain.Claim
import play.api.mvc.Results._

trait AsyncSubmittable extends ClaimSubmittable with ClaimTransactionComponent {

  this: CachedClaim =>

  val claimTransaction = new ClaimTransaction

  def submit:Action[AnyContent] = claimingWithCheck { implicit claim => implicit request => implicit lang =>

    val transId = getTransactionIdAndRegisterGenerated(copyInstance(claim))

    val updatedClaim = copyInstance(claim withTransactionId transId)

    AsyncActors.asyncManagerActor ! updatedClaim

    updatedClaim -> Redirect(StatusRoutingController.redirectSubmitting(updatedClaim))
  }

  def getTransactionIdAndRegisterGenerated(claim:Claim) = {
    val transId = claimTransaction.generateId
    claimTransaction.registerId(transId,AsyncClaimSubmissionService.GENERATED,claimType(claim))
    transId
  }
}

