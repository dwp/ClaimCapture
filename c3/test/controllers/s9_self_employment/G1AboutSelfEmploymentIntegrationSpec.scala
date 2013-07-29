package controllers.s9_self_employment

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import utils.pageobjects.s9_self_employment.{G1AboutSelfEmploymentPage, G1AboutSelfEmploymentPageContext}
import utils.pageobjects.ClaimScenario
import controllers.ClaimScenarioFactory

class G1AboutSelfEmploymentIntegrationSpec extends Specification with Tags {

  "About Self Employment" should {
    "be presented" in new WithBrowser with G1AboutSelfEmploymentPageContext {
      page goToThePage ()
    }

    "contain errors on invalid submission" in {
      "missing mandatory field" in new WithBrowser with G1AboutSelfEmploymentPageContext {
        val claim = new ClaimScenario
        claim.SelfEmployedAreYouSelfEmployedNow = ""
        page goToThePage()
        page fillPageWith claim
        val pageWithErrors = page.submitPage()
        pageWithErrors.listErrors.size mustEqual 1
      }
      
      "self employed now but invalid date" in new WithBrowser with G1AboutSelfEmploymentPageContext {
        val claim = new ClaimScenario
        claim.SelfEmployedAreYouSelfEmployedNow = "yes"
        claim.SelfEmployedWhenDidYouStartThisJob = "01/01/0000"
        page goToThePage ()
        page fillPageWith claim
        val pageWithErrors = page.submitPage()
        pageWithErrors.listErrors.size mustEqual 1
        pageWithErrors.listErrors(0).contains("date")
      }
    }
    
    "accept submit if all mandatory fields are populated" in new WithBrowser with G1AboutSelfEmploymentPageContext {
      val claim = ClaimScenarioFactory.s9SelfEmployment
      page goToThePage()
      page fillPageWith claim
      page submitPage()
    }
    
    "navigate to next page on valid submission" in new WithBrowser with G1AboutSelfEmploymentPageContext {
      val claim = ClaimScenarioFactory.s9SelfEmployment
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage()

      nextPage must not(beAnInstanceOf[G1AboutSelfEmploymentPage])
    }
  }

}

