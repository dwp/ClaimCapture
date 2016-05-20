package controllers.s_eligibility

import app.ConfigProperties._
import models.domain.Benefits
import models.view.ClaimHandling
import org.joda.time.DateTime
import org.specs2.mutable._
import play.api.Play._
import play.api.i18n.{MMessages, MessagesApi}
import utils.pageobjects.s_eligibility.{GEligibilityPage, GBenefitsPage}
import utils.pageobjects.{PageObjects, PageObjectsContext, TestData}
import utils.{LightFakeApplication, WithBrowser, WithJsBrowser}

class GBenefitsIntegrationSpec extends Specification {
  section("integration", models.domain.CarersAllowance.id)
  "Carer's Allowance - Benefits - Integration" should {
    "be presented" in new WithJsBrowser with PageObjects {
		  val page = GBenefitsPage(context)
      page goToThePage ()
    }

    "contain a link to gov.uk" in new WithJsBrowser with PageObjects {
		  val page = GBenefitsPage(context)
      page goToThePage ()
      page.source must contain("https://www.gov.uk")
    }

    "contain errors on invalid submission" in new WithJsBrowser with PageObjects {
		  val page = GBenefitsPage(context)
      val claim = new TestData
      page goToThePage()
      val pageWithErrors = page.submitPage()
      pageWithErrors.listErrors.size mustEqual 1
    }
    
    "accept submit if all mandatory fields are populated" in new WithJsBrowser with PageObjects {
		  val page = GBenefitsPage(context)
      val claim = new TestData
      claim.CanYouGetCarersAllowanceWhatBenefitDoesThePersonYouCareForGet = "AA"
      page goToThePage()
      page fillPageWith claim
      page submitPage()
    }

    "warn if answer is 'none of the benefits' to person get one of benefits" in new WithJsBrowser with PageObjects {
		  val page = GBenefitsPage(context)
      val claim = new TestData
      claim.CanYouGetCarersAllowanceWhatBenefitDoesThePersonYouCareForGet = "NONE"
      page goToThePage()
      page fillPageWith claim
      page visible("#answerNoMessageWrap") must beTrue
    }

    "navigate to next page on valid submission with 'PIP' selected" in new WithJsBrowser with PageObjects {
		   verifyAnswerMessageAndSubmit(Benefits.pip, context)
    }

    "navigate to next page on valid submission with 'DLA' selected" in new WithJsBrowser with PageObjects {
      verifyAnswerMessageAndSubmit(Benefits.dla, context)
    }

    "navigate to next page on valid submission with 'AA' selected" in new WithJsBrowser with PageObjects {
      verifyAnswerMessageAndSubmit(Benefits.aa, context)
    }

    "navigate to next page on valid submission with 'CAA' selected" in new WithJsBrowser with PageObjects {
      verifyAnswerMessageAndSubmit(Benefits.caa, context)
    }

    "navigate to next page on valid submission with 'AFIP' selected" in new WithJsBrowser with PageObjects {
      verifyAnswerMessageAndSubmit(Benefits.afip, context)
    }

    "contains C3Version cookie with correct expiry" in new WithJsBrowser with PageObjects {
      browser.goTo(GBenefitsPage.url)
      ( browser.getCookie(ClaimHandling.C3VERSION).getValue == ClaimHandling.C3VERSION_VALUE ) must beTrue

      // The cookie should expire in 10 hours according to ClaimHandling constants.
      // Since we dont know the exact time the cookie was created lets just check its within plus minus 1 minute of 10 hours
      val cookieExpiry=browser.getCookie(ClaimHandling.C3VERSION).getExpiry.getTime()
      val tenHourLess1min=DateTime.now.plusHours(10).minusMinutes(1)
      val tenHourPlus1min=DateTime.now.plusHours(10).plusMinutes(1)
      tenHourLess1min.isBefore(cookieExpiry) should beTrue
      tenHourPlus1min.isAfter(cookieExpiry) should beTrue
    }

    "feedback link should contain claim feedback" in new WithJsBrowser with PageObjects {
      val page = GBenefitsPage(context)
      page goToThePage ()

      page.source must contain(getFeedbackLink())
    }

    "contains english link in footer when welsh lang selected" in new WithJsBrowser with PageObjects {
      browser.goTo(GBenefitsPage.url+"?lang=cy")
      (browser.getCookie("PLAY_LANG").getValue == "cy") must beTrue
      browser.pageSource() must contain("English")
    }

    "contains welsh link in footer when english lang is default" in new WithJsBrowser with PageObjects {
      browser.goTo(GBenefitsPage.url)
      (browser.getCookie("PLAY_LANG").getValue == "") must beTrue
      browser.pageSource() must contain("Cymraeg")
    }

    "contains no welsh or english link in footer when GB-NIR is default" in new WithBrowser(app = LightFakeApplication(additionalConfiguration = Map("origin.tag" -> "GB-NIR"))) with PageObjects {
      browser.goTo(GBenefitsPage.url)
      (browser.getCookie("PLAY_LANG").getValue == "") must beTrue
      browser.pageSource() must not contain("Cymraeg")
      browser.pageSource() must not contain("English")
    }

    "contains no welsh or english link in footer when GB-NIR when welsh selected" in new WithBrowser(app = LightFakeApplication(additionalConfiguration = Map("origin.tag" -> "GB-NIR"))) with PageObjects {
      browser.goTo(GBenefitsPage.url+"?lang=cy")
      (browser.getCookie("PLAY_LANG").getValue == "") must beTrue
      browser.pageSource() must not contain("Cymraeg")
      browser.pageSource() must not contain("English")
    }
  }
  section("integration", models.domain.CarersAllowance.id)

  private def getFeedbackLink() = {
    val messages: MessagesApi = current.injector.instanceOf[MMessages]
    getBooleanProperty("feedback.cads.enabled") match {
      case true => messages("feedback.link")
      case _ => messages("feedback.old.link")
    }
  }

  private def verifyAnswerMessageAndSubmit(benefitAnswer:String, context:PageObjectsContext) = {
    val page = GBenefitsPage(context)
    val claim = new TestData
    claim.CanYouGetCarersAllowanceWhatBenefitDoesThePersonYouCareForGet = benefitAnswer
    page goToThePage()
    page fillPageWith claim

    page visible("#answerNoMessageWrap") must beFalse

    val nextPage = page submitPage()

    nextPage must beAnInstanceOf[GEligibilityPage]
  }
}
