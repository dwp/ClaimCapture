package controllers.s_eligibility

import org.specs2.mutable._
import utils.{WithJsBrowser, WithBrowser}
import utils.pageobjects.common.ErrorPage
import utils.pageobjects.{PageObjects, TestData}
import utils.pageobjects.s_eligibility._
import utils.pageobjects.s_claim_date.GClaimDatePage

class GApproveIntegrationSpec extends Specification {
  section("integration",models.domain.CarersAllowance.id)
  "Approve" should {
    "be presented" in new WithBrowser with PageObjects{
			val page =  GApprovePage(context)
      page goToThePage()
      page.jsCheckEnabled must beTrue
    }
  }
  section("integration",models.domain.CarersAllowance.id)

  section("integration", models.domain.CarersAllowance.id)
  "Carer's Allowance" should {
    val notRightPage: String = "Next Page is not of the right type."

    "be approved" in new WithBrowser with PageObjects {
			val page =  GBenefitsPage(context)
      val claim = new TestData
      claim.CanYouGetCarersAllowanceWhatBenefitDoesThePersonYouCareForGet = "AA"
      page goToThePage()
      page fillPageWith claim
      val hoursPage = page submitPage()
      claim.CanYouGetCarersAllowanceDoYouSpend35HoursorMoreEachWeekCaring = "Yes"
      hoursPage fillPageWith claim
      val over16Page = hoursPage submitPage()
      claim.CanYouGetCarersAllowanceAreYouAged16OrOver = "Yes"
      over16Page fillPageWith claim
      val livingGBPage = over16Page submitPage()
      claim.CanYouGetCarersAllowanceDoYouNormallyLiveinGb = "Yes"
      livingGBPage fillPageWith claim
      val approvePage = livingGBPage submitPage()

      approvePage match {
        case p: GApprovePage =>
          p.ctx.previousPage must beSome(livingGBPage)
          p.isApproved must beTrue
        case _ => ko(notRightPage)
      }
    }

    "be declined" in new WithBrowser with PageObjects {
			val page =  GBenefitsPage(context)
      val claim = new TestData
      claim.CanYouGetCarersAllowanceWhatBenefitDoesThePersonYouCareForGet = "DLA"
      claim.CanYouGetCarersAllowanceDoYouSpend35HoursorMoreEachWeekCaring = "Yes"
      claim.CanYouGetCarersAllowanceAreYouAged16OrOver = "Yes"
      claim.CanYouGetCarersAllowanceDoYouNormallyLiveinGb = "No"
      page goToThePage()
      val approvePage = page runClaimWith (claim, GApprovePage.url)

      approvePage match {
        case p: GApprovePage =>
          p.ctx.previousPage.get must beAnInstanceOf[GEligibilityPage]
          p.isNotApproved must beTrue
        case _ => ko(notRightPage)
      }
    }

    "navigate to next section" in new WithBrowser with PageObjects {
			val page =  GBenefitsPage(context)
      val claim = new TestData
      claim.CanYouGetCarersAllowanceWhatBenefitDoesThePersonYouCareForGet = "CAA"
      claim.CanYouGetCarersAllowanceDoYouSpend35HoursorMoreEachWeekCaring = "Yes"
      claim.CanYouGetCarersAllowanceAreYouAged16OrOver = "Yes"
      claim.CanYouGetCarersAllowanceDoYouNormallyLiveinGb = "Yes"
      page goToThePage()
      page runClaimWith (claim, GClaimDatePage.url)
    }

    "If go to error page after this page. Retry allows to come back to this page" in new WithJsBrowser with PageObjects {
      val page =  GBenefitsPage(context)
      val claim = new TestData
      claim.CanYouGetCarersAllowanceWhatBenefitDoesThePersonYouCareForGet = "AA"
      claim.CanYouGetCarersAllowanceDoYouSpend35HoursorMoreEachWeekCaring = "Yes"
      claim.CanYouGetCarersAllowanceAreYouAged16OrOver = "Yes"
      claim.CanYouGetCarersAllowanceDoYouNormallyLiveinGb = "Yes"
      page goToThePage()
      page runClaimWith (claim, GApprovePage.url)
      val errorPage = ErrorPage(context)
      errorPage goToThePage()
      val tryPage = errorPage.clickLinkOrButton("#TryJs")
      tryPage match {
        case p: GApprovePage =>  ok("Error try again worked.")
        case _ => ko(notRightPage)
      }
    }
  }
  section("integration", models.domain.CarersAllowance.id)
}
