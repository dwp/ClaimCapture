package controllers.s9_other_money

import org.specs2.mutable.{ Tags, Specification }
import controllers.{ BrowserMatchers, Formulate, ClaimScenarioFactory }
import play.api.test.WithBrowser
import utils.pageobjects.s9_other_money._
import utils.pageobjects.{PageObjects, TestData}


class G1AboutOtherMoneyIntegrationSpec extends Specification with Tags {
  "About Other Money" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      browser.goTo("/other-money/about-other-money")
      titleMustEqual("Details about other money - About Other Money")
    }

    "contain errors on invalid submission" in {
      "no fields selected" in new WithBrowser with BrowserMatchers {
        browser.goTo("/other-money/about-other-money")
        browser.submit("button[type='submit']")
        titleMustEqual("Details about other money - About Other Money")

        findMustEqualSize("div[class=validation-summary] ol li", 2)
      }
    }

    "navigate to next page on valid submission with all text fields enabled and filled in" in new WithBrowser with BrowserMatchers {
      Formulate.nationalityAndResidency(browser)
      Formulate.moreAboutYou(browser)
      Formulate.aboutOtherMoney(browser)
      titleMustEqual("Statutory Sick Pay - About Other Money")
    }

    "navigate to next page on valid submission with first two mandatory fields set to no" in new WithBrowser with BrowserMatchers {
      browser.goTo("/other-money/about-other-money")
      browser.click("#yourBenefits_answer_no")
      browser.click("#anyPaymentsSinceClaimDate_answer_no")
      browser.submit("button[type='submit']")
      titleMustEqual("Statutory Sick Pay - About Other Money")
    }

    "be presented" in new WithBrowser with PageObjects{
			val page =  G1AboutOtherMoneyPage(context)
      page goToThePage ()
    }

    "present errors if mandatory fields are not populated" in new WithBrowser with PageObjects{
			val page =  G1AboutOtherMoneyPage(context)
      page goToThePage ()
      page.submitPage().listErrors.size mustEqual 2
    }

    "accept submit if all mandatory fields are populated" in new WithBrowser with PageObjects{
			val page =  G1AboutOtherMoneyPage(context)
      val claim = ClaimScenarioFactory.s9otherMoney
      page goToThePage ()
      page fillPageWith claim

      val nextPage = page submitPage ()

      nextPage must beAnInstanceOf[G5StatutorySickPayPage]
    }

    "navigate to next page on valid submission with other field selected" in new WithBrowser with PageObjects {
      val page = G1AboutOtherMoneyPage(context)
      val claim = ClaimScenarioFactory.s9otherMoney
      claim.OtherMoneyHaveYouClaimedOtherBenefits = "yes"
      claim.OtherMoneyAnyPaymentsSinceClaimDate = "yes"
      claim.OtherMoneyWhoPaysYou = "The Man"
      claim.OtherMoneyHowMuch = "12"
      claim.OtherMoneyHowOften = "other"
      claim.OtherMoneyHowOftenOther = "every day and twice on Sundays"

      page goToThePage ()
      page fillPageWith claim

      val nextPage = page submitPage ()
      nextPage must beAnInstanceOf[G5StatutorySickPayPage]
    }

    "contain errors on invalid submission" in {
      "mandatory fields empty" in new WithBrowser with PageObjects{
			val page =  G1AboutOtherMoneyPage(context)
        val claim = new TestData
        page goToThePage ()
        page fillPageWith claim
        val pageWithErrors = page.submitPage()
        pageWithErrors.listErrors.size mustEqual 2
      }

      "howOften frequency of other with no other text entered" in new WithBrowser with PageObjects {
        val page = G1AboutOtherMoneyPage(context)
        val claim = new TestData
        claim.OtherMoneyHaveYouClaimedOtherBenefits = "yes"
        claim.OtherMoneyAnyPaymentsSinceClaimDate = "yes"
        claim.OtherMoneyWhoPaysYou = "The Man"
        claim.OtherMoneyHowMuch = "34"
        claim.OtherMoneyHowOften = "other"
        page goToThePage ()
        page fillPageWith claim

        val errors = page.submitPage().listErrors

        errors.size mustEqual 1
        errors(0) must contain("How often?")
      }
    }
  } section ("integration", models.domain.OtherMoney.id)
}