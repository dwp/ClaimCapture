package controllers.s_information

import app.ConfigProperties._
import gov.dwp.carers.xml.schemavalidations.SchemaValidation
import models.domain.Claiming
import org.specs2.mutable._
import play.api.test.FakeRequest
import play.api.test.Helpers._
import models.view.CachedClaim
import utils.{LightFakeApplication, WithApplication}

class GAdditionalInformationSpec extends Specification {
  val validYesInput = Seq(
    "anythingElse.answer" -> "yes",
    "anythingElse.text" -> "Additional info text",
    "welshCommunication" -> "yes"
  )

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

    "handle gracefully when bad schema number passed to SchemaValidation getRestriction" in new WithApplication {
      val schemaVersion = "BAD-SCHEMA"
      additionalInformationMaxLength(schemaVersion) mustEqual 2990
    }

    "pull maxlength from xml commons OK" in new WithApplication {
      val schemaVersion = getProperty("xml.schema.version", "NOT-SET")
      schemaVersion must not be "NOT-SET"
      additionalInformationMaxLength(schemaVersion) mustEqual 3000
    }
/*
    "have text maxlength set correctly in present()" in new WithApplication {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)
        .withFormUrlEncodedBody(validYesInput: _*)

      val result = GAdditionalInfo.present(request)
      val source = contentAsString(result)
      // we have max in 3 places 1) on the textarea 2) on the char counter 3) on the char counter javascript init
      source must contain("maxLength=\"3000\"")
      source must contain("3000 characters left")
      source must contain("maxChars:3000")
    }
*/
  }
  section("unit", models.domain.AdditionalInfo.id)

  def additionalInformationMaxLength(schemaVersion: String) = {
    lazy val validation = new SchemaValidation(schemaVersion)
    Option(validation.getRestriction("OtherInformation//AdditionalInformation//Why//Answer")) match {
      case Some(restriction) => if (restriction.getMaxlength != null) restriction.getMaxlength else 2990
      case _ => 2990
    }
  }
}
