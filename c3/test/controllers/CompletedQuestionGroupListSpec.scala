package controllers

import play.api.test.WithBrowser
import org.specs2.mutable.Specification
import org.specs2.mutable.Tags
import java.util.concurrent.TimeUnit

class CompletedQuestionGroupListSpec extends Specification with Tags {

  "Completed Question Group List" should {
    "increase when navigating forwards" in new WithBrowser {
      FormHelper.fillTheirPersonalDetails(browser)
      FormHelper.fillTheirContactDetails(browser)
      browser.find("div[class=completed] ul li").size mustEqual 2

      FormHelper.fillMoreAboutThePersonWithClaimedAllowanceBefore(browser)

      browser.find("div[class=completed] ul li").size mustEqual 3
    }

    "decrease when navigating backwards" in new WithBrowser {
      FormHelper.fillTheirPersonalDetails(browser)
      FormHelper.fillTheirContactDetails(browser)
      FormHelper.fillMoreAboutThePersonWithClaimedAllowanceBefore(browser)
      browser.find("div[class=completed] ul li").size mustEqual 3

      browser.click("#backButton")

      browser.find("div[class=completed] ul li").size mustEqual 2
    }

    "contain the correct items when navigating S4G3 ClaimedAllowanceBefore positive answer path" in new WithBrowser {
      FormHelper.fillTheirPersonalDetails(browser)
      FormHelper.fillTheirContactDetails(browser)
      FormHelper.fillMoreAboutThePersonWithClaimedAllowanceBefore(browser)
      FormHelper.fillPreviousCarerPersonalDetails(browser)
      FormHelper.fillPreviousCarerContactDetails(browser)
      FormHelper.fillRepresentativesForThePerson(browser)

      browser.find("div[class=completed] ul li").size mustEqual 6
      browser.find("div[class=completed] ul li").get(2).getText must contain("More about the person you care for")
      browser.find("div[class=completed] ul li").get(3).getText must contain("About the previous Carer")
      browser.find("div[class=completed] ul li").get(4).getText must contain("More about the care you provide")
      browser.find("div[class=completed] ul li").get(5).getText must contain("Representatives for the person you care for")
    }

    "contain the correct items when navigating S4G3 ClaimedAllowanceBefore negative answer path" in new WithBrowser {
      FormHelper.fillTheirPersonalDetails(browser)
      FormHelper.fillTheirContactDetails(browser)
      FormHelper.fillMoreAboutThePersonWithNotClaimedAllowanceBefore(browser)
      FormHelper.fillRepresentativesForThePerson(browser)

      browser.find("div[class=completed] ul li").size mustEqual 4
      browser.find("div[class=completed] ul li").get(2).getText must contain("More about the person you care for")
      browser.find("div[class=completed] ul li").get(3).getText must contain("Representatives for the person you care for")
    }

    "remove invalidated history after completing the S4G3 ClaimedAllowanceBefore postive answer path but goes back and changes to the negative answer path" in new WithBrowser {
      def titleMustEqual(title: String) = {
        browser.waitUntil[Boolean](30, TimeUnit.SECONDS) {
          browser.title mustEqual title
        }
      }

      FormHelper.fillTheirPersonalDetails(browser)
      titleMustEqual("Their Contact Details - Care You Provide")

      FormHelper.fillTheirContactDetails(browser)
      titleMustEqual("More About The Person You Care For - Care You Provide")

      FormHelper.fillMoreAboutThePersonWithClaimedAllowanceBefore(browser)
      titleMustEqual("Details Of The Person Who Claimed Before - Care You Provide")

      FormHelper.fillPreviousCarerPersonalDetails(browser)
      titleMustEqual("Contact Details Of The Person Who Claimed Before - Care You Provide")

      FormHelper.fillPreviousCarerContactDetails(browser)
      titleMustEqual("Representatives For The Person - Care You Provide")

      FormHelper.fillRepresentativesForThePerson(browser)
      titleMustEqual("More about the care you provide - Care You Provide")

      browser.click("#backButton")
      titleMustEqual("Representatives For The Person - Care You Provide")

      browser.click("#backButton")
      titleMustEqual("Contact Details Of The Person Who Claimed Before - Care You Provide")

      FormHelper.fillMoreAboutThePersonWithNotClaimedAllowanceBefore(browser)
      titleMustEqual("Representatives For The Person - Care You Provide")

      FormHelper.fillRepresentativesForThePerson(browser)
      titleMustEqual("More about the care you provide - Care You Provide")

      browser.find("div[class=completed] ul li").size mustEqual 4
      browser.find("div[class=completed] ul li").get(2).getText must contain("More about the person you care for")
      browser.find("div[class=completed] ul li").get(3).getText must contain("Representatives for the person you care for")
    }
  } section "integration"
}