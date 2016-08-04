package controllers.breaks_in_care

import controllers.mappings.Mappings
import models.domain._
import models.view.CachedClaim
import org.specs2.mutable._
import play.api.test.FakeRequest
import play.api.test.Helpers._
import utils.WithApplication

class GBreaksInCareHospitalSpec extends Specification {
  section("unit", models.domain.Breaks.id)

  "GBreakInCareHospital screen" should {
    "present correctly" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)

      val result = GBreaksInCareHospital.present(request)
      status(result) mustEqual OK
    }

    "fail submit for no questions answered" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)

      val result = GBreakInCareHospital.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "fail submit for other weeks not answered" in new WithApplication with Claiming {
      val input = Seq("breaktype_none" -> "true")
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)

      val result = GBreakTypes.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "fail submit for hospital/carehome/none not answered" in new WithApplication with Claiming {
      val input = Seq("breaktype_other" -> "yes")
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)

      val result = GBreakTypes.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "fail submit for hospital and none answered" in new WithApplication with Claiming {
      val input = Seq("breaktype_hospital" -> "true", "breaktype_none" -> "true", "breaktype_other" -> "yes")
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)

      val result = GBreakTypes.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "fail submit for carehome and none answered" in new WithApplication with Claiming {
      val input = Seq("breaktype_carehome" -> "true", "breaktype_none" -> "true", "breaktype_other" -> "yes")
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)

      val result = GBreakTypes.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "fail submit for carehome and hospital and none answered" in new WithApplication with Claiming {
      val input = Seq("breaktype_hospital" -> "true", "breaktype_carehome" -> "true", "breaktype_none" -> "true", "breaktype_other" -> "yes")
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)

      val result = GBreakTypes.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "success for hospital and other answered" in new WithApplication with Claiming {
      val input = Seq("breaktype_hospital" -> "true", "breaktype_other" -> "yes")
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey).withFormUrlEncodedBody(input: _*)

      val result = GBreakTypes.submit(request)
      status(result) mustEqual SEE_OTHER
    }

    "success for carehome and other answered" in new WithApplication with Claiming {
      val input = Seq("breaktype_carehome" -> "true", "breaktype_other" -> "yes")
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey).withFormUrlEncodedBody(input: _*)

      val result = GBreakTypes.submit(request)
      status(result) mustEqual SEE_OTHER
    }

    "success for hospital and carehome and other answered" in new WithApplication with Claiming {
      val input = Seq("breaktype_hospital" -> "true", "breaktype_carehome" -> "true", "breaktype_other" -> "yes")
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey).withFormUrlEncodedBody(input: _*)

      val result = GBreakTypes.submit(request)
      status(result) mustEqual SEE_OTHER
    }

    "success for none and other answered" in new WithApplication with Claiming {
      val input = Seq("breaktype_none" -> "true", "breaktype_other" -> "yes")
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey).withFormUrlEncodedBody(input: _*)

      val result = GBreakTypes.submit(request)
      status(result) mustEqual SEE_OTHER
    }

    "add submitted data to the cached claim" in new WithApplication with Claiming {
      val input = Seq("breaktype_none" -> "true", "breaktype_other" -> Mappings.yes)
      val request = FakeRequest().withFormUrlEncodedBody(input: _*)

      val result = GBreakTypes.submit(request)
      val claim = getClaimFromCache(result)
      claim.questionGroup(BreaksInCareType) must beLike {
        case Some(f: BreaksInCareType) => {
          f.hospital shouldEqual None
          f.carehome shouldEqual None
          f.none shouldEqual Some("true")
          f.other shouldEqual Some("yes")
        }
      }
    }
  }
  section("unit", models.domain.Breaks.id)
}
