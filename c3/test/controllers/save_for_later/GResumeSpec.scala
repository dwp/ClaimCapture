package controllers.save_for_later

import models.{DayMonthYear, NationalInsuranceNumber}
import models.domain._
import models.view.{CachedClaim}
import models.view.cache.{EncryptedCacheHandling, CacheHandling}
import org.specs2.mutable._
import play.api.test.FakeRequest
import play.api.test.Helpers._
import utils.{SaveForLaterEncryption, LightFakeApplication, WithApplication}
import scala.concurrent.duration._

class GResumeSpec extends Specification {

  section("unit", models.domain.YourDetails.id)
  
  // Output from C3EncryptionSpec.scala ..... to create a set of xor pairs and decrypt key
  // With key of:88a976e1-e926-4bb4-9322-15aabc6d0516 created xor pair of:0bcd1234-0000-0000-0000-abcd1234cdef and:174650142322392746796619227917559908601
  val encryptkey = "88a976e1-e926-4bb4-9322-15aabc6d0516"
  val uuid = "0bcd1234-0000-0000-0000-abcd1234cdef"
  val decodeint = "174650142322392746796619227917559908601"

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

    "return validation error not found when claim not found" in new WithApplication(app = LightFakeApplication(additionalConfiguration = Map("saveForLaterResumeEnabled" -> "true", "saveForLater.uuid.secret.key" -> encryptkey))) with Claiming {
      val request = FakeRequest(GET, "?x=" + decodeint)
      val result = GResume.present(request)
      val bodyText: String = contentAsString(result)
      bodyText must contain("This application can't be found")
      status(result) mustEqual BAD_REQUEST
    }

    "return ok resume screen when claim is found in sfl cache" in new WithApplication(app = LightFakeApplication(additionalConfiguration = Map("saveForLaterResumeEnabled" -> "true", "saveForLater.uuid.secret.key" -> encryptkey))) with Claiming {
      var claim = new Claim(CachedClaim.key, uuid = uuid)
      val details = new YourDetails("Mr", "", None, "green", NationalInsuranceNumber(Some("AB123456D")), DayMonthYear(None, None, None))
      claim = claim + details
      val encryptedCacheHandling = new EncryptedCacheHandling() {
        val cacheKey = uuid
      }
      encryptedCacheHandling.saveForLaterInCache(claim, "/lastlocation")

      val request = FakeRequest(GET, "?x=" + decodeint)
      val result = GResume.present(request)
      val bodyText: String = contentAsString(result)
      bodyText must contain("Enter your details to resume your application")
      status(result) mustEqual OK
    }

    "return sfl claim expired screen when claim is found in sfl cache with status of EXPIRED" in new
        WithApplication(app = LightFakeApplication(additionalConfiguration = Map("saveForLaterResumeEnabled" -> "true", "saveForLater.uuid.secret.key" -> encryptkey))) with Claiming {
      // Inject the saved claim directly to cache so we can set the status to EXPIRED
      var claim = new Claim(CachedClaim.key, uuid = uuid)
      val details = new YourDetails("Mr", "John", None, "Green", NationalInsuranceNumber(Some("AB123456D")), DayMonthYear(1, 1, 1970))
      claim = claim + details
      val encryptedCacheHandling = new EncryptedCacheHandling() {
        val cacheKey = uuid
      }
      val key = encryptedCacheHandling.createSaveForLaterKey(claim)
      val saveForLater = new SaveForLater(SaveForLaterEncryption.encryptClaim(claim, key), "/about-you/nationality-and-residency", 3, "EXPIRED", -1, -1, "V1.00")
      encryptedCacheHandling.cache.set("SFL-" + uuid, saveForLater, Duration(CacheHandling.saveForLaterCacheExpiry + CacheHandling.saveForLaterGracePeriod, SECONDS))

      val request = FakeRequest(GET, "?x=" + decodeint)
      val result = GResume.present(request)
      val bodyText: String = contentAsString(result)

      bodyText must contain( "This application has been deleted")
      status(result) mustEqual BAD_REQUEST
    }

    // Save a claim, resume, sleep a sec, resume again and confirm that the expiryDates have increased between the first and second resumes
    "set the expiry date correctly on the claim and be visible when successfully resumed" in new
        WithApplication(app = LightFakeApplication(additionalConfiguration =
          Map("cache.saveForLaterCacheExpirySecs" -> "1000", "cache.saveForLaterGracePeriodSecs" -> "2000", "saveForLaterResumeEnabled" -> "true", "saveForLater.uuid.secret.key" -> encryptkey))) with Claiming {
      var claim = new Claim(CachedClaim.key, uuid = uuid)
      val details = new YourDetails("Mr", "John", None, "Green", NationalInsuranceNumber(Some("AB123456D")), DayMonthYear(1, 1, 1970))
      claim = claim + details
      val encryptedCacheHandling = new EncryptedCacheHandling() {
        val cacheKey = uuid
      }
      encryptedCacheHandling.saveForLaterInCache(claim, "/lastlocation")

      val resumeSaveForLater=new ResumeSaveForLater("John","Green",NationalInsuranceNumber(Some("AB123456D")), DayMonthYear(1, 1, 1970), decodeint)

      val resume1 = resumeSaveForLaterFromCache(resumeSaveForLater, claim.getDecryptedUuid(resumeSaveForLater.uuid))
      Thread.sleep(1000)
      val resume2 = resumeSaveForLaterFromCache(resumeSaveForLater, claim.getDecryptedUuid(resumeSaveForLater.uuid))
      resume2.get.applicationExpiry > resume1.get.applicationExpiry must beTrue
      resume2.get.cacheExpiryPeriod > resume1.get.cacheExpiryPeriod must beTrue
    }

    // Save a claim, attempt to resume with bad credentials, sleep a sec and attempt resume again and confirm that the expiryDates have not increased
    // between the first and second resumes
    "renew the expiry date on the claim when successfully resumed" in new
        WithApplication(app = LightFakeApplication(additionalConfiguration =
          Map("cache.saveForLaterCacheExpirySecs" -> "1000", "cache.saveForLaterGracePeriodSecs" -> "2000", "saveForLaterResumeEnabled" -> "true", "saveForLater.uuid.secret.key" -> encryptkey))) with Claiming {
      var claim = new Claim(CachedClaim.key, uuid = uuid)
      val details = new YourDetails("Mr", "John", None, "Green", NationalInsuranceNumber(Some("AB123456D")), DayMonthYear(1, 1, 1970))
      claim = claim + details
      val encryptedCacheHandling = new EncryptedCacheHandling() {
        val cacheKey = uuid
      }
      encryptedCacheHandling.saveForLaterInCache(claim, "/lastlocation")

      val badResumeSaveForLater=new ResumeSaveForLater("BadJohn","Green",NationalInsuranceNumber(Some("AB123456D")), DayMonthYear(1, 1, 1970), decodeint)
      val badResume1 = resumeSaveForLaterFromCache(badResumeSaveForLater, claim.getDecryptedUuid(badResumeSaveForLater.uuid))
      Thread.sleep(1000)
      val badResume2 = resumeSaveForLaterFromCache(badResumeSaveForLater, claim.getDecryptedUuid(badResumeSaveForLater.uuid))
      badResume2.get.applicationExpiry == badResume1.get.applicationExpiry must beTrue
      badResume2.get.cacheExpiryPeriod == badResume1.get.cacheExpiryPeriod must beTrue
    }
  }
  section("unit", models.domain.YourDetails.id)
}
