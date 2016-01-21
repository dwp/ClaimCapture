package views

import org.specs2.mutable._
import utils.pageobjects.PageObjects
import utils.pageobjects.circumstances.start_of_process.{GReportAChangeInYourCircumstancesPage, GReportChangesPage}
import utils.pageobjects.s_eligibility.{GBenefitsPage, GEligibilityPage}

import scala.collection.JavaConversions._
import utils.WithJsBrowser

class BackButtonPageIntegrationSpec extends Specification {
  section("unit")
  "Back button page" should {
    "show if browser back button clicked in claim Thank You page" in new WithJsBrowser with PageObjects {
      val benefits = new GBenefitsPage(context).goToThePage()
      val hours = new GEligibilityPage(context).goToThePage()

      browser goTo "/thankyou/apply-carers"
      browser url() mustEqual "/thankyou/apply-carers"

      browser.getCookies.toSet.exists(cookie => cookie.getName == "application-finished" && cookie.getValue == "true") must beTrue

      browser executeScript "window.history.back()"

      browser url() mustEqual "/back-button-page"
    }

    "show if browser back button clicked in circs Thank You page" in new WithJsBrowser with PageObjects {
      val report = new GReportChangesPage(context).goToThePage()
      val benefits = new GReportAChangeInYourCircumstancesPage(context).goToThePage()

      browser goTo "/thankyou/change-carers"
      browser url() mustEqual "/thankyou/change-carers"

      browser.getCookies.toSet.exists(cookie => cookie.getName == "application-finished" && cookie.getValue == "true") must beTrue

      browser executeScript "window.history.back()"

      browser url() mustEqual "/circs-back-button-page"
    }
  }
  section("unit")
}
