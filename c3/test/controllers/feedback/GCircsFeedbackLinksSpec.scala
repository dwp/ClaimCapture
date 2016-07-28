package controllers.feedback

import org.specs2.mutable._
import utils.WithBrowser

class GCircsFeedbackLinksSpec extends Specification {
  val CircsFeedbackUrl = "/circumstances/feedback"

  section("unit", models.domain.ThirdParty.id)
  "Various pages with feedback links such as" should {
    "Circs start page should contain footer feedback link in newtab" in new WithBrowser() {
      browser.goTo("/circumstances/report-changes/selection")

      val footerfeedback = browser.$("#footer-feedback")
      footerfeedback.getText() mustEqual ("Feedback")
      footerfeedback.getAttribute("href") must contain(CircsFeedbackUrl)
      footerfeedback.getAttribute("rel") mustEqual ("external")
      footerfeedback.getAttribute("target") mustEqual ("_blank")
    }

    "Circs thank you page should contain main link and footer feedback link in same tab" in new WithBrowser {
      browser.goTo("/thankyou/change-carers")

      val mainfeedback = browser.$("#feedback")
      mainfeedback.getText() mustEqual ("What did you think of this service?")
      mainfeedback.getAttribute("href") must contain(CircsFeedbackUrl)
      mainfeedback.getAttribute("rel") mustEqual null
      mainfeedback.getAttribute("target") mustEqual null

      val footerfeedback = browser.$("#footer-feedback")
      footerfeedback.getText() mustEqual ("Feedback")
      footerfeedback.getAttribute("href") must contain(CircsFeedbackUrl)
      footerfeedback.getAttribute("rel") mustEqual null
      footerfeedback.getAttribute("target") mustEqual null
    }

    "Error circs page should contain main link and footer feedback link in same tab" in new WithBrowser {
      browser.goTo("/circs-error")

      val mainfeedback = browser.$("#feedback")
      mainfeedback.getText() mustEqual ("What did you think of this service?")
      mainfeedback.getAttribute("href") must contain(CircsFeedbackUrl)
      mainfeedback.getAttribute("rel") mustEqual null
      mainfeedback.getAttribute("target") mustEqual null

      val footerfeedback = browser.$("#footer-feedback")
      footerfeedback.getText() mustEqual ("Feedback")
      footerfeedback.getAttribute("href") must contain(CircsFeedbackUrl)
      footerfeedback.getAttribute("rel") mustEqual null
      footerfeedback.getAttribute("target") mustEqual null
    }
  }
  section("unit", models.domain.ThirdParty.id)
}

