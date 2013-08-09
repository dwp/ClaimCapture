package controllers.s7_employment

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{Formulate, BrowserMatchers}
import play.api.i18n.Messages

class EmploymentIntegrationSpec extends Specification with Tags {
  "Employment" should {
    "present completion" in new WithBrowser with BrowserMatchers {
      browser.goTo("/employment/completed")
      titleMustEqual("Completion - Employment")
    }

    """progress to next section i.e. "self employed".""" in new WithBrowser with BrowserMatchers {
      browser.goTo("/employment/completed")
      titleMustEqual("Completion - Employment")

      browser.submit("button[type='submit']")
      titleMustEqual("Self Employment - About Self Employment")
    }

    """go back to start of employment i.e. "employment history".""" in new WithBrowser with BrowserMatchers with EmployedSinceClaimDate {
      beginClaim

      browser.goTo("/employment/completed")
      titleMustEqual("Completion - Employment")

      browser.click("#backButton")
      titleMustEqual("Your employment history - Employment")
    }
  } section("integration", models.domain.Employed.id)
}

trait EmployedSinceClaimDate extends BrowserMatchers {
  this: WithBrowser[_] =>

  def beginClaim = {
    Formulate.claimDate(browser)
    titleMustEqual(Messages("s2.g5") + " - About You")

    Formulate.employment(browser)
    titleMustEqual(Messages("s2.g7") + " - About You")
  }
}

trait NotEmployedSinceClaimDate extends BrowserMatchers {
  this: WithBrowser[_] =>

  def beginClaim = {
    Formulate.claimDate(browser)

    browser.goTo("/aboutyou/employment")
    browser.click("#beenEmployedSince6MonthsBeforeClaim_no")
    browser.click("#beenSelfEmployedSince1WeekBeforeClaim_no")
    browser.submit("button[type='submit']")

    titleMustEqual(Messages("s2.g7") + " - About You")
  }
}