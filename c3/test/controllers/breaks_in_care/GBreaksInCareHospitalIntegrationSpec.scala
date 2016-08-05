package controllers.breaks_in_care

import app.CircsBreaksWhereabouts._
import controllers.{ClaimScenarioFactory, Formulate, BrowserMatchers, WithBrowserHelper}
import models.DayMonthYear
import org.specs2.mutable._
import play.api.Logger
import utils.pageobjects._
import utils.pageobjects.breaks_in_care.GBreaksInCareHospitalPage
import utils.pageobjects.s_claim_date.GClaimDatePage
import utils.pageobjects.s_education.GYourCourseDetailsPage
import utils.{WithBrowsers, WithBrowser, WithJsBrowser}

class GBreaksInCareHospitalIntegrationSpec extends Specification {
  section("integration", models.domain.Breaks.id)
  "Break" should {
    "be presented" in new WithJsBrowser with BreakFiller with WithBrowserHelper with BrowserMatchers {
      goTo(GBreaksInCareHospitalPage.url)
      urlMustEqual(GBreaksInCareHospitalPage.url)
    }

    """present "completed" when no more breaks are required""" in new WithJsBrowser  with BreakFiller with WithBrowserHelper with BrowserMatchers {
      Formulate.theirPersonalDetails(browser)
      goTo(GBreaksInCareHospitalPage.url)
      urlMustEqual(GBreaksInCareHospitalPage.url)

      click("#answer_no")
      next
      urlMustEqual(GYourCourseDetailsPage.url)
    }

    "display dynamic question text if user answered that they did NOT care for this person for 35 hours or more each week before your claim date" in new WithBrowser with PageObjects {
      val breaksInCare = GClaimDatePage(context) goToThePage() runClaimWith(ClaimScenarioFactory.careYouProvideWithBreaksInCareYou(false),GBreaksInCareHospitalPage.url)
      breaksInCare.source contains "About the break from care since 10 October 2016" should beTrue
    }

    "display dynamic question text if user answered that they care for this person for 35 hours or more each week before your claim date (within 6 months)" in new WithBrowser with PageObjects {
      val claim = ClaimScenarioFactory.careYouProvideWithBreaksInCareYou(true)
      claim.ClaimDateWhenDidYouStartToCareForThisPerson = "10/08/2016"
      val breaksInCare = GClaimDatePage(context) goToThePage() runClaimWith(claim, GBreaksInCareHospitalPage.url)
      breaksInCare.source contains "About the break from care since 10 August 2016" should beTrue
    }

    "display dynamic question text if user answered that they care for this person for 35 hours or more each week before your claim date (more than 6 months)" in new WithBrowser with PageObjects {
      val claim = ClaimScenarioFactory.careYouProvideWithBreaksInCareYou(true)
      claim.ClaimDateWhenDidYouStartToCareForThisPerson = "10/02/2016"
      val breaksInCare = GClaimDatePage(context) goToThePage() runClaimWith(claim, GBreaksInCareHospitalPage.url)
      breaksInCare.source contains "About the break from care since 10 April 2016" should beTrue
    }

    """give 2 errors when missing 2 mandatory fields of data - missing "start date" and "medical" """ in new WithJsBrowser  with BreakFiller with WithBrowserHelper with BrowserMatchers {
      goTo(GBreaksInCareHospitalPage.url)
      click("#answer_yes")
      next
      Logger.info("spec" + browser.url)
      urlMustEqual(GBreaksInCareHospitalPage.url)

      fill("#start_day").`with`("1")
      fill("#start_month").`with`("1")

      click("#whereYou_answer_In_hospital")
      click("#wherePerson_answer_In_hospital")

      click("#hasBreakEnded_answer_no")

      next
      Logger.info("spec" + browser.url)
      urlMustEqual(GBreaksInCareHospitalPage.url)
      findAll("div[class=validation-summary] ol li").size shouldEqual 2
    }

    """show 2 breaks in "break table" upon providing 2 breaks""" in new WithJsBrowser  with BreakFiller with WithBrowserHelper with BrowserMatchers {
      goTo(GBreaksInCareHospitalPage.url)
      urlMustEqual(GBreaksInCareHospitalPage.url)

      click("#answer_yes")
      next
      urlMustEqual(GBreaksInCareHospitalPage.url)

      break()
      next
      urlMustEqual(GBreaksInCareHospitalPage.url)

      click("#answer_yes")
      next
      urlMustEqual(GBreaksInCareHospitalPage.url)

      break()
      next
      urlMustEqual(GBreaksInCareHospitalPage.url)

      $("#breaks .data-table ul li").size() shouldEqual 2
    }

    "add two breaks and edit the second's start year" in new WithJsBrowser  with BreakFiller with WithBrowserHelper with BrowserMatchers {
      goTo(GBreaksInCareHospitalPage.url)
      urlMustEqual(GBreaksInCareHospitalPage.url)

      click("#answer_yes")
      next
      urlMustEqual(GBreaksInCareHospitalPage.url)

      break()
      next
      urlMustEqual(GBreaksInCareHospitalPage.url)

      click("#answer_yes")
      next
      urlMustEqual(GBreaksInCareHospitalPage.url)

      break()
      next
      urlMustEqual(GBreaksInCareHospitalPage.url)

      findFirst("input[value='Change']").click()

      urlMustEqual(GBreaksInCareHospitalPage.url)
      $("#start_year").getValue mustEqual 2001.toString

      fill("#start_year") `with` "1999"
      next
      urlMustEqual(GBreaksInCareHospitalPage.url)

      $("ul.break-data li").size() mustEqual 2
      $("ul.break-data").findFirst("li").findFirst("h3").getText shouldEqual "01/01/1999 to 01/01/2001"
    }

    """show "all options" for "Where was the person you care for during the break?".""" in new WithJsBrowser  with WithBrowserHelper with BrowserMatchers {
      import scala.collection.JavaConverters._
      Formulate.theirPersonalDetails(browser)

      goTo(s"${GBreaksInCareHospitalPage.url}/1")
      urlMustEqual(GBreaksInCareHospitalPage.url)

      text("#wherePerson_answer li").asScala should containAllOf(List(Home, Hospital, Holiday, RespiteCare, SomewhereElse).map(e => e.toLowerCase))
    }
  }
  section("integration", models.domain.Breaks.id)
}

trait BreakFiller {
  this: WithBrowsers[_] with WithBrowserHelper =>

  def break(start: DayMonthYear = DayMonthYear(1, 1, 2001),
            end: DayMonthYear = DayMonthYear(1, 1, 2001),
            whoWasInHospital: String = "You",
            breaksInCareStillCaring: Boolean = false) = {
    browser.click(s"whoWasInHospital_$whoWasInHospital")
    if (whoWasInHospital == "You") {
      browser.fill(s"#whenWereYouAdmitted_day") `with` start.day.get.toString
      browser.fill(s"#whenWereYouAdmitted_month") `with` start.month.get.toString
      browser.fill("#whenWereYouAdmitted_year") `with` start.year.get.toString
      browser.click("yourStayEnded_answer_yes")
      browser.fill(s"#yourStayEnded_date_day") `with` end.day.get.toString
      browser.fill(s"#yourStayEnded_date_month") `with` end.month.get.toString
      browser.fill("#yourStayEnded_date_year") `with` end.year.get.toString
    } else {
      browser.fill(s"#whenWsDpAdmitted_day") `with` start.day.get.toString
      browser.fill(s"#whenWasDpAdmitted_month") `with` start.month.get.toString
      browser.fill("#whenWasDpAdmitted_year") `with` start.year.get.toString
      browser.click("dpStayEnded_answer_yes")
      browser.fill(s"#dpStayEnded_date_day") `with` end.day.get.toString
      browser.fill(s"#dpStayEnded_date_month") `with` end.month.get.toString
      browser.fill("#dpStayEnded_date_year") `with` end.year.get.toString
      browser.click(s"""#breaksInCareStillCaring_${if (breaksInCareStillCaring) "yes" else "no"}""")
    }
  }
}
