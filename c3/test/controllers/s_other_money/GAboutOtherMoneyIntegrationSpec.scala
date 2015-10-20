package controllers.s_other_money

import org.specs2.mutable.{ Tags, Specification }
import controllers.ClaimScenarioFactory
import utils.{WithBrowser, WithJsBrowser}
import utils.pageobjects.s_other_money._
import utils.pageobjects._
import utils.pageobjects.s_pay_details.GHowWePayYouPage


class GAboutOtherMoneyIntegrationSpec extends Specification with Tags {
  "Other Money" should {
    "be presented" in new WithBrowser with PageObjects {
      val page = GAboutOtherMoneyPage(context)
      page goToThePage ()
    }

    "navigate to next page on valid submission with the three mandatory fields set to no" in new WithBrowser with PageObjects {
      val page =  GAboutOtherMoneyPage(context)
      page goToThePage ()
      val claim = new TestData
      claim.OtherMoneyAnyPaymentsSinceClaimDate = "no"
      claim.OtherMoneyHaveYouSSPSinceClaim = "no"
      claim.OtherMoneyHaveYouSMPSinceClaim = "no"
      page fillPageWith claim
      page submitPage() must beAnInstanceOf[GHowWePayYouPage]
    }

    "present errors if mandatory fields are not populated" in new WithBrowser with PageObjects{
			val page =  GAboutOtherMoneyPage(context)
      page goToThePage ()
      page.submitPage().listErrors.size mustEqual 3
    }

    "accept submit if all mandatory fields are populated" in new WithBrowser with PageObjects{
			val page =  GAboutOtherMoneyPage(context)
      val claim = ClaimScenarioFactory.s9otherMoneyOther
      page goToThePage ()
      page fillPageWith claim

      val nextPage = page submitPage ()

      nextPage must beAnInstanceOf[GHowWePayYouPage]
    }

    "navigate to next page on valid submission with other field selected" in new WithBrowser with PageObjects {
      val page = GAboutOtherMoneyPage(context)
      val claim = ClaimScenarioFactory.s9otherMoneyOther

      page goToThePage ()
      page fillPageWith claim

      val nextPage = page submitPage ()

      nextPage must beAnInstanceOf[GHowWePayYouPage]
    }

    "howOften frequency of other with no other text entered" in new WithBrowser with PageObjects {
      val page = GAboutOtherMoneyPage(context)
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

    "howOften frequency of other with no other text entered then select no and be able to move to next page" in new WithJsBrowser with PageObjects {
      val page = GAboutOtherMoneyPage(context)
      val claim = new TestData
      claim.OtherMoneyAnyPaymentsSinceClaimDate = "yes"
      claim.OtherMoneyWhoPaysYou = "The Man"
      claim.OtherMoneyHowMuch = "34"
      claim.OtherMoneyHowOften = "other"
      page goToThePage ()
      page fillPageWith claim

      val submittedPage = page.submitPage()
      val errors = submittedPage.listErrors

      errors.size mustEqual 3
      errors(0) must contain("How often?")

      val claimAfterError = new TestData
      claimAfterError.OtherMoneyAnyPaymentsSinceClaimDate = "no"
      claimAfterError.OtherMoneyHaveYouSSPSinceClaim = "no"
      claimAfterError.OtherMoneyHaveYouSMPSinceClaim = "no"

      submittedPage fillPageWith claimAfterError
      val differentPage = submittedPage submitPage()
      differentPage must beAnInstanceOf[GHowWePayYouPage]
    }

  } section ("integration", models.domain.OtherMoney.id)

}