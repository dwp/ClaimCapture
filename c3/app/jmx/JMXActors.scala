package jmx

import akka.actor.{ActorSystem, Props}
import jmx.claiminspector.ClaimInspector

object JMXActors {
  val actorSystem = ActorSystem("jmx-actor-system")

  val claimInspector = actorSystem.actorOf(Props[ClaimInspector], name = "claim-inspector")
}
