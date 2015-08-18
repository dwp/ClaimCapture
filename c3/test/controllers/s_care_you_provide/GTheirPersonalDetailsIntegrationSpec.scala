package controllers.s_care_you_provide

import org.specs2.mutable.{Tags, Specification}
import utils.{WebDriverHelper, WithBrowser}
import controllers.{PreviewTestUtils, ClaimScenarioFactory}
import utils.pageobjects.s_your_partner.GYourPartnerPersonalDetailsPage
import utils.pageobjects._
import utils.pageobjects.s_claim_date.GClaimDatePage
import utils.pageobjects.preview.PreviewPage
import utils.pageobjects.s_care_you_provide.{GTheirContactDetailsPage, GTheirPersonalDetailsPage}
import utils.WithJsBrowser

class GTheirPersonalDetailsIntegrationSpec extends Specification with Tags {
  "Their Personal Details" should {
    "be presented" in new WithJsBrowser  with PageObjects {
      val page = GTheirPersonalDetailsPage(context)
      page goToThePage()
    }

    "contain errors on invalid submission" in new WithJsBrowser  with PageObjects {
      val theirDetailsPage = GTheirPersonalDetailsPage(context)
      theirDetailsPage goToThePage()
      val submittedPage = theirDetailsPage submitPage()
      submittedPage must beAnInstanceOf[GTheirPersonalDetailsPage]
      submittedPage.listErrors.size mustEqual 6
    }

    "navigate to next page on valid submission" in new WithJsBrowser  with PageObjects {
      val theirPersonalDetailsPage = GTheirPersonalDetailsPage(context)
      val claim = ClaimScenarioFactory.s4CareYouProvide(hours35 = false)
      theirPersonalDetailsPage goToThePage()
      theirPersonalDetailsPage fillPageWith claim
      val contactDetailsPage = theirPersonalDetailsPage submitPage()
      contactDetailsPage must beAnInstanceOf[GTheirContactDetailsPage]
    }

    """navigate back to "Partner details - About your partner" when they have had a partner/spouse at any time since the claim date""" in new WithJsBrowser  with PageObjects {
      val claimDatePage = GClaimDatePage(context)
      claimDatePage goToThePage()
      val claimDate = ClaimScenarioFactory.s12ClaimDate()
      claimDatePage fillPageWith claimDate
      claimDatePage submitPage()

      val partnerPage = GYourPartnerPersonalDetailsPage(context)
      val partnerData = ClaimScenarioFactory.s2ands3WithTimeOUtsideUKAndProperty()
      partnerPage goToThePage()
      partnerPage fillPageWith partnerData
      val theirPersonalDetailsPage =  partnerPage submitPage()
      theirPersonalDetailsPage must beAnInstanceOf[GTheirPersonalDetailsPage]
      theirPersonalDetailsPage goBack() must beAnInstanceOf[GYourPartnerPersonalDetailsPage]
    }

    
    "be pre-populated if user answered yes to claiming for partner/spouse in yourPartner/personYouCareFor section" in new WithJsBrowser  with PageObjects {
      val claimDatePage = GClaimDatePage(context)
      claimDatePage goToThePage()
      val claimDate = ClaimScenarioFactory.s12ClaimDate()
      claimDatePage fillPageWith claimDate
      claimDatePage submitPage()

      val partnerPage = GYourPartnerPersonalDetailsPage(context)
      val partnerData = ClaimScenarioFactory.s2ands3WithTimeOUtsideUKAndProperty()
      partnerData.AboutYourPartnerIsYourPartnerThePersonYouAreClaimingCarersAllowancefor = "Yes"
      partnerPage goToThePage()
      partnerPage fillPageWith partnerData
      val theirPersonalDetailsPage =  partnerPage submitPage()

      theirPersonalDetailsPage must beAnInstanceOf[GTheirPersonalDetailsPage]
      val title = theirPersonalDetailsPage readRadio("#title")
      title.get mustEqual "Mrs"
      val firstName = theirPersonalDetailsPage readInput("#firstName")
      firstName.get mustEqual "Cloe"
      val surname = theirPersonalDetailsPage readInput("#surname")
      surname.get mustEqual "Smith"
      val dateOfBirth = theirPersonalDetailsPage readDate("#dateOfBirth")
      dateOfBirth.get mustEqual "12/07/1990"
    }

    "fields must not be visible if user answered yes to claiming for partner/spouse in yourPartner/personYouCareFor section" in new WithJsBrowser  with PageObjects {
      val claimDatePage = GClaimDatePage(context)
      claimDatePage goToThePage()
      val claimDate = ClaimScenarioFactory.s12ClaimDate()
      claimDatePage fillPageWith claimDate
      claimDatePage submitPage()

      val partnerPage = GYourPartnerPersonalDetailsPage(context)
      val partnerData = ClaimScenarioFactory.s2ands3WithTimeOUtsideUKAndProperty()
      partnerData.AboutYourPartnerIsYourPartnerThePersonYouAreClaimingCarersAllowancefor = "Yes"
      partnerPage goToThePage()
      partnerPage fillPageWith partnerData
      val theirPersonalDetailsPage =  partnerPage submitPage()

      theirPersonalDetailsPage must beAnInstanceOf[GTheirPersonalDetailsPage]

      theirPersonalDetailsPage visible("#careYouProvideWrap") must beFalse
    }

    "fields must be visible if user answered no to claiming for partner/spouse in yourPartner/personYouCareFor section" in new WithJsBrowser  with PageObjects {
      val claimDatePage = GClaimDatePage(context)
      claimDatePage goToThePage()
      val claimDate = ClaimScenarioFactory.s12ClaimDate()
      claimDatePage fillPageWith claimDate
      claimDatePage submitPage()

      val partnerPage = GYourPartnerPersonalDetailsPage(context)
      val partnerData = ClaimScenarioFactory.s2ands3WithTimeOUtsideUKAndProperty()
      partnerData.AboutYourPartnerIsYourPartnerThePersonYouAreClaimingCarersAllowancefor = "No"
      partnerPage goToThePage()
      partnerPage fillPageWith partnerData
      val theirPersonalDetailsPage =  partnerPage submitPage()

      theirPersonalDetailsPage must beAnInstanceOf[GTheirPersonalDetailsPage]

      theirPersonalDetailsPage visible("#careYouProvideWrap") must beTrue
    }

    "Modify name from preview page" in new WithJsBrowser  with PageObjects{
      val modifiedData = new TestData
      modifiedData.AboutTheCareYouProvideTitlePersonCareFor = "Mrs"
      modifiedData.AboutTheCareYouProvideFirstNamePersonCareFor = "Jane"
      modifiedData.AboutTheCareYouProvideMiddleNamePersonCareFor = "Doe"
      modifiedData.AboutTheCareYouProvideSurnamePersonCareFor = "Antony"

      verifyPreviewData(context, "care_you_provide_name", "Mr Tom Potter Wilson", modifiedData, "Mrs Jane Doe Antony")
    }

    "Modify nino from preview page" in new WithJsBrowser  with PageObjects{
      val modifiedData = new TestData
      modifiedData.AboutTheCareYouProvideNINOPersonCareFor = "AB123456D"

      verifyPreviewData(context, "care_you_provide_nino", "AA123456A", modifiedData, "AB123456D")
    }

    "Modify date of birth from preview page" in new WithJsBrowser  with PageObjects{
      val modifiedData = new TestData
      modifiedData.AboutTheCareYouProvideDateofBirthPersonYouCareFor = "02/04/1991"

      verifyPreviewData(context, "care_you_provide_dob", "02 March, 1990", modifiedData, "02 April, 1991")
    }

    "Modify relationship from preview page" in new WithJsBrowser  with PageObjects{
      val modifiedData = new TestData
      modifiedData.AboutTheCareYouProvideWhatTheirRelationshipToYou = "wife"

      verifyPreviewData(context, "care_you_provide_relationship", "Father", modifiedData, "wife")
    }


    "data should be emptied if answered yes to person you care for is partner then going back to answer no" in new WithJsBrowser  with PageObjects {
      val claimDatePage = GClaimDatePage(context)
      claimDatePage goToThePage()
      val claimDate = ClaimScenarioFactory.s12ClaimDate()
      claimDatePage fillPageWith claimDate
      claimDatePage submitPage()

      val partnerPage = GYourPartnerPersonalDetailsPage(context)
      val partnerData = ClaimScenarioFactory.s2ands3WithTimeOUtsideUKAndProperty()
      partnerData.AboutYourPartnerIsYourPartnerThePersonYouAreClaimingCarersAllowancefor = "Yes"
      partnerPage goToThePage()
      partnerPage fillPageWith partnerData
      val theirPersonalDetailsPage =  partnerPage submitPage()

      theirPersonalDetailsPage must beAnInstanceOf[GTheirPersonalDetailsPage]
      val title = theirPersonalDetailsPage readRadio("#title")
      title.get mustEqual "Mrs"
      val firstName = theirPersonalDetailsPage readInput("#firstName")
      firstName.get mustEqual "Cloe"
      val surname = theirPersonalDetailsPage readInput("#surname")
      surname.get mustEqual "Smith"
      val dateOfBirth = theirPersonalDetailsPage readDate("#dateOfBirth")
      dateOfBirth.get mustEqual "12/07/1990"

      val newPartner = theirPersonalDetailsPage.goBack()
      val newPartnerData = ClaimScenarioFactory.s2ands3WithTimeOUtsideUKAndProperty()
      newPartnerData.AboutYourPartnerIsYourPartnerThePersonYouAreClaimingCarersAllowancefor = "No"
      newPartner fillPageWith newPartnerData

      val newTheirPersonalDetailsPage =  newPartner submitPage()
      theirPersonalDetailsPage must beAnInstanceOf[GTheirPersonalDetailsPage]

      val title2 = theirPersonalDetailsPage readRadio("#title")
      title2 must beNone
      val firstName2 = theirPersonalDetailsPage readInput("#firstName")
      firstName2.get mustEqual ""
      val surname2 = theirPersonalDetailsPage readInput("#surname")
      surname2.get mustEqual ""
      val dateOfBirth2 = theirPersonalDetailsPage readDate("#dateOfBirth")
      dateOfBirth2.get mustEqual "00/00/"



    }

    "data should be emptied if answered yes to did you have a partner then going back to answer no" in new WithJsBrowser  with PageObjects {
      val claimDatePage = GClaimDatePage(context)
      claimDatePage goToThePage()
      val claimDate = ClaimScenarioFactory.s12ClaimDate()
      claimDatePage fillPageWith claimDate
      claimDatePage submitPage()

      val partnerPage = GYourPartnerPersonalDetailsPage(context)
      val partnerData = ClaimScenarioFactory.s2ands3WithTimeOUtsideUKAndProperty()
      partnerData.AboutYourPartnerIsYourPartnerThePersonYouAreClaimingCarersAllowancefor = "Yes"
      partnerPage goToThePage()
      partnerPage fillPageWith partnerData
      val theirPersonalDetailsPage =  partnerPage submitPage()

      theirPersonalDetailsPage must beAnInstanceOf[GTheirPersonalDetailsPage]
      val title = theirPersonalDetailsPage readRadio("#title")
      title.get mustEqual "Mrs"
      val firstName = theirPersonalDetailsPage readInput("#firstName")
      firstName.get mustEqual "Cloe"
      val surname = theirPersonalDetailsPage readInput("#surname")
      surname.get mustEqual "Smith"
      val dateOfBirth = theirPersonalDetailsPage readDate("#dateOfBirth")
      dateOfBirth.get mustEqual "12/07/1990"

      val newPartner = theirPersonalDetailsPage.goBack()
      val newPartnerData = new TestData

      newPartnerData.AboutYourPartnerHadPartnerSinceClaimDate = "No"

      newPartner fillPageWith newPartnerData

      val newTheirPersonalDetailsPage =  newPartner submitPage()
      theirPersonalDetailsPage must beAnInstanceOf[GTheirPersonalDetailsPage]

      val title2 = theirPersonalDetailsPage readRadio("#title")
      title2 must beNone
      val firstName2 = theirPersonalDetailsPage readInput("#firstName")
      firstName2.get mustEqual ""
      val surname2 = theirPersonalDetailsPage readInput("#surname")
      surname2.get mustEqual ""
      val dateOfBirth2 = theirPersonalDetailsPage readDate("#dateOfBirth")
      dateOfBirth2.get mustEqual "00/00/"



    }

  } section("integration", models.domain.CareYouProvide.id)

  def goToPreviewPage(context:PageObjectsContext):Page = {
    val claimDatePage = GClaimDatePage(context)
    claimDatePage goToThePage()
    val claimDate = ClaimScenarioFactory.s12ClaimDate()
    claimDatePage fillPageWith claimDate
    claimDatePage submitPage()

    val partnerPage = GYourPartnerPersonalDetailsPage(context)
    val partnerData = new TestData
    partnerData.AboutYourPartnerHadPartnerSinceClaimDate = "No"
    partnerPage goToThePage()
    partnerPage fillPageWith partnerData
    partnerPage submitPage()

    val theirPersonalDetailsPage = GTheirPersonalDetailsPage(context)
    val claim = ClaimScenarioFactory.s4CareYouProvide(hours35 = false)
    theirPersonalDetailsPage goToThePage()
    theirPersonalDetailsPage fillPageWith claim
    theirPersonalDetailsPage submitPage()

    val previewPage = PreviewPage(context)
    previewPage goToThePage()
  }

  def verifyPreviewData(context:PageObjectsContext, id:String, initialData:String, modifiedTestData:TestData, modifiedData:String) = {
    val previewPage = goToPreviewPage(context)
    val answerText = PreviewTestUtils.answerText(id, _:Page)

    answerText(previewPage) mustEqual initialData
    val theirPersonalDetailsPage = previewPage.clickLinkOrButton(s"#$id")

    theirPersonalDetailsPage must beAnInstanceOf[GTheirPersonalDetailsPage]

    theirPersonalDetailsPage fillPageWith modifiedTestData
    val previewPageModified = theirPersonalDetailsPage submitPage()

    previewPageModified must beAnInstanceOf[PreviewPage]
    answerText(previewPageModified) mustEqual modifiedData
  }
}