package integration.s2_aboutyou

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import integration.Helper

class G4ClaimDateSpec extends Specification with Tags {

  "Claim Date" should{
    "be presented" in new WithBrowser {
      browser.goTo("/aboutyou/claimDate")
      browser.title() mustEqual "Claim Date - About You"
    }

    "contain 2 completed forms" in new WithBrowser {
      Helper.fillYourDetails(browser)
      Helper.fillContactDetails(browser)

      browser.title() mustEqual "Claim Date - About You"
      browser.find("div[class=completed] ul li").size() mustEqual 2


    }
  } section "integration"
}
