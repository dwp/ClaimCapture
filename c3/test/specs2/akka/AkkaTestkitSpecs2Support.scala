package specs2.akka

import akka.testkit.{ImplicitSender, TestKit}
import akka.actor.ActorSystem
import org.specs2.mutable.After

abstract class AkkaTestkitSpecs2Support extends TestKit(ActorSystem()) with ImplicitSender with After {
  def after = system.shutdown()
}
