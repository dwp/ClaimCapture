package controllers.save_for_later

import models.domain._
import models.view.CachedClaim
import models.view.cache.{EncryptedCacheHandling, CacheHandling}
import org.specs2.mutable._
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
      println("bodyText:"+bodyText)
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
      println("Submit result:"+bodyText)
      println( "Status:"+status(result))
      bodyText must contain("This application can't be found")
      status(result) mustEqual BAD_REQUEST
    }

    "return validation error not found when claim not found" in new WithApplication(app = LightFakeApplication(additionalConfiguration = Map("saveForLaterResumeEnabled" -> "true"))) with Claiming {
      val request=FakeRequest(GET, "?savekey=1234")
      val result=GResume.present(request)
      val bodyText: String = contentAsString(result)
      println("Submit result:"+bodyText)
      println( "Status:"+status(result))
      bodyText must contain("This application can't be found")
      status(result) mustEqual BAD_REQUEST
    }
/*
    "return validation error when no fields completed" in new WithApplication(app = LightFakeApplication(additionalConfiguration = Map("saveForLaterResumeEnabled" -> "true"))) with Claiming {
      var claim = new Claim(CachedClaim.key, uuid="123456")
      val encryptedCacheHandling = new EncryptedCacheHandling() { val cacheKey = "123456" }
      encryptedCacheHandling.saveForLaterInCache(claim, "/lastlocation")

      val request = FakeRequest().withFormUrlEncodedBody("savekey" -> "123456")
      val result = GResume.present(request)
      val bodyText: String = contentAsString(result)
      println("Submit result:"+bodyText)
      println( "Status:"+status(result))
      status(result) mustEqual BAD_REQUEST
    }
*/
  }
  section("unit", models.domain.YourPartner.id)
}
