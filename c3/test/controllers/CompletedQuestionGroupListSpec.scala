package controllers

import org.specs2.mutable.Specification
import org.specs2.mutable.Tags
import play.api.test.WithBrowser

class CompletedQuestionGroupListSpec extends Specification with Tags {

  "Completed Question Group List" should {
    "increase when navigating forwards" in new WithBrowser with BrowserMatchers {
      Formulate.theirPersonalDetails(browser)
      titleMustEqual("Their Contact Details - Care You Provide")

      Formulate.theirContactDetails(browser)
      titleMustEqual("More About The Person You Care For - Care You Provide")
      browser.find("div[class=completed] ul li").size mustEqual 2

      Formulate.moreAboutThePersonWithClaimedAllowanceBefore(browser)
      titleMustEqual("Details Of The Person Who Claimed Before - Care You Provide")
      browser.find("div[class=completed] ul li").size mustEqual 3
    }

    "decrease when navigating backwards" in new WithBrowser with BrowserMatchers {
      Formulate.theirPersonalDetails(browser)
      titleMustEqual("Their Contact Details - Care You Provide")

      Formulate.theirContactDetails(browser)
      titleMustEqual("More About The Person You Care For - Care You Provide")

      Formulate.moreAboutThePersonWithClaimedAllowanceBefore(browser)
      titleMustEqual("Details Of The Person Who Claimed Before - Care You Provide")
      browser.find("div[class=completed] ul li").size mustEqual 3

      browser.click("#backButton")
      titleMustEqual("More About The Person You Care For - Care You Provide")

      browser.find("div[class=completed] ul li").size mustEqual 2
    }

    "contain the correct items when navigating S4G3 ClaimedAllowanceBefore positive answer path" in new WithBrowser with BrowserMatchers {
      skipped

      /*Formulate.theirPersonalDetails(browser)
      titleMustEqual("Their Contact Details - Care You Provide")

      Formulate.theirContactDetails(browser)
      titleMustEqual("More About The Person You Care For - Care You Provide")

      Formulate.moreAboutThePersonWithClaimedAllowanceBefore(browser)
      titleMustEqual("Details Of The Person Who Claimed Before - Care You Provide")

      Formulate.previousCarerPersonalDetails(browser)
      titleMustEqual("Contact Details Of The Person Who Claimed Before - Care You Provide")

      Formulate.previousCarerContactDetails(browser)
      titleMustEqual("Representatives For The Person - Care You Provide")

      Formulate.representativesForThePerson(browser)
      titleMustEqual("More about the care you provide - Care You Provide")

      browser.find("div[class=completed] ul li").size mustEqual 6
      browser.find("div[class=completed] ul li").get(2).getText must contain("More about the person you care for")
      browser.find("div[class=completed] ul li").get(3).getText must contain("About the previous Carer")
      browser.find("div[class=completed] ul li").get(4).getText must contain("More about the care you provide")
      browser.find("div[class=completed] ul li").get(5).getText must contain("Representatives for the person you care for")*/
    }

    "contain the correct items when navigating S4G3 ClaimedAllowanceBefore negative answer path" in new WithBrowser with BrowserMatchers {
      Formulate.theirPersonalDetails(browser)
      titleMustEqual("Their Contact Details - Care You Provide")

      Formulate.theirContactDetails(browser)
      titleMustEqual("More About The Person You Care For - Care You Provide")

      Formulate.moreAboutThePersonWithNotClaimedAllowanceBefore(browser)
      titleMustEqual("Representatives For The Person - Care You Provide")

      Formulate.representativesForThePerson(browser)
      titleMustEqual("More about the care you provide - Care You Provide")

      browser.find("div[class=completed] ul li").size mustEqual 4
      browser.find("div[class=completed] ul li").get(2).getText must contain("More about the person you care for")
      browser.find("div[class=completed] ul li").get(3).getText must contain("Representatives for the person you care for")
    }

    """remove invalidated history after completing the S4G3 ClaimedAllowanceBefore postive answer path
       but goes back and changes to the negative answer path""" in new WithBrowser with BrowserMatchers {
      Formulate.theirPersonalDetails(browser)
      titleMustEqual("Their Contact Details - Care You Provide")

      Formulate.theirContactDetails(browser)
      titleMustEqual("More About The Person You Care For - Care You Provide")

      Formulate.moreAboutThePersonWithClaimedAllowanceBefore(browser)
      titleMustEqual("Details Of The Person Who Claimed Before - Care You Provide")

      Formulate.previousCarerPersonalDetails(browser)
      titleMustEqual("Contact Details Of The Person Who Claimed Before - Care You Provide")

      Formulate.previousCarerContactDetails(browser)
      titleMustEqual("Representatives For The Person - Care You Provide")

      Formulate.representativesForThePerson(browser)
      titleMustEqual("More about the care you provide - Care You Provide")

      browser.click("#backButton")
      titleMustEqual("Representatives For The Person - Care You Provide")

      browser.click("#backButton")
      titleMustEqual("Contact Details Of The Person Who Claimed Before - Care You Provide")

      Formulate.moreAboutThePersonWithNotClaimedAllowanceBefore(browser)
      titleMustEqual("Representatives For The Person - Care You Provide")

      Formulate.representativesForThePerson(browser)
      titleMustEqual("More about the care you provide - Care You Provide")

      browser.find("div[class=completed] ul li").size mustEqual 4
      browser.find("div[class=completed] ul li").get(2).getText must contain("More about the person you care for")
      browser.find("div[class=completed] ul li").get(3).getText must contain("Representatives for the person you care for")
    }
  } section "integration"
}