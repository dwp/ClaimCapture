package monitoring

import org.specs2.mutable.Specification
import play.api.test.{FakeApplication, WithBrowser}
import utils.pageobjects.PageObjects
import utils.pageobjects.s1_carers_allowance.G1BenefitsPage
import services.submission.MockInjector

class HealthMonitorSpec extends Specification with MockInjector {
  "Health Monitor" should {
    "must report a healthy database" in new WithBrowser with PageObjects {
      val page = G1BenefitsPage(context)
      page goToThePage()
      ProdHealthMonitor.runHealthChecks().get("c3-transaction-db").get.isHealthy must beTrue
    }

    "must report an unhealthy database" in new WithBrowser(app = FakeApplication(withGlobal = Some(global))) with PageObjects {
      val page = G1BenefitsPage(context)
      page goToThePage()
      resolve(classOf[HealthMonitor]).runHealthChecks().get("c3-transaction-db").get.isHealthy must beFalse
    }
    "must report a healthy cache" in new WithBrowser with PageObjects {
      val page = G1BenefitsPage(context)
      page goToThePage()
      ProdHealthMonitor.runHealthChecks().get("c3-cache").get.isHealthy must beTrue
    }
  } section "unit"
}
