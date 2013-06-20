package integration.s4_careYouProvide

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser

class G11BreaksInCare extends Specification with Tags {
   "Breaks in care" should {
     "be presented" in new WithBrowser {
       browser.goTo("/careYouProvide/breaksInCare")
       browser.title() mustEqual "Breaks in Care - Care You Provide"
     }

     """present "completed" when no more breaks are required""" in new WithBrowser {
       browser.goTo("/careYouProvide/breaksInCare")
       browser.click("#moreBreaks_no")
       browser.submit("button[value='next']")
       browser.pageSource() must contain("Completed - Care You Provide")
     }

     """represent when more breaks are required""" in new WithBrowser {
       browser.goTo("/careYouProvide/breaksInCare")
       browser.click("#moreBreaks_yes")
       browser.submit("button[value='next']")
       browser.title() mustEqual "Breaks in Care - Care You Provide"
     }
   } section "integration"
 }