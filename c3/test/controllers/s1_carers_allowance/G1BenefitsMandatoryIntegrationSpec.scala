package controllers.s1_carers_allowance

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import utils.pageobjects.ClaimScenario
import utils.pageobjects.s1_carers_allowance.G1BenefitsMandatoryPageContext
import utils.pageobjects.s1_carers_allowance.G2HoursMandatoryPage

class G1BenefitsMandatoryIntegrationSpec extends Specification with Tags {
  "Carer's Allowance - Benefits - Integration" should {
    "be presented" in new WithBrowser with G1BenefitsMandatoryPageContext {
      page goToThePage ()
    }

    "contain errors on invalid submission" in {
      "missing mandatory field" in new WithBrowser with G1BenefitsMandatoryPageContext {
        val claim = new ClaimScenario
        claim.CanYouGetCarersAllowanceDoesthePersonYouCareforGetOneofTheseBenefits = ""
        page goToThePage()
        page fillPageWith claim
        val pageWithErrors = page.submitPage()
        pageWithErrors.listErrors.size mustEqual 1
      }
    }
    
    "accept submit if all mandatory fields are populated" in new WithBrowser with G1BenefitsMandatoryPageContext {
      val claim = new ClaimScenario
      claim.CanYouGetCarersAllowanceDoesthePersonYouCareforGetOneofTheseBenefits = "yes"
      page goToThePage()
      page fillPageWith claim
      page submitPage()
    }
    
    "navigate to next page on valid submission" in new WithBrowser with G1BenefitsMandatoryPageContext {
      val claim = new ClaimScenario
      claim.CanYouGetCarersAllowanceDoesthePersonYouCareforGetOneofTheseBenefits = "yes"
      page goToThePage()
      page fillPageWith claim
      
      val nextPage = page submitPage()

      nextPage must beAnInstanceOf[G2HoursMandatoryPage]
    }
  } section "integration"
}