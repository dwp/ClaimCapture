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
       browser.click("#break_start_day option[value='1']")
       browser.click("#break_start_month option[value='1']")
       browser.fill("#break_start_year") `with` "2001"
       browser.click("#break_whereYou_location option[value='Holiday']")
       browser.click("#break_wherePerson_location option[value='Holiday']")
       browser.submit("button[value='next']")
       browser.pageSource() must contain("Completed - Care You Provide")
     }

     """re-present when more breaks are required""" in new WithBrowser {
       browser.goTo("/careYouProvide/breaksInCare")
       browser.click("#moreBreaks_yes")
       browser.submit("button[value='next']")
       browser.title() mustEqual "Breaks in Care - Care You Provide"
     }

     """show 2 breaks in "break table" upon providing 2 break and choosing to "add" another break""" in new WithBrowser {
       browser.goTo("/careYouProvide/breaksInCare")

       def addBreak = {
         browser.click("#break_start_day option[value='1']")
         browser.click("#break_start_month option[value='1']")
         browser.fill("#break_start_year") `with` "2001"

         browser.click("#break_end_day option[value='1']")
         browser.click("#break_end_month option[value='1']")
         browser.fill("#break_end_year") `with` "2001"

         browser.click("#moreBreaks_yes")
         browser.submit("button[value='next']")
       }

       addBreak
       addBreak

       browser.$("#trips table tbody tr").size() mustEqual 2
     }
   } section "integration"
 }