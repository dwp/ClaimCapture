package controllers.s3_your_partner

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{PreviewTestUtils, ClaimScenarioFactory, BrowserMatchers, Formulate}
import utils.pageobjects.s4_care_you_provide.G1TheirPersonalDetailsPage
import utils.pageobjects._
import utils.pageobjects.s1_2_claim_date.G1ClaimDatePage
import utils.pageobjects.s3_your_partner.G1YourPartnerPersonalDetailsPage
import utils.pageobjects.preview.PreviewPage
import utils.pageobjects.s2_about_you.G4NationalityAndResidencyPage

class G1YourPartnerPersonalDetailsIntegrationSpec extends Specification with Tags {

  "Your Partner Personal Details" should {
    "be presented if carer has partner" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)
      Formulate.nationalityAndResidency(browser)
      Formulate.otherEEAStateOrSwitzerland(browser)
      titleMustEqual("Partner details - About your partner")

      browser.goTo("/your-partner/personal-details")
      titleMustEqual("Partner details - About your partner")
    }

    "contain errors on invalid submission" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)
      Formulate.nationalityAndResidency(browser)
      Formulate.otherEEAStateOrSwitzerland(browser)
      titleMustEqual("Partner details - About your partner")

      browser.goTo("/your-partner/personal-details")
      titleMustEqual("Partner details - About your partner")
      browser.submit("button[type='submit']")

      browser.find("div[class=validation-summary] ol li").size mustEqual 6
    }

    "navigate to next page on valid submission" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)
      Formulate.nationalityAndResidency(browser)
      Formulate.otherEEAStateOrSwitzerland(browser)
      titleMustEqual("Partner details - About your partner")

      Formulate.yourPartnerPersonalDetails(browser)
      titleMustEqual(G1TheirPersonalDetailsPage.title)
    }

    "navigate back to Employment - About you - the carer" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)
      Formulate.nationalityAndResidency(browser)
      Formulate.otherEEAStateOrSwitzerland(browser)

      titleMustEqual("Partner details - About your partner")

      browser.goTo("/your-partner/personal-details")
      titleMustEqual("Partner details - About your partner")

      browser.click("#backButton")
      titleMustEqual("Payments from abroad and working abroad - About you - the carer")
    }

    "navigate back to About your partner/spouse - Partner/Spouse details" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)
      Formulate.nationalityAndResidency(browser)
      Formulate.otherEEAStateOrSwitzerland(browser)
      titleMustEqual("Partner details - About your partner")

      Formulate.yourPartnerPersonalDetails(browser)
      titleMustEqual("Details of the person you care for - About the care you provide")
      Formulate.clickBackButton(browser)
      titleMustEqual("Partner details - About your partner")
    }

    "nationality should not be visible when the carer is British" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)
      Formulate.nationalityAndResidency(browser)
      browser.goTo("/your-partner/personal-details")
      browser.click("#hadPartnerSinceClaimDate_yes")
      browser.find("#nationality").size shouldEqual 0
    }

    "nationality should be visible when the carer is not british and married" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)
      Formulate.nationalityAndResidencyNotBritishMarried(browser)
      browser.goTo("/your-partner/personal-details")
      browser.click("#hadPartnerSinceClaimDate_yes")
      browser.find("#nationality").size shouldEqual 1
    }

    "nationality should be visible when the carer is not british and Living with partner" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)
      Formulate.nationalityAndResidencyNotBritishWithPartner(browser)
      browser.goTo("/your-partner/personal-details")
      browser.click("#hadPartnerSinceClaimDate_yes")
      browser.find("#nationality").size shouldEqual 1
    }

    "nationality should not be visible when the carer is not british and neither married or living with partner" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)
      Formulate.nationalityAndResidencyNotBritishSingle(browser)
      browser.goTo("/your-partner/personal-details")
      browser.click("#hadPartnerSinceClaimDate_yes")
      browser.find("#nationality").size shouldEqual 0
    }

    "Modify 'had a partner since claim date' from preview page" in new WithBrowser with PageObjects{
      val modifiedData = new TestData
      modifiedData.AboutYourPartnerHadPartnerSinceClaimDate = "No"

      verifyPreviewData(context, "partner_hadPartner", "Yes", modifiedData, "No")
    }

    "Click on back button to take you to the preview page" in new WithBrowser with PageObjects{
       val id = "partner_hadPartner"
       val previewPage = goToPreviewPage(context)

       val partnerDetailsPage = ClaimPageFactory.buildPageFromFluent(previewPage.click(s"#$id"))
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

      verifyPreviewData(context, "partner_nino", "AB 12 34 56 A", modifiedData, "AB 12 34 56 D")
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
    val claimDatePage = G1ClaimDatePage(context)
    claimDatePage goToThePage()
    val claimDate = ClaimScenarioFactory.s12ClaimDate()
    claimDatePage fillPageWith claimDate
    claimDatePage submitPage()

    val nationalityPage =  G4NationalityAndResidencyPage(context)
    val claim = ClaimScenarioFactory.yourNationalityAndResidencyNonResident
    claim.AboutYouWhatIsYourMaritalOrCivilPartnershipStatus = app.MaritalStatus.Married
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
    val partnerPersonalDetailsPage = ClaimPageFactory.buildPageFromFluent(previewPage.click(s"#$id"))

    partnerPersonalDetailsPage must beAnInstanceOf[G1YourPartnerPersonalDetailsPage]

    partnerPersonalDetailsPage fillPageWith modifiedTestData
    val previewPageModified = partnerPersonalDetailsPage submitPage()

    previewPageModified must beAnInstanceOf[PreviewPage]
    answerText(previewPageModified) mustEqual modifiedData
  }

}