package integration.s4_care_you_provide

import org.specs2.mutable.{Tags, Specification}
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
     }

     "chose no options navigate back twice to Previous Carer Contact Details" in new WithBrowser { // [SKW] This tests a problem I was having where pressing back twice wasn't getting back passed the S4 G4, the problem was with Controller action fetching previous question groups being different for pages using backHref.
      Helper.fillMoreAboutThePersonWithClaimedAllowanceBefore(browser)
      Helper.fillPreviousCarerPersonalDetails(browser)
      Helper.fillPreviousCarerContactDetails(browser)
      browser.title() mustEqual "Representatives For The Person - Care You Provide"
      browser.click("#actForPerson_no")
      browser.click("#someoneElseActForPerson_no")
      browser.submit("button[type='submit']")
      browser.title() mustEqual "More about the care you provide - Care You Provide" // Landed on S4 G6
      browser.click("#backButton")
      browser.title() mustEqual "Representatives For The Person - Care You Provide"
      browser.click("#backButton")
      browser.title() mustEqual "Contact Details Of The Person Who Claimed Before - Care You Provide" // Back to S4 G4
    }

   "choose yes options navigate back twice to Previous Carer Contact Details" in new WithBrowser { // [SKW] This tests a problem I was having where pressing back twice wasn't getting back passed the S4 G4, the problem was with Controller action fetching previous question groups being different for pages using backHref.
     Helper.fillTheirPersonalDetails(browser)
     Helper.fillTheirContactDetails(browser)
     Helper.fillMoreAboutThePersonWithClaimedAllowanceBefore(browser)
      Helper.fillPreviousCarerPersonalDetails(browser)
      Helper.fillPreviousCarerContactDetails(browser)
      browser.click("#actForPerson_yes")
      browser.click("#actAs option[value='guardian']")
      browser.click("#someoneElseActForPerson_yes")
      browser.click("#someoneElseActAs option[value='attorney']")
      browser.submit("button[type='submit']")
      browser.title() mustEqual "More about the care you provide - Care You Provide" // Landed on S4 G7
      browser.click("#backButton")
      browser.click("#backButton")
      browser.title() mustEqual "Contact Details Of The Person Who Claimed Before - Care You Provide" // Back to S4 G4
    }
   }
 }
