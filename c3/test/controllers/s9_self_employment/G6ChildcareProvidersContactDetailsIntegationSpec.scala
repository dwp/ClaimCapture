package controllers.s9_self_employment

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import utils.pageobjects.s9_self_employment._
import controllers.ClaimScenarioFactory
import utils.pageobjects.ClaimScenario

class G6ChildcareProvidersContactDetailsIntegationSpec extends Specification with Tags {
  "Childcare provider's contact Details" should {
    "be presented" in new WithBrowser with G6ChildcareProvidersContactDetailsPageContext {
      page goToThePage ()
    }

    "contain the completed forms" in new WithBrowser with G1AboutSelfEmploymentPageContext {
      val claim = ClaimScenarioFactory.s9SelfEmployment
      page goToThePage()
      page fillPageWith claim
      val g2 = page submitPage()
      val g8 = g2 goToPage(new G6ChildcareProvidersContactDetailsPage(browser))
      g8.listCompletedForms.size mustEqual 1
    }

    "contain errors on invalid submission" in {
      "invalid postcode" in new WithBrowser with G6ChildcareProvidersContactDetailsPageContext {
        val claim = new ClaimScenario
        claim.SelfEmployedChildcareProviderPostcode = "INVALID"
        page goToThePage ()
        page fillPageWith claim
        val pageWithErrors = page.submitPage()
        pageWithErrors.listErrors.size mustEqual 1
        pageWithErrors.listErrors(0).contains("postcode")
      }
    }
    
    "navigate to next page on valid submission" in new WithBrowser with G6ChildcareProvidersContactDetailsPageContext {
      val claim = ClaimScenarioFactory.s9SelfEmployment
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage()

      nextPage must not(beAnInstanceOf[G9CompletedPage])
    }
  } section "integration"
}