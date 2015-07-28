package controllers.s4_care_you_provide

import org.specs2.mutable.{Tags, Specification}
import play.api.Logger
import controllers.{ClaimScenarioFactory, WithBrowserHelper, BrowserMatchers, Formulate}
import models.DayMonthYear
import java.util.concurrent.TimeUnit
import play.api.test.WithBrowser
import utils.pageobjects.PageObjects
import utils.pageobjects.s1_2_claim_date.G1ClaimDatePage
import utils.pageobjects.s4_care_you_provide.{G11BreakPage, G10BreaksInCarePage}
import app.CircsBreaksWhereabouts._
import utils.pageobjects.s6_education.G1YourCourseDetailsPage
import utils.WithJsBrowser

class G11BreakIntegrationSpec extends Specification with Tags {
  "Break" should {
    "be presented" in new WithJsBrowser with BreakFiller with WithBrowserHelper with BrowserMatchers {
      goTo(G10BreaksInCarePage.url)
      urlMustEqual(G10BreaksInCarePage.url)
    }

    """present "completed" when no more breaks are required""" in new WithJsBrowser  with BreakFiller with WithBrowserHelper with BrowserMatchers {
      Formulate.theirPersonalDetails(browser)
      goTo(G10BreaksInCarePage.url)
      urlMustEqual(G10BreaksInCarePage.url)

      click("#answer_no")
      next
      urlMustEqual(G1YourCourseDetailsPage.url)
    }

    "display dynamic question text if user answered that they did NOT care for this person for 35 hours or more each week before your claim date" in new WithBrowser with PageObjects{
      val breaksInCare = G1ClaimDatePage(context) goToThePage() runClaimWith(ClaimScenarioFactory.s4CareYouProvideWithBreaksInCare(false),G11BreakPage.url)
      breaksInCare.source contains "About the break from care since 10 October 2016" should beTrue
    }

    "display dynamic question text if user answered that they care for this person for 35 hours or more each week before your claim date (within 6 months)" in new WithBrowser with PageObjects{
      val claim = ClaimScenarioFactory.s4CareYouProvideWithBreaksInCare(true)
      claim.ClaimDateWhenDidYouStartToCareForThisPerson = "10/08/2016"
      val breaksInCare = G1ClaimDatePage(context) goToThePage() runClaimWith(claim,G11BreakPage.url)
      breaksInCare.source contains "About the break from care since 10 August 2016" should beTrue
    }

    "display dynamic question text if user answered that they care for this person for 35 hours or more each week before your claim date (more than 6 months)" in new WithBrowser with PageObjects{
      val claim = ClaimScenarioFactory.s4CareYouProvideWithBreaksInCare(true)
      claim.ClaimDateWhenDidYouStartToCareForThisPerson = "10/02/2016"
      val breaksInCare = G1ClaimDatePage(context) goToThePage() runClaimWith(claim,G11BreakPage.url)
      breaksInCare.source contains "About the break from care since 10 April 2016" should beTrue
    }

    """give 2 errors when missing 2 mandatory fields of data - missing "start date" and "medical" """ in new WithJsBrowser  with BreakFiller with WithBrowserHelper with BrowserMatchers {
      goTo(G10BreaksInCarePage.url)
      click("#answer_yes")
      next
      Logger.info("spec" + browser.url)
      urlMustEqual(G11BreakPage.url)

      fill("#start_day").`with`("1")
      fill("#start_month").`with`("1")

      click("#whereYou_answer_In_hospital")
      click("#wherePerson_answer_In_hospital")

      click("#hasBreakEnded_answer_no")

      next
      Logger.info("spec" + browser.url)
      urlMustEqual(G11BreakPage.url)
      findAll("div[class=validation-summary] ol li").size shouldEqual 2
    }

    """show 2 breaks in "break table" upon providing 2 breaks""" in new WithJsBrowser  with BreakFiller with WithBrowserHelper with BrowserMatchers {
      goTo(G10BreaksInCarePage.url)
      urlMustEqual(G10BreaksInCarePage.url)

      click("#answer_yes")
      next
      urlMustEqual(G11BreakPage.url)

      break()
      next
      urlMustEqual(G10BreaksInCarePage.url)

      click("#answer_yes")
      next
      urlMustEqual(G11BreakPage.url)

      break()
      next
      urlMustEqual(G10BreaksInCarePage.url)

      $("#breaks .data-table ul li").size() shouldEqual 2
    }

    "add two breaks and edit the second's start year" in new WithJsBrowser  with BreakFiller with WithBrowserHelper with BrowserMatchers {
      goTo(G10BreaksInCarePage.url)
      urlMustEqual(G10BreaksInCarePage.url)

      click("#answer_yes")
      next
      urlMustEqual(G11BreakPage.url)

      break()
      next
      urlMustEqual(G10BreaksInCarePage.url)

      click("#answer_yes")
      next
      urlMustEqual(G11BreakPage.url)

      break()
      next
      urlMustEqual(G10BreaksInCarePage.url)

      findFirst("input[value='Change']").click()

      urlMustEqual(G11BreakPage.url)
      $("#start_year").getValue mustEqual 2001.toString

      fill("#start_year") `with` "1999"
      next
      urlMustEqual(G10BreaksInCarePage.url)

      $("ul li").size() mustEqual 2
      $("ul").findFirst("li").findFirst("h3").getText shouldEqual "01/01/1999 to 01/01/2001"
    }

    """show "all options" for "Where was the person you care for during the break?".""" in new WithJsBrowser  with WithBrowserHelper with BrowserMatchers {
      import scala.collection.JavaConverters._
      Formulate.theirPersonalDetails(browser)

      goTo(s"${G11BreakPage.url}/1")
      urlMustEqual(G11BreakPage.url)

      text("#wherePerson_answer li").asScala should containAllOf(List(Home, Hospital, Holiday, RespiteCare, SomewhereElse).map(e => e.toLowerCase))
    }
  } section("integration", models.domain.CareYouProvide.id)
}

trait BreakFiller {
  this: WithBrowser[_] with WithBrowserHelper =>

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