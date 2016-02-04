package controllers.feedback

import org.specs2.mutable._
import utils.{WithBrowser}

class GFeedbackLinksSpec extends Specification {

  section("unit", models.domain.ThirdParty.id)
  "Various pages with feedback links such as" should {

    /*
      claim.feedback.link = <a href="https://www.gov.uk/done/apply-carers-allowance" rel="external" target="_blank" class="secondary">What did you think of this service?</a> (Takes 30 seconds.)
      circs.feedback.link = <a href="https://www.gov.uk/done/report-change-carers-allowance" rel="external" target="_blank" class="secondary">What did you think of this service?</a> (Takes 30 seconds.)
    */

    "Eligibility benefits page should contain footer feedback link in newtab" in new WithBrowser {
      browser.goTo("/allowance/benefits")

      val footerfeedback = browser.$("#footer-feedback")
      footerfeedback.getText() mustEqual ("Feedback")
      footerfeedback.getAttribute("href") must contain("/feedback/feedback")
      footerfeedback.getAttribute("rel") mustEqual ("external")
      footerfeedback.getAttribute("target") mustEqual ("_blank")
    }

    "/error page should contain main link and footer feedback link in same tab" in new WithBrowser {
      browser.goTo("/error")

      val mainfeedback = browser.$("#feedback")
      mainfeedback.getText() mustEqual ("Leave feedback for this service")
      mainfeedback.getAttribute("href") must contain("/feedback/feedback")
      mainfeedback.getAttribute("rel") mustEqual null
      mainfeedback.getAttribute("target") mustEqual null

      val footerfeedback = browser.$("#footer-feedback")
      footerfeedback.getText() mustEqual ("Feedback")
      footerfeedback.getAttribute("href") must contain("/feedback/feedback")
      footerfeedback.getAttribute("rel") mustEqual null
      footerfeedback.getAttribute("target") mustEqual null
    }
    "Error cookie page should contain main link and footer feedback link in same tab" in new WithBrowser {
      browser.goTo("/claim-error-cookie-retry")

      val mainfeedback = browser.$("#feedback")
      mainfeedback.getText() mustEqual ("Leave feedback for this service")
      mainfeedback.getAttribute("href") must contain("/feedback/feedback")
      mainfeedback.getAttribute("rel") mustEqual null
      mainfeedback.getAttribute("target") mustEqual null

      val footerfeedback = browser.$("#footer-feedback")
      footerfeedback.getText() mustEqual ("Feedback")
      footerfeedback.getAttribute("href") must contain("/feedback/feedback")
      footerfeedback.getAttribute("rel") mustEqual null
      footerfeedback.getAttribute("target") mustEqual null
    }
    "Error back button page should contain main link and footer feedback link in same tab" in new WithBrowser {
      browser.goTo("/claim-error-browser-backbutton")

      val mainfeedback = browser.$("#feedback")
      mainfeedback.getText() mustEqual ("Leave feedback for this service")
      mainfeedback.getAttribute("href") must contain("/feedback/feedback")
      mainfeedback.getAttribute("rel") mustEqual null
      mainfeedback.getAttribute("target") mustEqual null

      val footerfeedback = browser.$("#footer-feedback")
      footerfeedback.getText() mustEqual ("Feedback")
      footerfeedback.getAttribute("href") must contain("/feedback/feedback")
      footerfeedback.getAttribute("rel") mustEqual null
      footerfeedback.getAttribute("target") mustEqual null
    }

    "Error circs page should contain main link and footer feedback link in same tab" in new WithBrowser {
      browser.goTo("/circs-error")

      val mainfeedback = browser.$("#feedback")
      mainfeedback.getText() mustEqual ("Leave feedback for this service")
      mainfeedback.getAttribute("href") must contain("/feedback/feedback")
      mainfeedback.getAttribute("rel") mustEqual null
      mainfeedback.getAttribute("target") mustEqual null

      val footerfeedback = browser.$("#footer-feedback")
      footerfeedback.getText() mustEqual ("Feedback")
      footerfeedback.getAttribute("href") must contain("/feedback/feedback")
      footerfeedback.getAttribute("rel") mustEqual null
      footerfeedback.getAttribute("target") mustEqual null
    }

    "Claim thank you page should contain main link and footer feedback link in same tab" in new WithBrowser {
      browser.goTo("/thankyou/apply-carers")
      val mainfeedback = browser.$("#feedback")
      mainfeedback.getText() mustEqual ("What did you think of this service?")
      mainfeedback.getAttribute("href") must contain("/feedback/feedback")
      mainfeedback.getAttribute("rel") mustEqual null
      mainfeedback.getAttribute("target") mustEqual null

      val footerfeedback = browser.$("#footer-feedback")
      footerfeedback.getText() mustEqual ("Feedback")
      footerfeedback.getAttribute("href") must contain("/feedback/feedback")
      footerfeedback.getAttribute("rel") mustEqual null
      footerfeedback.getAttribute("target") mustEqual null
    }

    "Circs thank you page should contain main link and footer feedback link in same tab" in new WithBrowser {
      browser.goTo("/thankyou/change-carers")
      val mainfeedback = browser.$("#feedback")
      mainfeedback.getText() mustEqual ("What did you think of this service?")
      mainfeedback.getAttribute("href") must contain("/circumstances/feedback")
      mainfeedback.getAttribute("rel") mustEqual null
      mainfeedback.getAttribute("target") mustEqual null

      val footerfeedback = browser.$("#footer-feedback")
      footerfeedback.getText() mustEqual ("Feedback")
      footerfeedback.getAttribute("href") must contain("/circumstances/feedback")
      footerfeedback.getAttribute("rel") mustEqual null
      footerfeedback.getAttribute("target") mustEqual null
    }
  }
  section("unit", models.domain.ThirdParty.id)
}

