package integration.s4_care_you_provide

import org.specs2.mutable.{ Tags, Specification }
import play.api.test.WithBrowser
import integration.Helper

class G4PreviousCarerPersonalDetailsSpec extends Specification with Tags {

  "Previous Carer Personal Details" should {
    "navigate to Previous Carer Details, if anyone else claimed allowance for this person before" in new WithBrowser {
      Helper.fillMoreAboutThePersonWithClaimedAllowanceBefore(browser)
      browser.goTo("/careYouProvide/previousCarerPersonalDetails")
      browser.title() mustEqual "Details Of The Person Who Claimed Before - Care You Provide"
    }

    "navigate to Representatives For The Person, if nobody claimed allowance for this person before" in new WithBrowser {
      browser.goTo("/careYouProvide/previousCarerPersonalDetails")
      browser.title() mustEqual "Representatives For The Person - Care You Provide"
    }

    "navigate to Previous Carer Contact Details on submission of empty form" in new WithBrowser {
      Helper.fillMoreAboutThePersonWithClaimedAllowanceBefore(browser)
      browser.goTo("/careYouProvide/previousCarerPersonalDetails")
      browser.submit("button[type='submit']")
      browser.title() mustEqual "Contact Details Of The Person Who Claimed Before - Care You Provide"
    }
    
    "contain errors on invalid submission" in new WithBrowser {
      Helper.fillMoreAboutThePersonWithClaimedAllowanceBefore(browser)
      browser.goTo("/careYouProvide/previousCarerPersonalDetails")
      browser.fill("#nationalInsuranceNumber_ni1") `with` "12345"
      browser.submit("button[type='submit']")
      browser.find("div[class=validation-summary] ol li").size mustEqual 1
    }
    
    /*

     



     "navigate back to Their Contact Details" in new WithBrowser {
       pending("todo")
       Helper.fillTheirContactDetails(browser)
       browser.click("#backButton")
       browser.title() mustEqual "Their Contact Details - Care You Provide"
     }
     
     "contain the completed forms" in new WithBrowser {
       pending("todo")
       Helper.fillTheirPersonalDetails(browser)
       Helper.fillTheirContactDetails(browser)
       browser.find("div[class=completed] ul li").size() mustEqual 2
     }
     */

  }
}
