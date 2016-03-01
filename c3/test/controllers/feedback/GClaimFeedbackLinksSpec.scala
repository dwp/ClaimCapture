package controllers.feedback

import controllers.s_eligibility
import models.domain.{Claiming, Eligibility, Benefits, Claim}
import models.view.CachedClaim
import org.specs2.mutable._
import play.api.test.FakeRequest
import play.api.test.Helpers._
import utils.{WithJsBrowser, LightFakeApplication, WithBrowser}

class GClaimFeedbackLinksSpec extends Specification {
  val ClaimsFeedbackUrl = "/feedback/feedback"
  val OldClaimsFeedbackUrl = "/done/apply-carers-allowance"

  section("unit", models.domain.ThirdParty.id)
  "Various pages with feedback links such as" should {

    "Eligibility benefits page should contain footer feedback link in newtab" in new WithBrowser {
      browser.goTo("/allowance/benefits")

      val footerfeedback = browser.$("#footer-feedback")
      footerfeedback.getText() mustEqual ("Feedback")
      footerfeedback.getAttribute("href") must contain(ClaimsFeedbackUrl)
      footerfeedback.getAttribute("rel") mustEqual ("external")
      footerfeedback.getAttribute("target") mustEqual ("_blank")
    }

    "Claim thank you page should contain main link and footer feedback link in same tab" in new WithBrowser {
      browser.goTo("/thankyou/apply-carers")

      val mainfeedback = browser.$("#feedback")
      mainfeedback.getText() mustEqual ("What did you think of this service?")
      mainfeedback.getAttribute("href") must contain(ClaimsFeedbackUrl)
      mainfeedback.getAttribute("rel") mustEqual null
      mainfeedback.getAttribute("target") mustEqual null
      mainfeedback.getAttribute("class") mustEqual ("secondary")

      val footerfeedback = browser.$("#footer-feedback")
      footerfeedback.getText() mustEqual ("Feedback")
      footerfeedback.getAttribute("href") must contain(ClaimsFeedbackUrl)
      footerfeedback.getAttribute("rel") mustEqual null
      footerfeedback.getAttribute("target") mustEqual null
    }

    "Back button page should contain main link and footer feedback link in same tab" in new WithBrowser {
      browser.goTo("/back-button-page")

      val mainfeedback = browser.$("#feedback")
      mainfeedback.getText() mustEqual ("What did you think of this service?")
      mainfeedback.getAttribute("href") must contain(ClaimsFeedbackUrl)
      mainfeedback.getAttribute("rel") mustEqual null
      mainfeedback.getAttribute("target") mustEqual null

      val footerfeedback = browser.$("#footer-feedback")
      footerfeedback.getText() mustEqual ("Feedback")
      footerfeedback.getAttribute("href") must contain(ClaimsFeedbackUrl)
      footerfeedback.getAttribute("rel") mustEqual null
      footerfeedback.getAttribute("target") mustEqual null
    }

    "Back button page should contain both english and welsh feedback links" in new WithBrowser {
      browser.goTo("/back-button-page")

      val englishfeedback = browser.$("a[id=\"feedback\"]").get(0)
      englishfeedback.getText() mustEqual ("What did you think of this service?")
      englishfeedback.getAttribute("href") must contain(ClaimsFeedbackUrl)
      englishfeedback.getAttribute("rel") mustEqual null
      englishfeedback.getAttribute("target") mustEqual null

      val welshfeedback = browser.$("a[id=\"feedback\"]").get(1)
      welshfeedback.getText() mustEqual ("Beth oeddech chi'n ei feddwl o'r gwasanaeth hwn?")
      welshfeedback.getAttribute("href") must contain(ClaimsFeedbackUrl)
      welshfeedback.getAttribute("rel") mustEqual null
      welshfeedback.getAttribute("target") mustEqual null
    }

    /*
    Finish link after answering ineligible and saying No to Js question "Do you want to apply?" so check the hidden item.
    <div class="finish-button" id="feedbackLinkId" style="display:none">@Html(messages("claim.eligibility.feedback.link"))</div>
    claim.eligibility.feedback.link = <a href="https://www.gov.uk/done/apply-carers-allowance" rel="external" target="_blank" class="secondary" onclick="analyticEligibilityCallback()">Finish</a>
     */
    "Approval page when ineligible should contain finish link and footer feedback link in same tab" in new WithJsBrowser with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)
      val claim = Claim(CachedClaim.key).update(Benefits(benefitsAnswer = "yes"))
        .update(Eligibility(hours = "yes", over16 = "no", livesInGB = "yes"))
      cache.set("default" + claimKey, claim)

      val result = s_eligibility.CarersAllowance.approve(request)
      val source = contentAsString(result)
      source must not contain (OldClaimsFeedbackUrl)
    }

    "Consent and declaration error page should contain main link and footer feedback link in same tab" in new WithBrowser {
      browser.goTo("/consent-and-declaration/error")
      val footerfeedback = browser.$("#footer-feedback")
      footerfeedback.getText() mustEqual ("Feedback")
      footerfeedback.getAttribute("href") must contain(ClaimsFeedbackUrl)
      footerfeedback.getAttribute("rel") mustEqual null
      footerfeedback.getAttribute("target") mustEqual null
    }

    "/error page should contain main link and footer feedback link in same tab" in new WithBrowser {
      browser.goTo("/error")

      val mainfeedback = browser.$("#feedback")
      mainfeedback.getText() mustEqual ("Leave feedback for this service")
      mainfeedback.getAttribute("href") must contain(ClaimsFeedbackUrl)
      mainfeedback.getAttribute("rel") mustEqual null
      mainfeedback.getAttribute("target") mustEqual null

      val footerfeedback = browser.$("#footer-feedback")
      footerfeedback.getText() mustEqual ("Feedback")
      footerfeedback.getAttribute("href") must contain(ClaimsFeedbackUrl)
      footerfeedback.getAttribute("rel") mustEqual null
      footerfeedback.getAttribute("target") mustEqual null
    }

    "Error cookie page should contain main link and footer feedback link in same tab" in new WithBrowser {
      browser.goTo("/claim-error-cookie-retry")

      val mainfeedback = browser.$("#feedback")
      mainfeedback.getText() mustEqual ("Leave feedback for this service")
      mainfeedback.getAttribute("href") must contain(ClaimsFeedbackUrl)
      mainfeedback.getAttribute("rel") mustEqual null
      mainfeedback.getAttribute("target") mustEqual null

      val footerfeedback = browser.$("#footer-feedback")
      footerfeedback.getText() mustEqual ("Feedback")
      footerfeedback.getAttribute("href") must contain(ClaimsFeedbackUrl)
      footerfeedback.getAttribute("rel") mustEqual null
      footerfeedback.getAttribute("target") mustEqual null
    }

    "Error back button page should contain main link and footer feedback link in same tab" in new WithBrowser {
      browser.goTo("/claim-error-browser-backbutton")

      val mainfeedback = browser.$("#feedback")
      mainfeedback.getText() mustEqual ("Leave feedback for this service")
      mainfeedback.getAttribute("href") must contain(ClaimsFeedbackUrl)
      mainfeedback.getAttribute("rel") mustEqual null
      mainfeedback.getAttribute("target") mustEqual null

      val footerfeedback = browser.$("#footer-feedback")
      footerfeedback.getText() mustEqual ("Feedback")
      footerfeedback.getAttribute("href") must contain(ClaimsFeedbackUrl)
      footerfeedback.getAttribute("rel") mustEqual null
      footerfeedback.getAttribute("target") mustEqual null
    }

  }
  section("unit", models.domain.ThirdParty.id)
}

