package controllers.s4_care_you_provide

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser

class G9ContactDetailsOfPayingPersonIntegrationSpec extends Specification with Tags {
  "Contact details of paying person" should {
    "be presented" in new WithBrowser {
      browser.goTo("/careYouProvide/contactDetailsOfPayingPerson")
      //browser.title() mustEqual "Contact Details of Paying Person - Care You Provide"
    }

    """be submitted and proceed to "breaks" """ in new WithBrowser {
      browser.goTo("/careYouProvide/contactDetailsOfPayingPerson")
      browser.submit("button[value='next']")

      browser.title() mustEqual "Has Breaks - Care You Provide"
    }
  } section "integration"
}