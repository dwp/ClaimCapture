package integration.s4_care_you_provide

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import java.util.concurrent.TimeUnit

class G11BreaksInCareSpec extends Specification with Tags {
  class BreakWithBrowser extends WithBrowser {
    def break = {
      browser.click("#break_start_day option[value='1']")
      browser.click("#break_start_month option[value='1']")
      browser.fill("#break_start_year") `with` "2001"

      browser.click("#break_end_day option[value='1']")
      browser.click("#break_end_month option[value='1']")
      browser.fill("#break_end_year") `with` "2001"

      browser.click("#break_whereYou_location option[value='Holiday']")
      browser.click("#break_wherePerson_location option[value='Holiday']")

      browser.click("#moreBreaks_yes")

      browser.submit("button[value='next']")
    }
  }

  "Breaks in care" should {
    "be presented" in new WithBrowser {
      browser.goTo("/careYouProvide/breaksInCare")
      browser.title() mustEqual "Breaks in Care - Care You Provide"
    }

    """present "completed" when no more breaks are required""" in new WithBrowser {
      browser.goTo("/careYouProvide/breaksInCare")

      browser.click("#break_start_day option[value='1']")
      browser.click("#break_start_month option[value='1']")
      browser.fill("#break_start_year") `with` "2001"
      browser.click("#break_whereYou_location option[value='Holiday']")
      browser.click("#break_wherePerson_location option[value='Holiday']")
      browser.click("#moreBreaks_no")

      browser.submit("button[value='next']")

      browser.pageSource() must contain("Completed - Care You Provide")
    }

    """re-present when more breaks are required""" in new WithBrowser {
      browser.goTo("/careYouProvide/breaksInCare")
      browser.click("#moreBreaks_yes")
      browser.submit("button[value='next']")
      browser.title() mustEqual "Breaks in Care - Care You Provide"
    }

    """show 2 breaks in "break table" upon providing 2 break and choosing to "add" another break""" in new BreakWithBrowser {
      browser.goTo("/careYouProvide/breaksInCare")

      break
      break

      browser.$("#trips table tbody tr").size() mustEqual 2
    }

    "show zero breaks after creating one and then deleting" in new WithBrowser {
      browser.goTo("/careYouProvide/breaksInCare")

      browser.click("#break_start_day option[value='1']")
      browser.click("#break_start_month option[value='1']")
      browser.fill("#break_start_year") `with` "2001"
      browser.click("#break_whereYou_location option[value='Holiday']")
      browser.click("#break_wherePerson_location option[value='Holiday']")
      browser.click("#moreBreaks_yes")

      browser.submit("button[value='next']")
      browser.$("tbody tr").size() mustEqual 1

      browser.click("input[value='Delete']")
      browser.await().atMost(5, TimeUnit.SECONDS).until("tbody tr").hasSize(0)
    }

    "show two breaks after creating three and then deleting one" in new BreakWithBrowser {
      browser.goTo("/careYouProvide/breaksInCare")

      break
      break
      break

      browser.$("tbody tr").size() mustEqual 3

      browser.findFirst("tbody tr input[value='Delete']").click()
      browser.await().atMost(5, TimeUnit.SECONDS).until("tbody tr").hasSize(2)
    }

    "add two breaks and edit the second's start year" in new BreakWithBrowser {
      browser.goTo("/careYouProvide/breaksInCare")

      break
      break

      browser.findFirst("input[value='Edit']").click()
      browser.title() mustEqual "Break Edit - Care You Provide"
      browser.$("#start_year").getValue mustEqual 2001.toString

      browser.fill("#start_year") `with` "1999"
      browser.submit("button[type='submit']")

      browser.title() mustEqual "Breaks in Care - Care You Provide"
      browser.$("tbody tr").size() mustEqual 2
      browser.$("tbody").findFirst("tr").findFirst("td").getText must contain("1999")
    }
  } section "integration"
}