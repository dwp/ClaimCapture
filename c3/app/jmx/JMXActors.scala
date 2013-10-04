package jmx

import akka.actor.{ActorSystem, Props}
import jmx.claiminspector.{ChangeOfCircsInspector, ClaimInspector}

object JMXActors {
  val actorSystem = ActorSystem("jmx-actor-system")

  val applicationInspector = actorSystem.actorOf(Props[ApplicationInspector], name = "application-inspector")

  val claimInspector = actorSystem.actorOf(Props[ClaimInspector], name = "claim-inspector")

  val changeOfCircsInspector = actorSystem.actorOf(Props[ChangeOfCircsInspector], name = "change-of-circs-inspector")
}
