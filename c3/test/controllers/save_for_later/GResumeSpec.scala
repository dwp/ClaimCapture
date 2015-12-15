package controllers.save_for_later

import models.{DayMonthYear, NationalInsuranceNumber}
import models.domain._
import models.view.CachedClaim
import models.view.cache.{EncryptedCacheHandling, CacheHandling}
import org.specs2.mutable._
import play.api.test.FakeRequest
import play.api.test.Helpers._
import utils.{SaveForLaterEncryption, LightFakeApplication, WithApplication}
import scala.concurrent.duration._

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

    // ColinG 14/12/2015 Passing a none encrypted uuid fails decrypt but drops through OK with uuid correctly set
    "return validation error not found when claim not found" in new WithApplication(app = LightFakeApplication(additionalConfiguration = Map("saveForLaterResumeEnabled" -> "true"))) with Claiming {
      val request=FakeRequest(GET, "?x=1234")
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

      val request=FakeRequest(GET, "?x=123456")
      val result = GResume.present(request)
      val bodyText: String = contentAsString(result)
      bodyText must contain( "Enter your details to resume your application")
      status(result) mustEqual OK
    }

    "return sfl claim expired screen when claim is found in sfl cache with status of EXPIRED" in new WithApplication(app = LightFakeApplication(additionalConfiguration = Map("saveForLaterResumeEnabled" -> "true"))) with Claiming {
      // Inject the saved claim directly to cache so we can set the status to EXPIRED
      var claim = new Claim(CachedClaim.key, uuid="223344")
      val details = new YourDetails("",None, "John",None, "Green",NationalInsuranceNumber(Some("AB123456D")), DayMonthYear(1, 1, 1970))
      claim=claim+details
      val encryptedCacheHandling = new EncryptedCacheHandling() { val cacheKey = "223344" }
      val key=encryptedCacheHandling.createSaveForLaterKey(claim)
      val saveForLater=new SaveForLater(SaveForLaterEncryption.encryptClaim(claim,key), "/about-you/nationality-and-residency", 3, "EXPIRED", -1, -1, "V1.00"  )
      encryptedCacheHandling.cache.set("SFL-223344", saveForLater, Duration(CacheHandling.saveForLaterCacheExpiry + CacheHandling.saveForLaterGracePeriod, DAYS))

      val request=FakeRequest(GET, "?x=223344")
      val result = GResume.present(request)
      val bodyText: String = contentAsString(result)
      bodyText must contain( "This application has been deleted")
      status(result) mustEqual BAD_REQUEST
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
