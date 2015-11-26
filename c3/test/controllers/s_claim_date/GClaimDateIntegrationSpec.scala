package controllers.s_claim_date

import org.specs2.mutable._
import utils.WithBrowser
import controllers.{ClaimScenarioFactory, PreviewTestUtils}
import utils.pageobjects._
import utils.pageobjects.s_about_you.GYourDetailsPage
import utils.pageobjects.preview.PreviewPage
import utils.pageobjects.s_claim_date.GClaimDatePage
import utils.pageobjects.s_care_you_provide.GMoreAboutTheCarePage
import utils.helpers.PreviewField._

class GClaimDateIntegrationSpec extends Specification {

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
      val page = claimDatePage submitPage() goToPage(GYourDetailsPage(context))
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
      val claimDatePageSecondTime = nextPage goBack ()
      claimDatePageSecondTime must beAnInstanceOf[GClaimDatePage]
      claimDatePageSecondTime visible("#beforeClaimCaring_date_year") must beTrue
    }

<<<<<<< HEAD
    "show warning if date is further than 3 months in the future" in new WithJsBrowser with PageObjects {
      val claimDatePage = GClaimDatePage(context)
      claimDatePage goToThePage()

      val futuredate = DateTimeFormat.forPattern("dd/MM/yyyy").print(new DateTime().plusMonths(3).plusDays(1))
      val data = new TestData
      // Claim date
      data.ClaimDateWhenDoYouWantYourCarersAllowanceClaimtoStart = futuredate
      data.ClaimDateDidYouCareForThisPersonfor35Hours = "No"

      claimDatePage fillPageWith data

      val warning = claimDatePage.ctx.browser.find("#claimDateWarning").get(0)

      warning.isDisplayed must beTrue


    }

  } section("unit", models.domain.YourClaimDate.id)
=======
  }
  section("unit", models.domain.YourClaimDate.id)
>>>>>>> integration

}
