package controllers.s9_pay_details

import utils.pageobjects.s9_pay_details.{G1HowWePayYouPageContext, G3PayDetailsCompletedPage, G3PayDetailsCompletedPageContext}
import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{Formulate, ClaimScenarioFactory}

class G3CompletedIntegrationSpec extends Specification with Tags {

  "PayDetails Completion" should {
    "be presented" in new WithBrowser with G3PayDetailsCompletedPageContext {
      page goToThePage()
    }

    "be hidden when having state pension" in new WithBrowser {
      Formulate.claimDate(browser)
      Formulate.moreAboutYou(browser)
      browser.goTo("/payDetails/completed")
      browser.title shouldEqual "Additional Information - Consent And Declaration"
    }


    "contain the completed forms" in new WithBrowser with G1HowWePayYouPageContext {
      val claim = ClaimScenarioFactory.s6PayDetails()
      page goToThePage()
      page fillPageWith claim
      page submitPage()
      val completedPage = page goToPage (new G3PayDetailsCompletedPage(browser))
      completedPage must beAnInstanceOf[G3PayDetailsCompletedPage]
      completedPage.listCompletedForms.size shouldEqual 1
    }

    "navigate back to 'Bank Building Society Details'" in new WithBrowser with G3PayDetailsCompletedPageContext {
      page goToThePage()
      val g2Page = page.goBack()
      g2Page.pageTitle shouldEqual "Bank Building Society Details - Pay Details"
    }

    "navigate to 'Consent And Declaration'" in new WithBrowser with G3PayDetailsCompletedPageContext {
      page goToThePage()
      val consentPage = page.submitPage()
      consentPage.pageTitle shouldEqual "Additional Information - Consent And Declaration"
    }

  } section "integration"


}
