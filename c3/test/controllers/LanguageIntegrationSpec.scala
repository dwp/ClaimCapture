package controllers

import play.api.test.WithBrowser
import org.specs2.mutable.{Tags, Specification}

class LanguageIntegrationSpec extends Specification with Tags {
  "Language - Change Language" should {
    "claim will be presented in English by default" in new WithBrowser {
      browser.goTo("/allowance/benefits")
      browser.pageSource() must contain("Yes")
    }

    "claim will be presented in Welsh" in new WithBrowser {
      browser.goTo("/allowance/benefits")
      browser.waitUntil(browser.click("#lang-cy"))
      browser.pageSource() must contain("Nesaf")
    }

    "claim will be presented in English after selecting Welsh and then English" in new WithBrowser {
      browser.goTo("/allowance/benefits")
      browser.waitUntil(browser.click("#lang-cy"))
      browser.waitUntil(browser.click("#lang-en"))
      browser.pageSource() must contain("Yes")
    }

    "change of circs will be presented in English by default" in new WithBrowser {
      browser.goTo("/circumstances/identification/about-you")
      browser.pageSource() must contain("details")
    }

    "change of circs will be presented in Welsh" in new WithBrowser {
      browser.goTo("/circumstances/identification/about-you")
      browser.waitUntil(browser.click("#lang-cy"))
      browser.pageSource() must contain("Nesaf")
    }

    "change of circs will be presented in English after selecting Welsh and then English" in new WithBrowser {
      browser.goTo("/circumstances/identification/about-you")
      browser.waitUntil(browser.click("#lang-cy"))
      browser.waitUntil(browser.click("#lang-en"))
      browser.pageSource() must contain("details")
    }
  } section "unit"
}
