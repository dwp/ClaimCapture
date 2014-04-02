package controllers.submission

import play.api.mvc.{AnyContent, Action}
import models.view.CachedClaim
import services.ClaimTransactionComponent
import services.submission.AsyncClaimSubmissionService
import services.async.AsyncActors
import models.domain.Claim
import play.api.mvc.Results._
import play.api.Logger
import monitoring.BotChecking

trait AsyncSubmittable extends ClaimSubmittable with ClaimTransactionComponent  {

  this: CachedClaim with BotChecking =>

  val claimTransaction = new ClaimTransaction

  def submit:Action[AnyContent] = claiming { implicit claim => implicit request => implicit lang =>

    if (isHoneyPotBot(claim)) {
      // Only log honeypot for now.
      // May send to an error page in the future
      Logger.warn(s"Honeypot ! User-Agent : ${request.headers.get("User-Agent").orNull}")
    }

    if (isSpeedBot(claim)) {
      // Only log speed check for now.
      // May send to an error page in the future
      Logger.warn(s"Speed check ! User-Agent : ${request.headers.get("User-Agent").orNull}")
    }

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

