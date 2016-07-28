package controllers.feedback

import org.specs2.mutable._
import utils.{LightFakeApplication, WithBrowser}

class GOldCircsFeedbackLinksSpec extends Specification {
  /* ColinG. Circs Error pages still point to Claims Feedback. No point changing since we move to new feedback soon. */
  val ClaimsFeedbackUrl = "https://www.gov.uk/done/apply-carers-allowance"
  val CircsFeedbackUrl = "https://www.gov.uk/done/report-change-carers-allowance"

  section("unit", models.domain.ThirdParty.id)
  "Various pages with OLD feedback links such as" should {

    /*
      claim.feedback.link = <a href="https://www.gov.uk/done/apply-carers-allowance" rel="external" target="_blank" class="secondary">What did you think of this service?</a> (Takes 30 seconds.)
      circs.feedback.link = <a href="https://www.gov.uk/done/report-change-carers-allowance" rel="external" target="_blank" class="secondary">What did you think of this service?</a> (Takes 30 seconds.)
<a id="claim-feedback" rel="external" target="_blank" href="https://www.gov.uk/done/apply-carers-allowance"
onmousedown="trackEvent('/circumstances/report-changes/selection','Feedback');" onkeydown="trackEvent('/circumstances/report-changes/selection','Feedback');">Feedback</a>
    */

    "Circs start page should contain footer feedback link in newtab" in new WithBrowser(app = LightFakeApplication(additionalConfiguration = Map("feedback.cads.enabled" -> "false"))) {
      browser.goTo("/circumstances/report-changes/selection")

      val footerfeedback = browser.$("#claim-feedback")
      footerfeedback.getText() mustEqual ("Feedback")
      footerfeedback.getAttribute("href") mustEqual (CircsFeedbackUrl)
      footerfeedback.getAttribute("rel") mustEqual ("external")
      footerfeedback.getAttribute("target") mustEqual ("_blank")
    }

    "Circs thank you page should contain main link and footer feedback link in new tab" in new WithBrowser(app = LightFakeApplication(additionalConfiguration = Map("feedback.cads.enabled" -> "false"))) {
      browser.goTo("/thankyou/change-carers")

      // No id on old style main body feedback link need to find 1st matching href
      val mainfeedback = browser.$("a[href=\"" + CircsFeedbackUrl + "\"]").first()
      mainfeedback.getText() mustEqual ("What did you think of this service?")
      mainfeedback.getAttribute("href") mustEqual (CircsFeedbackUrl)
      mainfeedback.getAttribute("rel") mustEqual ("external")
      mainfeedback.getAttribute("target") mustEqual ("_blank")
      mainfeedback.getAttribute("class") mustEqual ("secondary")

      val footerfeedback = browser.$("#claim-feedback")
      footerfeedback.getText() mustEqual ("Feedback")
      footerfeedback.getAttribute("href") mustEqual (CircsFeedbackUrl)
      footerfeedback.getAttribute("rel") mustEqual ("external")
      footerfeedback.getAttribute("target") mustEqual ("_blank")
    }


    "Error circs page should contain main link and footer feedback link in new tab" in new WithBrowser(app = LightFakeApplication(additionalConfiguration = Map("feedback.cads.enabled" -> "false"))) {
      browser.goTo("/circs-error")

      // No id on old style main body feedback link need to find 1st matching href
      val mainfeedback = browser.$("a[href=\"" + CircsFeedbackUrl + "\"]").first()
      mainfeedback.getText() mustEqual ("What did you think of this service?")
      mainfeedback.getAttribute("href") mustEqual (CircsFeedbackUrl)
      mainfeedback.getAttribute("rel") mustEqual ("external")
      mainfeedback.getAttribute("target") mustEqual ("_blank")

      val footerfeedback = browser.$("#claim-feedback")
      footerfeedback.getText() mustEqual ("Feedback")
      footerfeedback.getAttribute("href") mustEqual (CircsFeedbackUrl)
      footerfeedback.getAttribute("rel") mustEqual ("external")
      footerfeedback.getAttribute("target") mustEqual ("_blank")
    }

  }
  section("unit", models.domain.ThirdParty.id)
}

