package models.view

import controllers.save_for_later.GResume
import models.{DayMonthYear, NationalInsuranceNumber}
import models.domain.{YourDetails, Claim, Claiming}
import models.view.cache.{EncryptedCacheHandling, CacheHandling}
import org.specs2.mutable._
import play.api.test.FakeRequest
import play.api.test.Helpers._
import utils.{LightFakeApplication, WithApplication}

class CacheHandlingSpec extends Specification {
  section("unit")

  "Default EHCache Key List Handling" should {
    "concat keys into single cs string list" in new WithApplication(app = LightFakeApplication()) with Claiming {
      val cacheHandling=new EncryptedCacheHandling() {
        val cacheKey = "12345678"
      }
      cacheHandling.createFeedbackInList("ABCD-1234")
      cacheHandling.createFeedbackInList("WXYZ-4321")

      val keyList=cacheHandling.getFeedbackList
      keyList mustEqual( "ABCD-1234,WXYZ-4321")
    }

    "remove duplicate entries" in new WithApplication(app = LightFakeApplication()) with Claiming {
      val cacheHandling=new EncryptedCacheHandling() {
        val cacheKey = "12345678"
      }
      cacheHandling.createFeedbackInList("ABCD-1234")
      cacheHandling.createFeedbackInList("WXYZ-4321")
      cacheHandling.createFeedbackInList("ABCD-1234")

      val keyList=cacheHandling.getFeedbackList
      keyList mustEqual( "ABCD-1234,WXYZ-4321")
    }
  }
  section("unit")
}
