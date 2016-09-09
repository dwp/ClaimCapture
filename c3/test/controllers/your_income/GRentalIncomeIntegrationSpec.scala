package controllers.your_income

import org.specs2.mutable._
import utils.pageobjects._
import utils.pageobjects.s_pay_details.GHowWePayYouPage
import utils.pageobjects.your_income.{GRentalIncomePage, GYourIncomePage}
import utils.{WithBrowser, WithJsBrowser}

class GRentalIncomeIntegrationSpec extends Specification {
  val rentalIncome = "Rental Testing 123"

  section ("integration", models.domain.OtherPayments.id)
  "Rental income screen" should {
    "be presented when selected" in new WithBrowser with PageObjects {
      GYourIncomePage.fillYourIncomes(context, testData => { testData.YourIncomeRentalIncome= "true" })
      val page = GRentalIncomePage(context)
      page goToThePage ()
      page must beAnInstanceOf[GRentalIncomePage]
    }

    "navigate to next page on valid submission" in new WithJsBrowser with PageObjects {
      GYourIncomePage.fillYourIncomes(context, testData => { testData.YourIncomeRentalIncome = "true" })
      val page =  GRentalIncomePage(context)
      page goToThePage ()

      val claim = new TestData
      claim.RentalIncomeInfo = rentalIncome
      page fillPageWith claim
      page submitPage() must beAnInstanceOf[GHowWePayYouPage]
    }

    "present errors if mandatory fields are not populated" in new WithJsBrowser with PageObjects {
      GYourIncomePage.fillYourIncomes(context, testData => { testData.YourIncomeRentalIncome = "true" })
			val page =  GRentalIncomePage(context)
      page goToThePage ()
      page.submitPage().listErrors.size mustEqual 1
    }

    "reject when other payments info spaces entered" in new WithJsBrowser with PageObjects {
      GYourIncomePage.fillYourIncomes(context, testData => { testData.YourIncomeRentalIncome = "true" })
      val page = GRentalIncomePage(context)
      val claim = new TestData
      claim.RentalIncomeInfo = "  "
      page goToThePage ()
      page fillPageWith claim

      val errors = page.submitPage().listErrors

      errors.size mustEqual 1
      errors(0) must contain("What rental income have you had since")
    }

    //if you use WithJsBrowser javascript kicks in to truncate field length
    "reject when other payments info too many characters entered" in new WithBrowser with PageObjects {
      GYourIncomePage.fillYourIncomes(context, testData => { testData.YourIncomeRentalIncome = "true" })
      val page = GRentalIncomePage(context)
      val claim = new TestData
      claim.RentalIncomeInfo = "testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing"
      page goToThePage ()
      page fillPageWith claim

      val errors = page.submitPage().listErrors

      errors.size mustEqual 1
      errors(0) must contain("What rental income have you had since")
    }

    "other payments info has errors then enter text and navigate to next field" in new WithJsBrowser with PageObjects {
      GYourIncomePage.fillYourIncomes(context, testData => { testData.YourIncomeRentalIncome = "true" })
      val page =  GRentalIncomePage(context)
      page goToThePage ()
      val submittedPage = page.submitPage()
      submittedPage.listErrors.size mustEqual 1

      val claimAfterError = new TestData
      claimAfterError.RentalIncomeInfo = rentalIncome

      submittedPage fillPageWith claimAfterError
      val differentPage = submittedPage submitPage()
      differentPage must beAnInstanceOf[GHowWePayYouPage]
    }

    "data should be saved in claim and displayed when go back to page" in new WithJsBrowser with PageObjects {
      GYourIncomePage.fillYourIncomes(context, testData => { testData.YourIncomeRentalIncome = "true" })
      val page =  GRentalIncomePage(context)
      val claim = new TestData
      claim.RentalIncomeInfo = rentalIncome

      page goToThePage ()
      page fillPageWith claim

      val nextPage = page submitPage ()
      nextPage must beAnInstanceOf[GHowWePayYouPage]
      val otherPaymentsPageAgain = nextPage.goBack()
      otherPaymentsPageAgain.source must contain(rentalIncome)
    }
  }
  section ("integration", models.domain.OtherPayments.id)
}
