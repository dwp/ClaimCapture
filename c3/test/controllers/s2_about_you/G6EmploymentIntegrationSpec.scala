package controllers.s2_about_you

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.FormHelper

class G6EmploymentIntegrationSpec extends Specification with Tags {

  "Employment" should {
    "be presented" in new WithBrowser {
      FormHelper.fillClaimDate(browser)
      browser.goTo("/aboutyou/employment")

      browser.title mustEqual "Employment - About You"
    }

    "be presented without claim date" in new WithBrowser {
      browser.goTo("/aboutyou/employment")

      browser.title mustEqual "Benefits - Carer's Allowance"
    }

    "contain 4 completed forms" in new WithBrowser {
      FormHelper.fillYourDetails(browser)
      FormHelper.fillYourContactDetails(browser)
      FormHelper.fillClaimDate(browser)
      FormHelper.fillMoreAboutYou(browser)

      browser.title mustEqual "Employment - About You"
      browser.find("div[class=completed] ul li").size() mustEqual 4
    }

    "fill all fields" in new WithBrowser {
      FormHelper.fillClaimDate(browser)
      FormHelper.fillEmployment(browser)

      browser.title mustEqual "Property and Rent - About You"
    }

    "failed to fill the form" in new WithBrowser {
      FormHelper.fillClaimDate(browser)
      browser.goTo("/aboutyou/employment")

      browser.title mustEqual "Employment - About You"
      browser.submit("button[type='submit']")

      browser.find("p[class=error]").size() mustEqual 2

      browser.click("#beenEmployedSince6MonthsBeforeClaim_yes")
      browser.submit("button[type='submit']")

      browser.find("p[class=error]").size() mustEqual 1

      browser.click("#beenSelfEmployedSince1WeekBeforeClaim_yes")

      browser.submit("button[type='submit']")

      browser.title mustEqual "Property and Rent - About You"

    }

  } section "integration"
}