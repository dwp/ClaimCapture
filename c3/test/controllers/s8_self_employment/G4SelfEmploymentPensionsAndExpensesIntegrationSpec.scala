package controllers.s8_self_employment

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import utils.pageobjects.s8_self_employment.{G4SelfEmploymentPensionsAndExpensesPage, G6ChildcareProvidersContactDetailsPage, G4SelfEmploymentPensionsAndExpensesPageContext}
import utils.pageobjects.ClaimScenario
import controllers.ClaimScenarioFactory
import utils.pageobjects.s2_about_you.{G7PropertyAndRentPage, G4ClaimDatePageContext}
import utils.pageobjects.s9_other_money.G1AboutOtherMoneyPage


class G4SelfEmploymentPensionsAndExpensesIntegrationSpec extends Specification with Tags {

  "About Self Employment" should {
    "be presented" in new WithBrowser with G4SelfEmploymentPensionsAndExpensesPageContext {
      page goToThePage ()
    }

    "not be presented if section not visible" in new WithBrowser with G4ClaimDatePageContext {
      val claim = ClaimScenarioFactory.s2AnsweringNoToQuestions()
      page goToThePage()
      page runClaimWith (claim, G7PropertyAndRentPage.title, waitForPage = true)

      val nextPage = page goToPage( throwException = false, page = new G4SelfEmploymentPensionsAndExpensesPage(browser))
      nextPage must beAnInstanceOf[G1AboutOtherMoneyPage]
    }

    "contain errors on invalid submission" in {
      "missing mandatory field" in new WithBrowser with G4SelfEmploymentPensionsAndExpensesPageContext {
        val claim = new ClaimScenario
        claim.SelfEmployedDoYouPayTowardsPensionScheme = ""
        claim.SelfEmployedDoYouPayAnyonetoLookAfterYourChild = ""
        claim.SelfEmployedDoYouPayAnyonetoLookAfterPersonYouCareFor = ""
        page goToThePage()
        page fillPageWith claim
        val pageWithErrors = page.submitPage()
        pageWithErrors.listErrors.size mustEqual 3
      }
    }

    "accept submit if all mandatory fields are populated" in new WithBrowser with G4SelfEmploymentPensionsAndExpensesPageContext {
      val claim = ClaimScenarioFactory.s9SelfEmploymentPensionsAndExpenses
      page goToThePage()
      page fillPageWith claim
      page submitPage()
    }

    "navigate to next page on valid submission" in new WithBrowser with G4SelfEmploymentPensionsAndExpensesPageContext {
      val claim = ClaimScenarioFactory.s9SelfEmploymentPensionsAndExpenses
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage()

      nextPage must not(beAnInstanceOf[G6ChildcareProvidersContactDetailsPage])
    }
  } section("integration", models.domain.SelfEmployment.id)
}