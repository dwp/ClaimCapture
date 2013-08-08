package controllers.s4_care_you_provide

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{BrowserMatchers, Formulate}

class G11BreakIntegrationSpec extends Specification with Tags {
  "Break" should {
    sequential

    "be presented" in new BreakWithBrowser {
      browser.goTo("/careYouProvide/break")
      titleMustEqual("Break - About the care you provide")
    }

    """present "completed" when no more breaks are required""" in new BreakWithBrowser {
      Formulate.theirPersonalDetails(browser)
      browser.goTo("/careYouProvide/breaksInCare")
      titleMustEqual("Breaks in care - About the care you provide")

      browser.click("#answer_no")
      browser.submit("button[value='next']")
      titleMustEqual("Completion - About the care you provide")
    }

    """give 2 errors when missing 2 mandatory fields of data - missing "start year" and "medical" """ in new BreakWithBrowser {
      browser.goTo("/careYouProvide/breaksInCare")
      browser.click("#answer_yes")
      browser.submit("button[value='next']")
      titleMustEqual("Break - About the care you provide")

      browser.click("#start_day option[value='1']")
      browser.click("#start_month option[value='1']")

      browser.click("#whereYou_location option[value='Hospital']")
      browser.click("#wherePerson_location option[value='Hospital']")

      browser.submit("button[value='next']")

      titleMustEqual("Break - About the care you provide")
      browser.find("div[class=validation-summary] ol li").size shouldEqual 2
    }

    """show 2 breaks in "break table" upon providing 2 breaks""" in new BreakWithBrowser {
      browser.goTo("/careYouProvide/breaksInCare")
      titleMustEqual("Breaks in care - About the care you provide")

      browser.click("#answer_yes")
      browser.submit("button[value='next']")
      titleMustEqual("Break - About the care you provide")
      break()

      browser.click("#answer_yes")
      browser.submit("button[value='next']")
      titleMustEqual("Break - About the care you provide")
      break()

      browser.$("#breaks table tbody tr").size() shouldEqual 2
    }

    "show zero breaks after creating one and then deleting" in new BreakWithBrowser {
      skipped("Front end dynamic assertions not working correctly.")

      /*browser.goTo("/careYouProvide/breaksInCare")
      titleMustEqual("Breaks in care - About the care you provide")

      browser.click("#answer_yes")
      browser.submit("button[value='next']")
      titleMustEqual("Break - About the care you provide")

      break()
      browser.$("tbody tr").size() mustEqual 1

      browser.click("input[value='Delete']")
      browser.await().atMost(10, TimeUnit.SECONDS).until(".breaks-prompt").areDisplayed
      browser.click("input[value='Yes']")

      browser.await().atMost(10, TimeUnit.SECONDS).until("tbody tr").hasSize(0)*/
    }

    "show two breaks after creating three and then deleting one" in new BreakWithBrowser {
      skipped("Front end dynamic assertions not working correctly.")

      /*browser.goTo("/careYouProvide/breaksInCare")

      browser.click("#answer_yes")
      browser.submit("button[value='next']")
      titleMustEqual("Break - About the care you provide")
      break()

      browser.click("#answer_yes")
      browser.submit("button[value='next']")
      titleMustEqual("Break - About the care you provide")
      break()

      browser.click("#answer_yes")
      browser.submit("button[value='next']")
      titleMustEqual("Break - About the care you provide")
      break()

      browser.$("tbody tr").size() mustEqual 3

      browser.findFirst("tbody tr input[value='Delete']").click()
      browser.await().atMost(30, TimeUnit.SECONDS).until(".breaks-prompt").areDisplayed
      browser.click("input[value='Yes']")

      browser.await().atMost(30, TimeUnit.SECONDS).until("tbody tr").hasSize(2)*/
    }

    "add two breaks and edit the second's start year" in new BreakWithBrowser {
      skipped("Ridiculous - Run this on its own and it's fine!")

      browser.goTo("/careYouProvide/breaksInCare")
      titleMustEqual("Breaks in care - About the care you provide")

      browser.click("#answer_yes")
      browser.submit("button[value='next']")
      titleMustEqual("Break - About the care you provide")
      break()

      browser.click("#answer_yes")
      browser.submit("button[value='next']")
      titleMustEqual("Break - About the care you provide")
      break()

      browser.findFirst("input[value='Edit']").click()
      titleMustEqual("Break - About the care you provide")
      browser.$("#start_year").getValue mustEqual 2001.toString

      browser.fill("#start_year") `with` "1999"
      browser.submit("button[type='submit']")
      titleMustEqual("Breaks in care - About the care you provide")

      browser.$("tbody tr").size() mustEqual 2
      browser.$("tbody").findFirst("tr").findFirst("td").getText must contain("1999")
    }
  } section("integration",models.domain.CareYouProvide.id)

  class BreakWithBrowser extends WithBrowser with BrowserMatchers {
    def break() {
      browser.click("#start_day option[value='1']")
      browser.click("#start_month option[value='1']")
      browser.fill("#start_year") `with` "2001"

      browser.click("#end_day option[value='1']")
      browser.click("#end_month option[value='1']")
      browser.fill("#end_year") `with` "2001"

      browser.click("#whereYou_location option[value='Hospital']")
      browser.click("#wherePerson_location option[value='Hospital']")

      browser.click("#medicalDuringBreak_no")

      browser.submit("button[value='next']")
      titleMustEqual("Breaks in care - About the care you provide")
    }
  }
}