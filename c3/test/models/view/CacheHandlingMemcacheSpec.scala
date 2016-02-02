package models.view

import gov.dwp.carers.play2.resilientmemcached.MemcachedCacheApi
import models.domain.Claiming
import models.view.cache.EncryptedCacheHandling
import org.specs2.mutable._
import play.api.Play._
import utils.WithApplication

class CacheHandlingMemcacheSpec extends Specification {
  section("unit")
  "Memcache Key List Handling" should {
    "concat keys into single cs string list" in new WithApplication() with Claiming {
      override val cache: MemcachedCacheApi = current.injector.instanceOf[MemcachedCacheApi]
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

    "remove duplicate entries" in new WithApplication() with Claiming {
      override val cache: MemcachedCacheApi = current.injector.instanceOf[MemcachedCacheApi]
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

