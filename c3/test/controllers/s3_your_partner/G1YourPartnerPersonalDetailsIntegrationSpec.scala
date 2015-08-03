package controllers.s3_your_partner

import org.specs2.mutable.{Tags, Specification}
import utils.WithBrowser
import controllers.{PreviewTestUtils, ClaimScenarioFactory}
import utils.pageobjects.s4_care_you_provide.G1TheirPersonalDetailsPage
import utils.pageobjects._
import utils.pageobjects.s_claim_date.GClaimDatePage
import utils.pageobjects.s3_your_partner.G1YourPartnerPersonalDetailsPage
import utils.pageobjects.preview.PreviewPage
import utils.pageobjects.s_about_you.{GMaritalStatusPage, GOtherEEAStateOrSwitzerlandPage, GNationalityAndResidencyPage}
import app.MaritalStatus

class G1YourPartnerPersonalDetailsIntegrationSpec extends Specification with Tags {

  "Your Partner Personal Details" should {
    "be presented" in new WithBrowser with PageObjects {
      val page = G1YourPartnerPersonalDetailsPage(context)
      page goToThePage()
      page.url mustEqual G1YourPartnerPersonalDetailsPage.url
    }

    "contain error on invalid submission" in new WithBrowser with PageObjects {
      val page = G1YourPartnerPersonalDetailsPage(context)
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

      val partnerPage = G1YourPartnerPersonalDetailsPage(context)
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

      val partnerPage = G1YourPartnerPersonalDetailsPage(context)
      partnerPage goToThePage ()
      partnerPage fillPageWith ClaimScenarioFactory.s3YourPartnerNotThePersonYouCareFor()
      val theirPersonaDetailsPage = partnerPage submitPage()

      theirPersonaDetailsPage must beAnInstanceOf[G1TheirPersonalDetailsPage]

    }

    "navigate to next page 'when have you lived with a partner is no' on valid submission" in new WithBrowser with PageObjects {
      val partnerPage = G1YourPartnerPersonalDetailsPage(context)
      partnerPage goToThePage ()
      val claim = new TestData
      claim.AboutYourPartnerHadPartnerSinceClaimDate = "No"
      partnerPage fillPageWith claim

      val theirPersonaDetailsPage = partnerPage submitPage()

      theirPersonaDetailsPage must beAnInstanceOf[G1TheirPersonalDetailsPage]

    }

    "navigate back to 'Payments from abroad and working abroad' page " in new WithBrowser with PageObjects {
      val paymentsAbroadPage = GOtherEEAStateOrSwitzerlandPage(context)
      paymentsAbroadPage goToThePage()
      paymentsAbroadPage fillPageWith ClaimScenarioFactory.otherEuropeanEconomicArea
      val partnerDetailsPage = paymentsAbroadPage submitPage()

      partnerDetailsPage must beAnInstanceOf[G1YourPartnerPersonalDetailsPage]

      partnerDetailsPage goBack() must beAnInstanceOf[GOtherEEAStateOrSwitzerlandPage]
    }

    "navigate back to About your partner/spouse - Partner/Spouse details" in new WithBrowser with PageObjects {
      val partnerPage = G1YourPartnerPersonalDetailsPage(context)
      partnerPage goToThePage ()
      partnerPage fillPageWith ClaimScenarioFactory.s3YourPartnerNotThePersonYouCareForWithBritishNationality()
      val theirPersonaDetailsPage = partnerPage submitPage()

      theirPersonaDetailsPage must beAnInstanceOf[G1TheirPersonalDetailsPage]
      theirPersonaDetailsPage goBack() must beAnInstanceOf[G1YourPartnerPersonalDetailsPage]
    }

    "Modify 'had a partner since claim date' from preview page" in new WithBrowser with PageObjects{
      val modifiedData = new TestData
      modifiedData.AboutYourPartnerHadPartnerSinceClaimDate = "No"

      verifyPreviewData(context, "partner_hadPartner", "Yes", modifiedData, "No")
    }

    "Click on back button to take you to the preview page" in new WithBrowser with PageObjects{
       val id = "partner_hadPartner"
       val previewPage = goToPreviewPage(context)

       val partnerDetailsPage = previewPage.clickLinkOrButton(s"#$id")
       partnerDetailsPage must beAnInstanceOf[G1YourPartnerPersonalDetailsPage]
       partnerDetailsPage goBack() must beAnInstanceOf[PreviewPage]
    }

    "Modify 'partner name' from preview page" in new WithBrowser with PageObjects{
      val modifiedData = new TestData
      modifiedData.AboutYourPartnerTitle = "Mr"
      modifiedData.AboutYourPartnerSurname = "John"

      verifyPreviewData(context, "partner_name", "Mrs Cloe Scott Smith", modifiedData, "Mr Cloe Scott John")
    }

    "Modify 'partner nino' from preview page" in new WithBrowser with PageObjects{
      val modifiedData = new TestData
      modifiedData.AboutYourPartnerNINO = "AB123456D"

      verifyPreviewData(context, "partner_nino", "AB123456A", modifiedData, "AB123456D")
    }

    "Modify 'partner date of birth' from preview page" in new WithBrowser with PageObjects{
      val modifiedData = new TestData
      modifiedData.AboutYourPartnerDateofBirth = "12/07/2000"

      verifyPreviewData(context, "partner_dateOfBirth", "12 July, 1990", modifiedData, "12 July, 2000")
    }

    "Modify 'partner nationality' from preview page" in new WithBrowser with PageObjects{
      val modifiedData = new TestData
      modifiedData.AboutYourPartnerNationality = "French"

      verifyPreviewData(context, "partner_nationality", "British", modifiedData, "French")
    }

    "Modify 'partner seperated' from preview page" in new WithBrowser with PageObjects{
      val modifiedData = new TestData
      modifiedData.AboutYourPartnerHaveYouSeparatedfromYourPartner = "No"

      verifyPreviewData(context, "partner_seperated", "Yes", modifiedData, "No")
    }

    "Modify 'partner is the person you care for' from preview page" in new WithBrowser with PageObjects{
      val modifiedData = new TestData
      modifiedData.AboutYourPartnerIsYourPartnerThePersonYouAreClaimingCarersAllowancefor = "Yes"

      verifyPreviewData(context, "partner_isPersonCareFor", "No", modifiedData, "Yes")
    }

  } section("integration", models.domain.YourPartner.id)

  def goToPreviewPage(context:PageObjectsContext):Page = {
    val claimDatePage = GClaimDatePage(context)
    claimDatePage goToThePage()
    val claimDate = ClaimScenarioFactory.s12ClaimDate()
    claimDatePage fillPageWith claimDate
    claimDatePage submitPage()

    val maritalStatusPage = GMaritalStatusPage(context)
    maritalStatusPage goToThePage()
    val maritalData = new TestData
    maritalData.AboutYouWhatIsYourMaritalOrCivilPartnershipStatus = app.MaritalStatus.Married
    maritalStatusPage fillPageWith maritalData
    maritalStatusPage.submitPage()

    val nationalityPage =  GNationalityAndResidencyPage(context)
    val claim = ClaimScenarioFactory.yourNationalityAndResidencyNonResident

    nationalityPage goToThePage()
    nationalityPage fillPageWith claim
    nationalityPage submitPage()

    val partnerPage = G1YourPartnerPersonalDetailsPage(context)
    partnerPage goToThePage ()
    partnerPage fillPageWith ClaimScenarioFactory.s3YourPartnerNotThePersonYouCareFor()
    partnerPage submitPage()

    val previewPage = PreviewPage(context)
    previewPage goToThePage()
  }

  def verifyPreviewData(context:PageObjectsContext, id:String, initialData:String, modifiedTestData:TestData, modifiedData:String) = {
    val previewPage = goToPreviewPage(context)
    val answerText = PreviewTestUtils.answerText(id, _:Page)

    answerText(previewPage) mustEqual initialData
    val partnerPersonalDetailsPage = previewPage.clickLinkOrButton(s"#$id")

    partnerPersonalDetailsPage must beAnInstanceOf[G1YourPartnerPersonalDetailsPage]

    partnerPersonalDetailsPage fillPageWith modifiedTestData
    val previewPageModified = partnerPersonalDetailsPage submitPage()

    previewPageModified must beAnInstanceOf[PreviewPage]
    answerText(previewPageModified) mustEqual modifiedData
  }

}