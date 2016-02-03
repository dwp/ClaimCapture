package models.view

import gov.dwp.carers.play2.resilientmemcached.MemcachedCacheApi
import models.domain.{Claim, Claiming}
import models.view.cache.EncryptedCacheHandling
import org.specs2.mutable._
import play.api.test.FakeRequest
import utils.WithMemcacheApplication

class CacheHandlingMemcacheSpec extends Specification {
  section("unit")
  "Memcache" should {
    "save and restore claim values using CacheHandlingWithClaim" in new WithMemcacheApplication with Claiming {
      cache.isInstanceOf[MemcachedCacheApi] mustEqual true

      val cacheHandler = new CacheHandlingWithClaim()
      val request = FakeRequest().withSession(cacheHandler.cacheKey -> claimKey)
      val claim=Claim(cacheHandler.cacheKey, List.empty, System.currentTimeMillis(), None, claimKey)
      cacheHandler.saveInCache(claim)

      val retrievedClaim = cacheHandler.fromCache(request).get
      retrievedClaim.uuid mustEqual(claim.uuid)
    }

    "save and restore claim values using CacheHandling" in new WithMemcacheApplication with Claiming {
      cache.isInstanceOf[MemcachedCacheApi] mustEqual true

      val cacheHandling = new EncryptedCacheHandling() {
        val cacheKey = "12345678"
      }
      var claim = new Claim(CachedClaim.key, uuid = "UUID-1234")

      cacheHandling.saveInCache(claim)
      val request = FakeRequest().withSession("UUID-1234" -> claimKey)
      val retrievedClaim=cacheHandling.fromCache(request, "UUID-1234")
      retrievedClaim.get.uuid mustEqual("UUID-1234")
    }

    "concat SFL keylist into single cs string list removing duplicates" in new WithMemcacheApplication() with Claiming {
      cache.isInstanceOf[MemcachedCacheApi] mustEqual true

      cache.remove("SFL")
      val cacheHandling = new EncryptedCacheHandling() {
        val cacheKey = "12345678"
      }
      cacheHandling.createClaimInSaveForLaterList("ABCD-1234")
      cacheHandling.createClaimInSaveForLaterList("WXYZ-4321")
      cacheHandling.createClaimInSaveForLaterList("ABCD-1234")

      val keyList = cacheHandling.getSaveForLaterList()
      keyList mustEqual ("ABCD-1234,WXYZ-4321")
    }

    "concat FB keylist into single cs string list removing duplicates" in new WithMemcacheApplication() with Claiming {
     cache.isInstanceOf[MemcachedCacheApi] mustEqual true

      cache.remove("FB")
      val cacheHandling = new EncryptedCacheHandling() {
        val cacheKey = "12345678"
      }
      cacheHandling.createFeedbackInList("ABCD-1234")
      cacheHandling.createFeedbackInList("WXYZ-4321")
      cacheHandling.createFeedbackInList("ABCD-1234")

      val keyList = cacheHandling.getFeedbackList
      keyList mustEqual ("ABCD-1234,WXYZ-4321")
    }

  }
  section("unit")
}

