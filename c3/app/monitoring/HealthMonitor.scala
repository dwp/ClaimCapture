package monitoring

import com.codahale.metrics.health.{HealthCheck, HealthCheckRegistry}
import collection.JavaConversions._
import scala.collection.immutable.SortedMap

object HealthMonitor  {
  val registry = new HealthCheckRegistry()

  def register(label: String, healthCheck: HealthCheck) {
    registry.register(label, healthCheck)
  }

  def runHealthChecks() = {
    SortedMap(registry.runHealthChecks().toSeq: _*)
  }
}

