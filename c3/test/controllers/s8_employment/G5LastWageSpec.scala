package controllers.s8_employment

import org.specs2.mutable.{Tags, Specification}
import play.api.test.FakeRequest
import play.api.test.Helpers._
import models.domain._
import play.api.cache.Cache
import models.domain.Claim
import models.view.CachedClaim
import controllers.mappings.Mappings._
import models.domain.Claim
import utils.WithApplication
import scala.Some
import play.api.data.Forms._
import models.domain.Claim
import scala.Some
import controllers.CarersForms._
import models.domain.Claim
import scala.Some

class G5LastWageSpec extends Specification with Tags {
  "Last wage" should {
    "present" in new WithApplication with Claiming {
      val request = FakeRequest()
      val result = G5LastWage.present("Dummy job ID")(request)
      status(result) mustEqual OK
    }

    """require "job ID" and "grossPay".""" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody("iterationID" -> "1",
          "oftenGetPaid.frequency" -> "Weekly",
          "whenGetPaid" -> "Mondays",
          "lastPaidDate.day" -> "1",
          "lastPaidDate.month" -> "1",
          "lastPaidDate.year" -> "2014",
          "grossPay" -> "200",
          "sameAmountEachTime" -> "yes",
          "employerOwesYouMoney" -> "no")

      val result = G5LastWage.submit(request)
      status(result) mustEqual SEE_OTHER
    }

    """be added to a (current) job""" in new WithApplication with Claiming {
      val result1 = G3JobDetails.submit(FakeRequest()
        withFormUrlEncodedBody(
        "iterationID" -> "1",
        "employerName" -> "Toys r not us",
        "phoneNumber" -> "12345678",
        "address.lineOne" -> "Street Test 1",
        "address.lineTwo" -> "Street Test 2",
        "startJobBeforeClaimDate" -> "yes",
        "finishedThisJob" -> "no"))

      val result2 = G5LastWage.submit(FakeRequest().withSession(CachedClaim.key -> extractCacheKey(result1))
        .withFormUrlEncodedBody("iterationID" -> "1",
          "oftenGetPaid.frequency" -> "Weekly",
          "whenGetPaid" -> "Mondays",
          "lastPaidDate.day" -> "1",
          "lastPaidDate.month" -> "1",
          "lastPaidDate.year" -> "2014",
          "grossPay" -> "200",
          "sameAmountEachTime" -> "yes",
          "employerOwesYouMoney" -> "no"))

      status(result2) mustEqual SEE_OTHER

      val claim = getClaimFromCache(result2)

      claim.questionGroup(Jobs) must beLike {
        case Some(js: Jobs) => {
          js.size shouldEqual 1

          js.find(_.iterationID == "1") must beLike { case Some(j: Iteration) => j.questionGroups.size shouldEqual 2 }
        }
      }
    }
  } section("unit", models.domain.Employed.id)
}