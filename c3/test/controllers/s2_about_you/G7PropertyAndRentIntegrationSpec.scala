package controllers.s2_about_you

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{ClaimScenarioFactory, BrowserMatchers, Formulate}
import utils.pageobjects.s2_about_you.{G1YourDetailsPageContext, G8AboutYouCompletedPage}
import play.api.i18n.Messages

class G7PropertyAndRentIntegrationSpec extends Specification with Tags {
  "Property and Rent" should {
    "present Benefits when there is no claim date" in new WithBrowser with BrowserMatchers {
      browser.goTo("/about-you/property-and-rent")
      titleMustEqual("Does the person you look after get one of these benefits? - Can you get Carer's Allowance?")
    }

    "be presented when there is a claim date" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)
      browser.goTo("/about-you/property-and-rent")
      titleMustEqual(Messages("s2.g7") + " - About you - the carer")
    }

    "contain the completed forms" in new WithBrowser with BrowserMatchers {
      Formulate.yourDetails(browser)
      Formulate.yourContactDetails(browser)
      Formulate.claimDate(browser)
      Formulate.moreAboutYou(browser)
      Formulate.employment(browser)

      titleMustEqual(Messages("s2.g7") + " - About you - the carer")
      findMustEqualSize("div[class=completed] ul li", 5)
    }

    "contain questions with claim dates" in new WithBrowser {
      val dateString = "03/04/1950"
      Formulate.claimDate(browser)
      browser.goTo("/about-you/property-and-rent")
      val h3 = browser.find("div[class=completed] ul li h3")
      h3.getText.contains(dateString) mustEqual true

      val questionLabels = browser.find("fieldset[class=form-elements] ul[class=group] > li > p")
      questionLabels.get(0).getText must contain(dateString)
      questionLabels.get(1).getText must contain(dateString)
    }

    "contain errors on invalid submission" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)
      browser.goTo("/about-you/property-and-rent")
      browser.title mustEqual Messages("s2.g7") + " - About you - the carer"
      browser.submit("button[type='submit']")

      findMustEqualSize("p[class=error]", 2)
    }

    "navigate to next page on valid submission" in new WithBrowser with G1YourDetailsPageContext {
      skipped("Timing issue i.e. when tests take too long to run (mainly because govMain accesses external resources that are not required in test)")

      val claim = ClaimScenarioFactory.s2AboutYouWithTimeOutside()
      page goToThePage()
      page runClaimWith (claim, G8AboutYouCompletedPage.title)
    }
  } section("integration", models.domain.AboutYou.id)
}