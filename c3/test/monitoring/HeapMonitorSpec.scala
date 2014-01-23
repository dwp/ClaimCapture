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
      val runtime = loadRuntime(max = 253, free = 4, total = 15)
      within(10 seconds) {
        val actor = system.actorOf(Props(classOf[HeapMonitor], runtime), name = "heap-monitor")
        actor ! HeapStats
        expectMsgType[Info] must be equalTo Info("Heap (Max : 253, Used : 11, Free : 4, Total : 15, Used :  4 %)")
      }
    }

    "produce a warn message for high usage" in new AkkaTestkitSpecs2Support {
      val runtime = loadRuntime(max = 253, free = 1, total = 250)
      within(10 seconds) {
        val actor = system.actorOf(Props(classOf[HeapMonitor], runtime), name = "heap-monitor")
        actor ! HeapStats
        expectMsgType[Warn] must be equalTo Warn("Heap (Max : 253, Used : 249, Free : 1, Total : 250, Used : 98 %)")
      }
    }
  } section("unit", "monitoring")


  private def loadRuntime(max: Long, free: Long, total: Long): Runtime = {
    val runtime = mock[Runtime]
    runtime.maxMemory returns convertToBytes(max)
    runtime.freeMemory returns convertToBytes(free)
    runtime.totalMemory returns convertToBytes(total)
    runtime
  }
}
