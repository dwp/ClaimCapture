package controllers.s_eligibility

import models.domain._
import org.specs2.mutable.{Specification, Tags}
import play.api.test.Helpers._
import play.api.test.FakeRequest
import utils.WithApplication

class GEligibilitySpec extends Specification with Tags {
  "Carer's Allowance - Hours - Controller" should {
    val answerHours = "yes"
    val answerOver16 = "no"
    val answerLivesInGB = "no"

    val eligibilityInput = Seq("hours.answer" -> answerHours, "over16.answer" -> answerOver16, "livesInGB.answer" -> answerLivesInGB)

    "present" in new WithApplication with Claiming {
      val request = FakeRequest()

      val result = controllers.s_eligibility.GEligibility.present(request)
      status(result) mustEqual OK
    }

    "missing mandatory field" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody("hours.answer" -> "")

      val result = controllers.s_eligibility.GEligibility.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "redirect to the next page after a valid submission" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody(eligibilityInput: _*)

      val result = controllers.s_eligibility.GEligibility.submit(request)
      status(result) mustEqual SEE_OTHER
    }

    "add submitted form to the cached claim when answered 'yes'" in new WithApplication with Claiming {
      val request = FakeRequest().withFormUrlEncodedBody(eligibilityInput: _*)

      val result = controllers.s_eligibility.GEligibility.submit(request)
      val claim = getClaimFromCache(result)
      val section: Section = claim.section(models.domain.CarersAllowance)
      section.questionGroup(Eligibility) must beLike {
        case Some(f: Eligibility) =>
          f.hours must equalTo(answerHours)
          f.over16 must equalTo(answerOver16)
          f.livesInGB must equalTo(answerLivesInGB)

      }
    }

    "add submitted form to the cached claim when answered 'no'" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody("hours.answer" -> "no","over16.answer" -> "no", "livesInGB.answer" -> "no")

      val result = controllers.s_eligibility.GEligibility.submit(request)
      val claim = getClaimFromCache(result)
      val section: Section = claim.section(models.domain.CarersAllowance)
      section.questionGroup(Eligibility) must beLike {
        case Some(f: Eligibility) =>
          f.hours must equalTo("no")

      }
    }
  } section("unit", models.domain.CarersAllowance.id)
}