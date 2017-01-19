package controllers.circs.breaks_in_care

import controllers.{BrowserMatchers, ClaimScenarioFactory, WithBrowserHelper}
import models.DayMonthYear
import org.specs2.mutable._
import utils.pageobjects._
import utils.pageobjects.circumstances.breaks_in_care.{GCircsBreaksInCareSummaryPage, GCircsBreaksInCareOtherPage}
import utils.pageobjects.s_claim_date.GClaimDatePage
import utils.{WithBrowser, WithBrowsers, WithJsBrowser}

class GBreaksInCareOtherIntegrationSpec extends Specification {
  section("integration", models.domain.Breaks.id)
  "Break" should {
    "be presented" in new WithJsBrowser with BreakFillerOther with WithBrowserHelper with BrowserMatchers {
      goTo(GCircsBreaksInCareOtherPage.url )
      urlMustEqual(GCircsBreaksInCareOtherPage.url)
    }

    "give errors when missing mandatory fields of data" in new WithJsBrowser with BreakFillerOther with WithBrowserHelper with BrowserMatchers {
      goTo(GCircsBreaksInCareOtherPage.url + "/1")
      next
      urlMustEqual(GCircsBreaksInCareOtherPage.url)

      findAll("div[class=validation-summary] ol li").size shouldEqual 2
    }

    """show 2 breaks in "break table" upon providing 2 breaks""" in new WithJsBrowser with BreakFillerOther with WithBrowserHelper with BrowserMatchers {
        goTo(GCircsBreaksInCareOtherPage.url + "/1")
        urlMustEqual(GCircsBreaksInCareOtherPage.url)
        break()
        next

        urlMustEqual(GCircsBreaksInCareSummaryPage.url) //will need to go to summary

        goTo(GCircsBreaksInCareOtherPage.url + "/2")
        urlMustEqual(GCircsBreaksInCareOtherPage.url)
        break()
        next

        $("#summary-table .data-table").size() shouldEqual 2 //check for number of breaks in summary
      }

      "add two breaks and edit the firsts start year" in new WithJsBrowser with BreakFillerOther with WithBrowserHelper with BrowserMatchers {
        goTo(GCircsBreaksInCareOtherPage.url + "/1")
        urlMustEqual(GCircsBreaksInCareOtherPage.url)
        break()
        next
        urlMustEqual(GCircsBreaksInCareSummaryPage.url) //will need to go to summary

        goTo(GCircsBreaksInCareOtherPage.url + "/2")
        urlMustEqual(GCircsBreaksInCareOtherPage.url)
        break()
        next

        findFirst("input[value='Change']").click()

        urlMustEqual(GCircsBreaksInCareOtherPage.url)
        $("#caringEnded_date_year").getValue mustEqual 2016.toString
        $("#caringStarted_date_year").getValue mustEqual 2016.toString
        fill("#caringEnded_date_year") `with` "2013"
        fill("#caringStarted_date_year") `with` "2014"

        next
        urlMustEqual(GCircsBreaksInCareSummaryPage.url) //will need to go to summary

        $("#summary-table tr.data-table").size() shouldEqual 2
        $("#summary-table tr.data-table").getText shouldEqual "YouOther04 January 201304 January 2014"
      }

    "add two breaks and edit the second's start year" in new WithJsBrowser with BreakFillerOther with WithBrowserHelper with BrowserMatchers {
      goTo(GCircsBreaksInCareOtherPage.url + "/1")
      urlMustEqual(GCircsBreaksInCareOtherPage.url)
      break()
      next
      urlMustEqual(GCircsBreaksInCareSummaryPage.url) //will need to go to summary

      goTo(GCircsBreaksInCareOtherPage.url + "/2")
      urlMustEqual(GCircsBreaksInCareOtherPage.url)
      break()
      next

      findAll("input[value='Change']").get(1).click()

      urlMustEqual(GCircsBreaksInCareOtherPage.url)
      $("#caringEnded_date_year").getValue mustEqual 2016.toString
      $("#caringStarted_date_year").getValue mustEqual 2016.toString
      fill("#caringEnded_date_year") `with` "2014"
      fill("#caringStarted_date_year") `with` "2015"
      next
      urlMustEqual(GCircsBreaksInCareSummaryPage.url) //will need to go to summary

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
