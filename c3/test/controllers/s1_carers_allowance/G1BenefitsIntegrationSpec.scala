package controllers.s1_carers_allowance

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import utils.pageobjects.TestData
import utils.pageobjects.s1_carers_allowance.G1BenefitsPageContext
import utils.pageobjects.s1_carers_allowance.G2HoursPage

class G1BenefitsIntegrationSpec extends Specification with Tags {
  "Carer's Allowance - Benefits - Integration" should {
    "be presented" in new WithBrowser with G1BenefitsPageContext {
      page goToThePage ()
    }

    "contain errors on invalid submission" in {
      "missing mandatory field" in new WithBrowser with G1BenefitsPageContext {
        val claim = new TestData
        page goToThePage()
        val pageWithErrors = page.submitPage()
        pageWithErrors.listErrors.size mustEqual 1
      }
    }
    
    "accept submit if all mandatory fields are populated" in new WithBrowser with G1BenefitsPageContext {
      val claim = new TestData
      claim.CanYouGetCarersAllowanceDoesthePersonYouCareforGetOneofTheseBenefits = "yes"
      page goToThePage()
      page fillPageWith claim
      page submitPage()
    }
    
    "navigate to next page on valid submission" in new WithBrowser with G1BenefitsPageContext {
      val claim = new TestData
      claim.CanYouGetCarersAllowanceDoesthePersonYouCareforGetOneofTheseBenefits = "yes"
      page goToThePage()
      page fillPageWith claim
      
      val nextPage = page submitPage()

      nextPage must beAnInstanceOf[G2HoursPage]
    }
  } section("integration", models.domain.CarersAllowance.id)
}