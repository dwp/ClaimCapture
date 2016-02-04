package controllers.feedback

import org.specs2.mutable._
import utils.{LightFakeApplication, WithBrowser}

class GOldClaimFeedbackLinksSpec extends Specification {

  section("unit", models.domain.ThirdParty.id)
  "Various pages with OLD feedback links such as" should {
    "Eligibility benefits page should contain footer feedback link in new tab" in new WithBrowser(app = LightFakeApplication(additionalConfiguration = Map("feedback.cads.enabled" -> "false"))) {
      browser.goTo("/allowance/benefits")

      val footerfeedback = browser.$("#claim-feedback")
      footerfeedback.getText() mustEqual ("Feedback")
      footerfeedback.getAttribute("href") mustEqual ("https://www.gov.uk/done/apply-carers-allowance")
      footerfeedback.getAttribute("rel") mustEqual ("external")
      footerfeedback.getAttribute("target") mustEqual ("_blank")
    }

    "Claim thank you page should contain main link and footer feedback link in new tab" in new WithBrowser(app = LightFakeApplication(additionalConfiguration = Map("feedback.cads.enabled" -> "false"))) {
      browser.goTo("/thankyou/apply-carers")

      // No id on old style main body feedback link need to find 1st matching href
      val mainfeedback = browser.$("a[href=\"https://www.gov.uk/done/apply-carers-allowance\"]").first()
      mainfeedback.getText() mustEqual ("What did you think of this service?")
      mainfeedback.getAttribute("href") mustEqual ("https://www.gov.uk/done/apply-carers-allowance")
      mainfeedback.getAttribute("rel") mustEqual ("external")
      mainfeedback.getAttribute("target") mustEqual ("_blank")
      mainfeedback.getAttribute("class") mustEqual ("secondary")

      val footerfeedback = browser.$("#claim-feedback")
      footerfeedback.getText() mustEqual ("Feedback")
      footerfeedback.getAttribute("href") mustEqual ("https://www.gov.uk/done/apply-carers-allowance")

      footerfeedback.getAttribute("rel") mustEqual ("external")
      footerfeedback.getAttribute("target") mustEqual ("_blank")
    }

    "/error page should contain main link and footer feedback link in new tab" in new WithBrowser(app = LightFakeApplication(additionalConfiguration = Map("feedback.cads.enabled" -> "false"))) {
      browser.goTo("/error")

      // No id on old style main body feedback link need to find 1st matching href
      val mainfeedback = browser.$("a[href=\"https://www.gov.uk/done/apply-carers-allowance\"]").first()
      mainfeedback.getText() mustEqual ("Leave feedback for this service")
      mainfeedback.getAttribute("href") must contain("https://www.gov.uk/done/apply-carers-allowance")
      mainfeedback.getAttribute("rel") mustEqual ("external")
      mainfeedback.getAttribute("target") mustEqual ("_blank")
      mainfeedback.getAttribute("id") mustEqual null

      val footerfeedback = browser.$("#claim-feedback")
      footerfeedback.getText() mustEqual ("Feedback")
      footerfeedback.getAttribute("href") mustEqual ("https://www.gov.uk/done/apply-carers-allowance")
      footerfeedback.getAttribute("rel") mustEqual ("external")
      footerfeedback.getAttribute("target") mustEqual ("_blank")
    }

    "/error-retry page should contain main link and footer feedback link in new tab" in new WithBrowser(app = LightFakeApplication(additionalConfiguration = Map("feedback.cads.enabled" -> "false"))) {
      browser.goTo("/error-retry")

      val footerfeedback = browser.$("#claim-feedback")
      footerfeedback.getText() mustEqual ("Feedback")
      footerfeedback.getAttribute("href") mustEqual ("https://www.gov.uk/done/apply-carers-allowance")
      footerfeedback.getAttribute("rel") mustEqual ("external")
      footerfeedback.getAttribute("target") mustEqual ("_blank")
    }

    "Error cookie page should contain main link and footer feedback link in new tab" in new WithBrowser(app = LightFakeApplication(additionalConfiguration = Map("feedback.cads.enabled" -> "false"))) {
      browser.goTo("/claim-error-cookie-retry")

      // No id on old style main body feedback link need to find 1st matching href
      val mainfeedback = browser.$("a[href=\"https://www.gov.uk/done/apply-carers-allowance\"]").first()
      mainfeedback.getText() mustEqual ("Leave feedback for this service")
      mainfeedback.getAttribute("href") mustEqual ("https://www.gov.uk/done/apply-carers-allowance")
      mainfeedback.getAttribute("rel") mustEqual ("external")
      mainfeedback.getAttribute("target") mustEqual ("_blank")

      val footerfeedback = browser.$("#claim-feedback")
      footerfeedback.getText() mustEqual ("Feedback")
      footerfeedback.getAttribute("href") mustEqual ("https://www.gov.uk/done/apply-carers-allowance")
      footerfeedback.getAttribute("rel") mustEqual ("external")
      footerfeedback.getAttribute("target") mustEqual ("_blank")
    }

    "Error back button page should contain main link and footer feedback link in new tab" in new WithBrowser(app = LightFakeApplication(additionalConfiguration = Map("feedback.cads.enabled" -> "false"))) {
      browser.goTo("/claim-error-browser-backbutton")

      // No id on old style main body feedback link need to find 1st matching href
      val mainfeedback = browser.$("a[href=\"https://www.gov.uk/done/apply-carers-allowance\"]").first()
      mainfeedback.getText() mustEqual ("Leave feedback for this service")
      mainfeedback.getAttribute("href") mustEqual ("https://www.gov.uk/done/apply-carers-allowance")
      mainfeedback.getAttribute("rel") mustEqual ("external")
      mainfeedback.getAttribute("target") mustEqual ("_blank")

      val footerfeedback = browser.$("#claim-feedback")
      footerfeedback.getText() mustEqual ("Feedback")
      footerfeedback.getAttribute("href") mustEqual ("https://www.gov.uk/done/apply-carers-allowance")
      footerfeedback.getAttribute("rel") mustEqual ("external")
      footerfeedback.getAttribute("target") mustEqual ("_blank")
    }
  }
  section("unit", models.domain.ThirdParty.id)
}

