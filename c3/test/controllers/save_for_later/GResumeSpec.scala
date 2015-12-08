package controllers.save_for_later

import models.{DayMonthYear, NationalInsuranceNumber}
import models.domain._
import models.view.CachedClaim
import models.view.cache.{EncryptedCacheHandling, CacheHandling}
import org.specs2.mutable._
import play.api.i18n.Lang
import play.api.test.FakeRequest
import play.api.test.Helpers._
import utils.{LightFakeApplication, WithApplication}

class GResumeSpec extends Specification {

  "Resume controller" should {
    "block present when switched off" in new WithApplication(app = LightFakeApplication(additionalConfiguration = Map("saveForLaterResumeEnabled" -> "false"))) with Claiming {
      val request = FakeRequest()
      val result = GResume.present(request)
      val bodyText: String = contentAsString(result)
      bodyText must contain("This service is currently switched off")
      status(result) mustEqual BAD_REQUEST
    }

    "block submit when switched off" in new WithApplication(app = LightFakeApplication(additionalConfiguration = Map("saveForLaterResumeEnabled" -> "false"))) with Claiming {
      val request = FakeRequest()
      val result = GResume.submit(request)
      val bodyText: String = contentAsString(result)
      bodyText must contain("This service is currently switched off")
      status(result) mustEqual BAD_REQUEST
    }


    "return error present when switched off" in new WithApplication(app = LightFakeApplication(additionalConfiguration = Map("saveForLaterResumeEnabled" -> "false"))) with Claiming {
      val request = FakeRequest()
      val result = GResume.present(request)
      val bodyText: String = contentAsString(result)
      bodyText must contain("This service is currently switched off")
      status(result) mustEqual BAD_REQUEST
    }


    "return validation error not found when no uuid is passed to present()" in new WithApplication(app = LightFakeApplication(additionalConfiguration = Map("saveForLaterResumeEnabled" -> "true"))) with Claiming {
      val request = FakeRequest()
      val result = GResume.present(request)
      val bodyText: String = contentAsString(result)
      bodyText must contain("This application can't be found")
      status(result) mustEqual BAD_REQUEST
    }

    "return validation error not found when claim not found" in new WithApplication(app = LightFakeApplication(additionalConfiguration = Map("saveForLaterResumeEnabled" -> "true"))) with Claiming {
      val request=FakeRequest(GET, "?savekey=1234")
      val result=GResume.present(request)
      val bodyText: String = contentAsString(result)
      bodyText must contain("This application can't be found")
      status(result) mustEqual BAD_REQUEST
    }

    "return ok resume screen when claim is found in sfl cache" in new WithApplication(app = LightFakeApplication(additionalConfiguration = Map("saveForLaterResumeEnabled" -> "true"))) with Claiming {
      var claim = new Claim(CachedClaim.key, uuid="123456")
      val details = new YourDetails("",None, "",None, "green",NationalInsuranceNumber(Some("AB123456D")), DayMonthYear(None, None, None))
      claim=claim+details
      val encryptedCacheHandling = new EncryptedCacheHandling() { val cacheKey = "123456" }
      encryptedCacheHandling.saveForLaterInCache(claim, "/lastlocation")

      val request=FakeRequest(GET, "?savekey=123456")
      val result = GResume.present(request)
      val bodyText: String = contentAsString(result)
      status(result) mustEqual OK
      bodyText must contain( "Enter your details to resume your application")
    }

    /* HOW TEST EXPIRED CLAIM ??? This needs setting by back end process*/
    "return sfl claim expired screen when claim is found in sfl cache with status of EXPIRED" in new WithApplication(app = LightFakeApplication(additionalConfiguration = Map("saveForLaterResumeEnabled" -> "true"))) with Claiming {
    }

    "return sfl claim screen with 2 retries left when claim has been tried once" in new WithApplication(app = LightFakeApplication(additionalConfiguration = Map("saveForLaterResumeEnabled" -> "true"))) with Claiming {
/*
      var claim = new Claim(CachedClaim.key, List(), System.currentTimeMillis(), Some(Lang("en")), "123456")
      val details = new YourDetails("",None, "",None, "green",NationalInsuranceNumber(Some("AB123456D")), DayMonthYear(None, None, None))
      claim=claim+details
      val encryptedCacheHandling = new EncryptedCacheHandling() { val cacheKey = "123456" }
      encryptedCacheHandling.saveForLaterInCache(claim, "/lastlocation")

      // Do a fail try to resume
      val submitrequest=FakeRequest()
        .withFormUrlEncodedBody("uuid" -> "123456", "surname"->"red")
      val failresult = GResume.submit(request)
      status(result) mustEqual BAD_REQUEST

      // Try again from link in email
      val request=FakeRequest(GET, "?savekey=123456")
      val result = GResume.present(request)
      val bodyText: String = contentAsString(result)
      println("Submit result:"+bodyText)
      println( "Status:"+status(result))
      status(result) mustEqual OK
      bodyText must contain( "Enter your details to resume your application")
      */
    }

    "return sfl claim screen with 1 retries left when claim has been tried once" in new WithApplication(app = LightFakeApplication(additionalConfiguration = Map("saveForLaterResumeEnabled" -> "true"))) with Claiming {
    }

    "return failed final screen when claim has been tried three times" in new WithApplication(app = LightFakeApplication(additionalConfiguration = Map("saveForLaterResumeEnabled" -> "true"))) with Claiming {
    }
  }
  section("unit", models.domain.YourPartner.id)
}
