package jmx

import akka.actor.Props

object JMXActors {
  import play.api.libs.concurrent.Akka

  val claimInspector = Akka.system.actorOf(Props[ClaimInspector], name = "claim-inspector")
}
