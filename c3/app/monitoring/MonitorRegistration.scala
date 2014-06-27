package monitoring

import app.ConfigProperties._
import com.codahale.metrics.Slf4jReporter
import com.kenshoo.play.metrics.MetricsRegistry
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit
import utils.Injector
import play.api.libs.concurrent.Akka
import scala.concurrent.duration._
import play.api.Play.current
import scala.concurrent.ExecutionContext
import ExecutionContext.Implicits.global

trait MonitorRegistration {
  this: Injector =>

  def registerReporters() {
    val frequency = getProperty("metrics.frequency", default = 1)
    if (getProperty("metrics.slf4j", default = false)) {
      Slf4jReporter.forRegistry(MetricsRegistry.default)
        .outputTo(LoggerFactory.getLogger("application"))
        .convertRatesTo(TimeUnit.SECONDS)
        .convertDurationsTo(TimeUnit.MILLISECONDS)
        .build()
        .start(getProperty("metrics.frequency", default = frequency), TimeUnit.MINUTES)
    }

    if (getProperty("health.logging", default = false)) {
      Akka.system.scheduler.schedule(frequency.minute, frequency.minute, new Runnable {
        override def run(): Unit = resolve(classOf[HealthMonitor]).reportHealth()
      })
    }
  }

  def registerHealthChecks() {
    resolve(classOf[HealthMonitor]).register("c3-transaction-db", resolve(classOf[ClaimTransactionCheck]))
    resolve(classOf[HealthMonitor]).register("c3-cache", new CacheCheck)
  }

}
