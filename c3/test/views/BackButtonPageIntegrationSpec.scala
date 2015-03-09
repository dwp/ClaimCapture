package views

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithApplication
import play.api.test.WithBrowser
import utils.pageobjects.PageObjects
import utils.pageobjects.circumstances.s1_about_you.G1ReportAChangeInYourCircumstancesPage
import utils.pageobjects.circumstances.s2_report_changes.G1ReportChangesPage
import utils.pageobjects.s1_carers_allowance.{G2EligibilityPage, G1BenefitsPage}
import scala.collection.JavaConversions._


class BackButtonPageIntegrationSpec extends Specification with Tags {

  "Back button page" should {
    "show if browser back button clicked in claim Thank You page" in new WithBrowser with PageObjects{

      val benefits = new G1BenefitsPage(context).goToThePage()
      val hours = new G2EligibilityPage(context).goToThePage()

      browser goTo "/thankyou/apply-carers"
      browser title() mustEqual "Application complete"

      browser.getCookies.toSet.exists(cookie => cookie.getName == "application-finished" && cookie.getValue == "true") must beTrue

      browser executeScript "window.history.back()"

      browser url() mustEqual "/back-button-page"
    }

    "show if browser back button clicked in circs Thank You page" in new WithBrowser with PageObjects{

      val benefits = new G1ReportAChangeInYourCircumstancesPage(context).goToThePage()
      val report = new G1ReportChangesPage(context).goToThePage()

      browser goTo "/thankyou/change-carers"
      browser title() mustEqual "Change complete"

      browser.getCookies.toSet.exists(cookie => cookie.getName == "application-finished" && cookie.getValue == "true") must beTrue

      browser executeScript "window.history.back()"

      browser url() mustEqual "/back-button-page"
    }
  }

}
