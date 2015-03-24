package controllers.s0_carers_allowance

import org.specs2.mutable.{Tags, Specification}
import play.api.Logger
import play.api.test.WithBrowser
import utils.pageobjects.common.ErrorPage
import utils.pageobjects.{PageObjects, TestData}
import utils.pageobjects.s0_carers_allowance._
import utils.pageobjects.s1_2_claim_date.G1ClaimDatePage

class G6ApproveIntegrationSpec extends Specification with Tags {
  "Approve" should {
    "be presented" in new WithBrowser with PageObjects{
			val page =  G6ApprovePage(context)
      page goToThePage()
      page.jsCheckEnabled must beTrue
    }
  } section("integration",models.domain.CarersAllowance.id)

  "Carer's Allowance" should {
    val notRightPage: String = "Next Page is not of the right type."

    "be approved" in new WithBrowser with PageObjects{
			val page =  G1BenefitsPage(context)
      val claim = new TestData
      claim.CanYouGetCarersAllowanceWhatBenefitDoesThePersonYouCareForGet = "AA"
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
        case p: G6ApprovePage =>
          p.ctx.previousPage must beSome(livingGBPage)
          p.isApproved must beTrue

        case _ => ko(notRightPage)
      }
    }

    "be declined" in new WithBrowser with PageObjects{
			val page =  G1BenefitsPage(context)
      val claim = new TestData
      claim.CanYouGetCarersAllowanceWhatBenefitDoesThePersonYouCareForGet = "DLA"
      claim.CanYouGetCarersAllowanceDoYouSpend35HoursorMoreEachWeekCaring = "Yes"
      claim.CanYouGetCarersAllowanceAreYouAged16OrOver = "Yes"
      claim.CanYouGetCarersAllowanceDoYouNormallyLiveinGb = "No"
      page goToThePage()
      val approvePage = page runClaimWith (claim, G6ApprovePage.url)

      approvePage match {
        case p: G6ApprovePage =>
          p.ctx.previousPage.get must beAnInstanceOf[G2EligibilityPage]
          p.isNotApproved must beTrue

        case _ => ko(notRightPage)
      }
    }

    "navigate to next section" in new WithBrowser with PageObjects{
			val page =  G1BenefitsPage(context)
      val claim = new TestData
      claim.CanYouGetCarersAllowanceWhatBenefitDoesThePersonYouCareForGet = "CAA"
      claim.CanYouGetCarersAllowanceDoYouSpend35HoursorMoreEachWeekCaring = "Yes"
      claim.CanYouGetCarersAllowanceAreYouAged16OrOver = "Yes"
      claim.CanYouGetCarersAllowanceDoYouNormallyLiveinGb = "Yes"
      page goToThePage()
      page runClaimWith (claim, G1ClaimDatePage.url)
    }

    "If go to error page after this page. Retry allows to come back to this page" in new WithBrowser with PageObjects{
      val page =  G1BenefitsPage(context)
      val claim = new TestData
      claim.CanYouGetCarersAllowanceWhatBenefitDoesThePersonYouCareForGet = "AA"
      claim.CanYouGetCarersAllowanceDoYouSpend35HoursorMoreEachWeekCaring = "Yes"
      claim.CanYouGetCarersAllowanceAreYouAged16OrOver = "Yes"
      claim.CanYouGetCarersAllowanceDoYouNormallyLiveinGb = "Yes"
      page goToThePage()
      page runClaimWith (claim, G6ApprovePage.url)
      val errorPage = ErrorPage(context)
      errorPage goToThePage()
      val tryPage = errorPage.clickLinkOrButton("button[type='submit']")
      tryPage match {
        case p: G6ApprovePage =>  ok("Error try again worked.")
        case _ => ko(notRightPage)
      }
    }


  } section("integration", models.domain.CarersAllowance.id)
}