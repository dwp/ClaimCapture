package controllers.s7_employment

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{WithBrowserHelper, BrowserMatchers}

class G1BeenEmployedIntegrationSpec extends Specification with Tags {
  sequential

  "Been Employed" should {
    "present, having indicated that the carer has been employed" in new WithBrowser with BrowserMatchers with EmployedSinceClaimDate {
      beginClaim

      browser.goTo("/employment/been-employed")
      titleMustEqual("Your employment history - Employment History")
    }

   """be bypassed and go onto "other money" having indicated that "employment" is not required.""" in new WithBrowser with BrowserMatchers with NotEmployedSinceClaimDate {
      beginClaim

      browser.goTo("/employment/been-employed")

      titleMustEqual("Details about other money - About Other Money")
    }

    "start employment entry" in new WithBrowser with WithBrowserHelper with BrowserMatchers with EmployedSinceClaimDate {
      beginClaim

      browser.goTo("/employment/been-employed").click("#beenEmployed_yes")
      next
      titleMustEqual("Your job - Employment History")
    }

    "show 1 error upon submitting no mandatory data" in new WithBrowser with WithBrowserHelper with BrowserMatchers with EmployedSinceClaimDate {
      beginClaim

      browser.goTo("/employment/been-employed")
      next
      titleMustEqual("Your employment history - Employment History")
      findMustEqualSize("div[class=validation-summary] ol li", 1)
    }

    """continue to "completion" when there are no more "jobs" to submit.""" in new WithBrowser with WithBrowserHelper with BrowserMatchers with EmployedSinceClaimDate {
      beginClaim

      browser.goTo("/employment/been-employed").click("#beenEmployed_no")
      next
      titleMustEqual("Completion - Employment History")
    }

    """go back to "education".""" in new WithBrowser with WithBrowserHelper with BrowserMatchers with EmployedSinceClaimDate {
      beginClaim

      browser.goTo("/employment/been-employed")
      back
      titleMustEqual("Completion - About your education")
    }

    """not record the "yes/no" answer upon starting to add a new employment but "cancel".""" in new WithBrowser with WithBrowserHelper with BrowserMatchers {
      browser goTo "/employment/been-employed"
      titleMustEqual("Your employment history - Employment History")

      browser.click("#beenEmployed_yes")
      next
      titleMustEqual("Your job - Employment History")

      back
      titleMustEqual("Your employment history - Employment History")
      browser.findFirst("#beenEmployed_yes").isSelected should beFalse
      browser.findFirst("#beenEmployed_no").isSelected should beFalse
    }

    """allow an employment to be added but not record the "yes/no" answer""" in new WithBrowser with WithBrowserHelper with BrowserMatchers {
      def job() = {
        val jobID = "dummyJobID"

        browser.goTo(s"/employment/about-expenses/$jobID")
        browser.click("#payForAnythingNecessary_yes")
        browser.click("#payAnyoneToLookAfterChildren_yes")
        browser.click("#payAnyoneToLookAfterPerson_yes")
        next
        titleMustEqual("Necessary expenses to do your job - Employment History")

        browser.goTo(s"/employment/care-provider/$jobID")
        titleMustEqual("Care provider's contact Details - Employment History")

        next
        titleMustEqual("Job Completion - Employment History")
      }

      browser goTo "/employment/been-employed"
      titleMustEqual("Your employment history - Employment History")

      browser.click("#beenEmployed_yes")
      next

      job()
      next
      titleMustEqual("Your employment history - Employment History")
      browser.findFirst("#beenEmployed_yes").isSelected should beFalse
      browser.findFirst("#beenEmployed_no").isSelected should beFalse
    }

    """remember "employment" upon stating "employment" and returning""" in new WithBrowser with WithBrowserHelper with BrowserMatchers {
      browser goTo "/employment/been-employed"
      titleMustEqual("Your employment history - Employment History")

      browser.click("#beenEmployed_no")
      next
      titleMustEqual("Completion - Employment History")

      back
      titleMustEqual("Your employment history - Employment History")
      browser.findFirst("#beenEmployed_yes").isSelected should beFalse
      browser.findFirst("#beenEmployed_no").isSelected should beTrue
    }
  } section("integration", models.domain.Employed.id)
}