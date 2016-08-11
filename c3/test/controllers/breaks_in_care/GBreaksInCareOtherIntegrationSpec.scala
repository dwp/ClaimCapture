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
      val breaksInCare = GClaimDatePage(context) goToThePage() runClaimWith(ClaimScenarioFactory.careYouProvideWithBreaksInCareOther(false), GBreaksInCareOtherPage.url)

      breaksInCare.source contains "You told us that there was a time you weren't able to care for Tom Wilson 35 hours a week. We need these details." should beTrue
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
      println(browser.pageSource())
        urlMustEqual(GBreaksInCareSummaryPage.url) //will need to go to summary

        goTo(GBreaksInCareOtherPage.url + "/2")
        urlMustEqual(GBreaksInCareOtherPage.url)
        break()
        next

        $("#summary-table .data-table").size() shouldEqual 2 //check for number of breaks in summary
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

        findFirst("input[value='Change']").click()

        urlMustEqual(GBreaksInCareOtherPage.url)
        $("#startedCaring_date_year").getValue mustEqual 2016.toString

        fill("#startedCaring_date_year") `with` "2017"
        next

        urlMustEqual(GBreaksInCareSummaryPage.url) //will need to go to summary

        $("#summary-table tr.data-table").size() shouldEqual 2
        $("#summary-table tr.data-table").getText shouldEqual "Other04/01/201604/01/2017"
      }

      "add two breaks and edit the second's start year which will be DP" in new WithJsBrowser with BreakFillerOther with WithBrowserHelper with BrowserMatchers {
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
        $("#dpOtherEnded_date_year").getValue mustEqual 2016.toString

        fill("#dpOtherEnded_date_year") `with` "2017"
        next
        urlMustEqual(GBreaksInCareSummaryPage.url) //will need to go to summary

        $("#summary-table tr.data-table").size() shouldEqual 2
        findAll("#summary-table tr.data-table").get(1).getText shouldEqual "Other04/01/201604/01/2017"
      }
  }
  section("integration", models.domain.Breaks.id)
}

trait BreakFillerOther {
  this: WithBrowsers[_] with WithBrowserHelper =>

  def break(start: DayMonthYear = DayMonthYear(4, 1, 2016),
            end: DayMonthYear = DayMonthYear(4, 1, 2016)) = {

    browser.fill("#dpOtherEnded_date_day") `with` end.day.get.toString
    browser.fill("#dpOtherEnded_date_month") `with` end.month.get.toString
    browser.fill("#dpOtherEnded_date_year") `with` end.year.get.toString
    browser.fill("#dpOtherEnded_time") `with` "10"
    browser.click("#startedCaring_answer_yes")
    browser.fill("#startedCaring_date_day") `with` start.day.get.toString
    browser.fill("#startedCaring_date_month") `with` start.month.get.toString
    browser.fill("#startedCaring_date_year") `with` start.year.get.toString
    browser.fill("#startedCaring_time") `with` "12"
    browser.click("#whereWasDp_answer_On_holiday")
    browser.click("#whereWereYou_answer_Somewhere_else")
    browser.fill("#whereWereYou_text") `with` "test"
  }
}
