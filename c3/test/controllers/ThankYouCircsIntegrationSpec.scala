package controllers

import utils.WithBrowser
import utils.pageobjects.PageObjects
import utils.pageobjects.circumstances.report_changes.GBreaksInCarePage
import org.specs2.mutable._

class   ThankYouCircsIntegrationSpec extends Specification {
  section("integration")
  "Change Thank You" should {
    "present 'Thank You' page" in new WithBrowser with BrowserMatchers {
      browser.goTo("/thankyou/change-carers")
      urlMustEqual("/thankyou/change-carers")
    }

    "display breaks in care message" in new WithBrowser with PageObjects {
      val page =  GBreaksInCarePage(context)
      val claim = CircumstancesScenarioFactory.reportBreakFromCaringBreaksInCareEndedNo
      page goToThePage()
      page fillPageWith claim
      page submitPage()

      browser.goTo("/thankyou/change-carers")
      browser.find("#breaksInCareMessageTitle").getText.nonEmpty must beTrue
    }

    "should not display breaks in care message when breaks in care has ended" in new WithBrowser with PageObjects {
      val page =  GBreaksInCarePage(context)
      val claim = CircumstancesScenarioFactory.reportBreakFromCaringBreaksInCareEndedNoAndExpectToStartCaringNo
      page goToThePage()
      page fillPageWith claim
      page submitPage()

      browser.goTo("/thankyou/change-carers")
      browser.find("#breaksInCareMessageTitle").getText must beNull
   }
  }
  section("integration")
}
