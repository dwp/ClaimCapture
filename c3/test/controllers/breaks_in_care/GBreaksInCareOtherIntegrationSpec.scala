package controllers.breaks_in_care

import app.BreaksInCareOtherOptions
import controllers.mappings.Mappings
import controllers.{ClaimScenarioFactory, BrowserMatchers, WithBrowserHelper}
import models.DayMonthYear
import org.specs2.mutable._
import utils.pageobjects._
import utils.pageobjects.breaks_in_care.{GBreaksInCareSummaryPage, GBreaksInCareOtherPage}
import utils.pageobjects.s_claim_date.GClaimDatePage
import utils.{WithBrowsers, WithBrowser, WithJsBrowser}

class GBreaksInCareOtherIntegrationSpec extends Specification {
  section("integration", models.domain.Breaks.id)
  "Break" should {
    "be presented" in new WithJsBrowser with BreakFillerOther with WithBrowserHelper with BrowserMatchers {
      goTo(GBreaksInCareOtherPage.url )
      urlMustEqual(GBreaksInCareOtherPage.url)
    }

    "display hospital text" in new WithBrowser with PageObjects {
      val breaksInCare = GClaimDatePage(context) goToThePage() runClaimWith(ClaimScenarioFactory.careYouProvideWithBreaksInCareOther(false), GBreaksInCareOtherPage.url + "/1")

      breaksInCare.source contains "You told us that there was a time you weren't able to care for Tom Wilson for 35 hours a week. We need these details." should beTrue
    }

    "give errors when missing mandatory fields of data" in new WithJsBrowser with BreakFillerOther with WithBrowserHelper with BrowserMatchers {
      goTo(GBreaksInCareOtherPage.url + "/1")
      next
      urlMustEqual(GBreaksInCareOtherPage.url)

      findAll("div[class=validation-summary] ol li").size shouldEqual 2
    }

    """show 2 breaks in "break table" upon providing 2 breaks""" in new WithJsBrowser with BreakFillerOther with WithBrowserHelper with BrowserMatchers {
        goTo(GBreaksInCareOtherPage.url + "/1")
        urlMustEqual(GBreaksInCareOtherPage.url)
        break()
        next

        urlMustEqual(GBreaksInCareSummaryPage.url) //will need to go to summary

        goTo(GBreaksInCareOtherPage.url + "/2")
        urlMustEqual(GBreaksInCareOtherPage.url)
        break()
        next

        $("#summary-table .data-table").size() shouldEqual 2 //check for number of breaks in summary
      }

      "add two breaks and edit the firsts start year" in new WithJsBrowser with BreakFillerOther with WithBrowserHelper with BrowserMatchers {
        goTo(GBreaksInCareOtherPage.url + "/1")
        urlMustEqual(GBreaksInCareOtherPage.url)
        break()
        next
        urlMustEqual(GBreaksInCareSummaryPage.url) //will need to go to summary

        goTo(GBreaksInCareOtherPage.url + "/2")
        urlMustEqual(GBreaksInCareOtherPage.url)
        break()
        next

        findFirst("input[value='Change']").click()

        urlMustEqual(GBreaksInCareOtherPage.url)
        $("#caringEnded_date_year").getValue mustEqual 2016.toString
        $("#caringStarted_date_year").getValue mustEqual 2016.toString
        fill("#caringEnded_date_year") `with` "2013"
        fill("#caringStarted_date_year") `with` "2014"

        next
        urlMustEqual(GBreaksInCareSummaryPage.url) //will need to go to summary

        $("#summary-table tr.data-table").size() shouldEqual 2
        $("#summary-table tr.data-table").getText shouldEqual "YouOther04 January 201304 January 2014"
      }

    "add two breaks and edit the second's start year" in new WithJsBrowser with BreakFillerOther with WithBrowserHelper with BrowserMatchers {
      goTo(GBreaksInCareOtherPage.url + "/1")
      urlMustEqual(GBreaksInCareOtherPage.url)
      break()
      next
      urlMustEqual(GBreaksInCareSummaryPage.url) //will need to go to summary

      goTo(GBreaksInCareOtherPage.url + "/2")
      urlMustEqual(GBreaksInCareOtherPage.url)
      break()
      next

      findAll("input[value='Change']").get(1).click()

      urlMustEqual(GBreaksInCareOtherPage.url)
      $("#caringEnded_date_year").getValue mustEqual 2016.toString
      $("#caringStarted_date_year").getValue mustEqual 2016.toString
      fill("#caringEnded_date_year") `with` "2014"
      fill("#caringStarted_date_year") `with` "2015"
      next
      urlMustEqual(GBreaksInCareSummaryPage.url) //will need to go to summary

      $("#summary-table tr.data-table").size() shouldEqual 2
      findAll("#summary-table tr.data-table").get(1).getText shouldEqual "YouOther04 January 201404 January 2015"
    }
  }
  section("integration", models.domain.Breaks.id)
}

trait BreakFillerOther {
  this: WithBrowsers[_] with WithBrowserHelper =>

  def break(start: DayMonthYear = DayMonthYear(4, 1, 2016),
            end: DayMonthYear = DayMonthYear(4, 1, 2016)) = {

    browser.fill("#caringEnded_date_day") `with` end.day.get.toString
    browser.fill("#caringEnded_date_month") `with` end.month.get.toString
    browser.fill("#caringEnded_date_year") `with` end.year.get.toString
    browser.fill("#caringEnded_time") `with` "10"
    browser.click("#caringStarted_answer_yes")
    browser.fill("#caringStarted_date_day") `with` start.day.get.toString
    browser.fill("#caringStarted_date_month") `with` start.month.get.toString
    browser.fill("#caringStarted_date_year") `with` start.year.get.toString
    browser.fill("#caringStarted_time") `with` "12"
    browser.click("#whereWasDp_answer_On_holiday")
    browser.click("#whereWereYou_answer_Somewhere_else")
    browser.fill("#whereWereYou_text") `with` "test"
  }
}
