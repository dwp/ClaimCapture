package controllers.s1_2_claim_date

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{ClaimScenarioFactory, PreviewTestUtils}
import utils.pageobjects._
import utils.pageobjects.s2_about_you.G1YourDetailsPage
import utils.pageobjects.preview.PreviewPage
import utils.pageobjects.s1_2_claim_date.G1ClaimDatePage

class G1ClaimDateIntegrationSpec extends Specification with Tags {

  "Your claim date" should {

    "be presented " in new WithBrowser with PageObjects {
      val claimDatePage = G1ClaimDatePage(context)
      claimDatePage goToThePage()
    }

    "navigate to next section" in new WithBrowser with PageObjects {
      val claimDatePage = G1ClaimDatePage(context)
      claimDatePage goToThePage()
      val claim = ClaimScenarioFactory.s12ClaimDate()
      claimDatePage fillPageWith claim
      val page = claimDatePage submitPage() goToPage(G1YourDetailsPage(context))
    }

    "Modify claim date from preview page" in new WithBrowser with PageObjects{
      val previewPage = goToPreviewPage(context)
      val id = "about_you_claimDate"
      val answerText = PreviewTestUtils.answerText(id, _:Page)

      answerText(previewPage) mustEqual "10 October, 2016"
      val claimDatePage = previewPage.clickLinkOrButton(s"#$id")

      claimDatePage must beAnInstanceOf[G1ClaimDatePage]
      val modifiedData = new TestData
      modifiedData.ClaimDateWhenDoYouWantYourCarersAllowanceClaimtoStart = "11/10/2016"

      claimDatePage fillPageWith modifiedData
      val previewPageModified = claimDatePage submitPage()

      previewPageModified must beAnInstanceOf[PreviewPage]
      answerText(previewPageModified) mustEqual "11 October, 2016"
    }

  } section("unit", models.domain.YourClaimDate.id)

  def goToPreviewPage(context:PageObjectsContext):Page = {
    val claimDatePage = G1ClaimDatePage(context)
    claimDatePage goToThePage()
    val claimDate = ClaimScenarioFactory.s12ClaimDate()
    claimDatePage fillPageWith claimDate
    claimDatePage submitPage()

    val previewPage = PreviewPage(context)
    previewPage goToThePage()
  }
}
