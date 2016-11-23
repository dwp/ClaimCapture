package controllers.your_income

import controllers.ClaimScenarioFactory
import org.specs2.mutable._
import utils.pageobjects._
import utils.pageobjects.s_pay_details.GHowWePayYouPage
import utils.pageobjects.your_income.{GStatutorySickPayPage, GYourIncomePage}
import utils.{WithBrowser, WithJsBrowser}

class GYourIncomesIntegrationSpec extends Specification {
  val yes = "yes"
  val no = "no"

  section ("integration", models.domain.YourIncomes.id)
  "Your Incomes" should {
    "be presented" in new WithBrowser with PageObjects {
      val page = GYourIncomePage(context)
      page goToThePage ()
    }

    "navigate to next page on valid submission" in new WithJsBrowser with PageObjects {
      val page =  GYourIncomePage(context)
      page goToThePage ()

      val claim = new TestData
      claim.EmploymentHaveYouBeenEmployedAtAnyTime_0 = no
      claim.EmploymentHaveYouBeenSelfEmployedAtAnyTime = no
      claim.YourIncomeStatutorySickPay = "true"
      claim.YourIncomePatMatAdopPay = "true"
      claim.YourIncomeFosteringAllowance = "true"
      claim.YourIncomeDirectPay = "true"
      claim.YourIncomeAnyOtherPay = "true"

      page fillPageWith claim
      page submitPage() must beAnInstanceOf[GStatutorySickPayPage]
    }

    "present errors if mandatory fields are not populated" in new WithJsBrowser with PageObjects {
			val page =  GYourIncomePage(context)
      page goToThePage ()
      page.submitPage().listErrors.size mustEqual 2
    }

    "navigate to next page on valid submission" in new WithJsBrowser with PageObjects {
      val page = GYourIncomePage(context)
      val claim = new TestData
      claim.EmploymentHaveYouBeenEmployedAtAnyTime_0 = no
      claim.EmploymentHaveYouBeenSelfEmployedAtAnyTime = no
      claim.YourIncomeStatutorySickPay = "true"

      page goToThePage ()
      page fillPageWith claim

      val nextPage = page submitPage ()

      nextPage must beAnInstanceOf[GStatutorySickPayPage]
    }

    "have not filled in employment and self-employment" in new WithJsBrowser with PageObjects {
      val page = GYourIncomePage(context)
      val claim = new TestData
      claim.YourIncomeStatutorySickPay = "true"
      claim.YourIncomePatMatAdopPay = "true"
      claim.YourIncomeFosteringAllowance = "true"
      claim.YourIncomeDirectPay = "true"
      claim.YourIncomeAnyOtherPay = "true"
      claim.YourIncomeNone = "true"

      page goToThePage ()
      page fillPageWith claim

      val errors = page.submitPage().listErrors

      errors.size mustEqual 2
      errors(0) must contain("Have you been an employee")
    }

    "select all checkboxes should raise error" in new WithJsBrowser with PageObjects {
      val page = GYourIncomePage(context)
      val claim = new TestData
      claim.EmploymentHaveYouBeenEmployedAtAnyTime_0 = no
      claim.EmploymentHaveYouBeenSelfEmployedAtAnyTime = no
      claim.YourIncomeStatutorySickPay = "true"
      claim.YourIncomePatMatAdopPay = "true"
      claim.YourIncomeFosteringAllowance = "true"
      claim.YourIncomeDirectPay = "true"
      claim.YourIncomeAnyOtherPay = "true"
      claim.YourIncomeNone = "true"

      page goToThePage ()
      page fillPageWith claim

      val errors = page.submitPage().listErrors

      errors.size mustEqual 1
      errors(0) must contain("You must select either another income or ")
    }

    "no checkboxes selected" in new WithJsBrowser with PageObjects {
      val page = GYourIncomePage(context)
      val claim = new TestData
      claim.EmploymentHaveYouBeenEmployedAtAnyTime_0 = no
      claim.EmploymentHaveYouBeenSelfEmployedAtAnyTime = no

      page goToThePage ()
      page fillPageWith claim

      val submittedPage = page.submitPage()
      val errors = submittedPage.listErrors

      errors.size mustEqual 1
      errors(0) must contain("What other income have you had")

      val claimAfterError = new TestData
      claimAfterError.EmploymentHaveYouBeenEmployedAtAnyTime_0 = no
      claimAfterError.EmploymentHaveYouBeenSelfEmployedAtAnyTime = no
      claimAfterError.YourIncomeStatutorySickPay = "true"
      claimAfterError.YourIncomePatMatAdopPay = "true"
      claimAfterError.YourIncomeFosteringAllowance = "true"
      claimAfterError.YourIncomeDirectPay = "true"
      claimAfterError.YourIncomeAnyOtherPay = "true"

      submittedPage fillPageWith claimAfterError
      val differentPage = submittedPage submitPage()
      differentPage must beAnInstanceOf[GStatutorySickPayPage]
    }

    "data should be saved in claim and displayed when go back to page" in new WithJsBrowser with PageObjects {
      val page =  GYourIncomePage(context)
      val claim = new TestData
      claim.EmploymentHaveYouBeenEmployedAtAnyTime_0 = no
      claim.EmploymentHaveYouBeenSelfEmployedAtAnyTime = no
      claim.YourIncomeStatutorySickPay = "true"
      claim.YourIncomePatMatAdopPay = "true"
      claim.YourIncomeFosteringAllowance = "true"
      claim.YourIncomeDirectPay = "true"
      claim.YourIncomeAnyOtherPay = "true"

      page goToThePage ()
      page fillPageWith claim

      val nextPage = page submitPage ()
      nextPage must beAnInstanceOf[GStatutorySickPayPage]
      val YourIncomesPageAgain = nextPage.goBack()
      YourIncomesPageAgain.source must contain("no")
      YourIncomesPageAgain.source must contain("true")
    }
  }
  section ("integration", models.domain.YourIncomes.id)
}
