package services.async

import scala.language.postfixOps
import scala.concurrent.duration._
import akka.actor.{Props, OneForOneStrategy, SupervisorStrategy, Actor}
import models.domain.Claim
import services.submission.{WebServiceClientComponent, AsyncClaimSubmissionService}
import services.ClaimTransactionComponent
import akka.actor.SupervisorStrategy.Restart
import play.api.Logger

class AsyncSubmissionActor extends Actor{

  val asyncSubmissionService = new AsyncClaimSubmissionService with ClaimTransactionComponent with WebServiceClientComponent {
    val webServiceClient = new WebServiceClient
    val claimTransaction = new ClaimTransaction
  }

  override def receive: Actor.Receive = {
    case claim:Claim =>
      Logger.debug(s"Processing claim with transactionid:${claim.transactionId.get}")
      asyncSubmissionService.submission(claim)
  }
}



class AsyncSubmissionManagerActor(childActorProps:Props) extends Actor {


  override def supervisorStrategy: SupervisorStrategy = OneForOneStrategy(maxNrOfRetries = 2, withinTimeRange = 30 seconds){
    case e:Exception => Restart
  }

  override def receive: Actor.Receive = {
    case claim:Claim =>
      Logger.debug(s"Received claim with transactionid:${claim.transactionId.get}")
      context.actorOf(childActorProps) ! claim
  }
}
