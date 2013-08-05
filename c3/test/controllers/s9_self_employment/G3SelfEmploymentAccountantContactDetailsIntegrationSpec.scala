package controllers.s9_self_employment

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import utils.pageobjects.s9_self_employment.{G3SelfEmploymentAccountantContactDetailsPage, G2SelfEmploymentYourAccountsPage, G6ChildcareProvidersContactDetailsPage, G3SelfEmploymentAccountantContactDetailsPageContext}
import utils.pageobjects.ClaimScenario
import controllers.ClaimScenarioFactory
import utils.pageobjects.s2_about_you.{G7PropertyAndRentPage, G4ClaimDatePageContext}
import utils.pageobjects.s8_other_money.G1AboutOtherMoneyPage

class G3SelfEmploymentAccountantContactDetailsIntegrationSpec extends Specification with Tags {

  "About Self Employment" should {
    "be presented" in new WithBrowser with G3SelfEmploymentAccountantContactDetailsPageContext {
      page goToThePage()
    }

    "not be presented if section not visible" in new WithBrowser with G4ClaimDatePageContext {
      val claim = ClaimScenarioFactory.s2AnsweringNoToQuestions()
      page goToThePage()
      page runClaimWith (claim, G7PropertyAndRentPage.title, waitForPage = true)

      val nextPage = page goToPage( throwException = false, page = new G3SelfEmploymentAccountantContactDetailsPage(browser))
      nextPage must beAnInstanceOf[G1AboutOtherMoneyPage]
    }

    "contain errors on invalid submission" in {
      "missing mandatory field" in new WithBrowser with G3SelfEmploymentAccountantContactDetailsPageContext {
        val claim = new ClaimScenario
        claim.SelfEmployedAccountantName = ""
        claim.SelfEmployedAccountantAddress = ""
        page goToThePage()
        page fillPageWith claim
        val pageWithErrors = page.submitPage()
        pageWithErrors.listErrors.size mustEqual 2
      }
    }

    "accept submit if all mandatory fields are populated" in new WithBrowser with G3SelfEmploymentAccountantContactDetailsPageContext {
      val claim = ClaimScenarioFactory.s9SelfEmploymentAccountantContactDetails
      page goToThePage()
      page fillPageWith claim
      page submitPage()
    }

    "navigate to next page on valid submission" in new WithBrowser with G3SelfEmploymentAccountantContactDetailsPageContext {
      val claim = ClaimScenarioFactory.s9SelfEmploymentAccountantContactDetails
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage()

      nextPage must not(beAnInstanceOf[G6ChildcareProvidersContactDetailsPage])
    }
  }

}
