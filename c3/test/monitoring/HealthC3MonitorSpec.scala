package monitoring

import org.specs2.mutable._
import utils.{LightFakeApplication, WithBrowser}
import utils.pageobjects.PageObjects
import utils.pageobjects.s_eligibility.GBenefitsPage
import monitor.HealthMonitor

class HealthC3MonitorSpec extends Specification {
  section("unit")
  "Health Monitor during tests" should {
    "must report an unhealthy database" in new WithBrowser(app = LightFakeApplication.fa) with PageObjects {
      val page = GBenefitsPage(context)
      page goToThePage()
      app.injector.instanceOf(classOf[HealthMonitor]).runHealthChecks().get("c3-transaction-db").get.isHealthy must beFalse
    }

    "must report a healthy cache" in new WithBrowser(app = LightFakeApplication.fa) with PageObjects {
      val page = GBenefitsPage(context)
      page goToThePage()
      app.injector.instanceOf(classOf[HealthMonitor]).runHealthChecks().get("c3-cache").get.isHealthy must beTrue
    }

    "must report a unhealthy IL3 connection" in new WithBrowser(app = LightFakeApplication.fa) with PageObjects {
      val page = GBenefitsPage(context)
      page goToThePage()
      app.injector.instanceOf(classOf[HealthMonitor]).runHealthChecks().get("c3-il3-connection").get.isHealthy must beFalse
    }
  }
  section("unit")
}
