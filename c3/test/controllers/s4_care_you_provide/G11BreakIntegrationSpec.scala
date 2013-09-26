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
      titleMustEqual("Completion - About the care you provide")
    }

    """give 2 errors when missing 3 mandatory fields of data - missing "start date" and "medical" """ in new WithBrowser with BreakFiller with WithBrowserHelper with BrowserMatchers {
      goTo("/care-you-provide/breaks-in-care")
      click("#answer_yes")
      next
      titleMustEqual("Break - About the care you provide")

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

      $("#breaks table tbody tr").size() shouldEqual 2
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
      $("tbody tr").size mustEqual 1

      click("input[value='Delete']")
      await().atMost(10, TimeUnit.SECONDS).until(".breaks-prompt").areDisplayed
      click("input[value='Yes']")

      await().atMost(10, TimeUnit.SECONDS).until("tbody tr").hasSize(0)
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

      $("tbody tr").size() mustEqual 3

      findFirst("tbody tr input[value='Delete']").click()
      await().atMost(30, TimeUnit.SECONDS).until(".breaks-prompt").areDisplayed
      click("input[value='Yes']")

      await().atMost(30, TimeUnit.SECONDS).until("tbody tr").hasSize(2)
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
      $("#start_date").getValue shouldEqual "01 January, 2001"

      fill("#start_date") `with` "01 January, 1999"
      next
      titleMustEqual(G10BreaksInCarePage.title)

      $("tbody tr").size() mustEqual 2
      $("tbody").findFirst("tr").findFirst("td").getText shouldEqual "01/01/1999 to 01/01/2001"
    }

    """show "all options" for "Where was the person you care for during the break?".""" in new WithBrowser with WithBrowserHelper with BrowserMatchers {
      import scala.collection.JavaConverters._

      goTo("/care-you-provide/break")
      titleMustEqual("Break - About the care you provide")

      text("#wherePerson_location option").asScala should containAllOf(List("Please select", "At Home", "Hospital", "Respite Care", "Care Home", "Nursing Home", "Other"))
    }
  } section("integration", models.domain.CareYouProvide.id)
}

trait BreakFiller {
  this: WithBrowser[_] with WithBrowserHelper =>

  def break(start: DayMonthYear = DayMonthYear(1, 1, 2001).withTime(9, 45),
            end: DayMonthYear = DayMonthYear(1, 1, 2001).withTime(9, 45),
            whereYouLocation: String = AtHome,
            wherePersonLocation: String = Hospital,
            medicalDuringBreak: Boolean = false) = {
    import language.implicitConversions

    implicit def optionIntToInt(i: Option[Int]) = i.getOrElse(0)

    def minutesValue(m: Int): String = {
      if ((0 to 14).contains(m)) "00"
      else if ((15 to 29).contains(m)) "15"
      else if ((30 to 44).contains(m)) "30"
      else "45"
    }

    fill("#start_date") `with` start.`dd month, yyyy`
    click(s"#start_hour option[value='${start.hour.getOrElse(0)}']")
    click(s"#start_minutes option[value='${minutesValue(start.minutes)}']")

    fill("#end_date") `with` end.`dd month, yyyy`
    click(s"#end_hour option[value='${end.hour.getOrElse(0)}']")
    click(s"#end_minutes option[value='${minutesValue(end.minutes)}']")

    click(s"#whereYou_location option[value='$whereYouLocation']")
    click(s"#wherePerson_location option[value='$wherePersonLocation']")

    click(s"""#medicalDuringBreak_${if (medicalDuringBreak) "yes" else "no"}""")
  }
}