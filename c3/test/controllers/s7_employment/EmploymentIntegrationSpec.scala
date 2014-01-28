package controllers.s7_employment

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{WithBrowserHelper, Formulate, BrowserMatchers}

class EmploymentIntegrationSpec extends Specification with Tags {
}

trait EmployedSinceClaimDate extends BrowserMatchers {
  this: WithBrowser[_] =>

  def beginClaim() = {
    Formulate.claimDate(browser)
    titleMustEqual("Your nationality and residency - About you - the carer")

    Formulate.employment(browser)
  }
}

trait NotEmployedSinceClaimDate extends BrowserMatchers {
  this: WithBrowser[_] =>

  def beginClaim() = {
    Formulate.claimDate(browser)

    browser.goTo("/about-you/employment")
    browser.click("#beenEmployedSince6MonthsBeforeClaim_no")
    browser.click("#beenSelfEmployedSince1WeekBeforeClaim_no")
    browser.submit("button[type='submit']")
  }
}