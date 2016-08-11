package controllers.breaks_in_care

import controllers.mappings.Mappings
import controllers.{ClaimScenarioFactory, BrowserMatchers, WithBrowserHelper}
import models.DayMonthYear
import org.specs2.mutable._
import utils.pageobjects._
import utils.pageobjects.breaks_in_care.{GBreaksInCareSummaryPage, GBreaksInCareHospitalPage}
import utils.pageobjects.s_claim_date.GClaimDatePage
import utils.{WithBrowsers, WithBrowser, WithJsBrowser}

class GBreaksInCareHospitalIntegrationSpec extends Specification {
  section("integration", models.domain.Breaks.id)
  "Break" should {
    "be presented" in new WithJsBrowser with BreakFillerHospital with WithBrowserHelper with BrowserMatchers {
      goTo(GBreaksInCareHospitalPage.url )
      urlMustEqual(GBreaksInCareHospitalPage.url)
    }

    "display hospital text" in new WithBrowser with PageObjects {
      val breaksInCare = GClaimDatePage(context) goToThePage() runClaimWith(ClaimScenarioFactory.careYouProvideWithBreaksInCareYou(false), GBreaksInCareHospitalPage.url + "/1")

      breaksInCare.source contains "You told us that you or Tom Wilson have been in hospital. You could still be paid Carer's Allowance for this time" should beTrue
    }

    "give errors when missing mandatory fields of data" in new WithJsBrowser with BreakFillerHospital with WithBrowserHelper with BrowserMatchers {
      goTo(GBreaksInCareHospitalPage.url + "/1")
      next
      urlMustEqual(GBreaksInCareHospitalPage.url)

      findAll("div[class=validation-summary] ol li").size shouldEqual 1
    }

    """show 2 breaks in "break table" upon providing 2 breaks""" in new WithJsBrowser with BreakFillerHospital with WithBrowserHelper with BrowserMatchers {
        goTo(GBreaksInCareHospitalPage.url + "/1")
        urlMustEqual(GBreaksInCareHospitalPage.url)
        break()
        next
        urlMustEqual(GBreaksInCareSummaryPage.url) //will need to go to summary

        goTo(GBreaksInCareHospitalPage.url + "/2")
        urlMustEqual(GBreaksInCareHospitalPage.url)
        break()
        next

        $("#summary-table .data-table").size() shouldEqual 2 //check for number of breaks in summary
      }

      "add two breaks and edit the second's start year" in new WithJsBrowser with BreakFillerHospital with WithBrowserHelper with BrowserMatchers {
        goTo(GBreaksInCareHospitalPage.url + "/1")
        urlMustEqual(GBreaksInCareHospitalPage.url)
        break()
        next
        urlMustEqual(GBreaksInCareSummaryPage.url) //will need to go to summary

        goTo(GBreaksInCareHospitalPage.url + "/2")
        urlMustEqual(GBreaksInCareHospitalPage.url)
        break()
        next

        findFirst("input[value='Change']").click()

        urlMustEqual(GBreaksInCareHospitalPage.url)
        $("#yourStayEnded_date_year").getValue mustEqual 2001.toString

        fill("#yourStayEnded_date_year") `with` "2002"
        next

        urlMustEqual(GBreaksInCareSummaryPage.url) //will need to go to summary

        $("#summary-table tr.data-table").size() shouldEqual 2
        $("#summary-table tr.data-table").getText shouldEqual "YouHospital01/01/200101/01/2002"
      }

      "add two breaks and edit the second's start year which will be DP" in new WithJsBrowser with BreakFillerHospital with WithBrowserHelper with BrowserMatchers {
        goTo(GBreaksInCareHospitalPage.url + "/1")
        urlMustEqual(GBreaksInCareHospitalPage.url)
        break()
        next
        urlMustEqual(GBreaksInCareSummaryPage.url) //will need to go to summary

        goTo(GBreaksInCareHospitalPage.url + "/2")
        urlMustEqual(GBreaksInCareHospitalPage.url)
        break(whoWasInHospital = "DP")
        next

        findAll("input[value='Change']").get(1).click()

        urlMustEqual(GBreaksInCareHospitalPage.url)
        $("#dpStayEnded_date_year").getValue mustEqual 2001.toString

        fill("#dpStayEnded_date_year") `with` "2002"
        next
        urlMustEqual(GBreaksInCareSummaryPage.url) //will need to go to summary

        $("#summary-table tr.data-table").size() shouldEqual 2
        findAll("#summary-table tr.data-table").get(1).getText shouldEqual "Hospital01/01/200101/01/2002"
      }
  }
  section("integration", models.domain.Breaks.id)
}

trait BreakFillerHospital {
  this: WithBrowsers[_] with WithBrowserHelper =>

  def break(start: DayMonthYear = DayMonthYear(1, 1, 2001),
            end: DayMonthYear = DayMonthYear(1, 1, 2001),
            whoWasInHospital: String = "You",
            breaksInCareStillCaring: Boolean = false) = {

    browser.click(s"#whoWasInHospital_$whoWasInHospital")
    if (whoWasInHospital == "You") {
      browser.fill("#whenWereYouAdmitted_day") `with` start.day.get.toString
      browser.fill("#whenWereYouAdmitted_month") `with` start.month.get.toString
      browser.fill("#whenWereYouAdmitted_year") `with` start.year.get.toString
      browser.click("#yourStayEnded_answer_yes")
      browser.fill("#yourStayEnded_date_day") `with` end.day.get.toString
      browser.fill("#yourStayEnded_date_month") `with` end.month.get.toString
      browser.fill("#yourStayEnded_date_year") `with` end.year.get.toString
    } else {
      browser.fill("#whenWasDpAdmitted_day") `with` start.day.get.toString
      browser.fill("#whenWasDpAdmitted_month") `with` start.month.get.toString
      browser.fill("#whenWasDpAdmitted_year") `with` start.year.get.toString
      browser.click("#dpStayEnded_answer_yes")
      browser.fill("#dpStayEnded_date_day") `with` end.day.get.toString
      browser.fill("#dpStayEnded_date_month") `with` end.month.get.toString
      browser.fill("#dpStayEnded_date_year") `with` end.year.get.toString
      browser.click(s"#breaksInCareStillCaring_${if (breaksInCareStillCaring) Mappings.yes else Mappings.no}")
    }
  }
}
