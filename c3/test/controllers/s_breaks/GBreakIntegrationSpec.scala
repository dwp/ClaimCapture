package controllers.s_breaks

import app.CircsBreaksWhereabouts._
import controllers.{BrowserMatchers, ClaimScenarioFactory, Formulate, WithBrowserHelper}
import models.DayMonthYear
import org.specs2.mutable._
import play.api.Logger
import utils.{WithBrowsers, WithBrowser, WithJsBrowser}
import utils.pageobjects.PageObjects
import utils.pageobjects.s_breaks.{GBreaksInCarePage, GBreakPage}
import utils.pageobjects.s_breaks.GBreaksInCarePage
import utils.pageobjects.s_claim_date.GClaimDatePage
import utils.pageobjects.s_education.GYourCourseDetailsPage

class GBreakIntegrationSpec extends Specification {
  "Break" should {
    "be presented" in new WithJsBrowser with BreakFiller with WithBrowserHelper with BrowserMatchers {
      goTo(GBreaksInCarePage.url)
      urlMustEqual(GBreaksInCarePage.url)
    }

    """present "completed" when no more breaks are required""" in new WithJsBrowser  with BreakFiller with WithBrowserHelper with BrowserMatchers {
      Formulate.theirPersonalDetails(browser)
      goTo(GBreaksInCarePage.url)
      urlMustEqual(GBreaksInCarePage.url)

      click("#answer_no")
      next
      urlMustEqual(GYourCourseDetailsPage.url)
    }

    "display dynamic question text if user answered that they did NOT care for this person for 35 hours or more each week before your claim date" in new WithBrowser with PageObjects{
      val breaksInCare = GClaimDatePage(context) goToThePage() runClaimWith(ClaimScenarioFactory.s4CareYouProvideWithBreaksInCare(false),GBreakPage.url)
      breaksInCare.source contains "About the break from care since 10 October 2016" should beTrue
    }

    "display dynamic question text if user answered that they care for this person for 35 hours or more each week before your claim date (within 6 months)" in new WithBrowser with PageObjects{
      val claim = ClaimScenarioFactory.s4CareYouProvideWithBreaksInCare(true)
      claim.ClaimDateWhenDidYouStartToCareForThisPerson = "10/08/2016"
      val breaksInCare = GClaimDatePage(context) goToThePage() runClaimWith(claim,GBreakPage.url)
      breaksInCare.source contains "About the break from care since 10 August 2016" should beTrue
    }

    "display dynamic question text if user answered that they care for this person for 35 hours or more each week before your claim date (more than 6 months)" in new WithBrowser with PageObjects{
      val claim = ClaimScenarioFactory.s4CareYouProvideWithBreaksInCare(true)
      claim.ClaimDateWhenDidYouStartToCareForThisPerson = "10/02/2016"
      val breaksInCare = GClaimDatePage(context) goToThePage() runClaimWith(claim,GBreakPage.url)
      breaksInCare.source contains "About the break from care since 10 April 2016" should beTrue
    }

    """give 2 errors when missing 2 mandatory fields of data - missing "start date" and "medical" """ in new WithJsBrowser  with BreakFiller with WithBrowserHelper with BrowserMatchers {
      goTo(GBreaksInCarePage.url)
      click("#answer_yes")
      next
      Logger.info("spec" + browser.url)
      urlMustEqual(GBreakPage.url)

      fill("#start_day").`with`("1")
      fill("#start_month").`with`("1")

      click("#whereYou_answer_In_hospital")
      click("#wherePerson_answer_In_hospital")

      click("#hasBreakEnded_answer_no")

      next
      Logger.info("spec" + browser.url)
      urlMustEqual(GBreakPage.url)
      findAll("div[class=validation-summary] ol li").size shouldEqual 2
    }

    """show 2 breaks in "break table" upon providing 2 breaks""" in new WithJsBrowser  with BreakFiller with WithBrowserHelper with BrowserMatchers {
      goTo(GBreaksInCarePage.url)
      urlMustEqual(GBreaksInCarePage.url)

      click("#answer_yes")
      next
      urlMustEqual(GBreakPage.url)

      break()
      next
      urlMustEqual(GBreaksInCarePage.url)

      click("#answer_yes")
      next
      urlMustEqual(GBreakPage.url)

      break()
      next
      urlMustEqual(GBreaksInCarePage.url)

      $("#breaks .data-table ul li").size() shouldEqual 2
    }

    "add two breaks and edit the second's start year" in new WithJsBrowser  with BreakFiller with WithBrowserHelper with BrowserMatchers {
      goTo(GBreaksInCarePage.url)
      urlMustEqual(GBreaksInCarePage.url)

      click("#answer_yes")
      next
      urlMustEqual(GBreakPage.url)

      break()
      next
      urlMustEqual(GBreaksInCarePage.url)

      click("#answer_yes")
      next
      urlMustEqual(GBreakPage.url)

      break()
      next
      urlMustEqual(GBreaksInCarePage.url)

      findFirst("input[value='Change']").click()

      urlMustEqual(GBreakPage.url)
      $("#start_year").getValue mustEqual 2001.toString

      fill("#start_year") `with` "1999"
      next
      urlMustEqual(GBreaksInCarePage.url)

      $("ul.break-data li").size() mustEqual 2
      $("ul.break-data").findFirst("li").findFirst("h3").getText shouldEqual "01/01/1999 to 01/01/2001"
    }

    """show "all options" for "Where was the person you care for during the break?".""" in new WithJsBrowser  with WithBrowserHelper with BrowserMatchers {
      import scala.collection.JavaConverters._
      Formulate.theirPersonalDetails(browser)

      goTo(s"${GBreakPage.url}/1")
      urlMustEqual(GBreakPage.url)

      text("#wherePerson_answer li").asScala should containAllOf(List(Home, Hospital, Holiday, RespiteCare, SomewhereElse).map(e => e.toLowerCase))
    }
  }
  section("integration", models.domain.CareYouProvide.id)
}

trait BreakFiller {
  this: WithBrowsers[_] with WithBrowserHelper =>

  def break(start: DayMonthYear = DayMonthYear(1, 1, 2001),
            end: DayMonthYear = DayMonthYear(1, 1, 2001),
            whereYouLocation: String = Home.replace(" ","_"),
            wherePersonLocation: String = Hospital.replace(" ","_"),
            medicalDuringBreak: Boolean = false) = {
    browser.fill(s"#start_day") `with` start.day.get.toString
    browser.fill(s"#start_month") `with` start.month.get.toString
    browser.fill("#start_year") `with` start.year.get.toString

    browser.click("#hasBreakEnded_answer_yes")
    browser.fill(s"#hasBreakEnded_date_day") `with` end.day.get.toString
    browser.fill(s"#hasBreakEnded_date_month") `with` end.month.get.toString
    browser.fill("#hasBreakEnded_date_year") `with` end.year.get.toString

    browser.click(s"#whereYou_answer_$whereYouLocation")
    browser.click(s"#wherePerson_answer_$wherePersonLocation")

    browser.click(s"""#medicalDuringBreak_${if (medicalDuringBreak) "yes" else "no"}""")
  }
}
