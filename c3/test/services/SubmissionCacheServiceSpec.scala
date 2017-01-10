package services

import models.{DayMonthYear, MultiLineAddress, NationalInsuranceNumber}
import models.domain.{Claim, _}
import models.view.CachedClaim
import org.specs2.mutable._
import utils.WithApplication

class SubmissionCacheServiceSpec extends Specification {
  section("unit")
  "CacheService" should {
    "store a value in the cache" in new WithApplication {
      val submissionCacheService = new SubmissionCacheService(){}
      submissionCacheService.storeInCache(getClaim("test", "aksdhfkj123"))
      submissionCacheService.getFromCache(getClaim("test","aksdhfkj123")) must not beEmpty
    }

    "remove a value from the cache" in new WithApplication {
      val submissionCacheService = new SubmissionCacheService(){}
      submissionCacheService.storeInCache(getClaim("test","aksdhfkj143"))
      submissionCacheService.removeFromCache(getClaim("test","aksdhfkj143"))
      submissionCacheService.getFromCache(getClaim("test","aksdhfkj143")) must beEmpty
    }

    "remove a value that does not exist in the cache should not throw exceptions but execute silently" in new WithApplication {
      val submissionCacheService = new SubmissionCacheService(){}
      submissionCacheService.removeFromCache(getClaim("test","aksdhfkj143x3"))
      submissionCacheService.getFromCache(getClaim("test","aksdhfkj143x3")) must beEmpty
    }
  }
  section("unit")

  def getClaim(surname: String, newuuid:String): Claim = {
    var claim = new Claim(CachedClaim.key, transactionId = Some("1234567"), uuid=newuuid)

    // need to set the qs groups used to create the fingerprint of the claim, otherwise a dup cache error will be thrown
    val det = new YourDetails("", "", None, surname, NationalInsuranceNumber(Some("AB123456D")), DayMonthYear(None, None, None))
    val contact = new ContactDetails(new MultiLineAddress(), None, None, None)
    val claimDate = new ClaimDate(DayMonthYear(Some(1), Some(1), Some(2014)))

    claim = claim + det
    claim = claim + contact
    claim = claim + claimDate

    claim = new Claim(claim.key, claim.sections, claim.created, claim.lang, claim.gacid, claim.uuid,claim.transactionId)(claim.navigation)
    claim
  }
}
