package models.view

import gov.dwp.carers.play2.resilientmemcached.MemcachedCacheApi
import models.domain.{Claim, Claiming}
import models.view.cache.EncryptedCacheHandling
import org.specs2.mutable._
import play.api.test.FakeRequest
import utils.{WithMemcacheApplication}

class CacheHandlingMemcacheSpec extends Specification {
  val uuid = "567896f2-1ad7-416a-8b0f-43428b65a7fa"
  val uuid1 = "567896f2-1ad7-416a-8b0f-43428b65a7fb"
  
  section("unit")
  "Memcache" should {
    "save and restore claim values using CacheHandlingWithClaim" in new WithMemcacheApplication with Claiming {
      cache.isInstanceOf[MemcachedCacheApi] mustEqual true

      val cacheHandler = new CacheHandlingWithClaim()
      val request = FakeRequest().withSession(cacheHandler.cacheKey -> claimKey)
      val claim=Claim(cacheHandler.cacheKey, List.empty, System.currentTimeMillis(), None, "gacid", claimKey)
      cacheHandler.saveInCache(claim)

      val retrievedClaim = cacheHandler.fromCache(request).get
      retrievedClaim.uuid mustEqual(claim.uuid)
    }

    "save and restore claim values using CacheHandling" in new WithMemcacheApplication with Claiming {
      cache.isInstanceOf[MemcachedCacheApi] mustEqual true

      val cacheHandling = new EncryptedCacheHandling() {
        val cacheKey = "12345678"
      }
      var claim = new Claim(CachedClaim.key, uuid = uuid)

      cacheHandling.saveInCache(claim)
      val request = FakeRequest().withSession("key" -> CachedClaim.key, "uuid" -> uuid)
      val retrievedClaim=cacheHandling.fromCache(request, uuid)
      retrievedClaim.get.uuid mustEqual(uuid)
    }

    "concat SFL keylist into single cs string list removing duplicates" in new WithMemcacheApplication() with Claiming {
      cache.isInstanceOf[MemcachedCacheApi] mustEqual true

      cache.remove("SFL")
      val cacheHandling = new EncryptedCacheHandling() {
        val cacheKey = "12345678"
      }
      cacheHandling.createClaimInSaveForLaterList(uuid)
      cacheHandling.createClaimInSaveForLaterList(uuid1)
      cacheHandling.createClaimInSaveForLaterList(uuid)

      val keyList = cacheHandling.getSaveForLaterList()
      keyList mustEqual (uuid + "," + uuid1)
    }

    "concat FB keylist into single cs string list removing duplicates" in new WithMemcacheApplication() with Claiming {
     cache.isInstanceOf[MemcachedCacheApi] mustEqual true

      cache.remove("FB")
      val cacheHandling = new EncryptedCacheHandling() {
        val cacheKey = "12345678"
      }
      cacheHandling.createFeedbackInList(uuid)
      cacheHandling.createFeedbackInList(uuid1)
      cacheHandling.createFeedbackInList(uuid)

      val keyList = cacheHandling.getFeedbackList
      keyList mustEqual (uuid + "," + uuid1)
    }

  }
  section("unit")
}

