package controllers.s_about_you

import org.specs2.mutable._
import utils.{LightFakeApplication, WithJsBrowser}
import controllers.{PreviewTestUtils, ClaimScenarioFactory}
import utils.pageobjects._
import utils.pageobjects.preview.PreviewPage
import utils.pageobjects.s_about_you._
import utils.pageobjects.{TestData, PageObjects}
import utils.pageobjects.s_claim_date.GClaimDatePage
import utils.helpers.PreviewField._

class GContactDetailsIntegrationSpec extends Specification {
  section("integration", models.domain.AboutYou.id)
  "Contact Details" should {
    "be presented" in new WithJsBrowser with PageObjects {
      val page = GContactDetailsPage(context)
      page goToThePage()
    }

    "contain error if address not filled in" in new WithJsBrowser with PageObjects {
      val page = GContactDetailsPage(context)
      val claim = ClaimScenarioFactory.yourDetailsWithNotTimeOutside()
      claim.AboutYouAddress = ""
      page goToThePage()
      page fillPageWith claim

      val errors = page.submitPage().listErrors
      errors.size mustEqual 1
      errors(0) must contain("Address")

    }

    "valid submission if 'Contact phone or mobile number' not filled in" in new WithJsBrowser with PageObjects {
      val page = GContactDetailsPage(context)
      val claim = ClaimScenarioFactory.yourDetailsWithNotTimeOutside()
      claim.HowWeContactYou = ""
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage()

      nextPage must beAnInstanceOf[GNationalityAndResidencyPage]
    }

    "valid submission if 'Contact phone or mobile number' is filled in with number" in new WithJsBrowser with PageObjects {
      val page = GContactDetailsPage(context)
      val claim = ClaimScenarioFactory.yourDetailsWithNotTimeOutside()
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage()

      nextPage must beAnInstanceOf[GNationalityAndResidencyPage]
    }

    "contain error if 'Contact number' is filled in with text" in new WithJsBrowser with PageObjects {
      val page = GContactDetailsPage(context)
      val claim = ClaimScenarioFactory.yourDetailsWithNotTimeOutside()
      claim.HowWeContactYou = "I do not have contact number"
      page goToThePage()
      page fillPageWith claim

      val errors = page.submitPage().listErrors
      errors.size mustEqual 1
      errors(0) must contain("Contact number - Invalid value")
    }

    "contain error if 'Contact number' is field length less than min length" in new WithJsBrowser with PageObjects {
      val page = GContactDetailsPage(context)
      val claim = ClaimScenarioFactory.yourDetailsWithNotTimeOutside()
      claim.HowWeContactYou = "012345"
      page goToThePage()
      page fillPageWith claim

      val errors = page.submitPage().listErrors
      errors.size mustEqual 1
      errors(0) must contain("Contact number - Invalid value")
    }

    "navigate to next page on valid submission" in new WithJsBrowser with PageObjects {
      val page = GContactDetailsPage(context)
      val claim = ClaimScenarioFactory.yourDetailsWithNotTimeOutside()
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage()

      nextPage must beAnInstanceOf[GNationalityAndResidencyPage]
    }

    "be able to navigate back " in new WithJsBrowser with PageObjects {
      val claimDatePage = GClaimDatePage(context)
      claimDatePage goToThePage()
      val claimDate = ClaimScenarioFactory.s12ClaimDate()
      claimDatePage fillPageWith claimDate

      val page = claimDatePage submitPage()
      val claim = ClaimScenarioFactory yourDetailsWithNotTimeOutside()
      page goToThePage()
      page fillPageWith claim
      val contactDetailsPage = page submitPage (waitForPage = true)
      val completedPage = contactDetailsPage goBack()
      completedPage must beAnInstanceOf[GYourDetailsPage]
    }

    "Present email neither yes or no help when no email selected" in new WithJsBrowser with PageObjects {
      val page = GContactDetailsPage(context)
      page goToThePage()
      page must beAnInstanceOf[GContactDetailsPage]
      page visible ("#emailYesHelper") must beFalse
      page visible ("#emailNoHelper") must beFalse
    }

    "Present email neither yes or no help when no email selected" in new WithJsBrowser with PageObjects {
      val previousPage = GMaritalStatusPage(context)
      previousPage goToThePage()
      previousPage fillPageWith ClaimScenarioFactory.maritalStatus()

      val page = previousPage submitPage()
      page must beAnInstanceOf[GContactDetailsPage]
      page.source must contain("id=\"wantsEmailContact_yes\"")
      page visible ("#emailYesHelper") must beFalse
      page visible ("#emailNoHelper") must beFalse
    }

    "Present email yes help only when email yes clicked" in new WithJsBrowser(app = LightFakeApplication(additionalConfiguration = Map("saveForLaterSaveEnabled" -> "true"))) with PageObjects {
      val page = GContactDetailsPage(context)
      page goToThePage()
      page.source must contain("id=\"wantsEmailContact_yes\"")
      page.clickLinkOrButton("#wantsEmailContact_yes")
      page visible ("#emailYesHelper") must beTrue
      page visible ("#emailNoHelper") must beFalse
    }

    "Present email no help only when email no clicked" in new WithJsBrowser(app = LightFakeApplication(additionalConfiguration = Map("saveForLaterSaveEnabled" -> "true"))) with PageObjects {
      val page = GContactDetailsPage(context)
      page goToThePage()
      page.source must contain("id=\"wantsEmailContact_no\"")
      page.clickLinkOrButton("#wantsEmailContact_no")
      page visible ("#emailYesHelper") must beFalse
      page visible ("#emailNoHelper") must beTrue
    }

    "Present email with no help only when email yes selected and return to page" in new WithJsBrowser(app = LightFakeApplication(additionalConfiguration = Map("saveForLaterSaveEnabled" -> "true"))) with PageObjects {
      val page = GContactDetailsPage(context)
      val claim = ClaimScenarioFactory.yourDetailsWithNotTimeOutside()
      claim.AboutYouWantsEmailContact = "Yes"
      claim.AboutYouMail = "fred@bt.com"
      claim.AboutYouMailConfirmation = "fred@bt.com"
      page goToThePage()
      page fillPageWith claim
      page.source must contain("id=\"wantsEmailContact_yes\"")
      page visible ("#emailYesHelper") must beTrue
      page visible ("#emailNoHelper") must beFalse

      val nextPage = page submitPage()
      nextPage must beAnInstanceOf[GNationalityAndResidencyPage]
      val contactPageAgain = nextPage goBack()
      contactPageAgain.source must contain("fred@bt.com")
      contactPageAgain visible ("#emailYesHelper") must beTrue
      contactPageAgain visible ("#emailNoHelper") must beFalse
    }

    "Present email no help only when email no selected and return to page" in new WithJsBrowser(app = LightFakeApplication(additionalConfiguration = Map("saveForLaterSaveEnabled" -> "true"))) with PageObjects {
      val page = GContactDetailsPage(context)
      val claim = ClaimScenarioFactory.yourDetailsWithNotTimeOutside()
      claim.AboutYouWantsEmailContact = "No"
      page goToThePage()
      page fillPageWith claim
      page.source must contain("id=\"wantsEmailContact_yes\"")
      page visible ("#emailYesHelper") must beFalse
      page visible ("#emailNoHelper") must beTrue

      val nextPage = page submitPage()
      nextPage must beAnInstanceOf[GNationalityAndResidencyPage]
      val contactPageAgain = nextPage goBack()
      contactPageAgain visible ("#emailYesHelper") must beFalse
      contactPageAgain visible ("#emailNoHelper") must beTrue
    }

    "Clear email when return to page and click email No" in new WithJsBrowser(app = LightFakeApplication(additionalConfiguration = Map("saveForLaterSaveEnabled" -> "true"))) with PageObjects {
      val page = GContactDetailsPage(context)
      val claim = ClaimScenarioFactory.yourDetailsWithNotTimeOutside()
      claim.AboutYouWantsEmailContact = "Yes"
      claim.AboutYouMail = "fred@bt.com"
      claim.AboutYouMailConfirmation = "fred@bt.com"
      page goToThePage()
      page fillPageWith claim
      page.source must contain("id=\"wantsEmailContact_yes\"")
      page visible ("#emailYesHelper") must beTrue
      page visible ("#emailNoHelper") must beFalse

      val nextPage = page submitPage()
      nextPage must beAnInstanceOf[GNationalityAndResidencyPage]
      val contactPageAgain = nextPage goBack()
      contactPageAgain.source must contain("fred@bt.com")
      contactPageAgain visible ("#emailYesHelper") must beTrue
      contactPageAgain visible ("#emailNoHelper") must beFalse

      contactPageAgain.clickLinkOrButton("#wantsEmailContact_no")
      contactPageAgain visible ("#emailYesHelper") must beFalse
      contactPageAgain visible ("#emailNoHelper") must beTrue
      contactPageAgain.source must not contain ("fred@bt.com")
    }

    "Modify address from preview page" in new WithJsBrowser with PageObjects {
      val page = GContactDetailsPage(context)
      val claim = ClaimScenarioFactory.yourDetailsWithNotTimeOutside()
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage()

      val id = "about_you_address"

      val answerText = PreviewTestUtils.answerText(id, _: Page)

      val previewPage = PreviewPage(context)
      previewPage goToThePage()
      answerText(previewPage) mustEqual "101 Clifton Street, Blackpool FY1 2RW"
      val contactDetails = previewPage.clickLinkOrButton(getLinkId(id))

      contactDetails must beAnInstanceOf[GContactDetailsPage]
      val modifiedData = new TestData
      modifiedData.AboutYouAddress = "10 someplace&Wherever"
      modifiedData.AboutYouPostcode = "M4 4TJ"

      contactDetails fillPageWith modifiedData
      val previewPageModified = contactDetails submitPage()

      previewPageModified must beAnInstanceOf[PreviewPage]
      answerText(previewPageModified) mustEqual "10 someplace, Wherever M4 4TJ"

    }

    "Modify contact number from preview page" in new WithJsBrowser with PageObjects {
      val page = GContactDetailsPage(context)
      val claim = ClaimScenarioFactory.yourDetailsWithNotTimeOutside()
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage()

      val id = "about_you_contact"

      val answerText = PreviewTestUtils.answerText(id, _: Page)

      val previewPage = PreviewPage(context)
      previewPage goToThePage()
      answerText(previewPage) mustEqual "01772 888901"
      val contactDetails = previewPage.clickLinkOrButton(getLinkId(id))

      contactDetails must beAnInstanceOf[GContactDetailsPage]
      val modifiedData = new TestData
      modifiedData.HowWeContactYou = "0123456789"

      contactDetails fillPageWith modifiedData
      val previewPageModified = contactDetails submitPage()

      previewPageModified must beAnInstanceOf[PreviewPage]
      answerText(previewPageModified) mustEqual "0123456789"

    }

    "Modify email from Preview page" in new WithJsBrowser with PageObjects {
      val contactPage = GContactDetailsPage(context)
      val claim = ClaimScenarioFactory.yourDetailsWithNotTimeOutside()
      contactPage goToThePage()
      contactPage fillPageWith claim
      val nextPage = contactPage submitPage()

      val id = "about_you_email"
      val answerText = PreviewTestUtils.answerText(id, _: Page)

      val previewPage = PreviewPage(context)
      previewPage goToThePage()
      answerText(previewPage) mustEqual "No"
      val contactPageRevisited = previewPage.clickLinkOrButton(getLinkId(id))
      contactPageRevisited must beAnInstanceOf[GContactDetailsPage]

      val modifiedData = new TestData
      modifiedData.AboutYouWantsEmailContact = "Yes"
      modifiedData.AboutYouMail = "myemail@website.com"
      modifiedData.AboutYouMailConfirmation = "myemail@website.com"
      contactPageRevisited fillPageWith modifiedData

      val previewPageModified = contactPageRevisited submitPage()

      previewPageModified must beAnInstanceOf[PreviewPage]
      answerText(previewPageModified) mustEqual "Yes - myemail@website.com"
    }

    "Present email with no help only when email yes and email has space selected and return to page" in new WithJsBrowser(app = LightFakeApplication(additionalConfiguration = Map("saveForLaterSaveEnabled" -> "true"))) with PageObjects {
      val page = GContactDetailsPage(context)
      val claim = ClaimScenarioFactory.yourDetailsWithNotTimeOutside()
      claim.AboutYouWantsEmailContact = "Yes"
      claim.AboutYouMail = "fred@bt.com "
      claim.AboutYouMailConfirmation = "fred@bt.com "
      page goToThePage()
      page fillPageWith claim
      page.source must contain("id=\"wantsEmailContact_yes\"")
      page visible ("#emailYesHelper") must beTrue
      page visible ("#emailNoHelper") must beFalse

      val nextPage = page submitPage()
      nextPage must beAnInstanceOf[GNationalityAndResidencyPage]
      val contactPageAgain = nextPage goBack()
      contactPageAgain.source must contain("fred@bt.com")
      contactPageAgain visible ("#emailYesHelper") must beTrue
      contactPageAgain visible ("#emailNoHelper") must beFalse
    }

    "Present postcode with space selected and return to page" in new WithJsBrowser with PageObjects {
      val page = GContactDetailsPage(context)
      val claim = ClaimScenarioFactory.yourDetailsWithNotTimeOutside()
      claim.AboutYouPostcode = " FY1  2RW "
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage()
      nextPage must beAnInstanceOf[GNationalityAndResidencyPage]
      val contactPageAgain = nextPage goBack()
      contactPageAgain.source must contain("FY1 2RW")
    }

  }
  section("integration", models.domain.AboutYou.id)
}
