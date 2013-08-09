package controllers.s1_carers_allowance

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import utils.pageobjects.ClaimScenario
import utils.pageobjects.s1_carers_allowance.G3Over16PageContext
import utils.pageobjects.s1_carers_allowance.G3Over16Page
import utils.pageobjects.s1_carers_allowance.G1BenefitsPageContext
import utils.pageobjects.s1_carers_allowance.G4LivesInGBPageContext
import utils.pageobjects.s1_carers_allowance.G5CarersResponsePage
import utils.pageobjects.s1_carers_allowance.G6ApprovePage
import utils.pageobjects.s1_carers_allowance.G4LivesInGBPage
import scala.Some

class G4LivesInGBIntegrationSpec extends Specification with Tags {
  "Carer's Allowance - Benefits - Integration" should {
    "be presented" in new WithBrowser with G4LivesInGBPageContext {
      page goToThePage ()
    }

    "contain errors on invalid submission" in {
      "missing mandatory field" in new WithBrowser with G4LivesInGBPageContext {
        val claim = new ClaimScenario
        claim.CanYouGetCarersAllowanceDoYouNormallyLiveinGb = ""
        page goToThePage()
        page fillPageWith claim
        val pageWithErrors = page.submitPage()
        pageWithErrors.listErrors.size mustEqual 1
      }
    }
    
    "accept submit if all mandatory fields are populated" in new WithBrowser with G4LivesInGBPageContext {
      val claim = new ClaimScenario
      claim.CanYouGetCarersAllowanceDoYouNormallyLiveinGb = "yes"
      page goToThePage()
      page fillPageWith claim
      page submitPage()
    }

    "navigate to next page on valid submission" in new WithBrowser with G4LivesInGBPageContext {
      val claim = new ClaimScenario
      claim.CanYouGetCarersAllowanceDoYouNormallyLiveinGb = "yes"
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage()

      nextPage must beAnInstanceOf[G6ApprovePage]
    }

    "contain the completed forms" in new WithBrowser with G1BenefitsPageContext {
      val claim = new ClaimScenario
      claim.CanYouGetCarersAllowanceDoesthePersonYouCareforGetOneofTheseBenefits = "no"
      claim.CanYouGetCarersAllowanceDoYouSpend35HoursorMoreEachWeekCaring = "yes"
      claim.CanYouGetCarersAllowanceAreYouAged16OrOver = "no"
      page goToThePage()
      page fillPageWith claim
      val s1g2 = page submitPage() 
      
      s1g2 fillPageWith claim
      val s1g3 = s1g2 submitPage()
      
      s1g3 fillPageWith claim
      val s1g4 = s1g3 submitPage()

      s1g4 match {
        case p: G4LivesInGBPage => {
          p numberSectionsCompleted() mustEqual 3
          val completed = p.findTarget("div[class=completed] ul li")
          completed(0) must contain("Q1")
          completed(0) must contain("No")
          completed(1) must contain("Q2")
          completed(1) must contain("Yes")
          completed(2) must contain("Q3")
          completed(2) must contain("No")
        }
        case _ => ko("Next Page is not of the right type.")
      }
    }
  } section("integration", models.domain.CarersAllowance.id)
}