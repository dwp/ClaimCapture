package controllers.s9_self_employment

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import utils.pageobjects.s9_self_employment._
import controllers.ClaimScenarioFactory
import utils.pageobjects.ClaimScenario
import utils.pageobjects.s2_about_you.{G7PropertyAndRentPage, G4ClaimDatePageContext}
import utils.pageobjects.s8_other_money.G1AboutOtherMoneyPage

class G8CareProvidersContactDetailsIntegationSpec extends Specification with Tags {
  "Care provider's contact Details" should {
    "be presented" in new WithBrowser with G8CareProvidersContactDetailsPageContext {
      page goToThePage ()
    }

    "not be presented if section not visible" in new WithBrowser with G4ClaimDatePageContext {
      val claim = ClaimScenarioFactory.s2AnsweringNoToQuestions()
      page goToThePage()
      page runClaimWith (claim, G7PropertyAndRentPage.title, waitForPage = true)

      val nextPage = page goToPage( throwException = false, page = new G8CareProvidersContactDetailsPage(browser))
      nextPage must beAnInstanceOf[G1AboutOtherMoneyPage]
    }

    "contain the completed forms" in new WithBrowser with G1AboutSelfEmploymentPageContext {
      val claim = ClaimScenarioFactory.s9SelfEmployment
      page goToThePage()
      page fillPageWith claim
      val g2 = page submitPage()
      val g8 = g2 goToPage(new G8CareProvidersContactDetailsPage(browser))
      g8.listCompletedForms.size mustEqual 1
    }

    "contain errors on invalid submission" in {
      "invalid postcode" in new WithBrowser with G8CareProvidersContactDetailsPageContext {
        val claim = new ClaimScenario
        claim.SelfEmployedCareProviderPostcode = "INVALID"
        page goToThePage ()
        page fillPageWith claim
        val pageWithErrors = page.submitPage()
        pageWithErrors.listErrors.size mustEqual 1
        pageWithErrors.listErrors(0).contains("postcode")
      }
    }
    
    "navigate back to previous page" in new WithBrowser with G7ExpensesWhileAtWorkPageContext {
      val claim = ClaimScenarioFactory.s9SelfEmployment
      page goToThePage()
      page fillPageWith claim
      val g8 = page submitPage()
      g8.goBack() must beAnInstanceOf[G7ExpensesWhileAtWorkPage]
    }
    
    "navigate to next page on valid submission" in new WithBrowser with G8CareProvidersContactDetailsPageContext {
      val claim = ClaimScenarioFactory.s9SelfEmployment
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage()

      nextPage must not(beAnInstanceOf[G8CareProvidersContactDetailsPage])
    }
  } section "integration"
}