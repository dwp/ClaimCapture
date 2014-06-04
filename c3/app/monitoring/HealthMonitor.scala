package monitoring

import com.codahale.metrics.health.{HealthCheck, HealthCheckRegistry}
import scala.collection.SortedMap
import collection.JavaConversions._

object HealthMonitor  {
  val healthChecks = new HealthCheckRegistry()

  def register(label: String, healthCheck: HealthCheck) {
    healthChecks.register(label, healthCheck)
  }

  def runHealthChecks(): SortedMap[String, HealthCheck.Result] = {
    SortedMap(healthChecks.runHealthChecks().toSeq: _*)
  }
}
