package controllers.s9_pay_details

import utils.pageobjects.s6_pay_details.{G3CompletedPage, G3CompletedPageContext}
import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.ClaimScenarioFactory
import utils.pageobjects.s9_pay_details.G1HowWePayYouPageContext


class G3CompletedSpec extends Specification with Tags {

  "PayDetails Completion" should {
    "be presented" in new WithBrowser with G3CompletedPageContext {
      page goToThePage()
    }


    "contain the completed forms" in new WithBrowser with G1HowWePayYouPageContext {
      val claim = ClaimScenarioFactory.s6PayDetails()
      page goToThePage()
      page fillPageWith claim
      page submitPage()
      val completedPage = page goToPage (new G3CompletedPage(browser))
      completedPage must beAnInstanceOf[G3CompletedPage]
      completedPage.listCompletedForms.size shouldEqual 1
    }

    "navigate back to 'Bank Building Society Details'" in new WithBrowser with G3CompletedPageContext {
      page goToThePage()
      val g2Page = page.goBack()
      g2Page.pageTitle shouldEqual "Bank Building Society Details - Pay Details"
    }

    "navigate to 'Consent And Declaration'" in new WithBrowser with G3CompletedPageContext {
      page goToThePage()
      val consentPage = page.submitPage()
      consentPage.pageTitle shouldEqual "Additional Information - Consent And Declaration"
    }

  } section "integration"


}
