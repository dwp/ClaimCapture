package controllers.s7_employment

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{WithBrowserHelper, Formulate, BrowserMatchers}

class EmploymentIntegrationSpec extends Specification with Tags {
  "Employment - Integration" should {
    "present completion" in new WithBrowser with WithBrowserHelper with BrowserMatchers {
      goTo("/employment/completed")
      titleMustEqual("Completion - Employment History")
    }

    """progress to next section i.e. "self employed".""" in new WithBrowser with WithBrowserHelper with BrowserMatchers {
      goTo("/employment/completed")
      titleMustEqual("Completion - Employment History")

      next
      titleMustEqual("Your job - About self-employment")
    }

    """go back to start of employment i.e. "employment history".""" in new WithBrowser with WithBrowserHelper with BrowserMatchers with EmployedSinceClaimDate {
      skipped("ISSUE - This can't be done anymore")
      beginClaim()

      goTo("/employment/completed")
      titleMustEqual("Completion - Employment History")

      back
      titleMustEqual("Your employment history - Employment History")
    }
  } section("integration", models.domain.Employed.id)
}

trait EmployedSinceClaimDate extends BrowserMatchers {
  this: WithBrowser[_] =>

  def beginClaim() = {
    Formulate.claimDate(browser)
    titleMustEqual("Your nationality and residency - About you - the carer")

    Formulate.employment(browser)
    titleMustEqual("Completion - About you - the carer")
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

    titleMustEqual("Completion - About you - the carer")
  }
}