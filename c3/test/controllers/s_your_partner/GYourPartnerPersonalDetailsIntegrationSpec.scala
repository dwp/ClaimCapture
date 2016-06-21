package controllers.s_your_partner

import org.specs2.mutable._
import utils.WithBrowser
import controllers.ClaimScenarioFactory
import utils.pageobjects.s_care_you_provide.GTheirPersonalDetailsPage
import utils.pageobjects._
import utils.pageobjects.s_your_partner.GYourPartnerPersonalDetailsPage
import utils.pageobjects.s_about_you.{GMaritalStatusPage, GPaymentsFromAbroadPage, GNationalityAndResidencyPage}
import app.MaritalStatus

class GYourPartnerPersonalDetailsIntegrationSpec extends Specification {
  section("integration", models.domain.YourPartner.id)
  "Your Partner Personal Details" should {
    "be presented" in new WithBrowser with PageObjects {
      val page = GYourPartnerPersonalDetailsPage(context)
      page goToThePage()
      page.url mustEqual GYourPartnerPersonalDetailsPage.url
    }

    "contain error on invalid submission" in new WithBrowser with PageObjects {
      val page = GYourPartnerPersonalDetailsPage(context)
      page goToThePage()
      page submitPage()

      page.listErrors.size mustEqual 1
    }

    "contain errors 'when have you lived with a partner is yes' on invalid submission" in new WithBrowser with PageObjects {
      val nationalityPage =  GNationalityAndResidencyPage(context)
      val claim = ClaimScenarioFactory.yourNationalityAndResidencyNonResident
      claim.AboutYouWhatIsYourMaritalOrCivilPartnershipStatus = MaritalStatus.Married
      nationalityPage goToThePage()
      nationalityPage fillPageWith claim
      nationalityPage submitPage()

      val maritalStatus = GMaritalStatusPage(context)
      maritalStatus goToThePage()
      maritalStatus fillPageWith claim
      maritalStatus submitPage()

      val partnerPage = GYourPartnerPersonalDetailsPage(context)
      val partnerData = new TestData
      partnerData.AboutYourPartnerHadPartnerSinceClaimDate = "Yes"
      partnerPage goToThePage ()
      partnerPage fillPageWith partnerData
      partnerPage submitPage()

      partnerPage.listErrors.size mustEqual 7
    }

    "navigate to next page on valid submission" in new WithBrowser with PageObjects {
      val nationalityPage =  GNationalityAndResidencyPage(context)
      val claim = ClaimScenarioFactory.yourNationalityAndResidencyNonResident
      claim.AboutYouWhatIsYourMaritalOrCivilPartnershipStatus = MaritalStatus.Married
      nationalityPage goToThePage()
      nationalityPage fillPageWith claim
      nationalityPage submitPage()

      val maritalStatus = GMaritalStatusPage(context)
      maritalStatus goToThePage()
      maritalStatus fillPageWith claim
      maritalStatus submitPage()

      val partnerPage = GYourPartnerPersonalDetailsPage(context)
      partnerPage goToThePage ()
      partnerPage fillPageWith ClaimScenarioFactory.s3YourPartnerNotThePersonYouCareFor()
      val theirPersonaDetailsPage = partnerPage submitPage()

      theirPersonaDetailsPage must beAnInstanceOf[GTheirPersonalDetailsPage]
    }

    "navigate to next page 'when have you lived with a partner is no' on valid submission" in new WithBrowser with PageObjects {
      val partnerPage = GYourPartnerPersonalDetailsPage(context)
      partnerPage goToThePage ()
      val claim = new TestData
      claim.AboutYourPartnerHadPartnerSinceClaimDate = "No"
      partnerPage fillPageWith claim

      val theirPersonaDetailsPage = partnerPage submitPage()

      theirPersonaDetailsPage must beAnInstanceOf[GTheirPersonalDetailsPage]
    }

    "navigate back to 'Payments from abroad and working abroad' page " in new WithBrowser with PageObjects {
      val paymentsAbroadPage = GPaymentsFromAbroadPage(context)
      paymentsAbroadPage goToThePage()
      paymentsAbroadPage fillPageWith ClaimScenarioFactory.otherEuropeanEconomicArea
      val partnerDetailsPage = paymentsAbroadPage submitPage()

      partnerDetailsPage must beAnInstanceOf[GYourPartnerPersonalDetailsPage]

      partnerDetailsPage goBack() must beAnInstanceOf[GPaymentsFromAbroadPage]
    }

    "navigate back to About your partner/spouse - Partner/Spouse details" in new WithBrowser with PageObjects {
      val partnerPage = GYourPartnerPersonalDetailsPage(context)
      partnerPage goToThePage ()
      partnerPage fillPageWith ClaimScenarioFactory.s3YourPartnerNotThePersonYouCareForWithBritishNationality()
      val theirPersonaDetailsPage = partnerPage submitPage()

      theirPersonaDetailsPage must beAnInstanceOf[GTheirPersonalDetailsPage]
      theirPersonaDetailsPage goBack() must beAnInstanceOf[GYourPartnerPersonalDetailsPage]
    }
  }
  section("integration", models.domain.YourPartner.id)
}
