package controllers.preview

import org.specs2.mutable._
import utils.{WithJsBrowser, WithBrowser}
import utils.pageobjects.preview.PreviewPage
import utils.pageobjects.your_income.{GRentalIncomePage, GYourIncomePage}
import utils.pageobjects.{TestData, PageObjects}

class PreviewPageYourIncomeRentalIncomeSpec extends Specification {
  section("preview")
  "Preview Page" should {
    "display your income rental income data" in new WithBrowser with PageObjects {
      GYourIncomePage.fillYourIncomes(context, testData => {
        testData.YourIncomeRentalIncome = "true"
      })
      val claim = new TestData
      claim.RentalIncomeInfo = "Some text about rental income"

      val previewPage = PreviewPage(context)
      previewPage goToThePage()
      val rentaltext = browser.find("#your_income_rental_income_value").getText()
      rentaltext mustEqual ("Rental income")
    }

    "navigate back-and-from income opening page from preview with correct return-to-check-your-answers button label" in new WithJsBrowser with PageObjects {
      GYourIncomePage.fillRentalIncome(context, testData => {})
      val previewPage = PreviewPage(context)
      previewPage goToThePage()
      val rentalIncomePage = previewPage.clickLinkOrButton("#your_income_link")
      rentalIncomePage must beAnInstanceOf[GYourIncomePage]
      val buttonText = browser.find("#ReturnToCheckYourAnswers").getText
      buttonText mustEqual ("Return to check your answers")
    }

    "navigate back-and-from rental page from preview with correct return-to-check-your-answers button label" in new WithBrowser with PageObjects {
      GYourIncomePage.fillRentalIncome(context, testData => {})
      val previewPage = PreviewPage(context)
      previewPage goToThePage()
      val rentalIncomePage = previewPage.clickLinkOrButton("#your_income_rental_income_link")
      rentalIncomePage must beAnInstanceOf[GRentalIncomePage]
      val buttonText = browser.find("#ReturnToCheckYourAnswers").getText
      buttonText mustEqual ("Return to check your answers")
    }
  }
  section("preview")
}
