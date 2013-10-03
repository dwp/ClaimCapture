package jmx

import akka.actor.Props
import play.api.Play.current
import play.api.libs.concurrent.Akka

object JMXActors {
  val claimInspector = Akka.system.actorOf(Props[ClaimInspector], name = "claim-inspector")
}
