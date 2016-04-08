package controllers.your_income

import controllers.ClaimScenarioFactory
import org.specs2.mutable._
import utils.pageobjects._
import utils.pageobjects.s_pay_details.GHowWePayYouPage
import utils.pageobjects.your_income.{GYourIncomePage, GOtherPaymentsPage}
import utils.{WithBrowser, WithJsBrowser}

class GOtherPaymentsIntegrationSpec extends Specification {
  val otherPayments = "Testing 123"

  section ("integration", models.domain.OtherPayments.id)
  "Other Payments" should {
    "be presented" in new WithBrowser with PageObjects {
      GYourIncomePage.fillYourIncomes(context, testData => { testData.YourIncomeAnyOtherPay = "true" })
      val page = GOtherPaymentsPage(context)
      page goToThePage ()
      page must beAnInstanceOf[GOtherPaymentsPage]
    }

    "navigate to next page on valid submission" in new WithJsBrowser with PageObjects {
      GYourIncomePage.fillYourIncomes(context, testData => { testData.YourIncomeAnyOtherPay = "true" })
      val page =  GOtherPaymentsPage(context)
      page goToThePage ()

      val claim = new TestData
      claim.OtherPaymentsInfo = otherPayments
      page fillPageWith claim
      page submitPage() must beAnInstanceOf[GHowWePayYouPage]
    }

    "present errors if mandatory fields are not populated" in new WithJsBrowser with PageObjects {
      GYourIncomePage.fillYourIncomes(context, testData => { testData.YourIncomeAnyOtherPay = "true" })
			val page =  GOtherPaymentsPage(context)
      page goToThePage ()
      page.submitPage().listErrors.size mustEqual 1
    }

    "reject when other payments info spaces entered" in new WithJsBrowser with PageObjects {
      GYourIncomePage.fillYourIncomes(context, testData => { testData.YourIncomeAnyOtherPay = "true" })
      val page = GOtherPaymentsPage(context)
      val claim = new TestData
      claim.OtherPaymentsInfo = "  "
      page goToThePage ()
      page fillPageWith claim

      val errors = page.submitPage().listErrors

      errors.size mustEqual 1
      errors(0) must contain("What other income have you had since")
    }

    //if you use WithJsBrowser javascript kicks in to trunacte field length
    "reject when other payments info too many characters entered" in new WithBrowser with PageObjects {
      GYourIncomePage.fillYourIncomes(context, testData => { testData.YourIncomeAnyOtherPay = "true" })
      val page = GOtherPaymentsPage(context)
      val claim = new TestData
      claim.OtherPaymentsInfo = "testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing testing"
      page goToThePage ()
      page fillPageWith claim

      val errors = page.submitPage().listErrors

      errors.size mustEqual 1
      errors(0) must contain("What other income have you had since")
    }

    "other payments info has errors then enter text and navigate to next field" in new WithJsBrowser with PageObjects {
      GYourIncomePage.fillYourIncomes(context, testData => { testData.YourIncomeAnyOtherPay = "true" })
      val page =  GOtherPaymentsPage(context)
      page goToThePage ()
      val submittedPage = page.submitPage()
      submittedPage.listErrors.size mustEqual 1

      val claimAfterError = new TestData
      claimAfterError.OtherPaymentsInfo = otherPayments

      submittedPage fillPageWith claimAfterError
      val differentPage = submittedPage submitPage()
      differentPage must beAnInstanceOf[GHowWePayYouPage]
    }

    "data should be saved in claim and displayed when go back to page" in new WithJsBrowser with PageObjects {
      GYourIncomePage.fillYourIncomes(context, testData => { testData.YourIncomeAnyOtherPay = "true" })
      val page =  GOtherPaymentsPage(context)
      val claim = new TestData
      claim.OtherPaymentsInfo = otherPayments

      page goToThePage ()
      page fillPageWith claim

      val nextPage = page submitPage ()
      nextPage must beAnInstanceOf[GHowWePayYouPage]
      val otherPaymentsPageAgain = nextPage.goBack()
      otherPaymentsPageAgain.source must contain(otherPayments)
    }
  }
  section ("integration", models.domain.OtherPayments.id)
}
