package controllers.s9_other_money

import org.specs2.mutable.{ Tags, Specification }
import controllers.{PreviewTestUtils, BrowserMatchers, Formulate, ClaimScenarioFactory}
import utils.WithBrowser
import utils.pageobjects.s9_other_money._
import utils.pageobjects._
import utils.pageobjects.s11_pay_details.G1HowWePayYouPage
import utils.pageobjects.s_claim_date.GClaimDatePage
import utils.pageobjects.s2_about_you.G7OtherEEAStateOrSwitzerlandPage
import utils.pageobjects.preview.PreviewPage

class G1AboutOtherMoneyIntegrationSpec extends Specification with Tags {
  "Other Money" should {
    "be presented" in new WithBrowser with PageObjects {
      val page = G1AboutOtherMoneyPage(context)
      page goToThePage ()
    }

    "navigate to next page on valid submission with the three mandatory fields set to no" in new WithBrowser with PageObjects {
      val page =  G1AboutOtherMoneyPage(context)
      page goToThePage ()
      val claim = new TestData
      claim.OtherMoneyAnyPaymentsSinceClaimDate = "no"
      claim.OtherMoneyHaveYouSSPSinceClaim = "no"
      claim.OtherMoneyHaveYouSMPSinceClaim = "no"
      page fillPageWith claim
      page submitPage() must beAnInstanceOf[G1HowWePayYouPage]
    }

    "present errors if mandatory fields are not populated" in new WithBrowser with PageObjects{
			val page =  G1AboutOtherMoneyPage(context)
      page goToThePage ()
      page.submitPage().listErrors.size mustEqual 3
    }

    "accept submit if all mandatory fields are populated" in new WithBrowser with PageObjects{
			val page =  G1AboutOtherMoneyPage(context)
      val claim = ClaimScenarioFactory.s9otherMoneyOther
      page goToThePage ()
      page fillPageWith claim

      val nextPage = page submitPage ()

      nextPage must beAnInstanceOf[G1HowWePayYouPage]
    }

    "navigate to next page on valid submission with other field selected" in new WithBrowser with PageObjects {
      val page = G1AboutOtherMoneyPage(context)
      val claim = ClaimScenarioFactory.s9otherMoneyOther

      page goToThePage ()
      page fillPageWith claim

      val nextPage = page submitPage ()

      nextPage must beAnInstanceOf[G1HowWePayYouPage]
    }

    "howOften frequency of other with no other text entered" in new WithBrowser with PageObjects {
      val page = G1AboutOtherMoneyPage(context)
      val claim = new TestData
      claim.OtherMoneyAnyPaymentsSinceClaimDate = "yes"
      claim.OtherMoneyWhoPaysYou = "The Man"
      claim.OtherMoneyHowMuch = "34"
      claim.OtherMoneyHowOften = "other"
      page goToThePage ()
      page fillPageWith claim

      val errors = page.submitPage().listErrors

      errors.size mustEqual 3
      errors(0) must contain("How often?")
    }


    "Modify any payments since claim date from preview page" in new WithBrowser with PageObjects{
      val previewPage = goToPreviewPage(context)
      val id = "other_money_anyPaymentsSinceClaimDate"
      val answerText = PreviewTestUtils.answerText(id, _:Page)

      answerText(previewPage) mustEqual "Yes - Details provided"
      val otherMoneyPage = previewPage.clickLinkOrButton(s"#$id")

      otherMoneyPage must beAnInstanceOf[G1AboutOtherMoneyPage]
      val modifiedData = new TestData
      modifiedData.OtherMoneyAnyPaymentsSinceClaimDate = "No"

      otherMoneyPage fillPageWith modifiedData
      val previewPageModified = otherMoneyPage submitPage()

      previewPageModified must beAnInstanceOf[PreviewPage]
      answerText(previewPageModified) mustEqual "No"
    }

    "Modify statutory pay from preview page" in new WithBrowser with PageObjects{
      val previewPage = goToPreviewPage(context)
      val id = "other_money_statutoryPay"
      val answerText = PreviewTestUtils.answerText(id, _:Page)

      answerText(previewPage) mustEqual "Yes - Details provided"
      val otherMoneyPage = previewPage.clickLinkOrButton(s"#$id")

      otherMoneyPage must beAnInstanceOf[G1AboutOtherMoneyPage]
      val modifiedData = new TestData
      modifiedData.OtherMoneyHaveYouSSPSinceClaim = "No"

      otherMoneyPage fillPageWith modifiedData
      val previewPageModified = otherMoneyPage submitPage()

      previewPageModified must beAnInstanceOf[PreviewPage]
      answerText(previewPageModified) mustEqual "No"
    }

    "Modify other statutory pay from preview page" in new WithBrowser with PageObjects{
      val previewPage = goToPreviewPage(context)
      val id = "other_money_otherStatutoryPay"
      val answerText = PreviewTestUtils.answerText(id, _:Page)

      answerText(previewPage) mustEqual "Yes - Details provided"
      val otherMoneyPage = previewPage.clickLinkOrButton(s"#$id")

      otherMoneyPage must beAnInstanceOf[G1AboutOtherMoneyPage]
      val modifiedData = new TestData
      modifiedData.OtherMoneyHaveYouSMPSinceClaim = "No"

      otherMoneyPage fillPageWith modifiedData
      val previewPageModified = otherMoneyPage submitPage()

      previewPageModified must beAnInstanceOf[PreviewPage]
      answerText(previewPageModified) mustEqual "No"
    }

  } section ("integration", models.domain.OtherMoney.id)

  def goToPreviewPage(context:PageObjectsContext):Page = {
    val claimDatePage = GClaimDatePage(context)
    claimDatePage goToThePage()
    val claimDate = ClaimScenarioFactory.s12ClaimDate()
    claimDatePage fillPageWith claimDate
    claimDatePage submitPage()

    val otherMoneyPage = G1AboutOtherMoneyPage(context)
    val claim = ClaimScenarioFactory.s9otherMoney
    otherMoneyPage goToThePage()
    otherMoneyPage fillPageWith claim
    otherMoneyPage submitPage()

    val previewPage = PreviewPage(context)
    previewPage goToThePage()
  }

}