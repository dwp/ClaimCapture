package controllers.s5_time_spent_abroad

import org.specs2.mutable.{Specification, Tags}
import play.api.test.WithBrowser
import controllers.BrowserMatchers

class G4TripIntegrationSpec extends Specification with Tags {
  "4 weeks trip" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      browser.goTo("/timeSpentAbroad/trip/4Weeks")
      titleMustEqual("Trip - Time Spent Abroad")
    }

    "be submitted with all mandatory data" in new WithBrowser with BrowserMatchers {
      browser.goTo("/timeSpentAbroad/trip/4Weeks")

      browser.click("#start_day option[value='1']")
      browser.click("#start_month option[value='1']")
      browser.fill("#start_year") `with` "2000"

      browser.click("#end_day option[value='1']")
      browser.click("#end_month option[value='1']")
      browser.fill("#end_year") `with` "2000"

      browser.fill("#where") `with` "Scotland"

      browser.submit("button[value='next']")
      titleMustEqual("Abroad for more than 4 weeks - Time Spent Abroad")
    }

    """give 2 errors when missing 2 mandatory fields of data - missing "start year" and "where".""" in new WithBrowser with BrowserMatchers {
      browser.goTo("/timeSpentAbroad/trip/4Weeks")

      browser.click("#start_day option[value='1']")
      browser.click("#start_month option[value='1']")

      browser.click("#end_day option[value='1']")
      browser.click("#end_month option[value='1']")
      browser.fill("#end_year") `with` "2000"

      browser.submit("button[value='next']")
      titleMustEqual("Trip - Time Spent Abroad")
      browser.find("div[class=validation-summary] ol li").size mustEqual 2
    }
  }
}


/*
import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import java.util.concurrent.TimeUnit
import controllers.FormHelper

class G11BreakIntegrationSpec extends Specification with Tags {

  class BreakWithBrowser extends WithBrowser {
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
      titleMustEqual("Breaks in Care - Care You Provide")
    }

    def titleMustEqual(title: String)(implicit seconds: Int = 30) = {
      browser.waitUntil[Boolean](seconds, TimeUnit.SECONDS) {
        browser.title mustEqual title
      }
    }
  }

  "Break" should {

    """show 2 breaks in "break table" upon providing 2 breaks""" in new BreakWithBrowser {
      browser.goTo("/careYouProvide/breaksInCare")

      browser.click("#answer_yes")
      browser.submit("button[value='next']")
      titleMustEqual("Break - Care You Provide")
      break()

      browser.click("#answer_yes")
      browser.submit("button[value='next']")
      titleMustEqual("Break - Care You Provide")
      break()

      browser.$("#breaks table tbody tr").size() mustEqual 2
    }

    "show zero breaks after creating one and then deleting" in new BreakWithBrowser {
      browser.goTo("/careYouProvide/breaksInCare")

      browser.click("#answer_yes")
      browser.submit("button[value='next']")
      titleMustEqual("Break - Care You Provide")
      break()
      browser.$("tbody tr").size() mustEqual 1

      browser.click("input[value='Delete']")
      browser.await().atMost(30, TimeUnit.SECONDS).until(".breaks-prompt").areDisplayed
      browser.click("input[value='Yes']")

      browser.await().atMost(3, TimeUnit.SECONDS).until("tbody tr").hasSize(0)
    }.pendingUntilFixed("Above assert used to work!!!")

    "show two breaks after creating three and then deleting one" in new BreakWithBrowser {
      browser.goTo("/careYouProvide/breaksInCare")

      browser.click("#answer_yes")
      browser.submit("button[value='next']")
      titleMustEqual("Break - Care You Provide")
      break()

      browser.click("#answer_yes")
      browser.submit("button[value='next']")
      titleMustEqual("Break - Care You Provide")
      break()

      browser.click("#answer_yes")
      browser.submit("button[value='next']")
      titleMustEqual("Break - Care You Provide")
      break()

      browser.$("tbody tr").size() mustEqual 3

      browser.findFirst("tbody tr input[value='Delete']").click()
      browser.await().atMost(30, TimeUnit.SECONDS).until(".breaks-prompt").areDisplayed
      browser.click("input[value='Yes']")

      browser.await().atMost(30, TimeUnit.SECONDS).until("tbody tr").hasSize(2)
    }.pendingUntilFixed("Above assert used to work!!!")

    "add two breaks and edit the second's start year" in new BreakWithBrowser {
      browser.goTo("/careYouProvide/breaksInCare")

      browser.click("#answer_yes")
      browser.submit("button[value='next']")
      titleMustEqual("Break - Care You Provide")
      break()
      titleMustEqual("Breaks in Care - Care You Provide")

      browser.click("#answer_yes")
      browser.submit("button[value='next']")
      titleMustEqual("Break - Care You Provide")
      break()
      titleMustEqual("Breaks in Care - Care You Provide")

      browser.findFirst("input[value='Edit']").click()
      titleMustEqual("Break - Care You Provide")
      browser.$("#start_year").getValue mustEqual 2001.toString

      browser.fill("#start_year") `with` "1999"
      browser.submit("button[type='submit']")
      titleMustEqual("Breaks in Care - Care You Provide")

      browser.$("tbody tr").size() mustEqual 2
      browser.$("tbody").findFirst("tr").findFirst("td").getText must contain("1999")
    }
  } section "integration"
}
*/