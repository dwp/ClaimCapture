package views

import org.specs2.mutable.{Tags, Specification}
import utils.{WebDriverHelper, WithBrowser}
import utils.pageobjects.PageObjects
import utils.pageobjects.circumstances.s1_start_of_process.{G2ReportAChangeInYourCircumstancesPage, G1ReportChangesPage}
import utils.pageobjects.s0_carers_allowance.{G1BenefitsPage, G2EligibilityPage}

import scala.collection.JavaConversions._
import utils.WithJsBrowser

class BackButtonPageIntegrationSpec extends Specification with Tags {

  "Back button page" should {
    "show if browser back button clicked in claim Thank You page" in new WithJsBrowser  with PageObjects{

      val benefits = new G1BenefitsPage(context).goToThePage()
      val hours = new G2EligibilityPage(context).goToThePage()

      browser goTo "/thankyou/apply-carers"
      browser url() mustEqual "/thankyou/apply-carers"

      browser.getCookies.toSet.exists(cookie => cookie.getName == "application-finished" && cookie.getValue == "true") must beTrue

      browser executeScript "window.history.back()"

      browser url() mustEqual "/back-button-page"
    }

    "show if browser back button clicked in circs Thank You page" in new WithJsBrowser  with PageObjects{

      val report = new G1ReportChangesPage(context).goToThePage()
      val benefits = new G2ReportAChangeInYourCircumstancesPage(context).goToThePage()

      browser goTo "/thankyou/change-carers"
      browser url() mustEqual "/thankyou/change-carers"

      browser.getCookies.toSet.exists(cookie => cookie.getName == "application-finished" && cookie.getValue == "true") must beTrue

      browser executeScript "window.history.back()"

      browser url() mustEqual "/back-button-page"
    }
  }

}
