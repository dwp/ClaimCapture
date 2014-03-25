package controllers.s1_carers_allowance

import org.specs2.mutable.{ Tags, Specification }
import play.api.test.{ WithApplication, FakeRequest }
import play.api.test.Helpers._
import play.api.cache.Cache
import java.util.concurrent.TimeUnit
import models.domain._
import models.domain.Claim
import models.view.CachedClaim

class G1BenefitsSpec extends Specification with Tags {
  "Carer's Allowance - Benefits - Controller" should {
    val answerYesNo = "yes"
    val benefitsInput = Seq("benefits.answer" -> answerYesNo)

    "start with a new Claim" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)

      G1Benefits.present(request)
      val claim = Cache.getAs[Claim](claimKey)

      TimeUnit.MILLISECONDS.sleep(100)

      val result = G1Benefits.present(request)
      header(CACHE_CONTROL, result) must beSome("no-cache, no-store")

      Cache.getAs[Claim](claimKey) must beLike { case Some(c: Claim) => c.created mustNotEqual claim.get.created }
    }

    "present" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)

      val result = controllers.s1_carers_allowance.G1Benefits.present(request)
      status(result) mustEqual OK
    }

    "missing mandatory field" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)
        .withFormUrlEncodedBody("benefits.answer" -> "")

      val result = controllers.s1_carers_allowance.G1Benefits.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "redirect to the next page after a valid submission" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)
        .withFormUrlEncodedBody(benefitsInput: _*)

      val result = controllers.s1_carers_allowance.G1Benefits.submit(request)
      status(result) mustEqual SEE_OTHER
    }

    "add submitted form to the cached claim when answered 'yes'" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)
        .withFormUrlEncodedBody(benefitsInput: _*)

      val result = controllers.s1_carers_allowance.G1Benefits.submit(request)
      val claim = Cache.getAs[Claim](claimKey).get
      val section: Section = claim.section(models.domain.CarersAllowance)
      section.questionGroup(Benefits) must beLike {
        case Some(f: Benefits) => {
          f.answerYesNo must equalTo(answerYesNo)
        }
      }
    }

    "add submitted form to the cached claim when answered 'no'" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)
        .withFormUrlEncodedBody("benefits.answer" -> "no")

      val result = controllers.s1_carers_allowance.G1Benefits.submit(request)
      val claim = Cache.getAs[Claim](claimKey).get
      val section: Section = claim.section(models.domain.CarersAllowance)
      section.questionGroup(Benefits) must beLike {
        case Some(f: Benefits) => {
          f.answerYesNo must equalTo("no")
        }
      }
    }
  } section("unit", models.domain.CarersAllowance.id)
}