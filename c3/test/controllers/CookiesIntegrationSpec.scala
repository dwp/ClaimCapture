package controllers

import org.specs2.mutable._
import utils.WithBrowser

class CookiesIntegrationSpec extends Specification {
  section("unit")
  "Cookies pages should" should {
    "cookies will be presented in English by default" in new WithBrowser {
      browser.goTo("/cookies/en")
      browser.pageSource() must contain("Cookies")
    }

    "cookies table will display text" in new WithBrowser {
      browser.goTo("/cookies/cookies-table")
      browser.pageSource() must contain("Service cookies")
    }

    "cookies table will display cookies page when back button clicked" in new WithBrowser {
      browser.goTo("/cookies/cookies-table")
      browser.click("#cookies-page");
      browser.pageSource() must contain("puts small files")
    }
  }
  section("unit")
}
