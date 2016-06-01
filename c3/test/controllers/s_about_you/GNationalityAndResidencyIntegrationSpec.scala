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

  val urlUnderTest = "/nationality/where-you-live"
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

    "lived-in-uk full details are saved correctly thus set when clicked back" in new WithJsBrowser with PageObjects{
      val page =  GNationalityAndResidencyPage(context)
      val claim = yourNationalityAllDetails
      page goToThePage()
      page fillPageWith claim
      val nextPage = page submitPage()
      val backPage=nextPage.goBack()
      browser.find("#actualnationality").getValue mustEqual("French")
      browser.find("#alwaysLivedInUK_yes").getAttribute("selected") mustEqual null
      browser.find("#alwaysLivedInUK_no").getAttribute("selected") mustEqual "true"
      browser.find("#liveInUKNow_yes").getAttribute("selected") mustEqual "true"
      browser.find("#arrivedInUK_more").getAttribute("selected") mustEqual null
      browser.find("#arrivedInUKDate_day").getValue mustEqual "1"
      browser.find("#arrivedInUKDate_month").getValue mustEqual "5"
      browser.find("#arrivedInUKDate_year").getValue mustEqual "2016"
      browser.find("#arrivedInUKFrom").getValue mustEqual("Turkey")
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

  def yourNationalityAllDetails() = {
    val claim = new TestData
    claim.AboutYouNationalityAndResidencyNationality = "Another nationality"
    claim.AboutYouNationalityAndResidencyActualNationality = "French"
    claim.AboutYouNationalityAndResidencyAlwaysLivedInUK = "No"
    claim.AboutYouNationalityAndResidencyLiveInUKNow = "Yes"
    claim.AboutYouNationalityAndResidencyArrivedInUK = "less"
    claim.AboutYouNationalityAndResidencyArrivedInUKDate = "01/05/2016"
    claim.AboutYouNationalityAndResidencyArrivedInUKFrom = "Turkey"

    claim.AboutYouNationalityAndResidencyTrip52Weeks = "No"
    claim
  }
}
