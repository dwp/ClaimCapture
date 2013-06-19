package integration.s4_CareYouProvide

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import integration.Helper

class G2TheirContactDetailsSpec extends Specification with Tags {

   "Their Contact Details" should {
     "be presented" in new WithBrowser {

       browser.goTo("/careYouProvide/theirContactDetails")
       browser.title() mustEqual "Their Contact Details - Care You Provide"

     }

            /*
     "contain errors on invalid submission" in new WithBrowser {
       browser.goTo("/careYouProvide/theirPersonalDetails")
       browser.title() mustEqual "Their Personal Details - Care You Provide"
       browser.submit("button[type='submit']")

       browser.find("div[class=validation-summary] ol li").size mustEqual 5
     }


     "navigate to next page on valid submission" in new WithBrowser {
       Helper.fillClaimDate(browser)
       Helper.fillMoreAboutYou(browser)
       browser.title() mustEqual "Their contact details"
     }  */
   }

 }
