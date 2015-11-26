package controllers.s_about_you

import org.specs2.mutable._
import utils.{WithJsBrowser, WithBrowser}
import controllers.{PreviewTestUtils, ClaimScenarioFactory}
import utils.pageobjects.s_about_you.GOtherEEAStateOrSwitzerlandPage
import utils.pageobjects._
import utils.pageobjects.s_your_partner.GYourPartnerPersonalDetailsPage
import utils.pageobjects.s_claim_date.GClaimDatePage
import utils.pageobjects.preview.PreviewPage
import utils.helpers.PreviewField._

class GOtherEEAStateOrSwitzerlandIntegrationSpec extends Specification {
  sequential

  val urlUnderTest = "/about-you/other-eea-state-or-switzerland"
  val submitButton = "button[type='submit']"
  val errorDiv = "div[class=validation-summary] ol li"

  "Other European Economic Area (EEA) states or Switzerland" should {
    "be presented" in new WithBrowser with PageObjects{
			val page =  GOtherEEAStateOrSwitzerlandPage(context)
      page goToThePage()
    }

    "contain errors on invalid submission" in new WithBrowser with PageObjects{
			val page =  GOtherEEAStateOrSwitzerlandPage(context)
      page goToThePage()
      val nextPage = page submitPage()

      nextPage must beAnInstanceOf[GOtherEEAStateOrSwitzerlandPage]
    }

    "navigate to next page on valid resident submission" in new WithBrowser with PageObjects{
			val page =  GOtherEEAStateOrSwitzerlandPage(context)
      val claim = ClaimScenarioFactory.otherEuropeanEconomicArea
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage()

      nextPage must beAnInstanceOf[GYourPartnerPersonalDetailsPage]
    }

    "navigate to next page on valid non resident submission" in new WithBrowser with PageObjects{
			val page =  GOtherEEAStateOrSwitzerlandPage(context)
      val claim = ClaimScenarioFactory.otherEuropeanEconomicArea
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage()

      nextPage must beAnInstanceOf[GYourPartnerPersonalDetailsPage]
    }

    "Modify benefits from EEA from preview page" in new WithBrowser with PageObjects{
      val previewPage = goToPreviewPage(context)
      val id = "about_you_benefitsFromEEA"
      val answerText = PreviewTestUtils.answerText(id, _:Page)

      answerText(previewPage) mustEqual "Yes - Details provided"
      val otherBenefitsPage = previewPage.clickLinkOrButton(getLinkId(id))

      otherBenefitsPage must beAnInstanceOf[GOtherEEAStateOrSwitzerlandPage]
      val modifiedData = new TestData
      modifiedData.OtherMoneyOtherAreYouReceivingPensionFromAnotherEEA = "No"

      otherBenefitsPage fillPageWith modifiedData
      val previewPageModified = otherBenefitsPage submitPage()

      previewPageModified must beAnInstanceOf[PreviewPage]
      answerText(previewPageModified) mustEqual "No"
    }

    "Modify working for EEA from preview page" in new WithBrowser with PageObjects{
      val previewPage = goToPreviewPage(context)
      val id = "about_you_workingForEEA"
      val answerText = PreviewTestUtils.answerText(id, _:Page)

      answerText(previewPage) mustEqual "Yes - Details provided"
      val otherBenefitsPage = previewPage.clickLinkOrButton(getLinkId(id))

      otherBenefitsPage must beAnInstanceOf[GOtherEEAStateOrSwitzerlandPage]
      val modifiedData = new TestData
      modifiedData.OtherMoneyOtherAreYouPayingInsuranceToAnotherEEA = "No"

      otherBenefitsPage fillPageWith modifiedData
      val previewPageModified = otherBenefitsPage submitPage()

      previewPageModified must beAnInstanceOf[PreviewPage]
      answerText(previewPageModified) mustEqual "No"
    }


    "Working For EEA Details must not be visible when time abroad page is displayed" in new WithJsBrowser  with PageObjects{
      val page =  GOtherEEAStateOrSwitzerlandPage(context)
      page goToThePage()
      page.ctx.browser.findFirst("#eeaGuardQuestion_benefitsFromEEADetails_answer_yes").isDisplayed should beFalse
      page.ctx.browser.findFirst("#eeaGuardQuestion_benefitsFromEEADetails_answer_no").isDisplayed should beFalse
      page.ctx.browser.findFirst("#eeaGuardQuestion_benefitsFromEEADetails_field").isDisplayed should beFalse
    }

    "Benefits from EEA Details must not be visible when time abroad page is displayed" in new WithJsBrowser  with PageObjects{
      val page =  GOtherEEAStateOrSwitzerlandPage(context)
      page goToThePage()
      page.ctx.browser.findFirst("#eeaGuardQuestion_workingForEEADetails_answer_yes").isDisplayed should beFalse
      page.ctx.browser.findFirst("#eeaGuardQuestion_workingForEEADetails_answer_no").isDisplayed should beFalse
      page.ctx.browser.findFirst("#eeaGuardQuestion_workingForEEADetails_field").isDisplayed should beFalse
    }

    "Working For EEA Details must be visible when returning back to the time abroad page" in new WithJsBrowser  with PageObjects{
      val page =  GOtherEEAStateOrSwitzerlandPage(context)

      val claim = ClaimScenarioFactory otherEuropeanEconomicArea()

      page goToThePage()

      page fillPageWith claim
      val nextPage = page submitPage()
      nextPage must beAnInstanceOf[GYourPartnerPersonalDetailsPage]

      val backPage = nextPage goBack()
      backPage must beAnInstanceOf[GOtherEEAStateOrSwitzerlandPage]

      backPage.ctx.browser.findFirst("#eeaGuardQuestion_benefitsFromEEADetails_answer_yes").isSelected should beTrue
      backPage.ctx.browser.findFirst("#eeaGuardQuestion_benefitsFromEEADetails_answer_no").isSelected should beFalse
      backPage.ctx.browser.findFirst("#eeaGuardQuestion_benefitsFromEEADetails_field").isDisplayed should beTrue
    }

    "Benefits from EEA Details must be visible when returning back to the time abroad page" in new WithJsBrowser  with PageObjects{
      val page =  GOtherEEAStateOrSwitzerlandPage(context)

      val claim = ClaimScenarioFactory otherEuropeanEconomicArea()

      page goToThePage()

      page fillPageWith claim
      val nextPage = page submitPage()
      nextPage must beAnInstanceOf[GYourPartnerPersonalDetailsPage]

      val backPage = nextPage goBack()
      backPage must beAnInstanceOf[GOtherEEAStateOrSwitzerlandPage]

      backPage.ctx.browser.findFirst("#eeaGuardQuestion_workingForEEADetails_answer_yes").isSelected should beTrue
      backPage.ctx.browser.findFirst("#eeaGuardQuestion_workingForEEADetails_answer_no").isSelected should beFalse
      backPage.ctx.browser.findFirst("#eeaGuardQuestion_workingForEEADetails_field").isDisplayed should beTrue
    }

  }
  section("integration", models.domain.AboutYou.id)

  def goToPreviewPage(context:PageObjectsContext):Page = {
    val claimDatePage = GClaimDatePage(context)
    claimDatePage goToThePage()
    val claimDate = ClaimScenarioFactory.s12ClaimDate()
    claimDatePage fillPageWith claimDate
    claimDatePage submitPage()

    val otherBenefitsPage = GOtherEEAStateOrSwitzerlandPage(context)
    val claim = ClaimScenarioFactory.otherEuropeanEconomicArea
    otherBenefitsPage goToThePage()
    otherBenefitsPage fillPageWith claim
    otherBenefitsPage submitPage()

    val previewPage = PreviewPage(context)
    previewPage goToThePage()
  }

}
