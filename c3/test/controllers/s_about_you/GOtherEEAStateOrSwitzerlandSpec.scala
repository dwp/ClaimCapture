package controllers.s_about_you

import org.specs2.mutable._
import play.api.test.FakeRequest
import play.api.test.Helpers._
import play.api.cache.Cache
import models.domain.{Claim, OtherEEAStateOrSwitzerland, Claiming}
import utils.WithApplication
import scala.Some
import models.view.CachedClaim

class GOtherEEAStateOrSwitzerlandSpec extends Specification {
  "Other EEA State of Switzerland" should {
    "present" in new WithApplication with Claiming {
      val request = FakeRequest()

      val result = GOtherEEAStateOrSwitzerland.present(request)
      status(result) mustEqual OK
    }

    "return bad request on invalid data" in new WithApplication with Claiming {
      val request = FakeRequest()

      val result = GOtherEEAStateOrSwitzerland.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    """be added to cached claim upon answering "no" to "benefits from other EEA state or Switzerland".""" in new WithApplication with Claiming {
      val request = FakeRequest().withFormUrlEncodedBody("eeaGuardQuestion.answer" -> "no")

      val result = GOtherEEAStateOrSwitzerland.submit(request)

      val claim = getClaimFromCache(result)

      claim.questionGroup(OtherEEAStateOrSwitzerland) must beLike {
        case Some(o: OtherEEAStateOrSwitzerland) => {
          o.guardQuestion.answer shouldEqual "no"
        }
      }
    }
  }
  section("unit", models.domain.OtherMoney.id)
}
