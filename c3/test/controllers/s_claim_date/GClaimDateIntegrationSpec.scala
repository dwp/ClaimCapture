package controllers.s_claim_date

import org.specs2.mutable.{Tags, Specification}
import utils.WithBrowser
import controllers.{ClaimScenarioFactory, PreviewTestUtils}
import utils.pageobjects._
import utils.pageobjects.s2_about_you.G1YourDetailsPage
import utils.pageobjects.preview.PreviewPage
import utils.pageobjects.s_claim_date.GClaimDatePage
import utils.pageobjects.s4_care_you_provide.G7MoreAboutTheCarePage

class GClaimDateIntegrationSpec extends Specification with Tags {

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
      val page = claimDatePage submitPage() goToPage(G1YourDetailsPage(context))
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

    "Modify claim date from preview page" in new WithBrowser with PageObjects{
      val previewPage = goToPreviewPage(context)
      val id = "about_you_claimDate"
      val answerText = PreviewTestUtils.answerText(id, _:Page)

      answerText(previewPage) mustEqual "10 October, 2016"
      val claimDatePage = previewPage.clickLinkOrButton(s"#$id")

      claimDatePage must beAnInstanceOf[GClaimDatePage]
      val modifiedData = new TestData
      modifiedData.ClaimDateWhenDoYouWantYourCarersAllowanceClaimtoStart = "11/10/2016"

      claimDatePage fillPageWith modifiedData
      val previewPageModified = claimDatePage submitPage()

      previewPageModified must beAnInstanceOf[PreviewPage]
      answerText(previewPageModified) mustEqual "11 October, 2016"
    }

  } section("unit", models.domain.YourClaimDate.id)

  def goToPreviewPage(context:PageObjectsContext):Page = {
    val claimDatePage = GClaimDatePage(context)
    claimDatePage goToThePage()
    val claimDate = ClaimScenarioFactory.s12ClaimDateSpent35HoursYes()
    claimDatePage fillPageWith claimDate
    claimDatePage submitPage()

    val previewPage = PreviewPage(context)
    previewPage goToThePage()
  }
}
