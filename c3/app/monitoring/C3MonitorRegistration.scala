package monitoring

import app.ConfigProperties._
import utils.Injector
import monitor.{HealthMonitor, MonitorRegistration}

trait C3MonitorRegistration extends MonitorRegistration {
  this: Injector =>

  override def getFrequency: Int = getProperty("metrics.frequency", default = 1)

  override def isLogMetrics: Boolean = getProperty("metrics.slf4j", default = false)

  override def isLogHealth: Boolean = getProperty("health.logging", default = false)

  def getHealthMonitor: HealthMonitor = {
    resolve(classOf[HealthMonitor])
  }

  override def registerHealthChecks() {
    resolve(classOf[HealthMonitor]).register("c3-transaction-db", resolve(classOf[ClaimTransactionCheck]))
    resolve(classOf[HealthMonitor]).register("c3-cache", new CacheCheck)
    resolve(classOf[HealthMonitor]).register("c3-il3-connection", new ClaimReceivedConnectionCheck)
  }
}