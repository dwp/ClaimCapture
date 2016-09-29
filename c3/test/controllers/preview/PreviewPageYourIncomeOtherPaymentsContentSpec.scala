package controllers.preview

import org.specs2.mutable._
import utils.WithBrowser
import utils.pageobjects.your_income.{GOtherPaymentsPage, GYourIncomePage, GStatutorySickPayPage}
import utils.pageobjects.{PageObjectsContext, PageObjects}
import utils.pageobjects.preview.PreviewPage
import utils.pageobjects.s_claim_date.GClaimDatePage
import controllers.ClaimScenarioFactory

class PreviewPageYourIncomeOtherPaymentsContentSpec extends Specification {
  section("preview")
  "Preview Page" should {
    "display your income other payments data" in new WithBrowser with PageObjects {
      GYourIncomePage.fillYourIncomes(context, testData => { testData.YourIncomeStatutorySickPay = "true" })
      fillStatutorySickPaySection(context)
      val page =  PreviewPage(context)
      page goToThePage()
      val source = page.source.toLowerCase
      source must contain("what other income have you had since")
      source must contain("statutory sick pay")
      source must contain("the man")
      source must contain("12 monthly")
      source must contain("still in payment")
    }

    "navigate back to income opening page from preview with correct return-to-check-your-answers button label" in new WithBrowser with PageObjects {
      GYourIncomePage.fillOtherIncome(context, testData => {})
      val previewPage =  PreviewPage(context)
      previewPage goToThePage()
      val incomePage=previewPage.clickLinkOrButton("#your_income_link")
      incomePage must beAnInstanceOf[GYourIncomePage]
      val buttonText=browser.find("#ReturnToCheckYourAnswers").getText
      buttonText mustEqual("Return to check your answers")
    }

    "navigate back to other income page from preview with correct return-to-check-your-answers button label" in new WithBrowser with PageObjects {
      GYourIncomePage.fillOtherIncome(context, testData => {})
      val previewPage =  PreviewPage(context)
      previewPage goToThePage()
      val otherIncomePage=previewPage.clickLinkOrButton("#your_income_other_payments_link")
      otherIncomePage must beAnInstanceOf[GOtherPaymentsPage]
      val buttonText=browser.find("#ReturnToCheckYourAnswers").getText
      buttonText mustEqual("Return to check your answers")
    }
  }
  section("preview")

  def fillStatutorySickPaySection (context:PageObjectsContext) = {
    val claimDatePage = GClaimDatePage(context)
    claimDatePage goToThePage()
    val claimDate = ClaimScenarioFactory.s12ClaimDate()
    claimDatePage fillPageWith claimDate
    claimDatePage submitPage()

    val statutorySickPayPage = GStatutorySickPayPage(context)
    statutorySickPayPage goToThePage ()
    statutorySickPayPage fillPageWith ClaimScenarioFactory.s9OtherIncome
    statutorySickPayPage submitPage()
  }
}
