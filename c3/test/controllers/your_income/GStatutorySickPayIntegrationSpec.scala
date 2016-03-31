package controllers.your_income

import controllers.ClaimScenarioFactory
import org.specs2.mutable._
import utils.pageobjects._
import utils.pageobjects.s_pay_details.GHowWePayYouPage
import utils.pageobjects.your_income.GStatutorySickPayPage
import utils.{WithBrowser, WithJsBrowser}

class GStatutorySickPayIntegrationSpec extends Specification {
  val whoPaysYou = "The Man"
  val howMuch = "12"
  val yes = "yes"
  val no = "no"
  val frequency = "Monthly"

  section ("integration", models.domain.StatutorySickPay.id)
  "Statutory Sick Pay" should {
    "be presented" in new WithBrowser with PageObjects {
      val page = GStatutorySickPayPage(context)
      page goToThePage ()
    }

    "navigate to next page on valid submission" in new WithBrowser with PageObjects {
      val page =  GStatutorySickPayPage(context)
      page goToThePage ()

      val claim = new TestData
      claim.StatutorySickPayStillBeingPaidStatutorySickPay = yes
      claim.StatutorySickPayWhoPaidYouStatutorySickPay = whoPaysYou
      claim.StatutorySickPayAmountOfStatutorySickPay = howMuch
      claim.StatutorySickPayHowOftenPaidStatutorySickPay = frequency
      page fillPageWith claim
      page submitPage() must beAnInstanceOf[GHowWePayYouPage]
    }

    "present errors if mandatory fields are not populated" in new WithBrowser with PageObjects {
			val page =  GStatutorySickPayPage(context)
      page goToThePage ()
      page.submitPage().listErrors.size mustEqual 4
    }

    "navigate to next page on valid submission with other field selected" in new WithBrowser with PageObjects {
      val page = GStatutorySickPayPage(context)
      val claim = ClaimScenarioFactory.s9StatutorySickPayOther

      page goToThePage ()
      page fillPageWith claim

      val nextPage = page submitPage ()

      nextPage must beAnInstanceOf[GHowWePayYouPage]
    }

    "howOften frequency of other with no other text entered" in new WithBrowser with PageObjects {
      val page = GStatutorySickPayPage(context)
      val claim = new TestData
      claim.StatutorySickPayStillBeingPaidStatutorySickPay = no
      claim.StatutorySickPayWhoPaidYouStatutorySickPay = whoPaysYou
      claim.StatutorySickPayAmountOfStatutorySickPay = "12"
      claim.StatutorySickPayHowOftenPaidStatutorySickPay = "Other"
      page goToThePage ()
      page fillPageWith claim

      val errors = page.submitPage().listErrors

      errors.size mustEqual 2
      errors(0) must contain("How often")
    }

    "howOften frequency of other with no other text entered then select yes and be able to move to next page" in new WithJsBrowser with PageObjects {
      val page = GStatutorySickPayPage(context)
      val claim = new TestData
      claim.StatutorySickPayStillBeingPaidStatutorySickPay = no
      claim.StatutorySickPayWhoPaidYouStatutorySickPay = whoPaysYou
      claim.StatutorySickPayAmountOfStatutorySickPay = "12"
      claim.StatutorySickPayHowOftenPaidStatutorySickPay = "Other"
      page goToThePage ()
      page fillPageWith claim

      val submittedPage = page.submitPage()
      val errors = submittedPage.listErrors

      errors.size mustEqual 2
      errors(0) must contain("How often")

      val claimAfterError = new TestData
      claimAfterError.StatutorySickPayStillBeingPaidStatutorySickPay = yes
      claimAfterError.StatutorySickPayWhoPaidYouStatutorySickPay = whoPaysYou
      claimAfterError.StatutorySickPayAmountOfStatutorySickPay = howMuch
      claimAfterError.StatutorySickPayHowOftenPaidStatutorySickPay = frequency

      submittedPage fillPageWith claimAfterError
      val differentPage = submittedPage submitPage()
      differentPage must beAnInstanceOf[GHowWePayYouPage]
    }

    "data should be saved in claim and displayed when go back to page" in new WithBrowser with PageObjects {
      val page =  GStatutorySickPayPage(context)
      val claim = ClaimScenarioFactory.s9StatutorySickPayOther

      page goToThePage ()
      page fillPageWith claim

      val nextPage = page submitPage ()
      nextPage must beAnInstanceOf[GHowWePayYouPage]
      val statutorySickPayPageAgain = nextPage.goBack()
      statutorySickPayPageAgain.source must contain(whoPaysYou)
      statutorySickPayPageAgain.source must contain(howMuch)
    }
  }
  section ("integration", models.domain.StatutorySickPay.id)
}
