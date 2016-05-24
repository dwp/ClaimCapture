package controllers.s_information

import app.ConfigProperties._
import models.domain.Claiming
import org.specs2.mutable._
import play.api.test.FakeRequest
import play.api.test.Helpers._
import models.view.CachedClaim
import utils.pageobjects.s_information.GAdditionalInfoPage
import utils.{WithBrowser, WithApplication}

class GAdditionalInformationSpec extends Specification {
  val validYesInput = Seq(
    "anythingElse.answer" -> "yes",
    "anythingElse.text" -> "Additional info text",
    "welshCommunication" -> "yes"
  )

  val invalidYesInput = Seq(
    "anythingElse.answer" -> "yes",
    "anythingElse.text" -> "  ",
    "welshCommunication" -> "yes"
  )

  val additionalInfoPath = "OtherInformation//AdditionalInformation//Why//Answer"

  section("unit", models.domain.AdditionalInfo.id)
  "Additional information" should {
    "present" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)

      val result = GAdditionalInfo.present(request)
      status(result) mustEqual OK
    }

    """enforce answer to all questions""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)

      val result = GAdditionalInfo.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    """accept answers""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)
        .withFormUrlEncodedBody(validYesInput: _*)

      val result = GAdditionalInfo.submit(request)
      redirectLocation(result) must beSome("/preview")
    }

    """fail when anything else text us empty""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)
        .withFormUrlEncodedBody(invalidYesInput: _*)

      val result = GAdditionalInfo.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "handle gracefully when bad schema number passed to SchemaValidation getRestriction" in new WithApplication {
      val schemaVersion = "BAD-SCHEMA"
      schemaMaxLength(schemaVersion, additionalInfoPath) mustEqual -1
    }

    "pull maxlength from xml commons OK" in new WithApplication {
      val schemaVersion = getStringProperty("xml.schema.version", throwError = false)
      schemaVersion must not be ""
      schemaMaxLength(schemaVersion, additionalInfoPath) mustEqual 3000
    }

    "have text maxlength set correctly in present()" in new WithBrowser {
      browser.goTo(GAdditionalInfoPage.url)
      browser.click("#anythingElse_answer_yes")
      val anythingelse = browser.$("#anythingElse_text")
      // use + css selector to find adjacent sibling i.e. <textarea id=anythingElse_text></textarea><span class=countdown><span>
      val countdown = browser.$("#anythingElse_text + .countdown")

      anythingelse.getAttribute("maxlength") mustEqual "3000"
      countdown.getText must contain( "3000 char")
      browser.pageSource must contain("maxChars:3000")
    }
  }
  section("unit", models.domain.AdditionalInfo.id)
}
