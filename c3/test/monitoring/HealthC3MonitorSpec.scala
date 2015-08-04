package monitoring

import org.specs2.mutable.Specification
import utils.{LightFakeApplication, WithBrowser}
import utils.pageobjects.PageObjects
import utils.pageobjects.s_eligibility.GBenefitsPage
import services.submission.{TestHealthMonitor, MockInjector}
import monitor.HealthMonitor

class HealthC3MonitorSpec extends Specification with MockInjector {

  "Health Monitor during tests" should {

    "must report an unhealthy database" in new WithBrowser(app = LightFakeApplication(withGlobal = Some(global))) with PageObjects {
      val page = GBenefitsPage(context)
      page goToThePage()
      resolve(classOf[HealthMonitor]).runHealthChecks().get("c3-transaction-db").get.isHealthy must beFalse
    }
    "must report a healthy cache" in new WithBrowser(app = LightFakeApplication(withGlobal = Some(global))) with PageObjects {
      val page = GBenefitsPage(context)
      page goToThePage()
      TestHealthMonitor.runHealthChecks().get("c3-cache").get.isHealthy must beTrue
    }
    "must report a unhealthy IL3 connection" in new WithBrowser(app = LightFakeApplication(withGlobal = Some(global))) with PageObjects {
      val page = GBenefitsPage(context)
      page goToThePage()
      TestHealthMonitor.runHealthChecks().get("c3-il3-connection").get.isHealthy must beFalse
    }
  } section "unit"

}
