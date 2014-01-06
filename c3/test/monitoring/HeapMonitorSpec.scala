package monitoring

import org.specs2.mutable.{Tags, Specification}
import org.specs2.time.NoTimeConversions
import specs2.akka.AkkaTestkitSpecs2Support
import org.specs2.mock.Mockito
import scala.concurrent.duration._
import akka.actor.Props
import app._

class HeapMonitorSpec extends Specification with Tags with Mockito with NoTimeConversions {
  "HeapMonitor" should {
    "produce a info message for low usage" in new AkkaTestkitSpecs2Support {
      val runtime = loadRuntime(free = 3345, total = 3961)
      within(10 seconds) {
        val actor = system.actorOf(Props(classOf[HeapMonitor], runtime), name = "heap-monitor")
        actor ! HeapStats
        expectMsgType[Info] must be equalTo Info("Heap (Used : 616, Free : 3345, Total : 3961, Used : 16 %)")
      }
    }

    "produce a warn message for high usage" in new AkkaTestkitSpecs2Support {
      val runtime = loadRuntime(free = 100, total = 3961)
      within(10 seconds) {
        val actor = system.actorOf(Props(classOf[HeapMonitor], runtime), name = "heap-monitor")
        actor ! HeapStats
        expectMsgType[Warn] must be equalTo Warn("Heap (Used : 3861, Free : 100, Total : 3961, Used : 97 %)")
      }
    }
  } section("unit", "monitoring")


  private def loadRuntime(free: Int, total: Int): Runtime = {
    val runtime = mock[Runtime]
    runtime.freeMemory returns convertToBytes(free)
    runtime.totalMemory returns convertToBytes(total)
    runtime
  }
}
