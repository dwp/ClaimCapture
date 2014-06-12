package controllers.s1_2_claim_date

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{Formulate, BrowserMatchers}

class G1ClaimDateIntegrationSpec extends Specification with Tags {

  "Your claim date" should {

    "be presented " in new WithBrowser with BrowserMatchers {
      browser.goTo("/your-claim-date/claim-date")
      titleMustEqual("Claim date - When do you want Carer's Allowance to start?")
    }

    "navigate to next section" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)

      browser.goTo("/about-you/your-details")
      titleMustEqual("Your details - About you - the carer")
    }

  } section("unit", models.domain.YourClaimDate.id)
}
