package controllers.s4_care_you_provide

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import java.util.concurrent.TimeUnit

class G11BreakIntegrationSpec extends Specification with Tags {
  class BreakWithBrowser extends WithBrowser {
    def break = {
      browser.click("#start_day option[value='1']")
      browser.click("#start_month option[value='1']")
      browser.fill("#start_year") `with` "2001"

      browser.click("#end_day option[value='1']")
      browser.click("#end_month option[value='1']")
      browser.fill("#end_year") `with` "2001"

      browser.click("#whereYou_location option[value='Holiday']")
      browser.click("#wherePerson_location option[value='Holiday']")

      browser.click("#medicalDuringBreak_no")

      browser.submit("button[value='next']")
    }
  }

  "Break" should {
    "be presented" in new WithBrowser {
      browser.goTo("/careYouProvide/break")
      browser.title() mustEqual "Break - Care You Provide"
    }

    """present "completed" when no more breaks are required""" in new WithBrowser {
      browser.goTo("/careYouProvide/breaksInCare")

      browser.click("#answer_no")
      browser.submit("button[value='next']")

      browser.pageSource() must contain("Completed - Care You Provide")
    }

    """give 2 errors when missing 2 mandatory fields of data - missing "start year" and "medical" """ in new WithBrowser {
      browser.goTo("/careYouProvide/breaksInCare")

      browser.click("#answer_yes")
      browser.submit("button[value='next']")

      browser.click("#start_day option[value='1']")
      browser.click("#start_month option[value='1']")

      browser.click("#whereYou_location option[value='Holiday']")
      browser.click("#wherePerson_location option[value='Holiday']")

      browser.submit("button[value='next']")

      browser.title() mustEqual "Break - Care You Provide"
      browser.find("div[class=validation-summary] ol li").size mustEqual 2
    }

    """show 2 breaks in "break table" upon providing 2 breaks""" in new BreakWithBrowser {
      browser.goTo("/careYouProvide/breaksInCare")

      browser.click("#answer_yes")
      browser.submit("button[value='next']")
      break

      browser.click("#answer_yes")
      browser.submit("button[value='next']")
      break

      browser.$("#trips table tbody tr").size() mustEqual 2
    }

    "show zero breaks after creating one and then deleting" in new BreakWithBrowser {
      browser.goTo("/careYouProvide/breaksInCare")

      browser.click("#answer_yes")
      browser.submit("button[value='next']")
      break
      browser.$("tbody tr").size() mustEqual 1

      browser.click("input[value='Delete']")
      browser.await().atMost(30, TimeUnit.SECONDS).until("tbody tr").hasSize(0)
    }

    "show two breaks after creating three and then deleting one" in new BreakWithBrowser {
      browser.goTo("/careYouProvide/breaksInCare")

      browser.click("#answer_yes")
      browser.submit("button[value='next']")
      break

      browser.click("#answer_yes")
      browser.submit("button[value='next']")
      break

      browser.click("#answer_yes")
      browser.submit("button[value='next']")
      break

      browser.$("tbody tr").size() mustEqual 3

      browser.findFirst("tbody tr input[value='Delete']").click()
      browser.await().atMost(30, TimeUnit.SECONDS).until("tbody tr").hasSize(2)
    }

    "add two breaks and edit the second's start year" in new BreakWithBrowser {
      browser.goTo("/careYouProvide/breaksInCare")

      browser.click("#answer_yes")
      browser.submit("button[value='next']")
      break

      browser.click("#answer_yes")
      browser.submit("button[value='next']")
      break

      browser.findFirst("input[value='Edit']").click()
      browser.title() mustEqual "Break - Care You Provide"
      browser.$("#start_year").getValue mustEqual 2001.toString

      browser.fill("#start_year") `with` "1999"
      browser.submit("button[type='submit']")

      browser.title() mustEqual "Breaks in Care - Care You Provide"
      browser.$("tbody tr").size() mustEqual 2
      browser.$("tbody").findFirst("tr").findFirst("td").getText must contain("1999")
    }
  } section "integration"
}