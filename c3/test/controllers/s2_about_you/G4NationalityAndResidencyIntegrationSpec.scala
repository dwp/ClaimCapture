package controllers.s2_about_you

import org.specs2.mutable.{Tags, Specification}
import utils.{WebDriverHelper, WithBrowser}
import controllers.{ClaimScenarioFactory,PreviewTestUtils}
import utils.pageobjects.s2_about_you.{G5AbroadForMoreThan52WeeksPage, G4NationalityAndResidencyPage}
import utils.pageobjects._
import utils.pageobjects.preview.PreviewPage
import utils.pageobjects.s1_2_claim_date.G1ClaimDatePage
import app.MaritalStatus
import utils.WithJsBrowser

class G4NationalityAndResidencyIntegrationSpec extends Specification with Tags {
  sequential

  val urlUnderTest = "/about-you/nationality-and-residency"
  val submitButton = "button[type='submit']"
  val errorDiv = "div[class=validation-summary] ol li"

  "Nationality and Residency" should {
    "be presented" in new WithJsBrowser  with PageObjects{
			val page =  G4NationalityAndResidencyPage(context)
      page goToThePage()
    }

    "contain errors on invalid submission" in new WithJsBrowser  with PageObjects{
			val page =  G4NationalityAndResidencyPage(context)
      page goToThePage()
      val nextPage = page submitPage()

      nextPage must beAnInstanceOf[G4NationalityAndResidencyPage]
    }

    "navigate to next page on valid resident submission" in new WithJsBrowser  with PageObjects{
			val page =  G4NationalityAndResidencyPage(context)
      val claim = ClaimScenarioFactory.yourNationalityAndResidencyResident
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage()

      nextPage must beAnInstanceOf[G5AbroadForMoreThan52WeeksPage]
    }

    "navigate to next page on valid non resident submission" in new WithJsBrowser  with PageObjects{
			val page =  G4NationalityAndResidencyPage(context)
      val claim = ClaimScenarioFactory.yourNationalityAndResidencyNonResident
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage()

      nextPage must beAnInstanceOf[G5AbroadForMoreThan52WeeksPage]
    }

    "contain errors on invalid non resident submission" in new WithJsBrowser  with PageObjects{
			val page =  G4NationalityAndResidencyPage(context)
      val claim = ClaimScenarioFactory.yourNationalityAndResidencyNonResident
      claim.AboutYouNationalityAndResidencyActualNationality = ""
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage()

      nextPage must beAnInstanceOf[G4NationalityAndResidencyPage]
    }

    "country normally live in visible when clicked back" in new WithJsBrowser  with PageObjects{
      val page =  G4NationalityAndResidencyPage(context)
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

      nationalityPage must beAnInstanceOf[G4NationalityAndResidencyPage]
      val modifiedData = new TestData
      modifiedData.AboutYouNationalityAndResidencyNationality = "British"

      nationalityPage fillPageWith modifiedData
      val previewPageModified = nationalityPage submitPage()

      previewPageModified must beAnInstanceOf[PreviewPage]
      answerText(previewPageModified) mustEqual "British"
    }


  } section("integration", models.domain.AboutYou.id)

  def goToPreviewPage(context:PageObjectsContext):Page = {
    val claimDatePage = G1ClaimDatePage(context)
    claimDatePage goToThePage()
    val claimDate = ClaimScenarioFactory.s12ClaimDate()
    claimDatePage fillPageWith claimDate
    claimDatePage submitPage()

    val nationalityPage = G4NationalityAndResidencyPage(context)
    val claim = ClaimScenarioFactory.yourNationalityAndResidencyNonResident
    nationalityPage goToThePage()
    nationalityPage fillPageWith claim
    nationalityPage submitPage()

    val previewPage = PreviewPage(context)
    previewPage goToThePage()
  }

}
