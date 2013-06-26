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

   }
 }
