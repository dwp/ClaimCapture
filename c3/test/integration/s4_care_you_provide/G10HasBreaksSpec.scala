package integration.s4_care_you_provide

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser

class G10HasBreaksSpec extends Specification with Tags {
  "Care you provide" should {
    """present "has breaks" """ in new WithBrowser {
      browser.goTo("/careYouProvide/hasBreaks")
      browser.title() mustEqual "Has Breaks - Care You Provide"
    }

    """re-present "has breaks" upon missing mandatory data""" in new WithBrowser {
      browser.goTo("/careYouProvide/hasBreaks")
      browser.submit("button[value='next']")
      browser.title() mustEqual "Has Breaks - Care You Provide"
    }

    """present "completed" when no more breaks are required""" in new WithBrowser {
      browser.goTo("/careYouProvide/hasBreaks")
      browser.click("#answer_no")
      browser.submit("button[value='next']")
      browser.pageSource() must contain("Completed - Care You Provide")
    }

    "show warning when changing answer after adding breaks" in new WithBrowser {
      browser.goTo("/careYouProvide/hasBreaks")
      browser.click("#answer_yes")
      browser.submit("button[value='next']")
      browser.title() mustEqual "Breaks in Care - Care You Provide"

      browser.click("#break_start_day option[value='1']")
      browser.click("#break_start_month option[value='1']")
      browser.fill("#break_start_year") `with` "2001"
      browser.click("#break_whereYou_location option[value='Holiday']")
      browser.click("#break_wherePerson_location option[value='Holiday']")
      browser.click("#moreBreaks_yes")

      browser.submit("button[value='next']")

      browser.goTo("/careYouProvide/hasBreaks")
      browser.click("#answer_no")

      browser.findFirst("#hasBreaksNoMessage").getElement.isDisplayed must beTrue
    }

    "go back to contact details" in new WithBrowser {
      pending("Once 'Contact details' are done, this example must be written")
    }
  } section "integration"
}