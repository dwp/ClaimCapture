package controllers.s1_carers_allowance

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import utils.pageobjects.s1_carers_allowance.{G6ApprovePage, G4LivingInGBPage, G1BenefitsPageContext, G6ApprovePageContext}
import utils.pageobjects.ClaimScenario
import utils.pageobjects.s2_about_you.G1YourDetailsPage

class G5ApproveIntegrationSpec extends Specification with Tags {
  sequential

  "Approve" should {
    "be presented" in new WithBrowser with G6ApprovePageContext {
      page goToThePage()
    }
  } section "integration"

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
      val hoursPage = page submitPage()
      hoursPage fillPageWith claim
      val over16Page = hoursPage submitPage()
      over16Page fillPageWith claim
      val livingGBPage = over16Page submitPage(waitForPage = true,waitDuration = 120)
      livingGBPage fillPageWith claim
      val approvePage = livingGBPage submitPage(waitForPage = true)

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
      val approvePage = page runClaimWith (claim, G6ApprovePage.title)
      approvePage match {
        case p: G6ApprovePage => {
          p.previousPage.get must beAnInstanceOf[G4LivingInGBPage]
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
      page runClaimWith (claim, G1YourDetailsPage.title)
    }
  } section "integration"
}
