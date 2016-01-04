package controllers.s_claim_date

import org.specs2.mutable._
import utils.{WithJsBrowser, WithBrowser}
import controllers.{ClaimScenarioFactory, PreviewTestUtils}
import utils.pageobjects._
import utils.pageobjects.s_about_you.GYourDetailsPage
import utils.pageobjects.s_claim_date.GClaimDatePage

class GClaimDateIntegrationSpec extends Specification {
  section("unit", models.domain.YourClaimDate.id)
  "Your claim date" should {
    "be presented " in new WithBrowser with PageObjects {
      val claimDatePage = GClaimDatePage(context)
      claimDatePage goToThePage()
    }

    "navigate to next section" in new WithBrowser with PageObjects {
      val claimDatePage = GClaimDatePage(context)
      claimDatePage goToThePage()
      val claim = ClaimScenarioFactory.s12ClaimDateSpent35HoursYes()
      claimDatePage fillPageWith claim
      val page = claimDatePage submitPage() goToPage (GYourDetailsPage(context))
    }

    "contains errors for optional mandatory data" in new WithBrowser with PageObjects {
      val page = GClaimDatePage(context)
      page goToThePage()
      val claim = new TestData
      claim.ClaimDateWhenDoYouWantYourCarersAllowanceClaimtoStart = "10/10/2016"
      claim.ClaimDateDidYouCareForThisPersonfor35Hours = "Yes"
      page fillPageWith claim
      page submitPage()
      page.listErrors.size mustEqual 1
    }

    "start to care for the person to be displayed when back button is clicked" in new WithBrowser with PageObjects {
      val claimDatePage = GClaimDatePage(context)
      claimDatePage goToThePage()
      val claim = ClaimScenarioFactory.s12ClaimDateSpent35HoursYes()
      claimDatePage fillPageWith claim
      val nextPage = claimDatePage submitPage()
      val claimDatePageSecondTime = nextPage goBack()
      claimDatePageSecondTime must beAnInstanceOf[GClaimDatePage]
      claimDatePageSecondTime visible ("#beforeClaimCaring_date_year") must beTrue
    }

    "display warning message when date more than 3 months in future" in new WithJsBrowser with PageObjects {
      val claimDatePage = GClaimDatePage(context) goToThePage()
      claimDatePage fillPageWith ClaimScenarioFactory.s12ClaimDateInFuture()
      claimDatePage.source must contain("You can't claim Carer's Allowance more than 3 months in advance.")
      claimDatePage visible ("#claimDateWarning") must beTrue
    }

    "display warning message when date more than 3 months in future and return back from next page" in new WithBrowser with PageObjects {
      val claimDatePage = GClaimDatePage(context) goToThePage()
      claimDatePage fillPageWith ClaimScenarioFactory.s12ClaimDateInFuture()
      val nextPage = claimDatePage submitPage()
      val claimDatePageSecondTime = nextPage goBack()
      claimDatePageSecondTime.source must contain("You can't claim Carer's Allowance more than 3 months in advance.")
      claimDatePageSecondTime visible ("#claimDateWarning") must beTrue
    }

    "not display warning message when date invalid 13 months" in new WithJsBrowser with PageObjects {
      val claimDatePage = GClaimDatePage(context) goToThePage()
      claimDatePage.fillInput("#dateOfClaim_day", "01")
      claimDatePage.fillInput("#dateOfClaim_month", "13")
      claimDatePage.fillInput("#dateOfClaim_year", "2099")
      claimDatePage.source must contain("You can't claim Carer's Allowance more than 3 months in advance.")
      claimDatePage visible ("#claimDateWarning") must beFalse
    }

    "not display warning message when date invalid 32 Jan" in new WithJsBrowser with PageObjects {
      val claimDatePage = GClaimDatePage(context) goToThePage()
      claimDatePage.fillInput("#dateOfClaim_day", "32")
      claimDatePage.fillInput("#dateOfClaim_month", "1")
      claimDatePage.fillInput("#dateOfClaim_year", "2099")
      claimDatePage.source must contain("You can't claim Carer's Allowance more than 3 months in advance.")
      claimDatePage visible ("#claimDateWarning") must beFalse
    }

    "not display warning message when date invalid 29 Feb none leap year" in new WithJsBrowser with PageObjects {
      val claimDatePage = GClaimDatePage(context) goToThePage()
      claimDatePage.fillInput("#dateOfClaim_day", "29")
      claimDatePage.fillInput("#dateOfClaim_month", "2")
      claimDatePage.fillInput("#dateOfClaim_year", "2099")
      claimDatePage.source must contain("You can't claim Carer's Allowance more than 3 months in advance.")
      claimDatePage visible ("#claimDateWarning") must beFalse
    }

    "display warning message when date valid 29 Feb in leap year" in new WithJsBrowser with PageObjects {
      val claimDatePage = GClaimDatePage(context) goToThePage()
      claimDatePage.fillInput("#dateOfClaim_day", "29")
      claimDatePage.fillInput("#dateOfClaim_month", "2")
      claimDatePage.fillInput("#dateOfClaim_year", "2096")
      claimDatePage.source must contain("You can't claim Carer's Allowance more than 3 months in advance.")
      claimDatePage visible ("#claimDateWarning") must beTrue
    }
  }
  section("unit", models.domain.YourClaimDate.id)
}
