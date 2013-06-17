package integration.s2_aboutyou

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import integration.Helper

class G6EmploymentSpec extends Specification with Tags {

  "Employment" should {
    "be presented" in new WithBrowser {
      Helper.fillClaimDate(browser)
      browser.goTo("/aboutyou/employment")

      browser.title() mustEqual "Employment - About You"
    }

    "be presented without claim date" in new WithBrowser {
      browser.goTo("/aboutyou/employment")

      browser.title() mustEqual "Benefits - Carer's Allowance"
    }

    "contain 4 completed forms" in new WithBrowser {
      Helper.fillYourDetails(browser)
      Helper.fillContactDetails(browser)
      Helper.fillClaimDate(browser)

      browser.click("#hadPartnerSinceClaimDate_yes")
      browser.click("#eitherClaimedBenefitSinceClaimDate_yes")
      browser.click("#beenInEducationSinceClaimDate_yes")
      browser.click("#receiveStatePension_yes")
      browser.submit("button[type='submit']")

      browser.title() mustEqual "Employment - About You"
      browser.find("div[class=completed] ul li").size() mustEqual 4
    }

    "fill all fields" in new WithBrowser {
      Helper.fillClaimDate(browser)
      browser.goTo("/aboutyou/employment")


      browser.click("#beenEmployedSince6MonthsBeforeClaim_yes")
      browser.click("#beenSelfEmployedSince1WeekBeforeClaim_yes")
      browser.submit("button[type='submit']")

      browser.title() mustEqual "Property and Rent - About You"

    }

    "failed to fill the form" in new WithBrowser {
      Helper.fillClaimDate(browser)
      browser.goTo("/aboutyou/employment")

      browser.title() mustEqual "Employment - About You"
      browser.submit("button[type='submit']")

      browser.find("p[class=error]").size() mustEqual 2

      browser.click("#beenEmployedSince6MonthsBeforeClaim_yes")
      browser.submit("button[type='submit']")

      browser.find("p[class=error]").size() mustEqual 1

      browser.click("#beenSelfEmployedSince1WeekBeforeClaim_yes")

      browser.submit("button[type='submit']")

      browser.title() mustEqual "Property and Rent - About You"

    }



  } section "integration"
}
