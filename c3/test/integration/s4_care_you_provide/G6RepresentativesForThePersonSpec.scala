package integration.s4_care_you_provide

import org.specs2.mutable.{ Tags, Specification }
import play.api.test.WithBrowser
import integration.Helper

class G6RepresentativesForThePersonSpec extends Specification with Tags {

  "Representatives For The Person" should {
    "be presented" in new WithBrowser {
      browser.goTo("/careYouProvide/representativesForPerson")
      browser.title() mustEqual "Representatives For The Person - Care You Provide"
    }

    "contain errors on invalid submission" in new WithBrowser {
      browser.goTo("/careYouProvide/representativesForPerson")
      browser.submit("button[type='submit']")
      browser.find("div[class=validation-summary] ol li").size mustEqual 2
    }

    "contains errors for optional mandatory data" in new WithBrowser {
      browser.goTo("/careYouProvide/representativesForPerson")
      browser.click("#actForPerson_yes")
      browser.click("#someoneElseActForPerson_yes")
      browser.submit("button[type='submit']")
      browser.find("div[class=validation-summary] ol li").size mustEqual 2
    }

    "navigate back to More About The Person" in new WithBrowser {
      Helper.fillMoreAboutThePersonWithNotClaimedAllowanceBefore(browser)
      browser.goTo("/careYouProvide/representativesForPerson")
      browser.click("#backButton")
      browser.title() mustEqual "More About The Person You Care For - Care You Provide"
    }

    "navigate back to Previous Carer Contact Details" in new WithBrowser {
      Helper.fillMoreAboutThePersonWithClaimedAllowanceBefore(browser)
      Helper.fillPreviousCarerPersonalDetails(browser)
      Helper.fillPreviousCarerContactDetails(browser)
      browser.title() mustEqual "Representatives For The Person - Care You Provide" // Landed on S4 G6
      browser.click("#backButton")
      browser.title() mustEqual "Contact Details Of The Person Who Claimed Before - Care You Provide" // Back to S4 G5
    }

    "navigate back twice to Previous Carer Personal Details" in new WithBrowser { // [SKW] This tests a problem I was having where pressing back twice wasn't getting back passed the S4 G4, the problem was with Controller action fetching previous question groups being different for pages using backHref.
      Helper.fillMoreAboutThePersonWithClaimedAllowanceBefore(browser)
      Helper.fillPreviousCarerPersonalDetails(browser)
      Helper.fillPreviousCarerContactDetails(browser)
      browser.title() mustEqual "Representatives For The Person - Care You Provide" // Landed on S4 G6
      browser.click("#backButton")
      browser.click("#backButton")
      browser.title() mustEqual "Details Of The Person Who Claimed Before - Care You Provide" // Back to S4 G4
    }

    "contain the completed forms" in new WithBrowser {
      Helper.fillRepresentativesForThePerson(browser)
      browser.find("div[class=completed] ul li").size() mustEqual 1
    }

    "navigate to More ABout The Care on valid submission" in new WithBrowser {
      Helper.fillRepresentativesForThePerson(browser)
      browser.title() mustEqual "More about the care you provide - Care You Provide"
    }
  }
}