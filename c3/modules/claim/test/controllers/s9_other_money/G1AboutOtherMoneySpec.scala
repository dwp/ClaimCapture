package controllers.s9_other_money

import org.specs2.mutable.{ Tags, Specification }
import play.api.test.{ FakeRequest, WithApplication }
import models.domain._
import play.api.test.Helpers._
import play.api.cache.Cache
import models.domain
import models.domain.Claim
import models.view.CachedClaim
import models.PaymentFrequency

class G1AboutOtherMoneySpec extends Specification with Tags {
  "Details about other money - Controller" should {
    val yourBenefits = "yes"
    val anyPaymentsSinceClaimDate = "yes"
    val whoPaysYou = "The Man"
    val howMuch = "Not much"
    val howOften_frequency = "other"
    val howOften_frequency_other = "Every day and twice on Sundays"
    val formInput = Seq("yourBenefits.answer" -> yourBenefits,
      "anyPaymentsSinceClaimDate.answer" -> anyPaymentsSinceClaimDate,
      "whoPaysYou" -> whoPaysYou,
      "howMuch" -> howMuch,
      "howOften.frequency" -> howOften_frequency,
      "howOften.frequency.other" -> howOften_frequency_other)

    "present 'Your course details'" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.claimKey -> claimKey)

      val result = G1AboutOtherMoney.present(request)

      status(result) mustEqual OK
    }

    "add submitted data to the cached claim" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.claimKey -> claimKey)
        .withFormUrlEncodedBody(formInput: _*)

      val result = G1AboutOtherMoney.submit(request)
      val claim = Cache.getAs[Claim](claimKey).get
      val section: Section = claim.section(domain.OtherMoney)

      section.questionGroup(AboutOtherMoney) must beLike {
        case Some(f: AboutOtherMoney) => {
          f.yourBenefits.answer must equalTo(yourBenefits)
          f.anyPaymentsSinceClaimDate.answer must equalTo(anyPaymentsSinceClaimDate)
          f.whoPaysYou must equalTo(Some(whoPaysYou))
          f.howMuch must equalTo(Some(howMuch))
          f.howOften must equalTo(Some(PaymentFrequency(howOften_frequency, Some(howOften_frequency_other))))
        }
      }
    }

    "return a bad request after an invalid submission" in {
      "reject invalid yesNo answers" in new WithApplication with Claiming {
        val request = FakeRequest().withSession(CachedClaim.claimKey -> claimKey)
          .withFormUrlEncodedBody("yourBenefits.answer" -> "INVALID",
            "anyPaymentsSinceClaimDate.answer" -> "INVALID")

        val result = controllers.s9_other_money.G1AboutOtherMoney.submit(request)
        status(result) mustEqual BAD_REQUEST
      }

      "missing mandatory fields" in new WithApplication with Claiming {
        val request = FakeRequest().withSession(CachedClaim.claimKey -> claimKey)
          .withFormUrlEncodedBody("" -> "")

        val result = controllers.s9_other_money.G5StatutorySickPay.submit(request)
        status(result) mustEqual BAD_REQUEST
      }

      "reject a howOften frequency of other with no other text entered" in new WithApplication with Claiming {
        val request = FakeRequest().withSession(CachedClaim.claimKey -> claimKey)
          .withFormUrlEncodedBody("yourBenefits.answer" -> yourBenefits,
            "anyPaymentsSinceClaimDate.answer" -> anyPaymentsSinceClaimDate,
            "whoPaysYou" -> whoPaysYou,
            "howMuch" -> howMuch,
            "howOften.frequency" -> "other",
            "howOften.frequency.other" -> "")

        val result = controllers.s9_other_money.G5StatutorySickPay.submit(request)
        status(result) mustEqual BAD_REQUEST
      }
    }

    "redirect to the next page after a valid submission" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.claimKey -> claimKey)
        .withFormUrlEncodedBody(formInput: _*)

      val result = G1AboutOtherMoney.submit(request)

      status(result) mustEqual SEE_OTHER
    }
  } section ("unit", models.domain.OtherMoney.id)
}
