package monitoring

import org.specs2.mutable.Specification
import play.api.test.WithBrowser
import utils.pageobjects.PageObjects
import utils.pageobjects.s1_carers_allowance.G1BenefitsPage

class HealthMonitorSpec extends Specification {
  "Health Monitor" should {
    "must report a healthy database" in new WithBrowser with PageObjects {
      val page = G1BenefitsPage(context)
      page goToThePage()
      HealthMonitor.runHealthChecks().get("c3-transaction-db").get.isHealthy must beTrue
    }
  } section "unit"
}
