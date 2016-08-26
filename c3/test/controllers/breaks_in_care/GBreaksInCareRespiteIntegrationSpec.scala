package controllers.breaks_in_care

import controllers.mappings.Mappings
import controllers.{ClaimScenarioFactory, BrowserMatchers, WithBrowserHelper}
import models.DayMonthYear
import org.specs2.mutable._
import utils.pageobjects._
import utils.pageobjects.breaks_in_care.{GBreaksInCareSummaryPage, GBreaksInCareRespitePage}
import utils.pageobjects.s_claim_date.GClaimDatePage
import utils.{WithBrowsers, WithBrowser, WithJsBrowser}

class GBreaksInCareRespiteIntegrationSpec extends Specification {
  section("integration", models.domain.Breaks.id)
  "Break" should {
    "be presented" in new WithJsBrowser with BreakFillerRespite with WithBrowserHelper with BrowserMatchers {
      goTo(GBreaksInCareRespitePage.url + "/1")
      urlMustEqual(GBreaksInCareRespitePage.url)
    }

    "display respite text" in new WithBrowser with PageObjects {
      val breaksInCare = GClaimDatePage(context) goToThePage() runClaimWith(ClaimScenarioFactory.careYouProvideWithBreaksInCareRespite(false), GBreaksInCareRespitePage.url + "/1")

      breaksInCare.source contains "You told us that you or Tom Wilson have been in respite or a care home. You could still be paid Carer's Allowance for this time" should beTrue
    }

    "give errors when missing mandatory fields of data" in new WithJsBrowser with BreakFillerRespite with WithBrowserHelper with BrowserMatchers {
      goTo(GBreaksInCareRespitePage.url + "/1")
      next
      urlMustEqual(GBreaksInCareRespitePage.url)
      findAll("div[class=validation-summary] ol li").size shouldEqual 1
    }

    """show 2 breaks in "break table" upon providing 2 breaks""" in new WithJsBrowser with BreakFillerRespite with WithBrowserHelper with BrowserMatchers {
      goTo(GBreaksInCareRespitePage.url + "/1")
      urlMustEqual(GBreaksInCareRespitePage.url)
      break()
      next
      urlMustEqual(GBreaksInCareSummaryPage.url) //will need to go to summary

      goTo(GBreaksInCareRespitePage.url + "/2")
      urlMustEqual(GBreaksInCareRespitePage.url)
      break()
      next

      $("#summary-table .data-table").size() shouldEqual 2 //check for number of breaks in summary
    }

    "add two breaks and edit the second's start year" in new WithJsBrowser with BreakFillerRespite with WithBrowserHelper with BrowserMatchers {
      goTo(GBreaksInCareRespitePage.url + "/1")
      urlMustEqual(GBreaksInCareRespitePage.url)
      break()
      next
      urlMustEqual(GBreaksInCareSummaryPage.url) //will need to go to summary

      goTo(GBreaksInCareRespitePage.url + "/2")
      urlMustEqual(GBreaksInCareRespitePage.url)
      break()
      next

      findFirst("input[value='Change']").click()

      urlMustEqual(GBreaksInCareRespitePage.url)
      $("#yourRespiteStayEnded_date_year").getValue mustEqual 2001.toString

      fill("#yourRespiteStayEnded_date_year") `with` "2002"
      next

      urlMustEqual(GBreaksInCareSummaryPage.url) //will need to go to summary

      $("#summary-table tr.data-table").size() shouldEqual 2
      $("#summary-table tr.data-table").getText shouldEqual "YouRespite or care home01 January 200101 January 2002"
    }

    "add two breaks and edit the second's start year which will be DP" in new WithJsBrowser with BreakFillerRespite with WithBrowserHelper with BrowserMatchers {
      goTo(GBreaksInCareRespitePage.url + "/1")
      urlMustEqual(GBreaksInCareRespitePage.url)
      break()
      next
      urlMustEqual(GBreaksInCareSummaryPage.url) //will need to go to summary

      goTo(GBreaksInCareRespitePage.url + "/2")
      urlMustEqual(GBreaksInCareRespitePage.url)
      break(whoWasInRespite = "DP")
      next

      findAll("input[value='Change']").get(1).click()

      urlMustEqual(GBreaksInCareRespitePage.url)
      $("#dpRespiteStayEnded_date_year").getValue mustEqual 2001.toString

      fill("#dpRespiteStayEnded_date_year") `with` "2002"
      next
      urlMustEqual(GBreaksInCareSummaryPage.url) //will need to go to summary

      $("#summary-table tr.data-table").size() shouldEqual 2
      findAll("#summary-table tr.data-table").get(1).getText shouldEqual "Respite or care home01 January 200101 January 2002"
    }
  }
  section("integration", models.domain.Breaks.id)
}

trait BreakFillerRespite {
  this: WithBrowsers[_] with WithBrowserHelper =>

  def break(start: DayMonthYear = DayMonthYear(1, 1, 2001),
            end: DayMonthYear = DayMonthYear(1, 1, 2001),
            whoWasInRespite: String = "You",
            breaksInCareStillCaring: Boolean = false) = {

    browser.click(s"#whoWasInRespite_$whoWasInRespite")
    if (whoWasInRespite == "You") {
      browser.fill("#whenWereYouAdmitted_day") `with` start.day.get.toString
      browser.fill("#whenWereYouAdmitted_month") `with` start.month.get.toString
      browser.fill("#whenWereYouAdmitted_year") `with` start.year.get.toString
      browser.click("#yourRespiteStayEnded_answer_yes")
      browser.fill("#yourRespiteStayEnded_date_day") `with` end.day.get.toString
      browser.fill("#yourRespiteStayEnded_date_month") `with` end.month.get.toString
      browser.fill("#yourRespiteStayEnded_date_year") `with` end.year.get.toString
      browser.click("#yourMedicalProfessional_yes")
    } else {
      browser.fill("#whenWasDpAdmitted_day") `with` start.day.get.toString
      browser.fill("#whenWasDpAdmitted_month") `with` start.month.get.toString
      browser.fill("#whenWasDpAdmitted_year") `with` start.year.get.toString
      browser.click("#dpRespiteStayEnded_answer_yes")
      browser.fill("#dpRespiteStayEnded_date_day") `with` end.day.get.toString
      browser.fill("#dpRespiteStayEnded_date_month") `with` end.month.get.toString
      browser.fill("#dpRespiteStayEnded_date_year") `with` end.year.get.toString
      browser.click(s"#breaksInCareRespiteStillCaring_${if (breaksInCareStillCaring) Mappings.yes else Mappings.no}")
      browser.click("#dpMedicalProfessional_yes")
    }
  }
}
