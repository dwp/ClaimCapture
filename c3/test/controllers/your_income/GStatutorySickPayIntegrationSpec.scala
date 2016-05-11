package controllers.your_income

import controllers.ClaimScenarioFactory
import org.specs2.mutable._
import utils.pageobjects._
import utils.pageobjects.s_pay_details.GHowWePayYouPage
import utils.pageobjects.your_income.{GYourIncomePage, GStatutoryMaternityPaternityAdoptionPayPage, GStatutorySickPayPage}
import utils.{WithBrowser, WithJsBrowser}

class GStatutorySickPayIntegrationSpec extends Specification {
  val whoPaysYou = "The Man"
  val howMuch = "12"
  val yes = "yes"
  val no = "no"
  val monthlyFrequency = "Monthly"

  section ("integration", models.domain.StatutorySickPay.id)
  "Statutory Sick Pay" should {
    "be presented" in new WithBrowser with PageObjects {
      GYourIncomePage.fillYourIncomes(context, testData => { testData.YourIncomeStatutorySickPay = "true" })
      val page = GStatutorySickPayPage(context)
      page goToThePage ()
      page must beAnInstanceOf[GStatutorySickPayPage]
    }

    "navigate to next page on valid submission" in new WithJsBrowser with PageObjects {
      GYourIncomePage.fillYourIncomes(context, testData => { testData.YourIncomeStatutorySickPay = "true"; testData.YourIncomePatMatAdopPay = "true" })
      val page =  GStatutorySickPayPage(context)
      page goToThePage ()

      val claim = new TestData
      claim.StillBeingPaidThisPay = yes
      claim.WhoPaidYouThisPay = whoPaysYou
      claim.AmountOfThisPay = howMuch
      claim.HowOftenPaidThisPay = monthlyFrequency
      page fillPageWith claim
      page submitPage() must beAnInstanceOf[GStatutoryMaternityPaternityAdoptionPayPage]
    }

    "present errors if mandatory fields are not populated" in new WithJsBrowser with PageObjects {
      GYourIncomePage.fillYourIncomes(context, testData => { testData.YourIncomeStatutorySickPay = "true"; testData.YourIncomePatMatAdopPay = "true" })
			val page =  GStatutorySickPayPage(context)
      page goToThePage ()
      page.submitPage().listErrors.size mustEqual 4
    }

    "navigate to next page on valid submission with other field selected" in new WithJsBrowser with PageObjects {
      GYourIncomePage.fillYourIncomes(context, testData => { testData.YourIncomeStatutorySickPay = "true"; testData.YourIncomePatMatAdopPay = "true" })
      val page = GStatutorySickPayPage(context)
      val claim = ClaimScenarioFactory.s9OtherIncomeOther

      page goToThePage ()
      page fillPageWith claim

      val nextPage = page submitPage ()

      nextPage must beAnInstanceOf[GStatutoryMaternityPaternityAdoptionPayPage]
    }

    "howOften frequency of other with no other text entered" in new WithJsBrowser with PageObjects {
      GYourIncomePage.fillYourIncomes(context, testData => { testData.YourIncomeStatutorySickPay = "true"; testData.YourIncomePatMatAdopPay = "true" })
      val page = GStatutorySickPayPage(context)
      val claim = new TestData
      claim.StillBeingPaidThisPay = no
      claim.WhoPaidYouThisPay = whoPaysYou
      claim.AmountOfThisPay = howMuch
      claim.HowOftenPaidThisPay = "Other"
      page goToThePage ()
      page fillPageWith claim

      val errors = page.submitPage().listErrors

      errors.size mustEqual 2
      errors(0) must contain("How often")
    }

    "howOften frequency of other with no other text entered then select yes and be able to move to next page" in new WithJsBrowser with PageObjects {
      GYourIncomePage.fillYourIncomes(context, testData => { testData.YourIncomeStatutorySickPay = "true"; testData.YourIncomePatMatAdopPay = "true" })
      val page = GStatutorySickPayPage(context)
      val claim = new TestData
      claim.StillBeingPaidThisPay = no
      claim.WhoPaidYouThisPay = whoPaysYou
      claim.AmountOfThisPay = howMuch
      claim.HowOftenPaidThisPay = "Other"
      page goToThePage ()
      page fillPageWith claim

      val submittedPage = page.submitPage()
      val errors = submittedPage.listErrors

      errors.size mustEqual 2
      errors(0) must contain("How often")

      val claimAfterError = new TestData
      claimAfterError.StillBeingPaidThisPay = yes
      claimAfterError.WhoPaidYouThisPay = whoPaysYou
      claimAfterError.AmountOfThisPay = howMuch
      claimAfterError.HowOftenPaidThisPay = monthlyFrequency

      submittedPage fillPageWith claimAfterError
      val differentPage = submittedPage submitPage()
      differentPage must beAnInstanceOf[GStatutoryMaternityPaternityAdoptionPayPage]
    }

    "data should be saved in claim and displayed when go back to page" in new WithJsBrowser with PageObjects {
      GYourIncomePage.fillYourIncomes(context, testData => { testData.YourIncomeStatutorySickPay = "true"; testData.YourIncomePatMatAdopPay = "true" })
      val page =  GStatutorySickPayPage(context)
      val claim = ClaimScenarioFactory.s9OtherIncomeOther

      page goToThePage ()
      page fillPageWith claim

      val nextPage = page submitPage ()
      nextPage must beAnInstanceOf[GStatutoryMaternityPaternityAdoptionPayPage]
      val statutorySickPayPageAgain = nextPage.goBack()
      statutorySickPayPageAgain.source must contain(whoPaysYou)
      statutorySickPayPageAgain.source must contain(howMuch)
    }
  }
  section ("integration", models.domain.StatutorySickPay.id)
}
