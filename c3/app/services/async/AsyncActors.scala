package services.async

import akka.actor.{Props, ActorSystem}


object AsyncActors {

  val actorSystem = ActorSystem("async-actor-system")

  def asyncActor = actorSystem.actorOf(Props[AsyncSubmissionActor])
}
