package integration.s2_aboutyou

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser

class G4ClaimDateSpec extends Specification with Tags {

  "Claim Date" should{
    "be presented" in new WithBrowser {
      browser.goTo("/aboutyou/claimDate")
      browser.title() mustEqual "Claim Date - About You"
    }

    "contain 2 completed forms" in new WithBrowser {
      browser.goTo("/aboutyou/yourDetails")

      browser.click("#title option[value='mr']")
      browser.fill("#firstName")`with`("John")
      browser.fill("#surname")`with`("Appleseed")
      browser.click("#dateOfBirth-day option[value='3']")
      browser.click("#dateOfBirth-month option[value='4']")
      browser.fill("#dateOfBirth-year")`with`("1950")
      browser.fill("#nationality")`with`("English")
      browser.click("#maritalStatus option[value='s']")
      browser.click("#alwaysLivedUK_yes")
      browser.submit("button[type='submit']")

      browser.title() mustEqual "Contact Details - About You"
      browser.fill("#address")`with`("My Address")
      browser.fill("#postcode")`with`("SE1 6EH")
      browser.submit("button[type='submit']")

      browser.title() mustEqual "Claim Date - About You"
      browser.find("div[class=completed] ul li").size() mustEqual 2


    }
  } section "integration"
}
