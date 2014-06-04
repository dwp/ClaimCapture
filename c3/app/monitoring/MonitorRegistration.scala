package monitoring

import app.ConfigProperties._
import com.codahale.metrics.Slf4jReporter
import com.kenshoo.play.metrics.MetricsRegistry
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit
import utils.Injector

object MonitorRegistration extends Injector {

  def registerReporters()  {
    if (getProperty("metrics.slf4j", default = false)) {
      Slf4jReporter.forRegistry(MetricsRegistry.default)
        .outputTo(LoggerFactory.getLogger("application"))
        .convertRatesTo(TimeUnit.SECONDS)
        .convertDurationsTo(TimeUnit.MILLISECONDS)
        .build()
        .start(1, TimeUnit.MINUTES)
    }
  }

  def registerHealthChecks() {
    HealthMonitor.register("c3-transaction-db", resolve(classOf[ClaimTransactionCheck]))
  }

}
