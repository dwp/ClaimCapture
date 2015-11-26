package utils.filters

import utils.{PlaySpecification, WithBrowser}


class ChannelShiftIntegrationSpec extends PlaySpecification {

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
  section("slow")
}
