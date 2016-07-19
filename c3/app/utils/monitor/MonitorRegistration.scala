package utils.monitor

import com.codahale.metrics.{SharedMetricRegistries, Slf4jReporter}
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit
import scala.concurrent.duration._
import play.api.libs.concurrent.Akka
import play.api.Play.current
import scala.concurrent.ExecutionContext
import ExecutionContext.Implicits.global
import akka.actor.Cancellable

object MonitorRegistration {
  val reporter : Slf4jReporter = Slf4jReporter.forRegistry(SharedMetricRegistries.getOrCreate(current.configuration.getString("metrics.name").getOrElse("default")))
    .outputTo(LoggerFactory.getLogger("application"))
    .convertRatesTo(TimeUnit.SECONDS)
    .convertDurationsTo(TimeUnit.MILLISECONDS)
    .build()
}

trait MonitorRegistration {

  def getFrequency: Int

  def isLogMetrics: Boolean

  def isLogHealth: Boolean

  def getHealthMonitor : HealthMonitor

  def registerHealthChecks()

  private lazy val reporter = MonitorRegistration.reporter

  var healthReporter : Cancellable = new Cancellable {
    override def isCancelled: Boolean = true
    override def cancel(): Boolean = true
  }


  def registerReporters() {
    if (isLogMetrics) {
      reporter.start(getFrequency, TimeUnit.MINUTES)
    }

    if (isLogHealth) {
      logHealth()
    }
  }

  def unregisterReporters() {
    reporter.stop()
    healthReporter.cancel()
  }


  def logHealth() {
    healthReporter = Akka.system.scheduler.schedule(10.seconds, getFrequency.minute, new Runnable {
      override def run(): Unit = getHealthMonitor.reportHealth()
    })
  }
}

