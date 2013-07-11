package controllers.s5_time_spent_abroad

import org.specs2.mutable.{Tags, Specification}
import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import models.domain._
import play.api.cache.Cache
import models.{DayMonthYear, NationalInsuranceNumber, domain}
import models.domain.Claim
import models.NationalInsuranceNumber
import scala.Some

class G1NormalResidenceAndCurrentLocationSpec extends Specification with Tags {
  "Normal residence and current location" should {
    "present" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)

      val result = G1NormalResidenceAndCurrentLocation.present(request)
      status(result) mustEqual OK
    }

    "add 'NormalResidenceAndCurrentLocation' to the cached claim" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey).withFormUrlEncodedBody("liveInUK.answer" -> "no", "liveInUK.whereDoYouLive" -> "Italy", "inGBNow" -> "no")

      val result = controllers.s5_time_spent_abroad.G1NormalResidenceAndCurrentLocation.submit(request)
      val claim = Cache.getAs[Claim](claimKey).get
      val section: Section = claim.section(domain.TimeSpentAbroad.id)

      section.questionGroup(NormalResidenceAndCurrentLocation) must beLike {
        case Some(f: NormalResidenceAndCurrentLocation) => {
          f.whereDoYouLive.answer must equalTo("no")
          f.whereDoYouLive.text must equalTo(Some("Italy"))
          f.inGBNow must equalTo("no")

        }
      }
    }

    "return bad request on invalid data" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey).withFormUrlEncodedBody("liveInUK.answer" -> "")

      val result = controllers.s5_time_spent_abroad.G1NormalResidenceAndCurrentLocation.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

  } section "unit"
}