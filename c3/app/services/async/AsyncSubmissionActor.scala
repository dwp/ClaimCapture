package services.async

import scala.language.postfixOps
import scala.concurrent.duration._
import akka.actor.{Props, OneForOneStrategy, SupervisorStrategy, Actor}
import models.domain.Claim
import services.submission.AsyncClaimSubmissionComponent
import akka.actor.SupervisorStrategy.Restart
import play.api.Logger
import utils.Injector

class AsyncSubmissionActor extends Actor with Injector {

  val asyncSubmissionService = resolve(classOf[AsyncClaimSubmissionComponent])

  override def receive: Actor.Receive = {
    case claim:Claim =>
      Logger.debug(s"Processing ${claim.key} ${claim.uuid} with transactionId [${claim.transactionId.get}]")
      asyncSubmissionService.submission(claim)
  }
}



class AsyncSubmissionManagerActor(childActorProps:Props) extends Actor {


  override def supervisorStrategy: SupervisorStrategy = OneForOneStrategy(maxNrOfRetries = 2, withinTimeRange = 30 seconds){
    case e:Exception => Restart
  }

  override def receive: Actor.Receive = {
    case claim:Claim =>
      Logger.debug(s"Received claim with transactionId [${claim.transactionId.get}]")
      context.actorOf(childActorProps) ! claim
  }
}
