package controllers.s9_other_money

import org.specs2.mutable.{ Tags, Specification }
import controllers.{ BrowserMatchers, Formulate, ClaimScenarioFactory }
import play.api.test.WithBrowser
import utils.pageobjects.s9_other_money._
import utils.pageobjects.{PageObjects, TestData}
import utils.pageobjects.s11_pay_details.G1HowWePayYouPage

class G1AboutOtherMoneyIntegrationSpec extends Specification with Tags {
  "Other Money" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      browser.goTo("/other-money/about-other-money")
      titleMustEqual("Statutory pay, benefits and payments")
    }

    "contain errors on invalid submission" in {
      "no fields selected" in new WithBrowser with BrowserMatchers {
        browser.goTo("/other-money/about-other-money")
        browser.submit("button[type='submit']")
        titleMustEqual("Statutory pay, benefits and payments")

        findMustEqualSize("div[class=validation-summary] ol li", 3)
      }
    }

    "navigate to next page on valid submission with the four mandatory fields set to no" in new WithBrowser with BrowserMatchers {
      browser.goTo("/other-money/about-other-money")
      browser.click("#anyPaymentsSinceClaimDate_answer_no")
      browser.click("#statutorySickPay_answer_no")
      browser.click("#otherStatutoryPay_answer_no")
      browser.submit("button[type='submit']")
      titleMustEqual("How would you like to get paid? - Pay details")
    }

    "be presented" in new WithBrowser with PageObjects{
			val page =  G1AboutOtherMoneyPage(context)
      page goToThePage ()
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

    "contain errors on invalid submission" in {
      "mandatory fields empty" in new WithBrowser with PageObjects{
			val page =  G1AboutOtherMoneyPage(context)
        val claim = new TestData
        page goToThePage ()
        page fillPageWith claim
        val pageWithErrors = page.submitPage()
        pageWithErrors.listErrors.size mustEqual 3
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
    }
  } section ("integration", models.domain.OtherMoney.id)
}