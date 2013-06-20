package integration.s4_careYouProvide

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import integration.Helper

class G2TheirContactDetailsSpec extends Specification with Tags {

   "Their Contact Details" should {
//     "be presented" in new WithBrowser {
//
//       browser.goTo("/careYouProvide/theirContactDetails")
//       browser.title() mustEqual "Their Contact Details - Care You Provide"
//
//     }

     "be prepopulated if lives at same address" in new WithBrowser {
        Helper.fillContactDetails(browser)
        Helper.fillTheirDetails(browser)
        browser.title() mustEqual "Their Contact Details - Care You Provide"

//        println("Address: " + browser.find("#address-lineOne").getText)
        browser.find("#address-lineOne").getValue mustEqual("My Address")

     }

   }

 }
