package controllers.s5_time_spent_abroad

import org.specs2.mutable.{Tags, Specification}
import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import models.domain._
import play.api.cache.Cache
import models.domain.Claim
import scala.Some

class G5otherEEAStateOrSwitzerlandSpec extends Specification with Tags {
  "Other EEA State of Switzerland" should {
    "present" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)

      val result = G5otherEEAStateOrSwitzerland.present(request)
      status(result) mustEqual OK
    }

    "return bad request on invalid data" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)

      val result = controllers.s5_time_spent_abroad.G5otherEEAStateOrSwitzerland.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    """be added to cached claim upon answering "no" to "benefits from other EEA state or Switzerland".""" in new WithApplication with Claiming {
      pending
      /*val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody("benefitsFromOtherEEAStateOrSwitzerland.answer" -> "no",
                                "workingForOtherEEAStateOrSwitzerland.answer" -> "no")

      val result = controllers.s5_time_spent_abroad.G5otherEEAStateOrSwitzerland.submit(request)

      val claim = Cache.getAs[Claim](claimKey).get

      claim.questionGroup(OtherEEAStateOrSwitzerland) must beLike {
        case Some(o: OtherEEAStateOrSwitzerland) => {
          o.benefitsFromOtherEEAStateOrSwitzerland.answer shouldEqual "no"
          o.benefitsFromOtherEEAStateOrSwitzerland.text should beNone
          o.workingForOtherEEAStateOrSwitzerland shouldEqual "no"
        }
      }*/
    }
  } section "unit"
}