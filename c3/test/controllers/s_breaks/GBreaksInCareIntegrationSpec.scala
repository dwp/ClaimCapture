package controllers.s_breaks

import controllers._
import org.openqa.selenium.By
import org.specs2.mutable._
import play.api.Logger
import utils.pageobjects._
import utils.pageobjects.s_breaks.{GBreakPage}
import utils.pageobjects.s_breaks.GBreaksInCarePage
import utils.pageobjects.s_claim_date.GClaimDatePage
import utils.pageobjects.s_education.GYourCourseDetailsPage
import utils.{WithBrowser, WithJsBrowser}

class GBreaksInCareIntegrationSpec extends Specification {
  "Breaks from care" should {
    "present" in new WithBrowser with PageObjects {
      GBreaksInCarePage(context) goToThePage() must beAnInstanceOf[GBreaksInCarePage]
    }

    """present "Education" when no more breaks are required""" in new WithBrowser with PageObjects {
      val breaksInCare = GBreaksInCarePage(context) goToThePage()
      val data = new TestData
      data.AboutTheCareYouProvideHaveYouHadAnyMoreBreaksInCare_1 = "no"

      val next = breaksInCare fillPageWith data submitPage()
      next must beAnInstanceOf[GYourCourseDetailsPage]
    }

    "display dynamic question text if user answered that they did NOT care for this person for 35 hours or more each week before your claim date" in new WithBrowser with PageObjects {
      val breaksInCare = GClaimDatePage(context) goToThePage() runClaimWith(ClaimScenarioFactory.s4CareYouProvide(false), GBreaksInCarePage.url)

      Logger.info(breaksInCare.source)

      breaksInCare.source contains "Have you had any breaks from caring for this person since 10 October 2016?" should beTrue
    }

    "display dynamic question text if user answered that they care for this person for 35 hours or more each week before your claim date (within 6 months)" in new WithBrowser with PageObjects {
      val claim = ClaimScenarioFactory.s4CareYouProvide(true)
      claim.ClaimDateWhenDidYouStartToCareForThisPerson = "10/08/2016"
      val breaksInCare = GClaimDatePage(context) goToThePage() runClaimWith(claim, GBreaksInCarePage.url)
      breaksInCare.source contains "Have you had any breaks from caring for this person since 10 August 2016?" should beTrue
    }

    "display dynamic question text if user answered that they care for this person for 35 hours or more each week before your claim date (more than 6 months)" in new WithBrowser with PageObjects {
      val claim = ClaimScenarioFactory.s4CareYouProvide(true)
      claim.ClaimDateWhenDidYouStartToCareForThisPerson = "10/02/2016"
      val breaksInCare = GClaimDatePage(context) goToThePage() runClaimWith(claim, GBreaksInCarePage.url)
      breaksInCare.source contains "Have you had any breaks from caring for this person since 10 April 2016?" should beTrue
    }

    """record the "yes/no" answer upon starting to add a new break""" in new WithBrowser with PageObjects {
      val breaksInCare = GBreaksInCarePage(context) goToThePage()
      val data = new TestData
      data.AboutTheCareYouProvideHaveYouHadAnyMoreBreaksInCare_1 = "yes"

      val next = breaksInCare fillPageWith data submitPage()
      next must beAnInstanceOf[GBreakPage]

      val back = next.goBack()

      back must beAnInstanceOf[GBreaksInCarePage]

      back.readYesNo("#answer") mustNotEqual None
    }

    """allow a new break to be added but not record the "yes/no" answer""" in new WithBrowser with PageObjects {
      val breaksInCare = GClaimDatePage(context) goToThePage() runClaimWith(ClaimScenarioFactory.s4CareYouProvideWithBreaksInCare(true), GBreaksInCarePage.url, upToIteration = 2)

      breaksInCare.isElemSelected("#answer_yes") should beFalse
      breaksInCare.isElemSelected("#answer_no") should beFalse
    }

    "delete a break in care" in new WithJsBrowser with PageObjects {
      pending("##Unit driver can't handle that JS form submission (Firefox do) enable this when changing to firefox driver")
      val breaksInCare = GClaimDatePage(context) goToThePage() runClaimWith(ClaimScenarioFactory.s4CareYouProvideWithBreaksInCare(true), GBreaksInCarePage.url, upToIteration = 2)

      Logger.info(breaksInCare.ctx.browser.getDriver.findElement(By.className("break-data")).getText)

      val list = breaksInCare.ctx.browser.$("input[name=changerow]")
      Logger.info(list.toString())
      val updated = breaksInCare.clickLinkOrButton(".break-data li input[name=deleterow]")
      val refreshed = updated.clickLinkOrButton("#yesDelete")

      refreshed.source must not contain "Hospital"
    }

    "have js enabled to allow js tests to run in breaks page" in new WithJsBrowser with PageObjects {
      val breaksInCare = GClaimDatePage(context) goToThePage() runClaimWith(ClaimScenarioFactory.s4CareYouProvideWithMultipleBreaksInCare(true), GBreaksInCarePage.url, upToIteration = 11)
      breaksInCare goToThePage()
      breaksInCare.jsCheckEnabled must beTrue
    }

    "not have warning on page when less than maximum breaks" in new WithJsBrowser with PageObjects {
      val breaksInCare = GClaimDatePage(context) goToThePage() runClaimWith(ClaimScenarioFactory.s4CareYouProvideWithMultipleBreaksInCare(true), GBreaksInCarePage.url, upToIteration = 10)
      breaksInCare goToThePage()
      val clicked = breaksInCare.clickLinkOrButton("#answer_yes")
      clicked.source must not contain "warningMessageWrap"
    }

    "show warning when got maximum breaks and click yes to add another" in new WithJsBrowser with PageObjects {
      val breaksInCare = GClaimDatePage(context) goToThePage() runClaimWith(ClaimScenarioFactory.s4CareYouProvideWithMultipleBreaksInCare(true), GBreaksInCarePage.url, upToIteration = 11)
      breaksInCare goToThePage()
      val clicked = breaksInCare.clickLinkOrButton("#answer_yes")
      clicked.source must contain("warningMessageWrap")
      clicked visible ("#warningMessageWrap") must beTrue
    }

    "hide warning when got maximum breaks and click yes then no" in new WithJsBrowser with PageObjects {
      val breaksInCare = GClaimDatePage(context) goToThePage() runClaimWith(ClaimScenarioFactory.s4CareYouProvideWithMultipleBreaksInCare(true), GBreaksInCarePage.url, upToIteration = 11)
      breaksInCare goToThePage()
      val clickedyes = breaksInCare.clickLinkOrButton("#answer_yes")
      clickedyes visible ("#warningMessageWrap") must beTrue
      val clickedno = clickedyes.clickLinkOrButton("#answer_no")
      clickedno.source must contain("warningMessageWrap")
      clickedno visible ("#warningMessageWrap") must beFalse
    }

  }
  section("integration", models.domain.CareYouProvide.id)

}
