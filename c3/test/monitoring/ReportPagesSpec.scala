package monitoring

import org.specs2.mutable._
import utils.WithBrowser

class ReportPagesSpec extends Specification {
  section("unit")
  "Application" should {
    "should respond to ping" in new WithBrowser {
      browser goTo "/report/ping"
      browser.pageSource must contain("ping")
    }

    "should report health" in new WithBrowser {
      browser goTo "/report/health"
      browser.pageSource must contain("isHealthy")
    }

    "should report metrics" in new WithBrowser {
      browser goTo "/report/metrics"
      browser.pageSource must contain("MetricsFilter")
    }
  }
  section("unit")
}
