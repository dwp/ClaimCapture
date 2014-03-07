package services.mail

import akka.actor.{Props, ActorSystem}

object EmailActors {
  val actorSystem = ActorSystem("email-actor-system")

  val manager = actorSystem.actorOf(Props[EmailManagerActor], name = "email-manager")
}
