package monitoring

import app.ConfigProperties._
import play.api.Play.current
import monitor.{HealthMonitor, MonitorRegistration}

trait C3MonitorRegistration extends MonitorRegistration {
  override def getFrequency: Int = getProperty("metrics.frequency", default = 1)

  override def isLogMetrics: Boolean = getProperty("metrics.slf4j", default = false)

  override def isLogHealth: Boolean = getProperty("health.logging", default = false)

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
