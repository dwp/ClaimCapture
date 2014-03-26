package services.async

import akka.actor.Actor
import models.domain.Claim
import services.submission.{WebServiceClientComponent, AsyncClaimSubmissionService}
import services.ClaimTransactionComponent

class AsyncSubmissionActor extends Actor{

  val asyncSubmissionService = new AsyncClaimSubmissionService with ClaimTransactionComponent with WebServiceClientComponent {
    val webServiceClient = new WebServiceClient
    val claimTransaction = new ClaimTransaction
  }

  override def receive: Actor.Receive = {
    case claim:Claim => asyncSubmissionService.submission(claim)
  }
}
