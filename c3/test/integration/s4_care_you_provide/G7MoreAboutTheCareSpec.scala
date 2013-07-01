package integration.s4_care_you_provide

import org.specs2.mutable.{ Tags, Specification }
import play.api.test.WithBrowser
import integration.Helper

class G7MoreAboutTheCareSpec extends Specification with Tags {

  "Representatives For The Person" should {
    "be presented" in new WithBrowser {
      browser.goTo("/careYouProvide/moreAboutTheCare")
      browser.title() mustEqual "More about the care you provide - Care You Provide"
    }

    "contain errors on invalid submission" in new WithBrowser {
      browser.goTo("/careYouProvide/moreAboutTheCare")
      browser.submit("button[type='submit']")
      browser.find("div[class=validation-summary] ol li").size mustEqual 3
    }

    "contains errors for optional mandatory data" in new WithBrowser {
      browser.goTo("/careYouProvide/moreAboutTheCare")
      browser.click("#spent35HoursCaring_yes")
      browser.click("#spent35HoursCaringBeforeClaim_yes")
      browser.click("#hasSomeonePaidYou_yes")
      browser.submit("button[type='submit']")
      browser.find("div[class=validation-summary] ol li").size mustEqual 1
    }

    "navigate back" in new WithBrowser {
      Helper.fillRepresentativesForThePerson(browser)
      browser.click("#backButton")
      browser.title() mustEqual "Representatives For The Person - Care You Provide"
    }

    "contain the completed forms" in new WithBrowser {
      Helper.fillMoreAboutTheCare(browser)
      browser.find("div[class=completed] ul li").size() mustEqual 1
      browser.find("div[class=completed] ul li").get(0).getText must contain("More about the care you provide")
    }
    
    "navigate to One Who Pays when submitting with hasSomeonePaidYou positive" in new WithBrowser {
      Helper.fillMoreAboutTheCare(browser)
      browser.title() mustEqual "One Who Pays You - Care You Provide"
    }

    """navigate to "breaks in care" when submitting with hasSomeonePaidYou negative""" in new WithBrowser {
      Helper.fillMoreAboutTheCareWithNotPaying(browser)
      browser.title() mustEqual "Breaks in Care - Care You Provide"
    }

    "navigate back to the start of the positive path navigate after completing the S4G3 ClaimedAllowanceBefore positive answer path and navigating back twice" in new WithBrowser { // [SKW] This tests a problem where history was stuck in a loop and would only go back once.
      Helper.fillMoreAboutThePersonWithClaimedAllowanceBefore(browser)
      Helper.fillPreviousCarerPersonalDetails(browser)
      Helper.fillPreviousCarerContactDetails(browser)
      Helper.fillRepresentativesForThePersonNegativeAnswers(browser)
      
      browser.click("#backButton")
      browser.click("#backButton")
      browser.title() mustEqual "Contact Details Of The Person Who Claimed Before - Care You Provide" // Back to S4 G4
    }
  }
}