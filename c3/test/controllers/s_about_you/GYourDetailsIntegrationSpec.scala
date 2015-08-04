package controllers.s_about_you

import org.specs2.mutable.{Tags, Specification}
import play.api.test.{TestBrowser}
import utils.WithBrowser
import utils.pageobjects.common.ClaimHelpPage
import utils.pageobjects.preview.PreviewPage
import utils.pageobjects.s_about_you._
import utils.pageobjects.s_eligibility.GApprovePage
import controllers.ClaimScenarioFactory
import utils.pageobjects._
import utils.pageobjects.s_claim_date.GClaimDatePage

class GYourDetailsIntegrationSpec extends Specification with Tags {
  "Your Details" should {
    "be presented" in new WithBrowser with PageObjects{
			val page =  GYourDetailsPage(context)
      page goToThePage()
    }

    "give access to help page" in new WithBrowser with PageObjects{
      val page =  GYourDetailsPage(context)
      page goToThePage()
      val helpPage = page.clickLinkOrButton("#helppage")
      helpPage.isInstanceOf[ClaimHelpPage] must beTrue
    }

    "navigate back to approve page" in new WithBrowser with PageObjects{
			val page =  GYourDetailsPage(context)
      browser goTo GApprovePage.url

      page goToThePage()
      val backPage = page goBack()
      backPage must beAnInstanceOf[GApprovePage]
    }

    "present errors if mandatory fields are not populated" in new WithBrowser with PageObjects{
			val page =  GYourDetailsPage(context)
      page goToThePage()
      page.submitPage().listErrors.size mustEqual 6
    }

    "Accept submit if all mandatory fields are populated" in new WithBrowser with PageObjects{
      val claimDatePage = GClaimDatePage(context)
      claimDatePage goToThePage()
      val claimDate = ClaimScenarioFactory.s12ClaimDate()
      claimDatePage fillPageWith claimDate

			val page =  claimDatePage submitPage()
      val claim = ClaimScenarioFactory.yourDetailsWithNotTimeOutside()
      page goToThePage()
      page fillPageWith claim

      val g2 = page submitPage()
      
      g2 must beAnInstanceOf[GMaritalStatusPage]
    }

    "Modify title, name, middlename and last name from preview page" in new WithBrowser with PageObjects{

      val id = "about_you_full_name"

      val answerText = textFromXPath(id, _:Page)

      val previewPage = goToPreviewPage(context)
      answerText(previewPage) mustEqual "Mr John Appleseed"
      val aboutYou = previewPage.clickLinkOrButton(s"#$id")

      aboutYou must beAnInstanceOf[GYourDetailsPage]
      val modifiedData = new TestData
      modifiedData.AboutYouTitle = "Mrs"
      modifiedData.AboutYouFirstName = "Jane"
      modifiedData.AboutYouSurname = "Pearson"

      aboutYou fillPageWith modifiedData
      val previewPageModified = aboutYou submitPage()

      previewPageModified must beAnInstanceOf[PreviewPage]
      answerText(previewPageModified) mustEqual "Mrs Jane Pearson"

    }

    "Modify national insurance number from preview page" in new WithBrowser with PageObjects{
      val previewPage = goToPreviewPage(context)
      val id = "about_you_nino"
      val answerText = textFromXPath(id, _:Page)

      answerText(previewPage) mustEqual "AB123456C"
      val aboutYou = previewPage.clickLinkOrButton(s"#$id")

      aboutYou must beAnInstanceOf[GYourDetailsPage]
      val modifiedData = new TestData
      modifiedData.AboutYouNINO = "AB123456D"

      aboutYou fillPageWith modifiedData
      val previewPageModified = aboutYou submitPage()

      previewPageModified must beAnInstanceOf[PreviewPage]
      answerText(previewPageModified) mustEqual "AB123456D"
    }

    "Modify date of birth from preview page" in new WithBrowser with PageObjects{
      val previewPage = goToPreviewPage(context)
      val id = "about_you_dob"
      val answerText = textFromXPath(id, _:Page)

      answerText(previewPage) mustEqual "03 April, 1950"
      val aboutYou = previewPage.clickLinkOrButton(s"#$id")

      aboutYou must beAnInstanceOf[GYourDetailsPage]
      val modifiedData = new TestData
      modifiedData.AboutYouDateOfBirth = "03/04/1952"

      aboutYou fillPageWith modifiedData
      val previewPageModified = aboutYou submitPage()

      previewPageModified must beAnInstanceOf[PreviewPage]
      answerText(previewPageModified) mustEqual "03 April, 1952"
    }


  } section("integration", models.domain.AboutYou.id)

  def goToPreviewPage(context:PageObjectsContext):Page = {
    val claimDatePage = GClaimDatePage(context)
    claimDatePage goToThePage()
    val claimDate = ClaimScenarioFactory.s12ClaimDate()
    claimDatePage fillPageWith claimDate

    val page =  claimDatePage submitPage()
    val claim = ClaimScenarioFactory.yourDetailsWithNotTimeOutside()
    page goToThePage()
    page fillPageWith claim
    page submitPage()

    val previewPage = PreviewPage(context)
    previewPage goToThePage()
  }

  def textFromXPath(id:String,previewPage:Page):String = {
    previewPage.xpath(s"//dt[./a[@id='$id']]/following-sibling::dd").getText
  }
}