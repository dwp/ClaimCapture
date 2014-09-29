package controllers.s4_care_you_provide

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{WithBrowserHelper, BrowserMatchers, Formulate}
import models.DayMonthYear
import java.util.concurrent.TimeUnit
import utils.pageobjects.s4_care_you_provide.{G11BreakPage, G10BreaksInCarePage}
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

      click("#answer_no")
      next
      titleMustEqual("Your course details - Education")
    }

    """give 2 errors when missing 2 mandatory fields of data - missing "start date" and "medical" """ in new WithBrowser with BreakFiller with WithBrowserHelper with BrowserMatchers {
      goTo("/care-you-provide/breaks-in-care")
      click("#answer_yes")
      next
      titleMustEqual("Break - About the care you provide")

      click("#start_day option[value='1']")
      click("#start_month option[value='1']")

      click("#whereYou_location option[value='Hospital']")
      click("#wherePerson_location option[value='Hospital']")

      next
      titleMustEqual("Break - About the care you provide")
      findAll("div[class=validation-summary] ol li").size shouldEqual 2
    }

    """show 2 breaks in "break table" upon providing 2 breaks""" in new WithBrowser with BreakFiller with WithBrowserHelper with BrowserMatchers {
      goTo("/care-you-provide/breaks-in-care")
      titleMustEqual(G10BreaksInCarePage.title)

      click("#answer_yes")
      next
      titleMustEqual("Break - About the care you provide")

      break()
      next
      titleMustEqual(G10BreaksInCarePage.title)

      click("#answer_yes")
      next
      titleMustEqual("Break - About the care you provide")

      break()
      next
      titleMustEqual(G10BreaksInCarePage.title)

      $("#breaks .data-table ul li").size() shouldEqual 2
    }

    "show zero breaks after creating one and then deleting" in new WithBrowser with BreakFiller with WithBrowserHelper with BrowserMatchers {
      skipped("HTMLUnit not handling dynamics/jquery")

      goTo("/care-you-provide/breaks-in-care")
      titleMustEqual(G10BreaksInCarePage.title)

      click("#answer_yes")
      next
      titleMustEqual("Break - About the care you provide")

      break()
      next
      titleMustEqual(G10BreaksInCarePage.title)
      $("ul li").size mustEqual 1

      click("input[value='Delete']")
      await().atMost(10, TimeUnit.SECONDS).until(".breaks-prompt").areDisplayed
      click("input[value='Yes']")

      await().atMost(10, TimeUnit.SECONDS).until("ul li").hasSize(0)
    }

    "show two breaks after creating three and then deleting one" in new WithBrowser with BreakFiller with WithBrowserHelper with BrowserMatchers {
      skipped("HTMLUnit not handling dynamics/jquery")

      goTo("/care-you-provide/breaks-in-care")

      click("#answer_yes")
      next
      titleMustEqual("Break - About the care you provide")

      break()
      next
      titleMustEqual(G10BreaksInCarePage.title)

      click("#answer_yes")
      next
      titleMustEqual("Break - About the care you provide")

      break()
      next
      titleMustEqual(G10BreaksInCarePage.title)

      click("#answer_yes")
      next
      titleMustEqual("Break - About the care you provide")

      break()
      next
      titleMustEqual(G10BreaksInCarePage.title)

      $("ul li").size() mustEqual 3

      findFirst("ul li input[value='Delete']").click()
      await().atMost(30, TimeUnit.SECONDS).until(".breaks-prompt").areDisplayed
      click("input[value='Yes']")

      await().atMost(30, TimeUnit.SECONDS).until("ul li").hasSize(2)
    }

    "add two breaks and edit the second's start year" in new WithBrowser with BreakFiller with WithBrowserHelper with BrowserMatchers {
      goTo("/care-you-provide/breaks-in-care")
      titleMustEqual(G10BreaksInCarePage.title)

      click("#answer_yes")
      next
      titleMustEqual("Break - About the care you provide")

      break()
      next
      titleMustEqual(G10BreaksInCarePage.title)

      click("#answer_yes")
      next
      titleMustEqual("Break - About the care you provide")

      break()
      next
      titleMustEqual(G10BreaksInCarePage.title)

      findFirst("input[value='Change']").click()

      titleMustEqual(G11BreakPage.title)
      $("#start_year").getValue mustEqual 2001.toString

      fill("#start_year") `with` "1999"
      next
      titleMustEqual(G10BreaksInCarePage.title)

      $("ul li").size() mustEqual 2
      $("ul").findFirst("li").findFirst("h3").getText shouldEqual "01/01/1999 to 01/01/2001"
    }

    """show "all options" for "Where was the person you care for during the break?".""" in new WithBrowser with WithBrowserHelper with BrowserMatchers {
      import scala.collection.JavaConverters._
      Formulate.theirPersonalDetails(browser)

      goTo("/care-you-provide/break")
      titleMustEqual("Break - About the care you provide")

      text("#wherePerson_location option").asScala should containAllOf(List("Please select", "Home", "Hospital", "Respite Care", "Care Home", "Nursing Home", "Other"))
    }
  } section("integration", models.domain.CareYouProvide.id)
}

trait BreakFiller {
  this: WithBrowser[_] with WithBrowserHelper =>

  def break(start: DayMonthYear = DayMonthYear(1, 1, 2001),
            end: DayMonthYear = DayMonthYear(1, 1, 2001),
            whereYouLocation: String = Home,
            wherePersonLocation: String = Hospital,
            medicalDuringBreak: Boolean = false) = {
    browser.fill(s"#start_day") `with` start.day.get.toString
    browser.fill(s"#start_month") `with` start.month.get.toString
    browser.fill("#start_year") `with` start.year.get.toString

    browser.fill(s"#end_day") `with` end.day.get.toString
    browser.fill(s"#end_month") `with` end.month.get.toString
    browser.fill("#end_year") `with` end.year.get.toString

    browser.click(s"#whereYou_location option[value='$whereYouLocation']")
    browser.click(s"#wherePerson_location option[value='$wherePersonLocation']")

    browser.click(s"""#medicalDuringBreak_${if (medicalDuringBreak) "yes" else "no"}""")
  }
}