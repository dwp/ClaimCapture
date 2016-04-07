package controllers.your_income

import controllers.ClaimScenarioFactory
import org.specs2.mutable._
import utils.pageobjects._
import utils.pageobjects.your_income.{GYourIncomePage, GFosteringAllowancePage, GStatutoryMaternityPaternityAdoptionPayPage}
import utils.{WithBrowser, WithJsBrowser}

class GStatutoryMaternityPaternityAdoptionPayIntegrationSpec extends Specification {
  val whoPaysYou = "The Man"
  val howMuch = "12"
  val yes = "yes"
  val no = "no"
  val monthlyFrequency = "Monthly"
  val paymentType = "MaternityOrPaternityPay"

  section ("integration", models.domain.StatutoryMaternityPaternityAdoptionPay.id)
  "Statutory Maternity Paternity Adoption Pay" should {
    "be presented" in new WithBrowser with PageObjects {
      GYourIncomePage.fillYourIncomes(context, testData => { testData.YourIncomePatMatAdopPay = "true" })
      val page = GStatutoryMaternityPaternityAdoptionPayPage(context)
      page goToThePage ()
    }

    "navigate to next page on valid submission" in new WithJsBrowser with PageObjects {
      GYourIncomePage.fillYourIncomes(context, testData => { testData.YourIncomePatMatAdopPay = "true"; testData.YourIncomeFosteringAllowance = "true" })
      val page =  GStatutoryMaternityPaternityAdoptionPayPage(context)
      page goToThePage ()

      val claim = new TestData
      claim.PaymentTypesForThisPay = paymentType
      claim.StillBeingPaidThisPay = yes
      claim.WhoPaidYouThisPay = whoPaysYou
      claim.AmountOfThisPay = howMuch
      claim.HowOftenPaidThisPay = monthlyFrequency
      page fillPageWith claim
      page submitPage() must beAnInstanceOf[GFosteringAllowancePage]
    }

    "present errors if mandatory fields are not populated" in new WithJsBrowser with PageObjects {
      GYourIncomePage.fillYourIncomes(context, testData => { testData.YourIncomePatMatAdopPay = "true"; testData.YourIncomeFosteringAllowance = "true" })
			val page =  GStatutoryMaternityPaternityAdoptionPayPage(context)
      page goToThePage ()
      page.submitPage().listErrors.size mustEqual 5
    }

    "navigate to next page on valid submission with other field selected" in new WithJsBrowser with PageObjects {
      GYourIncomePage.fillYourIncomes(context, testData => { testData.YourIncomePatMatAdopPay = "true"; testData.YourIncomeFosteringAllowance = "true" })
      val page = GStatutoryMaternityPaternityAdoptionPayPage(context)
      val claim = ClaimScenarioFactory.s9OtherIncomeOther

      page goToThePage ()
      page fillPageWith claim

      val nextPage = page submitPage ()

      nextPage must beAnInstanceOf[GFosteringAllowancePage]
    }

    "howOften frequency of other with no other text entered" in new WithJsBrowser with PageObjects {
      GYourIncomePage.fillYourIncomes(context, testData => { testData.YourIncomePatMatAdopPay = "true"; testData.YourIncomeFosteringAllowance = "true" })
      val page = GStatutoryMaternityPaternityAdoptionPayPage(context)
      val claim = new TestData
      claim.PaymentTypesForThisPay = paymentType
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

    "no payment type selected" in new WithJsBrowser with PageObjects {
      GYourIncomePage.fillYourIncomes(context, testData => { testData.YourIncomePatMatAdopPay = "true"; testData.YourIncomeFosteringAllowance = "true" })
      val page = GStatutoryMaternityPaternityAdoptionPayPage(context)
      val claim = new TestData
      claim.StillBeingPaidThisPay = no
      claim.WhoPaidYouThisPay = whoPaysYou
      claim.AmountOfThisPay = howMuch
      claim.HowOftenPaidThisPay = monthlyFrequency
      page goToThePage ()
      page fillPageWith claim

      val errors = page.submitPage().listErrors

      errors.size mustEqual 1
      errors(0) must contain("Which are you paid")
    }

    "howOften frequency of other with no other text entered then select yes and be able to move to next page" in new WithJsBrowser with PageObjects {
      GYourIncomePage.fillYourIncomes(context, testData => { testData.YourIncomePatMatAdopPay = "true"; testData.YourIncomeFosteringAllowance = "true" })
      val page = GStatutoryMaternityPaternityAdoptionPayPage(context)
      val claim = new TestData
      claim.PaymentTypesForThisPay = paymentType
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
      claimAfterError.PaymentTypesForThisPay = paymentType
      claimAfterError.StillBeingPaidThisPay = yes
      claimAfterError.WhoPaidYouThisPay = whoPaysYou
      claimAfterError.AmountOfThisPay = howMuch
      claimAfterError.HowOftenPaidThisPay = monthlyFrequency

      submittedPage fillPageWith claimAfterError
      val differentPage = submittedPage submitPage()
      differentPage must beAnInstanceOf[GFosteringAllowancePage]
    }

    "data should be saved in claim and displayed when go back to page" in new WithJsBrowser with PageObjects {
      GYourIncomePage.fillYourIncomes(context, testData => { testData.YourIncomePatMatAdopPay = "true"; testData.YourIncomeFosteringAllowance = "true" })
      val page =  GStatutoryMaternityPaternityAdoptionPayPage(context)
      val claim = ClaimScenarioFactory.s9OtherIncomeOther

      page goToThePage ()
      page fillPageWith claim

      val nextPage = page submitPage ()
      nextPage must beAnInstanceOf[GFosteringAllowancePage]
      val statutoryMaternityPaternityAdoptionPayPageAgain = nextPage.goBack()
      statutoryMaternityPaternityAdoptionPayPageAgain.source must contain(whoPaysYou)
      statutoryMaternityPaternityAdoptionPayPageAgain.source must contain(howMuch)
    }
  }
  section ("integration", models.domain.StatutoryMaternityPaternityAdoptionPay.id)
}
