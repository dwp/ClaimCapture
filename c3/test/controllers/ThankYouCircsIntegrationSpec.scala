package controllers

import play.api.test.WithBrowser
import utils.pageobjects.PageObjects
import utils.pageobjects.circumstances.s2_report_changes.G7BreaksInCarePage
import org.specs2.mutable.{Tags, Specification}

class ThankYouCircsIntegrationSpec extends Specification with Tags {

  "Change Thank You" should {
    "present 'Thank You' page" in new WithBrowser with BrowserMatchers {
      browser.goTo("/thankyou/change-carers")
      urlMustEqual("/thankyou/change-carers")
    }

    "display breaks in care message" in new WithBrowser with PageObjects{
      val page =  G7BreaksInCarePage(context)
      val claim = CircumstancesScenarioFactory.reportBreakFromCaringBreaksInCareEndedNo
      page goToThePage()
      page fillPageWith claim
      page submitPage()

      browser.goTo("/thankyou/change-carers")
      browser.find("#breaksInCareMessageTitle").getText must beEqualTo("What you need to do now")
    }

    "should not display breaks in care message when breaks in care has ended" in new WithBrowser with PageObjects{
      val page =  G7BreaksInCarePage(context)
      val claim = CircumstancesScenarioFactory.reportBreakFromCaringBreaksInCareEndedNoAndExpectToStartCaringNo
      page goToThePage()
      page fillPageWith claim
      page submitPage()

      browser.goTo("/thankyou/change-carers")
      browser.find("#breaksInCareMessageTitle").getText must beNull
   }
  } section "integration"
}
