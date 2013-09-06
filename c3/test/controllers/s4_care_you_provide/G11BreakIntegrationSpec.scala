package controllers.s4_care_you_provide

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{WithBrowserHelper, BrowserMatchers, Formulate}
import models.DayMonthYear
import java.util.concurrent.TimeUnit
import utils.pageobjects.s4_care_you_provide.G10BreaksInCarePage
import app.Whereabouts._

class G11BreakIntegrationSpec extends Specification with Tags {

  "Break" should {
    "be presented" in new WithBrowser with BreakFiller with WithBrowserHelper with BrowserMatchers {
      goTo("/care-you-provide/break")
      titleMustEqual("Break - About the care you provide")
    }

    """present "completed" when no more breaks are required""" in new WithBrowser with BreakFiller with WithBrowserHelper with BrowserMatchers {
      Formulate.theirPersonalDetails(browser)
      goTo("/care-you-provide/breaks-in-care")
      titleMustEqual(G10BreaksInCarePage.title)

      browser.click("#answer_no")
      next
      titleMustEqual("Completion - About the care you provide")
    }

    """give 2 errors when missing 2 mandatory fields of data - missing "start year" and "medical" """ in new WithBrowser with BreakFiller with WithBrowserHelper with BrowserMatchers {
      goTo("/care-you-provide/breaks-in-care")
      browser.click("#answer_yes")
      next
      titleMustEqual("Break - About the care you provide")

      browser.click("#start_day option[value='1']")
      browser.click("#start_month option[value='1']")

      browser.click("#whereYou_location option[value='Hospital']")
      browser.click("#wherePerson_location option[value='Hospital']")

      next
      titleMustEqual("Break - About the care you provide")
      browser.find("div[class=validation-summary] ol li").size shouldEqual 2
    }

    """show 2 breaks in "break table" upon providing 2 breaks""" in new WithBrowser with BreakFiller with WithBrowserHelper with BrowserMatchers {
      goTo("/care-you-provide/breaks-in-care")
      titleMustEqual(G10BreaksInCarePage.title)

      browser.click("#answer_yes")
      next
      titleMustEqual("Break - About the care you provide")

      break()
      next
      titleMustEqual(G10BreaksInCarePage.title)

      browser.click("#answer_yes")
      next
      titleMustEqual("Break - About the care you provide")

      break()
      next
      titleMustEqual(G10BreaksInCarePage.title)

      browser.$("#breaks table tbody tr").size() shouldEqual 2
    }

    "show zero breaks after creating one and then deleting" in new WithBrowser with BreakFiller with WithBrowserHelper with BrowserMatchers {
      skipped("HTMLUnit not handling dynamics/jquery")

      goTo("/care-you-provide/breaks-in-care")
      titleMustEqual(G10BreaksInCarePage.title)

      browser.click("#answer_yes")
      next
      titleMustEqual("Break - About the care you provide")

      break()
      next
      titleMustEqual(G10BreaksInCarePage.title)
      browser.$("tbody tr").size mustEqual 1

      browser.click("input[value='Delete']")
      browser.await().atMost(10, TimeUnit.SECONDS).until(".breaks-prompt").areDisplayed
      browser.click("input[value='Yes']")

      browser.await().atMost(10, TimeUnit.SECONDS).until("tbody tr").hasSize(0)
    }

    "show two breaks after creating three and then deleting one" in new WithBrowser with BreakFiller with WithBrowserHelper with BrowserMatchers {
      skipped("HTMLUnit not handling dynamics/jquery")

      goTo("/care-you-provide/breaks-in-care")

      browser.click("#answer_yes")
      next
      titleMustEqual("Break - About the care you provide")

      break()
      next
      titleMustEqual(G10BreaksInCarePage.title)

      browser.click("#answer_yes")
      next
      titleMustEqual("Break - About the care you provide")

      break()
      next
      titleMustEqual(G10BreaksInCarePage.title)

      browser.click("#answer_yes")
      next
      titleMustEqual("Break - About the care you provide")

      break()
      next
      titleMustEqual(G10BreaksInCarePage.title)

      browser.$("tbody tr").size() mustEqual 3

      browser.findFirst("tbody tr input[value='Delete']").click()
      browser.await().atMost(30, TimeUnit.SECONDS).until(".breaks-prompt").areDisplayed
      browser.click("input[value='Yes']")

      browser.await().atMost(30, TimeUnit.SECONDS).until("tbody tr").hasSize(2)
    }

    "add two breaks and edit the second's start year" in new WithBrowser with BreakFiller with WithBrowserHelper with BrowserMatchers {
      goTo("/care-you-provide/breaks-in-care")
      titleMustEqual(G10BreaksInCarePage.title)

      browser.click("#answer_yes")
      next
      titleMustEqual("Break - About the care you provide")

      break()
      next
      titleMustEqual(G10BreaksInCarePage.title)

      browser.click("#answer_yes")
      next
      titleMustEqual("Break - About the care you provide")

      break()
      next
      titleMustEqual(G10BreaksInCarePage.title)

      browser.findFirst("input[value='Change']").click()
      titleMustEqual("Break - About the care you provide")
      browser.$("#start_year").getValue mustEqual 2001.toString

      browser.fill("#start_year") `with` "1999"
      browser.submit("button[type='submit']")
      titleMustEqual(G10BreaksInCarePage.title)

      browser.$("tbody tr").size() mustEqual 2
      browser.$("tbody").findFirst("tr").findFirst("td").getText must contain("1999")
    }
  } section("integration", models.domain.CareYouProvide.id)
}

trait BreakFiller {
  this: WithBrowser[_] =>

  def break(start: DayMonthYear = DayMonthYear(1, 1, 2001),
            end: DayMonthYear = DayMonthYear(1, 1, 2001),
            whereYouLocation: String = AtHome,
            wherePersonLocation: String = Hospital,
            medicalDuringBreak: Boolean = false) = {
    browser.click(s"#start_day option[value='${start.day.get}']")
    browser.click(s"#start_month option[value='${start.month.get}']")
    browser.fill("#start_year") `with` s"${start.year.get}"

    browser.click(s"#end_day option[value='${end.day.get}']")
    browser.click(s"#end_month option[value='${end.month.get}']")
    browser.fill("#end_year") `with` s"${end.year.get}"

    browser.click(s"#whereYou_location option[value='$whereYouLocation']")
    browser.click(s"#wherePerson_location option[value='$wherePersonLocation']")

    browser.click(s"""#medicalDuringBreak_${if (medicalDuringBreak) "yes" else "no"}""")
  }
}