package controllers.s1_2_claim_date

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{ClaimScenarioFactory, PreviewTestUtils, Formulate, BrowserMatchers}
import utils.pageobjects._
import utils.pageobjects.s2_about_you.G4NationalityAndResidencyPage
import utils.pageobjects.preview.PreviewPage
import utils.pageobjects.s1_2_claim_date.G1ClaimDatePage

class G1ClaimDateIntegrationSpec extends Specification with Tags {

  "Your claim date" should {

    "be presented " in new WithBrowser with BrowserMatchers {
      browser.goTo("/your-claim-date/claim-date")
      titleMustEqual("Claim date - When do you want Carer's Allowance to start?")
    }

    "navigate to next section" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)

      browser.goTo("/about-you/your-details")
      titleMustEqual("Your details - About you - the carer")
    }

    "Modify claim date from preview page" in new WithBrowser with PageObjects{
      val previewPage = goToPreviewPage(context)
      val id = "about_you_claimDate"
      val answerText = PreviewTestUtils.answerText(id, _:Page)

      answerText(previewPage) mustEqual "10 October, 2016"
      val claimDatePage = ClaimPageFactory.buildPageFromFluent(previewPage.click(s"#$id"))

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
