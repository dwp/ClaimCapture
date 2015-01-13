package services

import models.{DayMonthYear, MultiLineAddress, NationalInsuranceNumber}
import models.domain.{Claim, _}
import models.view.CachedClaim
import org.specs2.mutable.{Specification, Tags}
import play.api.test.WithApplication

class SubmissionCacheServiceSpec extends Specification with Tags with SubmissionCacheService with CachedClaim {

  "CacheService" should {

    "store a value in the cache" in new WithApplication {
      storeInCache(getClaim("test", "aksdhfkj123"))
      getFromCache(getClaim("test","aksdhfkj123")) must not beEmpty
    }

    "remove a value from the cache" in new WithApplication {
      storeInCache(getClaim("test","aksdhfkj143"))
      removeFromCache(getClaim("test","aksdhfkj143"))
      getFromCache(getClaim("test","aksdhfkj143")) must beEmpty
    }

    "remove a value that does not exist in the cache should not throw exceptions but execute silently" in new WithApplication {
      removeFromCache(getClaim("test","aksdhfkj143x3"))
      getFromCache(getClaim("test","aksdhfkj143x3")) must beEmpty
    }

    "test that the cache times out after a specified period of time" in new WithApplication {
      skipped("Time dependent. Takes too long.")
      storeInCache(getClaim("test","akspngjkj143"))
      Thread.sleep(60000)
      getFromCache(getClaim("test","akspngjkj143")) must beEmpty
    } //.pendingUntilFixed("Need to find a good way of the tweaking the config property for the timeout in the tests")

    def getClaim(surname: String, newuuid:String): Claim = {
      var claim = new Claim(transactionId = Some("1234567"), uuid=newuuid)

      // need to set the qs groups used to create the fingerprint of the claim, otherwise a dup cache error will be thrown
      val det = new YourDetails("", "",None, surname,NationalInsuranceNumber(Some("AB123456D")), DayMonthYear(None, None, None))
      val contact = new ContactDetails(new MultiLineAddress(), None, "", None)
      val claimDate = new ClaimDate(DayMonthYear(Some(1), Some(1), Some(2014)))

      claim = claim + det
      claim = claim + contact
      claim = claim + claimDate

      claim = new Claim(claim.key, claim.sections, claim.created, claim.lang, claim.uuid,claim.transactionId)(claim.navigation) with FullClaim
      claim
    }

  }

}