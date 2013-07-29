package controllers.s7_employment

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{Formulate, BrowserMatchers}

class EmploymentIntegrationSpec extends Specification with Tags {
  "Employment" should {
    "present completion" in new WithBrowser with BrowserMatchers {
      browser.goTo("/employment/completed")
      titleMustEqual("Completion - Employment")
    }

    """progress to next section i.e. "self employed".""" in new WithBrowser with BrowserMatchers {
      browser.goTo("/employment/completed")
      browser.submit("button[type='submit']")
      titleMustEqual("Self Employment - About Self Employment")
    }

    """go back to start of employment i.e. "employment history".""" in new WithBrowser with BrowserMatchers with EmployedSinceClaimDate {
      beginClaim

      browser.goTo("/employment/completed")

      browser.click("#backButton")
      titleMustEqual("Your employment history - Employment")
    }
  } section "integration"
}

trait EmployedSinceClaimDate extends BrowserMatchers {
  this: WithBrowser[_] =>

  def beginClaim(): Unit = {
    Formulate.claimDate(browser)
    Formulate.employment(browser)
    titleMustEqual("Property and Rent - About You")
  }
}

trait NotEmployedSinceClaimDate extends BrowserMatchers {
  this: WithBrowser[_] =>

  def beginClaim(): Unit = {
    Formulate.claimDate(browser)

    browser.goTo("/aboutyou/employment")
    browser.click("#beenEmployedSince6MonthsBeforeClaim_no")
    browser.click("#beenSelfEmployedSince1WeekBeforeClaim_no")
    browser.submit("button[type='submit']")

    titleMustEqual("Property and Rent - About You")
  }
}