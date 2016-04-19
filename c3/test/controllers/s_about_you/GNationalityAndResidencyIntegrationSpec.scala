package controllers.s_about_you

import org.specs2.mutable._
import controllers.{ClaimScenarioFactory,PreviewTestUtils}
import utils.pageobjects.s_about_you.{GOtherEEAStateOrSwitzerlandPage, GNationalityAndResidencyPage}
import utils.pageobjects._
import utils.pageobjects.preview.PreviewPage
import utils.pageobjects.s_claim_date.GClaimDatePage
import utils.WithJsBrowser
import utils.helpers.PreviewField._

class GNationalityAndResidencyIntegrationSpec extends Specification {
  sequential

  val urlUnderTest = "/about-you/nationality-and-residency"
  val submitButton = "button[type='submit']"
  val errorDiv = "div[class=validation-summary] ol li"

  section("integration", models.domain.AboutYou.id)
  "Nationality and Residency" should {
    "be presented" in new WithJsBrowser with PageObjects{
			val page =  GNationalityAndResidencyPage(context)
      page goToThePage()
    }

    "contain errors on invalid submission" in new WithJsBrowser with PageObjects{
			val page =  GNationalityAndResidencyPage(context)
      page goToThePage()
      val nextPage = page submitPage()
      nextPage must beAnInstanceOf[GNationalityAndResidencyPage]
    }

    "navigate to next page on valid resident submission" in new WithJsBrowser with PageObjects{
			val page =  GNationalityAndResidencyPage(context)
      val claim = ClaimScenarioFactory.yourNationalityAndResidencyResident
      page goToThePage()
      page fillPageWith claim
      val nextPage = page submitPage()
      nextPage must beAnInstanceOf[GOtherEEAStateOrSwitzerlandPage]
    }

    "navigate to next page on valid non resident submission" in new WithJsBrowser with PageObjects{
			val page =  GNationalityAndResidencyPage(context)
      val claim = ClaimScenarioFactory.yourNationalityAndResidencyNonResident
      page goToThePage()
      page fillPageWith claim
      val nextPage = page submitPage()
      println(nextPage.source)
      nextPage must beAnInstanceOf[GOtherEEAStateOrSwitzerlandPage]
    }

    "contain errors on invalid non resident submission" in new WithJsBrowser with PageObjects{
			val page =  GNationalityAndResidencyPage(context)
      val claim = ClaimScenarioFactory.yourNationalityAndResidencyNonResident
      claim.AboutYouNationalityAndResidencyActualNationality = ""
      page goToThePage()
      page fillPageWith claim
      val nextPage = page submitPage()
      nextPage must beAnInstanceOf[GNationalityAndResidencyPage]
    }

    "nationality is visible when clicked back" in new WithJsBrowser with PageObjects{
      val page =  GNationalityAndResidencyPage(context)
      val claim = ClaimScenarioFactory.yourNationalityAndResidencyNonResident
      page goToThePage()
      page fillPageWith claim
      val nextPage = page submitPage()
      val backPage=nextPage.goBack()
      val actualnationality=browser.find("#actualnationality").getValue
      actualnationality mustEqual("French")
    }

    "Modify nationality from preview page" in new WithJsBrowser with PageObjects{
      val previewPage = goToPreviewPage(context)
      val id = "about_you_nationality"
      val answerText = PreviewTestUtils.answerText(id, _:Page)

      answerText(previewPage) mustEqual "French"
      val nationalityPage = previewPage.clickLinkOrButton(getLinkId(id))

      nationalityPage must beAnInstanceOf[GNationalityAndResidencyPage]
      val modifiedData = new TestData
      modifiedData.AboutYouNationalityAndResidencyNationality = "British"

      nationalityPage fillPageWith modifiedData
      val previewPageModified = nationalityPage submitPage()

      previewPageModified must beAnInstanceOf[PreviewPage]
      answerText(previewPageModified) mustEqual "British"
    }
/*
PREVIEW PAGE STILL TO COMPLETE
    "Modify Do you normally live in England, Scotland or Wales? from preview page" in new WithJsBrowser with PageObjects {
      val previewPage = goToPreviewPage(context)
      val id = "about_you_residence"
      val answerText = PreviewTestUtils.answerText(id, _:Page)

      answerText(previewPage) mustEqual "No - France"
      val nationalityPage = previewPage.clickLinkOrButton(getLinkId(id))
      nationalityPage must beAnInstanceOf[GNationalityAndResidencyPage]

      val modifiedData = new TestData
      modifiedData.AboutYouNationalityAndResidencyResideInUK = "Yes"
      nationalityPage fillPageWith modifiedData
      val previewPageModified = nationalityPage submitPage()

      previewPageModified must beAnInstanceOf[PreviewPage]
      answerText(previewPageModified) mustEqual "Yes"
    }
    */

    /* Tests from 52 weeks page which is dropped
        """remember "no more 52 weeks trips" upon stating "52 weeks trips" and returning""" in new WithJsBrowser with PageObjects {
    }

    "Trip details must not be visible when time abroad page is displayed" in new WithJsBrowser with PageObjects {
    }

    "Trip details must be visible when returning back to the time abroad page" in new WithJsBrowser with PageObjects {
    }

    "Modify time outside from preview page" in new WithJsBrowser with PageObjects {
    }
     */
  }
  section("integration", models.domain.AboutYou.id)

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
