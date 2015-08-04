package controllers.s_about_you

import org.specs2.mutable.{Tags, Specification}
import utils.{WebDriverHelper, WithBrowser}
import controllers.{ClaimScenarioFactory,PreviewTestUtils}
import utils.pageobjects.s_about_you.{GAbroadForMoreThan52WeeksPage, GNationalityAndResidencyPage}
import utils.pageobjects._
import utils.pageobjects.preview.PreviewPage
import utils.pageobjects.s_claim_date.GClaimDatePage
import app.MaritalStatus
import utils.WithJsBrowser

class GNationalityAndResidencyIntegrationSpec extends Specification with Tags {
  sequential

  val urlUnderTest = "/about-you/nationality-and-residency"
  val submitButton = "button[type='submit']"
  val errorDiv = "div[class=validation-summary] ol li"

  "Nationality and Residency" should {
    "be presented" in new WithJsBrowser  with PageObjects{
			val page =  GNationalityAndResidencyPage(context)
      page goToThePage()
    }

    "contain errors on invalid submission" in new WithJsBrowser  with PageObjects{
			val page =  GNationalityAndResidencyPage(context)
      page goToThePage()
      val nextPage = page submitPage()

      nextPage must beAnInstanceOf[GNationalityAndResidencyPage]
    }

    "navigate to next page on valid resident submission" in new WithJsBrowser  with PageObjects{
			val page =  GNationalityAndResidencyPage(context)
      val claim = ClaimScenarioFactory.yourNationalityAndResidencyResident
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage()

      nextPage must beAnInstanceOf[GAbroadForMoreThan52WeeksPage]
    }

    "navigate to next page on valid non resident submission" in new WithJsBrowser  with PageObjects{
			val page =  GNationalityAndResidencyPage(context)
      val claim = ClaimScenarioFactory.yourNationalityAndResidencyNonResident
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage()

      nextPage must beAnInstanceOf[GAbroadForMoreThan52WeeksPage]
    }

    "contain errors on invalid non resident submission" in new WithJsBrowser  with PageObjects{
			val page =  GNationalityAndResidencyPage(context)
      val claim = ClaimScenarioFactory.yourNationalityAndResidencyNonResident
      claim.AboutYouNationalityAndResidencyActualNationality = ""
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage()

      nextPage must beAnInstanceOf[GNationalityAndResidencyPage]
    }

    "country normally live in visible when clicked back" in new WithJsBrowser  with PageObjects{
      val page =  GNationalityAndResidencyPage(context)
      val claim = ClaimScenarioFactory.yourNationalityAndResidencyNonResident
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage()
      nextPage.goBack()
      browser.find("#resideInUK_text").size() mustEqual 1
    }

    "Modify nationality from preview page" in new WithJsBrowser  with PageObjects{
      val previewPage = goToPreviewPage(context)
      val id = "about_you_nationality"
      val answerText = PreviewTestUtils.answerText(id, _:Page)

      answerText(previewPage) mustEqual "French"
      val nationalityPage = previewPage.clickLinkOrButton(s"#$id")

      nationalityPage must beAnInstanceOf[GNationalityAndResidencyPage]
      val modifiedData = new TestData
      modifiedData.AboutYouNationalityAndResidencyNationality = "British"

      nationalityPage fillPageWith modifiedData
      val previewPageModified = nationalityPage submitPage()

      previewPageModified must beAnInstanceOf[PreviewPage]
      answerText(previewPageModified) mustEqual "British"
    }


  } section("integration", models.domain.AboutYou.id)

  def goToPreviewPage(context:PageObjectsContext):Page = {
    val claimDatePage = GClaimDatePage(context)
    claimDatePage goToThePage()
    val claimDate = ClaimScenarioFactory.s12ClaimDate()
    claimDatePage fillPageWith claimDate
    claimDatePage submitPage()

    val nationalityPage = GNationalityAndResidencyPage(context)
    val claim = ClaimScenarioFactory.yourNationalityAndResidencyNonResident
    nationalityPage goToThePage()
    nationalityPage fillPageWith claim
    nationalityPage submitPage()

    val previewPage = PreviewPage(context)
    previewPage goToThePage()
  }

}
