package utils.filters

import org.specs2.mutable.{Tags, Specification}
import play.api.test.{PlaySpecification, WithBrowser}


class ChannelShiftIntegrationSpec extends PlaySpecification with Tags {

  "Channel shift url" should {
    "bring you to gov.uk start claim page for uppercase url" in new WithBrowser{
      browser.goTo("/CS2015")

      browser.url() mustEqual "https://www.gov.uk/carers-allowance"
    }

    "bring you to gov.uk start claim page for lowercase url" in new WithBrowser{
      browser.goTo("/cs2015")

      browser.url() mustEqual "https://www.gov.uk/carers-allowance"
    }
  }
}
