package controllers.s7_employment

import org.specs2.mutable.{Tags, Specification}
import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import models.domain._
import play.api.cache.Cache
import models.domain.Claim
import models.view.CachedClaim
import controllers.Mappings._
import models.domain.Claim
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
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)
      val result = G5LastWage.present("Dummy job ID")(request)
      status(result) mustEqual OK
    }

    """require "job ID" and "grossPay".""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)
        .withFormUrlEncodedBody("jobID" -> "1",
          "oftenGetPaid.frequency" -> "Weekly",
          "whenGetPaid" -> "Mondays",
          "lastPaidDate.day" -> "1",
          "lastPaidDate.month" -> "1",
          "lastPaidDate.year" -> "2014",
          "grossPay" -> "200",
          "employerOwesYouMoney" -> "no")

      val result = G5LastWage.submit(request)
      status(result) mustEqual SEE_OTHER
    }

    """be added to a (current) job""" in new WithApplication with Claiming {
      G3JobDetails.submit(FakeRequest().withSession(CachedClaim.key -> claimKey)
        withFormUrlEncodedBody(
        "jobID" -> "1",
        "employerName" -> "Toys r not us",
        "phoneNumber" -> "12345678",
        "address.lineOne" -> "Street Test 1",
        "jobStartDate.day" -> "1",
        "jobStartDate.month" -> "1",
        "jobStartDate.year" -> "2000",
        "finishedThisJob" -> "no"))

      val result = G5LastWage.submit(FakeRequest().withSession(CachedClaim.key -> claimKey)
        .withFormUrlEncodedBody("jobID" -> "1",
          "oftenGetPaid.frequency" -> "Weekly",
          "whenGetPaid" -> "Mondays",
          "lastPaidDate.day" -> "1",
          "lastPaidDate.month" -> "1",
          "lastPaidDate.year" -> "2014",
          "grossPay" -> "200",
          "employerOwesYouMoney" -> "no"))

      status(result) mustEqual SEE_OTHER

      val claim = Cache.getAs[Claim](claimKey).get

      claim.questionGroup(Jobs) must beLike {
        case Some(js: Jobs) => {
          js.size shouldEqual 1

          js.find(_.jobID == "1") must beLike { case Some(j: Job) => j.questionGroups.size shouldEqual 2 }
        }
      }
    }
  } section("unit", models.domain.Employed.id)
}