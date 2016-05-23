package models.view

import gov.dwp.carers.play2.resilientmemcached.MemcachedCacheApi
import models.{DayMonthYear, NationalInsuranceNumber, MultiLineAddress}
import models.domain._
import org.specs2.mutable._
import play.api.i18n.Lang
import play.api.test.FakeRequest
import utils._

class ResilientMemcacheSpec extends Specification {
  section("unit")
  // These tests are all relate to each other and must run in order
  "Memcache" should {
    "write initial data alan to both memcache" in new WithMemcacheApplication(LightFakeApplicationWithMemcache.fa1only) with Claiming{
      cache.isInstanceOf[MemcachedCacheApi] mustEqual true
      var claim = new Claim(CachedClaim.key, List(), System.currentTimeMillis(), Some(Lang("en")),  "XX1234")
      val details = new YourDetails("Mr","alan", None, "green", NationalInsuranceNumber(Some("AB123456D")), DayMonthYear(10, 12, 2000))
      val contactDetails = new ContactDetails(new MultiLineAddress(), None, None, None, "yes", Some("bt@bt.com"), Some("bt@bt.com"))
      claim = claim + details
      val cacheHandler = new CacheHandlingWithClaim()
      val request = FakeRequest().withSession(cacheHandler.cacheKey -> "XX1234")
      cacheHandler.saveInCache(claim)

      val retrievedClaim = cacheHandler.fromCache(request).get
      retrievedClaim.uuid mustEqual(claim.uuid)
      val yd=retrievedClaim.questionGroup[YourDetails].getOrElse(YourDetails())
      yd.firstName mustEqual("alan")
    }

    "write update bob to memcache 1 only" in new WithMemcacheApplication(LightFakeApplicationWithMemcache.fa1only) with Claiming{
      cache.isInstanceOf[MemcachedCacheApi] mustEqual true
      var claim = new Claim(CachedClaim.key, List(), System.currentTimeMillis(), Some(Lang("en")),  "XX1234")
      val details = new YourDetails("Mr","bob", None, "green", NationalInsuranceNumber(Some("AB123456D")), DayMonthYear(10, 12, 2000))
      val contactDetails = new ContactDetails(new MultiLineAddress(), None, None, None, "yes", Some("bt@bt.com"), Some("bt@bt.com"))
      claim = claim + details
      val cacheHandler = new CacheHandlingWithClaim()
      val request = FakeRequest().withSession(cacheHandler.cacheKey -> "XX1234")
      cacheHandler.saveInCache(claim)

      val retrievedClaim = cacheHandler.fromCache(request).get
      retrievedClaim.uuid mustEqual(claim.uuid)
      val yd=retrievedClaim.questionGroup[YourDetails].getOrElse(YourDetails())
      yd.firstName mustEqual("bob")
    }

    "read bob back from both memcache" in new WithMemcacheApplication with Claiming{
      cache.isInstanceOf[MemcachedCacheApi] mustEqual true
      val cacheHandler = new CacheHandlingWithClaim()
      val request = FakeRequest().withSession(cacheHandler.cacheKey -> "XX1234")
      val retrievedClaim = cacheHandler.fromCache(request).get
      retrievedClaim.uuid mustEqual("XX1234")
      val yd=retrievedClaim.questionGroup[YourDetails].getOrElse(YourDetails())
      yd.firstName mustEqual("bob")
    }

    "read bob from memcache 1 only" in new WithMemcacheApplication(LightFakeApplicationWithMemcache.fa1only) with Claiming{
      cache.isInstanceOf[MemcachedCacheApi] mustEqual true
      val cacheHandler = new CacheHandlingWithClaim()
      val request = FakeRequest().withSession(cacheHandler.cacheKey -> "XX1234")
      val retrievedClaim = cacheHandler.fromCache(request).get
      retrievedClaim.uuid mustEqual("XX1234")
      val yd=retrievedClaim.questionGroup[YourDetails].getOrElse(YourDetails())
      yd.firstName mustEqual("bob")
    }

    "read bob from memcache 2 only" in new WithMemcacheApplication(LightFakeApplicationWithMemcache.fa2only) with Claiming{
      pending("Fixup memcache resilient to sync between reads")
      cache.isInstanceOf[MemcachedCacheApi] mustEqual true
      val cacheHandler = new CacheHandlingWithClaim()
      val request = FakeRequest().withSession(cacheHandler.cacheKey -> "XX1234")
      val retrievedClaim = cacheHandler.fromCache(request).get
      retrievedClaim.uuid mustEqual("XX1234")
      val yd=retrievedClaim.questionGroup[YourDetails].getOrElse(YourDetails())
      yd.firstName mustEqual("bob")
    }
  }
  section("unit")
}

