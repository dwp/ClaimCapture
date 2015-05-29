package controllers.s2_about_you

import org.specs2.mutable.{Tags, Specification}
import utils.WithBrowser
import controllers.{PreviewTestUtils, ClaimScenarioFactory}
import utils.pageobjects.s2_about_you.{G4NationalityAndResidencyPage, G7OtherEEAStateOrSwitzerlandPage}
import utils.pageobjects._
import utils.pageobjects.s3_your_partner.G1YourPartnerPersonalDetailsPage
import utils.pageobjects.s1_2_claim_date.G1ClaimDatePage
import utils.pageobjects.preview.PreviewPage

class G7OtherEEAStateOrSwitzerlandIntegrationSpec extends Specification with Tags {
  sequential

  val urlUnderTest = "/about-you/other-eea-state-or-switzerland"
  val submitButton = "button[type='submit']"
  val errorDiv = "div[class=validation-summary] ol li"

  "Other European Economic Area (EEA) states or Switzerland" should {
    "be presented" in new WithBrowser with PageObjects{
			val page =  G7OtherEEAStateOrSwitzerlandPage(context)
      page goToThePage()
    }

    "contain errors on invalid submission" in new WithBrowser with PageObjects{
			val page =  G7OtherEEAStateOrSwitzerlandPage(context)
      page goToThePage()
      val nextPage = page submitPage()

      nextPage must beAnInstanceOf[G7OtherEEAStateOrSwitzerlandPage]
    }

    "navigate to next page on valid resident submission" in new WithBrowser with PageObjects{
			val page =  G7OtherEEAStateOrSwitzerlandPage(context)
      val claim = ClaimScenarioFactory.otherEuropeanEconomicArea
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage()

      nextPage must beAnInstanceOf[G1YourPartnerPersonalDetailsPage]
    }

    "navigate to next page on valid non resident submission" in new WithBrowser with PageObjects{
			val page =  G7OtherEEAStateOrSwitzerlandPage(context)
      val claim = ClaimScenarioFactory.otherEuropeanEconomicArea
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage()

      nextPage must beAnInstanceOf[G1YourPartnerPersonalDetailsPage]
    }

    "Modify benefits from EEA from preview page" in new WithBrowser with PageObjects{
      val previewPage = goToPreviewPage(context)
      val id = "about_you_benefitsFromEEA"
      val answerText = PreviewTestUtils.answerText(id, _:Page)

      answerText(previewPage) mustEqual "Yes"
      val otherBenefitsPage = previewPage.clickLinkOrButton(s"#$id")

      otherBenefitsPage must beAnInstanceOf[G7OtherEEAStateOrSwitzerlandPage]
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

      answerText(previewPage) mustEqual "Yes"
      val otherBenefitsPage = previewPage.clickLinkOrButton(s"#$id")

      otherBenefitsPage must beAnInstanceOf[G7OtherEEAStateOrSwitzerlandPage]
      val modifiedData = new TestData
      modifiedData.OtherMoneyOtherAreYouPayingInsuranceToAnotherEEA = "No"

      otherBenefitsPage fillPageWith modifiedData
      val previewPageModified = otherBenefitsPage submitPage()

      previewPageModified must beAnInstanceOf[PreviewPage]
      answerText(previewPageModified) mustEqual "No"
    }

  } section("integration", models.domain.AboutYou.id)

  def goToPreviewPage(context:PageObjectsContext):Page = {
    val claimDatePage = G1ClaimDatePage(context)
    claimDatePage goToThePage()
    val claimDate = ClaimScenarioFactory.s12ClaimDate()
    claimDatePage fillPageWith claimDate
    claimDatePage submitPage()

    val otherBenefitsPage = G7OtherEEAStateOrSwitzerlandPage(context)
    val claim = ClaimScenarioFactory.otherEuropeanEconomicArea
    otherBenefitsPage goToThePage()
    otherBenefitsPage fillPageWith claim
    otherBenefitsPage submitPage()

    val previewPage = PreviewPage(context)
    previewPage goToThePage()
  }

}
