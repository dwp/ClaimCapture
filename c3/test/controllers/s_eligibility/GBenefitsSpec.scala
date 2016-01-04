package controllers.s_eligibility

import java.util.concurrent.TimeUnit

import models.domain._
import org.specs2.mutable._
import play.api.test.Helpers._
import play.api.test.FakeRequest
import utils.WithApplication

class GBenefitsSpec extends Specification {
  section("unit", models.domain.CarersAllowance.id)
  "Carer's Allowance - Benefits - Controller" should {
    val benefitsAnswer = Benefits.aa
    val benefitsInput = Seq("benefitsAnswer" -> benefitsAnswer)

    "start with a new Claim" in new WithApplication with Claiming {
      val request = FakeRequest()

      val result1 = GBenefits.present(request)
      val claim1 = getClaimFromCache(result1)

      TimeUnit.MILLISECONDS.sleep(100)

      val result2 = GBenefits.present(request)
      header(CACHE_CONTROL, result2) must beSome("must-revalidate,no-cache,no-store")
      val claim2 = getClaimFromCache(result2)
      claim1.created mustNotEqual claim2.created
      claim1.uuid mustNotEqual claim2.uuid
    }

    "present" in new WithApplication with Claiming {
      val request = FakeRequest()

      val result = controllers.s_eligibility.GBenefits.present(request)
      status(result) mustEqual OK
    }

    "missing mandatory field" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody("benefitsAnswer" -> "")

      val result = controllers.s_eligibility.GBenefits.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "redirect to the next page after a valid submission" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody(benefitsInput: _*)

      val result = controllers.s_eligibility.GBenefits.submit(request)
      status(result) mustEqual SEE_OTHER
    }

    "add submitted form to the cached claim when user selects 'AA'" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody(benefitsInput: _*)

      val result = controllers.s_eligibility.GBenefits.submit(request)
      val claim = getClaimFromCache(result)
      val section: Section = claim.section(models.domain.CarersAllowance)
      section.questionGroup(Benefits) must beLike {
        case Some(f: Benefits) => {
          f.benefitsAnswer must equalTo(benefitsAnswer)
        }
      }
    }

    "add submitted form to the cached claim when user selects 'PIP'" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody("benefitsAnswer" -> Benefits.pip)

      val result = controllers.s_eligibility.GBenefits.submit(request)
      val claim = getClaimFromCache(result)
      val section: Section = claim.section(models.domain.CarersAllowance)
      section.questionGroup(Benefits) must beLike {
        case Some(f: Benefits) => {
          f.benefitsAnswer must equalTo("PIP")
        }
      }
    }

    "add submitted form to the cached claim when user selects 'DLA'" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody("benefitsAnswer" -> Benefits.dla)

      val result = controllers.s_eligibility.GBenefits.submit(request)
      val claim = getClaimFromCache(result)
      val section: Section = claim.section(models.domain.CarersAllowance)
      section.questionGroup(Benefits) must beLike {
        case Some(f: Benefits) => {
          f.benefitsAnswer must equalTo("DLA")
        }
      }
    }

    "add submitted form to the cached claim when user selects 'CAA'" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody("benefitsAnswer" -> Benefits.caa)

      val result = controllers.s_eligibility.GBenefits.submit(request)
      val claim = getClaimFromCache(result)
      val section: Section = claim.section(models.domain.CarersAllowance)
      section.questionGroup(Benefits) must beLike {
        case Some(f: Benefits) => {
          f.benefitsAnswer must equalTo("CAA")
        }
      }
    }

    "add submitted form to the cached claim when user selects 'AFIP'" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody("benefitsAnswer" -> Benefits.afip)

      val result = controllers.s_eligibility.GBenefits.submit(request)
      val claim = getClaimFromCache(result)
      val section: Section = claim.section(models.domain.CarersAllowance)
      section.questionGroup(Benefits) must beLike {
        case Some(f: Benefits) => {
          f.benefitsAnswer must equalTo("AFIP")
        }
      }
    }

    "add submitted form to the cached claim when user selects 'none of the benefits'" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody("benefitsAnswer" -> Benefits.noneOfTheBenefits)

      val result = controllers.s_eligibility.GBenefits.submit(request)
      val claim = getClaimFromCache(result)
      val section: Section = claim.section(models.domain.CarersAllowance)
      section.questionGroup(Benefits) must beLike {
        case Some(f: Benefits) => {
          f.benefitsAnswer must equalTo("NONE")
        }
      }
    }
  }
  section("unit", models.domain.CarersAllowance.id)
}
