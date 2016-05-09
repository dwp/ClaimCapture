package monitoring

import app.ConfigProperties._
import play.api.Play.current
import monitor.{HealthMonitor, MonitorRegistration}

trait C3MonitorRegistration extends MonitorRegistration {
  override def getFrequency: Int = getIntProperty("metrics.frequency")

  override def isLogMetrics: Boolean = getBooleanProperty("metrics.slf4j")

  override def isLogHealth: Boolean = getBooleanProperty("health.logging")

  def getHealthMonitor: HealthMonitor = {
    current.injector.instanceOf[HealthMonitor]
  }

  override def registerHealthChecks() {
    val healthMonitor = getHealthMonitor
    healthMonitor.register("c3-transaction-db", current.injector.instanceOf[ClaimTransactionCheck])
    healthMonitor.register("c3-cache", new CacheCheck)
    healthMonitor.register("c3-il3-connection", new ClaimReceivedConnectionCheck)
  }
}
