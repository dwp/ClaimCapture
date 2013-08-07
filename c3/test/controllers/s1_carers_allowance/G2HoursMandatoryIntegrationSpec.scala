package controllers.s1_carers_allowance

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import utils.pageobjects.ClaimScenario
import utils.pageobjects.s1_carers_allowance.G2HoursMandatoryPageContext
import utils.pageobjects.s1_carers_allowance.G1BenefitsPageContext
import utils.pageobjects.s1_carers_allowance.G2HoursMandatoryPage
import utils.pageobjects.s1_carers_allowance.G3Over16MandatoryPage

class G2HoursMandatoryIntegrationSpec extends Specification with Tags {
  "Carer's Allowance - Benefits - Integration" should {
    "be presented" in new WithBrowser with G2HoursMandatoryPageContext {
      page goToThePage ()
    }

    "contain errors on invalid submission" in {
      "missing mandatory field" in new WithBrowser with G2HoursMandatoryPageContext {
        val claim = new ClaimScenario
        claim.CanYouGetCarersAllowanceDoYouSpend35HoursorMoreEachWeekCaring = ""
        page goToThePage()
        page fillPageWith claim
        val pageWithErrors = page.submitPage()
        pageWithErrors.listErrors.size mustEqual 1
      }
    }
    
    "accept submit if all mandatory fields are populated" in new WithBrowser with G2HoursMandatoryPageContext {
      val claim = new ClaimScenario
      claim.CanYouGetCarersAllowanceDoYouSpend35HoursorMoreEachWeekCaring = "yes"
      page goToThePage()
      page fillPageWith claim
      page submitPage()
    }
    
    "navigate to next page on valid submission" in new WithBrowser with G2HoursMandatoryPageContext {
      val claim = new ClaimScenario
      claim.CanYouGetCarersAllowanceDoYouSpend35HoursorMoreEachWeekCaring = "yes"
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage()

      nextPage must beAnInstanceOf[G3Over16MandatoryPage]
    }
    
    "contain the completed forms" in new WithBrowser with G1BenefitsPageContext {
      val claim = new ClaimScenario
      claim.CanYouGetCarersAllowanceDoesthePersonYouCareforGetOneofTheseBenefits = "yes"
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage() 

      nextPage match {
        case p: G2HoursMandatoryPage => {
          p numberSectionsCompleted() mustEqual 1
          val completed = p.findTarget("div[class=completed] ul li")
          completed(0) must contain("Q1")
          completed(0) must contain("Yes")
        }
        case _ => ko("Next Page is not of the right type.")
      }
    }
  } section "integration"
}