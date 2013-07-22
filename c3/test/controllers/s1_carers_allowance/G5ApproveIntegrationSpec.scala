package controllers.s1_carers_allowance

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.BrowserMatchers
import utils.pageobjects.s1_carers_allowance.{ApprovePage, LivingInGBPage, BenefitsPageContext, ApprovePageContext}
import utils.pageobjects.ClaimScenario
import utils.pageobjects.s2_about_you.YourDetailsPage

class G5ApproveIntegrationSpec extends Specification with Tags {

  "Approve" should {
    "be presented" in new WithBrowser with ApprovePageContext {
      page goToThePage()
    }
  } section "integration"

  "Carer's Allowance" should {
    val notRightPage: String = "Next Page is not of the right type."

    "be approved" in new WithBrowser with BenefitsPageContext {
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
      val livingGBPage = over16Page submitPage()
      livingGBPage fillPageWith claim
      val approvePage = livingGBPage submitPage()

      approvePage match {
        case p: ApprovePage => {
          p.previousPage must beSome(livingGBPage)
          p.isApproved must beTrue
        }
        case _ => ko(notRightPage)
      }
    }

    "be declined" in new WithBrowser with BenefitsPageContext {
      val claim = new ClaimScenario
      claim.CanYouGetCarersAllowanceDoesthePersonYouCareforGetOneofTheseBenefits = "Yes"
      claim.CanYouGetCarersAllowanceDoYouSpend35HoursorMoreEachWeekCaring = "Yes"
      claim.CanYouGetCarersAllowanceAreYouAged16OrOver = "Yes"
      claim.CanYouGetCarersAllowanceDoYouNormallyLiveinGb = "No"
      page goToThePage()
      val approvePage = page runClaimWith (claim, ApprovePage.title)
      approvePage match {
        case p: ApprovePage => {
          p.previousPage.get must beAnInstanceOf[LivingInGBPage]
          p.isNotApproved must beTrue
        }
        case _ => ko(notRightPage)
      }
    }

    "navigate to next section" in new WithBrowser with BenefitsPageContext {
      val claim = new ClaimScenario
      claim.CanYouGetCarersAllowanceDoesthePersonYouCareforGetOneofTheseBenefits = "Yes"
      claim.CanYouGetCarersAllowanceDoYouSpend35HoursorMoreEachWeekCaring = "Yes"
      claim.CanYouGetCarersAllowanceAreYouAged16OrOver = "Yes"
      claim.CanYouGetCarersAllowanceDoYouNormallyLiveinGb = "No"
      page goToThePage()
      page runClaimWith (claim, YourDetailsPage.title)
    }
  } section "integration"
}