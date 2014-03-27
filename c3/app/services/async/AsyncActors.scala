package services.async

import akka.actor.{Props, ActorSystem}


object AsyncActors {

  val actorSystem = ActorSystem("async-actor-system")

  val asyncActorProps = Props[AsyncSubmissionActor]

  val asyncManagerActorProps = Props(classOf[AsyncSubmissionManagerActor],asyncActorProps)

  val asyncManagerActor = actorSystem.actorOf(asyncManagerActorProps)

}
