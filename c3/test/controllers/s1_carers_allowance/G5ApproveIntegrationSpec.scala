package controllers.s1_carers_allowance

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import utils.pageobjects.ClaimScenario
import utils.pageobjects.s2_about_you.G1YourDetailsPage
import utils.pageobjects.s1_carers_allowance.G6ApprovePageContext
import utils.pageobjects.s1_carers_allowance.G1BenefitsPageContext
import utils.pageobjects.s1_carers_allowance.G6ApprovePage
import utils.pageobjects.s1_carers_allowance.G4LivesInGBPage

class G5ApproveIntegrationSpec extends Specification with Tags {
  sequential

  "Approve" should {
    "be presented" in new WithBrowser with G6ApprovePageContext {
      page goToThePage()
    }
  } section("integration",models.domain.CarersAllowance.id)

  "Carer's Allowance" should {
    val notRightPage: String = "Next Page is not of the right type."

    "be approved" in new WithBrowser with G1BenefitsPageContext {
      val claim = new ClaimScenario
      claim.CanYouGetCarersAllowanceDoesthePersonYouCareforGetOneofTheseBenefits = "Yes"
      claim.CanYouGetCarersAllowanceDoYouSpend35HoursorMoreEachWeekCaring = "Yes"
      claim.CanYouGetCarersAllowanceAreYouAged16OrOver = "Yes"
      claim.CanYouGetCarersAllowanceDoYouNormallyLiveinGb = "Yes"
      page goToThePage()
      page fillPageWith claim
      val hoursPage = page submitPage(waitForPage = true, waitDuration = 500)
      hoursPage fillPageWith claim
      val over16Page = hoursPage submitPage(waitForPage = true, waitDuration = 500)
      over16Page fillPageWith claim
      val livingGBPage = over16Page submitPage(waitForPage = true, waitDuration = 500)
      livingGBPage fillPageWith claim
      val approvePage = livingGBPage submitPage(waitForPage = true, waitDuration = 500)

      approvePage match {
        case p: G6ApprovePage => {
          p.previousPage must beSome(livingGBPage)
          p.isApproved must beTrue
        }
        case _ => ko(notRightPage)
      }
    }

    "be declined" in new WithBrowser with G1BenefitsPageContext {
      val claim = new ClaimScenario
      claim.CanYouGetCarersAllowanceDoesthePersonYouCareforGetOneofTheseBenefits = "Yes"
      claim.CanYouGetCarersAllowanceDoYouSpend35HoursorMoreEachWeekCaring = "Yes"
      claim.CanYouGetCarersAllowanceAreYouAged16OrOver = "Yes"
      claim.CanYouGetCarersAllowanceDoYouNormallyLiveinGb = "No"
      page goToThePage()
      val approvePage = page runClaimWith (claim, G6ApprovePage.title, waitForPage = true, waitDuration = 500)
      approvePage match {
        case p: G6ApprovePage => {
          p.previousPage.get must beAnInstanceOf[G4LivesInGBPage]
          p.isNotApproved must beTrue
        }
        case _ => ko(notRightPage)
      }
    }

    "navigate to next section" in new WithBrowser with G1BenefitsPageContext {
      val claim = new ClaimScenario
      claim.CanYouGetCarersAllowanceDoesthePersonYouCareforGetOneofTheseBenefits = "Yes"
      claim.CanYouGetCarersAllowanceDoYouSpend35HoursorMoreEachWeekCaring = "Yes"
      claim.CanYouGetCarersAllowanceAreYouAged16OrOver = "Yes"
      claim.CanYouGetCarersAllowanceDoYouNormallyLiveinGb = "No"
      page goToThePage()
      page runClaimWith (claim, G1YourDetailsPage.title, waitForPage = true, waitDuration = 500)
    }

    "contain the completed forms" in new WithBrowser with G1BenefitsPageContext {
      val claim = new ClaimScenario
      claim.CanYouGetCarersAllowanceDoesthePersonYouCareforGetOneofTheseBenefits = "no"
      claim.CanYouGetCarersAllowanceDoYouSpend35HoursorMoreEachWeekCaring = "yes"
      claim.CanYouGetCarersAllowanceAreYouAged16OrOver = "no"
      claim.CanYouGetCarersAllowanceDoYouNormallyLiveinGb = "yes"
      page goToThePage()
      page fillPageWith claim
      val s1g2 = page submitPage(waitForPage = true, waitDuration = 500)
      
      s1g2 fillPageWith claim
      val s1g3 = s1g2 submitPage(waitForPage = true, waitDuration = 500)
      
      s1g3 fillPageWith claim
      val s1g4 = s1g3 submitPage(waitForPage = true, waitDuration = 500)
      
      s1g4 fillPageWith claim
      val s1g5 = s1g4 submitPage(waitForPage = true, waitDuration = 500)

      s1g5 match {
        case p: G6ApprovePage => {
          p numberSectionsCompleted() mustEqual 4

          val completed = p.findTarget("div[class=completed] ul li")

          completed(0) must contain("Q1")
          completed(0) must contain("No")
          completed(1) must contain("Q2")
          completed(1) must contain("Yes")
          completed(2) must contain("Q3")
          completed(2) must contain("No")
          completed(3) must contain("Q4")
          completed(3) must contain("Yes")
        }
        case _ => ko("Next Page is not of the right type.")
      }
    }
  } section("integration",models.domain.CarersAllowance.id)
}