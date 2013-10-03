package jmx

import akka.actor.{ActorSystem, Props}

object JMXActors {
  val actorSystem = ActorSystem("jmx-actor-system")

  val claimInspector = actorSystem.actorOf(Props[ClaimInspector], name = "claim-inspector")
}
