package controllers.s1_carers_allowance

import org.specs2.mutable.{ Tags, Specification }
import play.api.test.{ WithApplication, FakeRequest }
import play.api.test.Helpers._
import play.api.cache.Cache
import models.domain._
import models.domain.Claim
import models.view.CachedClaim

class G4LivesInGBSpec extends Specification with Tags {
  "Carer's Allowance - LivesInGB - Controller" should {
    val answerYesNo = "yes"
    val hoursInput = Seq("livesInGB.answer" -> answerYesNo)

    "present" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)

      val result = controllers.s1_carers_allowance.G4LivesInGB.present(request)
      status(result) mustEqual OK
    }

    "missing mandatory field" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)
        .withFormUrlEncodedBody("livesInGB.answer" -> "")

      val result = controllers.s1_carers_allowance.G4LivesInGB.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "redirect to the next page after a valid submission" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)
        .withFormUrlEncodedBody(hoursInput: _*)

      val result = controllers.s1_carers_allowance.G4LivesInGB.submit(request)
      status(result) mustEqual SEE_OTHER
    }

    "add submitted form to the cached claim when answered 'yes'" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)
        .withFormUrlEncodedBody(hoursInput: _*)

      val result = controllers.s1_carers_allowance.G4LivesInGB.submit(request)
      val claim = Cache.getAs[Claim](claimKey).get
      val section: Section = claim.section(models.domain.CarersAllowance)
      section.questionGroup(LivesInGB) must beLike {
        case Some(f: LivesInGB) => {
          f.answerYesNo must equalTo(answerYesNo)
        }
      }
    }

    "add submitted form to the cached claim when answered 'no'" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)
        .withFormUrlEncodedBody("livesInGB.answer" -> "no")

      val result = controllers.s1_carers_allowance.G4LivesInGB.submit(request)
      val claim = Cache.getAs[Claim](claimKey).get
      val section: Section = claim.section(models.domain.CarersAllowance)
      section.questionGroup(LivesInGB) must beLike {
        case Some(f: LivesInGB) => {
          f.answerYesNo must equalTo("no")
        }
      }
    }
  } section("unit", models.domain.CarersAllowance.id)
}