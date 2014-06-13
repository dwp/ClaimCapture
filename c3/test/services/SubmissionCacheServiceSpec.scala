package services

import org.specs2.mutable.{Tags, Specification}
import models.domain._
import models.DayMonthYear
import models.view.CachedClaim
import models.MultiLineAddress
import models.domain.Claim
import models.NationalInsuranceNumber
import scala.Some

class SubmissionCacheServiceSpec extends Specification with Tags with SubmissionCacheService with CachedClaim {

  "CacheService" should {

    "store a value in the cache" in {
      storeInCache(getClaim("test"))
      getFromCache(getClaim("test")) must not beEmpty
    }

    "remove a value from the cache" in {
      storeInCache(getClaim("test"))
      removeFromCache(getClaim("test"))
      getFromCache(getClaim("test")) must beEmpty
    }

    "remove a value that does not exist in the cache should not throw exceptions but execute silently" in {
      removeFromCache(getClaim("test"))
      getFromCache(getClaim("test")) must beEmpty
    }

    "test that the cache times out after a specified period of time" in {
      storeInCache(getClaim("test"))
      Thread.sleep(60000)
      getFromCache(getClaim("test")) must beEmpty
    }.pendingUntilFixed("Need to find a good way of the tweaking the config property for the timeout in the tests")

    def getClaim(surname: String): Claim = {
      var claim = new Claim(transactionId = Some("1234567"))

      // need to set the qs groups used to create the fingerprint of the claim, otherwise a dup cache error will be thrown
      val det = new YourDetails("", "",None, surname,None, NationalInsuranceNumber(Some("AB"),Some("12"),Some("34"),Some("56"),Some("D")), DayMonthYear(None, None, None))
      val contact = new ContactDetails(new MultiLineAddress(), None, "", None)
      val claimDate = new ClaimDate(DayMonthYear(Some(1), Some(1), Some(2014)))

      claim = claim + det
      claim = claim + contact
      claim = claim + claimDate

      claim = new Claim(claim.key, claim.sections, claim.created, claim.lang, claim.transactionId)(claim.navigation) with FullClaim
      claim
    }

  }

}