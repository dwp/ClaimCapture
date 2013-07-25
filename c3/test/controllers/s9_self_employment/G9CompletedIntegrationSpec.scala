package controllers.s9_self_employment

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{BrowserMatchers, Formulate}
import java.util.concurrent.TimeUnit
import scala.concurrent.duration.Duration
import utils.pageobjects.s9_self_employment._
import controllers.ClaimScenarioFactory
import org.specs2.execute.PendingUntilFixed

class G9CompletedIntegrationSpec extends Specification with Tags with PendingUntilFixed {

  "Self Employment" should {
    "be presented" in new WithBrowser with G9CompletedPagePageContext {
      page goToThePage ()
    }.pendingUntilFixed("WIP")

    "contain the completed forms" in new WithBrowser with G1AboutSelfEmploymentPageContext {
      val claim = ClaimScenarioFactory.s8otherMoney
      page goToThePage()
      page fillPageWith claim
      val g2 = page submitPage()
      //println("title g2 " + g2.pageTitle)
      val g9 = g2 goToPage(new G9CompletedPage(browser))
      //println("title g9 " + g2.pageTitle)
      g9.listCompletedForms.size mustEqual 1
    }.pendingUntilFixed("WIP")
  } section "integration"
}