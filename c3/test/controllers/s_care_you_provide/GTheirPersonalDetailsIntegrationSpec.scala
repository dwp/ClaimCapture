package controllers.s_care_you_provide

import org.specs2.mutable._
import controllers.ClaimScenarioFactory
import utils.pageobjects.s_your_partner.GYourPartnerPersonalDetailsPage
import utils.pageobjects._
import utils.pageobjects.s_claim_date.GClaimDatePage
import utils.pageobjects.s_care_you_provide.{GMoreAboutTheCarePage, GTheirPersonalDetailsPage}
import utils.WithJsBrowser

class GTheirPersonalDetailsIntegrationSpec extends Specification {
  section("integration", models.domain.CareYouProvide.id)
  "Their Personal Details" should {
    "be presented" in new WithJsBrowser with PageObjects {
      val page = GTheirPersonalDetailsPage(context)
      page goToThePage()
    }

    "contain errors on invalid submission" in new WithJsBrowser with PageObjects {
      val theirDetailsPage = GTheirPersonalDetailsPage(context)
      theirDetailsPage goToThePage()
      val submittedPage = theirDetailsPage submitPage()
      submittedPage must beAnInstanceOf[GTheirPersonalDetailsPage]
      submittedPage.listErrors.size mustEqual 6
    }

    "navigate to next page on valid submission" in new WithJsBrowser with PageObjects {
      val theirPersonalDetailsPage = GTheirPersonalDetailsPage(context)
      val claim = ClaimScenarioFactory.s4CareYouProvide(hours35 = false)
      theirPersonalDetailsPage goToThePage()
      theirPersonalDetailsPage fillPageWith claim
      val moreAboutCare = theirPersonalDetailsPage submitPage()
      moreAboutCare must beAnInstanceOf[GMoreAboutTheCarePage]
    }

    """navigate back to "Partner details - About your partner" when they have had a partner/spouse at any time since the claim date""" in new WithJsBrowser with PageObjects {
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

    "be pre-populated if user answered yes to claiming for partner/spouse in yourPartner/personYouCareFor section" in new WithJsBrowser with PageObjects {
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
      val title = theirPersonalDetailsPage readInput("#title")
      title.get mustEqual "Mrs"
      val firstName = theirPersonalDetailsPage readInput("#firstName")
      firstName.get mustEqual "Cloe"
      val surname = theirPersonalDetailsPage readInput("#surname")
      surname.get mustEqual "Smith"
      val dateOfBirth = theirPersonalDetailsPage readDate("#dateOfBirth")
      dateOfBirth.get mustEqual "12/07/1990"
    }

    "fields must not be visible if user answered yes to claiming for partner/spouse in yourPartner/personYouCareFor section" in new WithJsBrowser with PageObjects {
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

    "fields must be visible if user answered no to claiming for partner/spouse in yourPartner/personYouCareFor section" in new WithJsBrowser with PageObjects {
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

    "data should be emptied if answered yes to person you care for is partner then going back to answer no" in new WithJsBrowser with PageObjects {
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
      val title = theirPersonalDetailsPage readInput("#title")
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
      newTheirPersonalDetailsPage must beAnInstanceOf[GTheirPersonalDetailsPage]

      val title2 = newTheirPersonalDetailsPage readInput("#title")
      title2.get mustEqual ""
      val firstName2 = newTheirPersonalDetailsPage readInput("#firstName")
      firstName2.get mustEqual ""
      val surname2 = newTheirPersonalDetailsPage readInput("#surname")
      surname2.get mustEqual ""
      val dateOfBirth2 = newTheirPersonalDetailsPage readDate("#dateOfBirth")
      dateOfBirth2.get mustEqual "00/00/"
    }

    "data should be emptied if answered yes to did you have a partner then going back to answer no" in new WithJsBrowser with PageObjects {
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
      val title = theirPersonalDetailsPage readInput("#title")
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
      newTheirPersonalDetailsPage must beAnInstanceOf[GTheirPersonalDetailsPage]

      val title2 = newTheirPersonalDetailsPage readInput("#title")
      title2.get mustEqual ""
      val firstName2 = newTheirPersonalDetailsPage readInput("#firstName")
      firstName2.get mustEqual ""
      val surname2 = newTheirPersonalDetailsPage readInput("#surname")
      surname2.get mustEqual ""
      val dateOfBirth2 = newTheirPersonalDetailsPage readDate("#dateOfBirth")
      dateOfBirth2.get mustEqual "00/00/"
    }
  }
  section("integration", models.domain.CareYouProvide.id)
}
