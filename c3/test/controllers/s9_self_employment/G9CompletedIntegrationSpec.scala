package controllers.s9_self_employment

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{BrowserMatchers, Formulate}
import java.util.concurrent.TimeUnit
import scala.concurrent.duration.Duration
import utils.pageobjects.s9_self_employment._
import controllers.ClaimScenarioFactory
import org.specs2.execute.PendingUntilFixed

class G9CompletedIntegrationSpec extends Specification with Tags {

  "Self Employment" should {
    "be presented" in new WithBrowser with G9CompletedPageContext {
      page goToThePage ()
    }

    "contain the completed forms" in new WithBrowser with G1AboutSelfEmploymentPageContext {
      val claim = ClaimScenarioFactory.s9SelfEmployment
      page goToThePage()
      page fillPageWith claim
      val g2 = page submitPage()
      val g9 = g2 goToPage(new G9CompletedPage(browser))
      g9.listCompletedForms.size mustEqual 1
    }

    "navigate back to previous page" in new WithBrowser with G9CompletedPageContext {
      page goToThePage()
      page.goBack() must beAnInstanceOf[G8CareProvidersContactDetailsPage]
    }

    "navigate to next page on valid submission" in new WithBrowser with G9CompletedPageContext {
      val claim = ClaimScenarioFactory.s8otherMoney
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage()

      nextPage must not(beAnInstanceOf[G9CompletedPage])
    }
  } section "integration"
}