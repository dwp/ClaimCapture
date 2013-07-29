package controllers.s9_self_employment

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import utils.pageobjects.s9_self_employment.{G6ChildcareProvidersContactDetailsPage, G4SelfEmploymentPensionsAndExpensesPageContext}
import utils.pageobjects.ClaimScenario
import controllers.ClaimScenarioFactory


class G4SelfEmploymentPensionsAndExpensesIntegrationSpec extends Specification with Tags {

  "About Self Employment" should {
    "be presented" in new WithBrowser with G4SelfEmploymentPensionsAndExpensesPageContext {
      page goToThePage ()
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

  }

}
