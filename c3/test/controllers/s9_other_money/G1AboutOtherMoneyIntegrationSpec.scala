package controllers.s9_other_money

import org.specs2.mutable.{ Tags, Specification }
import controllers.{ BrowserMatchers, Formulate, ClaimScenarioFactory }
import play.api.test.WithBrowser
import utils.pageobjects.s9_other_money._
import utils.pageobjects.ClaimScenario
import utils.pageobjects.s8_self_employment.G9CompletedPageContext
import utils.pageobjects.s8_self_employment.G9CompletedPage

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
      Formulate.moreAboutYou(browser)
      Formulate.aboutOtherMoney(browser)
      titleMustEqual("Statutory Sick Pay - About Other Money")
    }

    "navigate to next page on valid submission with first two mandatory fields set to no" in new WithBrowser with BrowserMatchers {
      browser.goTo("/other-money/about-other-money")
      browser.click("#yourBenefits_answer_no")
      browser.click("#anyPaymentsSinceClaimDate_answer_no")
      browser.fill("#whoPaysYou") `with` "The Man"
      browser.fill("#howMuch") `with` "Not much"
      browser.submit("button[type='submit']")
      titleMustEqual("Statutory Sick Pay - About Other Money")
    }

    "be presented" in new WithBrowser with G1AboutOtherMoneyPageContext {
      page goToThePage ()
    }

    "present errors if mandatory fields are not populated" in new WithBrowser with G1AboutOtherMoneyPageContext {
      page goToThePage ()
      page.submitPage().listErrors.size mustEqual 2
    }

    "accept submit if all mandatory fields are populated" in new WithBrowser with G1AboutOtherMoneyPageContext {
      val claim = ClaimScenarioFactory.s9otherMoney
      page goToThePage ()
      page fillPageWith claim

      val nextPage = page submitPage ()

      nextPage must beAnInstanceOf[G5StatutorySickPayPage]
    }

    "navigate to next page on valid submission with other field selected" in new WithBrowser with G1AboutOtherMoneyPageContext {
      val claim = ClaimScenarioFactory.s9otherMoney
      claim.OtherMoneyHaveYouClaimedOtherBenefits = "yes"
      claim.OtherMoneyAnyPaymentsSinceClaimDate = "yes"
      claim.OtherMoneyWhoPaysYou = "The Man"
      claim.OtherMoneyHowMuch = "Not much"
      claim.OtherMoneyHowOften = "other"
      claim.OtherMoneyHowOftenOther = "every day and twice on Sundays"

      page goToThePage ()
      page fillPageWith claim

      val nextPage = page submitPage ()
      nextPage must beAnInstanceOf[G5StatutorySickPayPage]
    }

    "contain errors on invalid submission" in {
      "mandatory fields empty" in new WithBrowser with G1AboutOtherMoneyPageContext {
        val claim = new ClaimScenario
        page goToThePage ()
        page fillPageWith claim
        val pageWithErrors = page.submitPage()
        pageWithErrors.listErrors.size mustEqual 2
      }

      "howOften frequency of other with no other text entered" in new WithBrowser with G1AboutOtherMoneyPageContext {
        val claim = new ClaimScenario
        claim.OtherMoneyHaveYouClaimedOtherBenefits = "yes"
        claim.OtherMoneyAnyPaymentsSinceClaimDate = "yes"
        claim.OtherMoneyWhoPaysYou = "The Man"
        claim.OtherMoneyHowMuch = "Not much"
        claim.OtherMoneyHowOften = "other"
        page goToThePage ()
        page fillPageWith claim

        val errors = page.submitPage().listErrors

        errors.size mustEqual 1
        errors(0) must contain("How often?")
      }
    }
    
    "navigate back to previous section" in new WithBrowser with G9CompletedPageContext {
      val claim = ClaimScenarioFactory.s9SelfEmployment
      page goToThePage()
      page must beAnInstanceOf[G9CompletedPage]
      page fillPageWith claim
      val s9g1 = page submitPage()
      s9g1 must beAnInstanceOf[G1AboutOtherMoneyPage]

      val previous = s9g1.goBack()
      
      previous must beAnInstanceOf[G9CompletedPage]
    }
  } section ("integration", models.domain.OtherMoney.id)
}