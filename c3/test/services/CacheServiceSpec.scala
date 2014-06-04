package services

import org.specs2.mutable.{Tags, Specification}

class CacheServiceSpec extends Specification with Tags with CacheService {

  "CacheService" should {

    "store a value in the cache" in {
      storeInCache("AB123456Dtest2014-1-1")
      getFromCache("AB123456Dtest2014-1-1") must not beEmpty
    }

    "remove a value from the cache" in {
      storeInCache("AB123456Dtest2014-1-1")
      removeFromCache("AB123456Dtest2014-1-1")
      getFromCache("AB123456Dtest2014-1-1") must beEmpty
    }

    "remove a value that does not exist in the cache should not throw exceptions but execute silently" in {
      removeFromCache("AB123456Dtest2014-2-1")
      getFromCache("AB123456Dtest2014-2-1") must beEmpty
    }

  }

}