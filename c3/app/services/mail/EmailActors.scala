package services.mail

import akka.actor.ActorSystem


object EmailActors {
  val actorSystem = ActorSystem("email-actor-system")

  val manager = actorSystem.actorOf(EmailActorsCreators.emailManagerProps, name = "email-manager")
}
