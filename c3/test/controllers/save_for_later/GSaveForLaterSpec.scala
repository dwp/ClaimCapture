package controllers.save_for_later

import models.domain._
import models.view.CachedClaim
import org.specs2.mutable._
import play.api.i18n.Lang
import play.api.test.FakeRequest
import play.api.test.Helpers._
import utils.{LightFakeApplication, WithApplication}
import models.{MultiLineAddress, DayMonthYear, NationalInsuranceNumber}

class GSaveForLaterSpec extends Specification {
  section("unit", models.domain.YourPartner.id)

  // Output from C3EncryptionSpec.scala ..... to create a set of xor pairs and decrypt key
  // With key of:88a976e1-e926-4bb4-9322-15aabc6d0516 created xor pair of:0bcd1234-0000-0000-0000-abcd1234cdef and:174650142322392746796619227917559908601
  val encryptkey = "88a976e1-e926-4bb4-9322-15aabc6d0516"
  val uuid = "0bcd1234-0000-0000-0000-abcd1234cdef"
  val decodeint = "174650142322392746796619227917559908601"

  "Save for later controller" should {
    "block submit when switched off" in new WithApplication(app = LightFakeApplication(additionalConfiguration = Map("saveForLaterSaveEnabled" -> "false"))) with Claiming {
      val request = FakeRequest()
      val result = GSaveForLater.submit(request)
      val bodyText: String = contentAsString(result)
      bodyText must contain("This service is currently switched off")
      status(result) mustEqual BAD_REQUEST
    }

    "block present when switched off" in new WithApplication(app = LightFakeApplication(additionalConfiguration = Map("saveForLaterSaveEnabled" -> "false"))) with Claiming {
      val request = FakeRequest()
      val result = GSaveForLater.present("resumeurl")(request)
      val bodyText: String = contentAsString(result)
      bodyText must contain("This service is currently switched off")
      status(result) mustEqual BAD_REQUEST
    }

    "present save screen" in new WithApplication(app = LightFakeApplication(additionalConfiguration = Map("saveForLaterSaveEnabled" -> "true"))) with Claiming {
      val request = FakeRequest()
      val result = GSaveForLater.present("resumeurl")(request)
      status(result) mustEqual OK
    }

    "allow submit and return save for later success screen" in new WithApplication(app = LightFakeApplication(additionalConfiguration = Map("saveForLaterSaveEnabled" -> "true"))) with Claiming {
      var claim = new Claim(CachedClaim.key, List(), System.currentTimeMillis(), Some(Lang("en")), uuid)
      val details = new YourDetails("Mr","", None, "green", NationalInsuranceNumber(Some("AB123456D")), DayMonthYear(None, None, None))
      val contactDetails = new ContactDetails(new MultiLineAddress(), None, None, None, "yes", Some("bt@bt.com"), Some("bt@bt.com"))
      claim = claim + details + contactDetails
      cache.set(uuid, claim)
      val request = FakeRequest().withFormUrlEncodedBody().withSession(CachedClaim.key -> claim.uuid)
      val result = GSaveForLater.submit(request)
      status(result) mustEqual SEE_OTHER
      redirectLocation(result) must beSome("/save/")
    }

    "not contain resume link when switched OFF" in new WithApplication(app = LightFakeApplication(additionalConfiguration = Map("saveForLaterShowResumeLink" -> "false"))) with Claiming {
      val request = FakeRequest()
      val result = GSaveForLater.present("resumeurl")(request)
      status(result) mustEqual OK
      val bodyText: String = contentAsString(result)
      bodyText must not contain("/resume")
    }

    "contain resume link when switched ON" in new WithApplication(app = LightFakeApplication(additionalConfiguration = Map("saveForLaterShowResumeLink" -> "true"))) with Claiming {
      val request = FakeRequest()
      val result = GSaveForLater.present("resumeurl")(request)
      status(result) mustEqual OK
      val bodyText: String = contentAsString(result)
      bodyText must contain("/resume")
    }
  }
  section("unit", "SaveForLater")
}
