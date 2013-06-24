package integration.s4_care_you_provide

import org.specs2.mutable.{Tags, Specification}
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

     "navigate back to Their Contact Details" in new WithBrowser {
       pending("Once G5 is done we will be able to do this")
     }



     "contain the completed forms" in new WithBrowser {
       Helper.fillRepresentativesForThePerson(browser)
       browser.find("div[class=completed] ul li").size() mustEqual 1
     }

   }
 }
