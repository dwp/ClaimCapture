package controllers.s1_carers_allowance

import models.domain._
import org.specs2.mutable.{Specification, Tags}
import play.api.test.Helpers._
import play.api.test.{FakeRequest, WithApplication}

class G2HoursSpec extends Specification with Tags {
  "Carer's Allowance - Hours - Controller" should {
    val answerYesNo = "yes"
    val hoursInput = Seq("hours.answer" -> answerYesNo)

    "present" in new WithApplication with Claiming {
      val request = FakeRequest()

      val result = controllers.s1_carers_allowance.G2Hours.present(request)
      status(result) mustEqual OK
    }

    "missing mandatory field" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody("hours.answer" -> "")

      val result = controllers.s1_carers_allowance.G2Hours.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "redirect to the next page after a valid submission" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody(hoursInput: _*)

      val result = controllers.s1_carers_allowance.G2Hours.submit(request)
      status(result) mustEqual SEE_OTHER
    }

    "add submitted form to the cached claim when answered 'yes'" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody(hoursInput: _*)

      val result = controllers.s1_carers_allowance.G2Hours.submit(request)
      val claim = getClaimFromCache(result)
      val section: Section = claim.section(models.domain.CarersAllowance)
      section.questionGroup(Hours) must beLike {
        case Some(f: Hours) => {
          f.answerYesNo must equalTo(answerYesNo)
        }
      }
    }

    "add submitted form to the cached claim when answered 'no'" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody("hours.answer" -> "no")

      val result = controllers.s1_carers_allowance.G2Hours.submit(request)
      val claim = getClaimFromCache(result)
      val section: Section = claim.section(models.domain.CarersAllowance)
      section.questionGroup(Hours) must beLike {
        case Some(f: Hours) => {
          f.answerYesNo must equalTo("no")
        }
      }
    }
  } section("unit", models.domain.CarersAllowance.id)
}