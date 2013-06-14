package integration.s2_aboutyou

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import integration.Helper

class G4ClaimDateSpec extends Specification with Tags {

  "Claim Date" should {
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

    "fill date" in new WithBrowser{
      Helper.fillClaimDate(browser)

      browser.title() mustEqual "More About You - About You"
      browser.find("div[class=completed] ul li h3").get(0).getText mustEqual "4 Your claim date: 01/01/2001"
    }

  } section "integration"
}
