package controllers.breaks_in_care

import app.CircsBreaksWhereabouts._
import controllers.mappings.Mappings
import controllers.{ClaimScenarioFactory, Formulate, BrowserMatchers, WithBrowserHelper}
import models.DayMonthYear
import org.specs2.mutable._
import play.api.Logger
import utils.pageobjects._
import utils.pageobjects.breaks_in_care.{GBreaksInCareTypePage, GBreaksInCareRespitePage}
import utils.pageobjects.s_care_you_provide.GTheirPersonalDetailsPage
import utils.pageobjects.s_claim_date.GClaimDatePage
import utils.pageobjects.s_education.GYourCourseDetailsPage
import utils.{WithBrowsers, WithBrowser, WithJsBrowser}

class GBreaksInCareRespiteIntegrationSpec extends Specification {
  section("integration", models.domain.Breaks.id)
  "Break" should {
    "be presented" in new WithJsBrowser with BreakFillerRespite with WithBrowserHelper with BrowserMatchers {
      goTo(GBreaksInCareRespitePage.url + "/1")
      urlMustEqual(GBreaksInCareRespitePage.url)
    }

    "display respite text" in new WithBrowser with PageObjects {
      val breaksInCare = GClaimDatePage(context) goToThePage() runClaimWith(ClaimScenarioFactory.careYouProvideWithBreaksInCareRespite(false), GBreaksInCareRespitePage.url)

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
      urlMustEqual(GBreaksInCareTypePage.url) //will need to go to summary

      goTo(GBreaksInCareRespitePage.url + "/2")
      urlMustEqual(GBreaksInCareRespitePage.url)
      break()
      next

      //$("#breaks .data-table ul li").size() shouldEqual 2 //check for number of breaks in summary
    }

    "add two breaks and edit the second's start year" in new WithJsBrowser with BreakFillerRespite with WithBrowserHelper with BrowserMatchers {
      goTo(GBreaksInCareRespitePage.url + "/1")
      urlMustEqual(GBreaksInCareRespitePage.url)
      break()
      next
      urlMustEqual(GBreaksInCareTypePage.url) //will need to go to summary

      goTo(GBreaksInCareRespitePage.url + "/2")
      urlMustEqual(GBreaksInCareRespitePage.url)
      break()
      next

//      findFirst("input[value='Change']").click()
//
//      urlMustEqual(GBreaksInCareRespitePage.url)
//      $("#yourStayEnded_date_year").getValue mustEqual 2001.toString
//
//      fill("#yourStayEnded_date_year") `with` "1999"
//      next
      urlMustEqual(GBreaksInCareTypePage.url) //will need to go to summary

      //$("ul.break-data li").size() mustEqual 2
      //$("ul.break-data").findFirst("li").findFirst("h3").getText shouldEqual "01/01/1999 to 01/01/2001"
    }

    "add two breaks and edit the second's start year which will be DP" in new WithJsBrowser with BreakFillerRespite with WithBrowserHelper with BrowserMatchers {
      goTo(GBreaksInCareRespitePage.url + "/1")
      urlMustEqual(GBreaksInCareRespitePage.url)
      break()
      next
      urlMustEqual(GBreaksInCareTypePage.url) //will need to go to summary

      goTo(GBreaksInCareRespitePage.url + "/2")
      urlMustEqual(GBreaksInCareRespitePage.url)
      break(whoWasInRespite = "DP")
      next

//      findFirst("input[value='Change']").click()
//
//      urlMustEqual(GBreaksInCareRespitePage.url)
//      $("#dpStayEnded_date_year").getValue mustEqual 2001.toString
//
//      fill("#dpStayEnded_date_year") `with` "1999"
//      next
      urlMustEqual(GBreaksInCareTypePage.url) //will need to go to summary

      //$("ul.break-data li").size() mustEqual 2
      //$("ul.break-data").findFirst("li").findFirst("h3").getText shouldEqual "01/01/1999 to 01/01/2001"
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
