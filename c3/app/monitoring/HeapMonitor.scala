package monitoring

import akka.actor.Actor
import app._

class HeapMonitor(runTime: Runtime) extends Actor {

  def receive: Actor.Receive = {
    case HeapStats =>
      val free = convertToMB(runTime.freeMemory)
      val total = convertToMB(runTime.totalMemory)
      val used = total - free
      val percentUsed = (used * 100.0) / total

      val message: String = f"Heap (Used : $used, Free : $free, Total : $total, Used : $percentUsed%2.0f %%)"
      val stats =
        if (percentUsed < 85)
          Info(message)
        else
          Warn(message)

      sender ! stats
  }
}

case class HeapStats()