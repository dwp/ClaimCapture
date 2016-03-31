package controllers.your_income

import models.domain._
import org.specs2.mutable._
import play.api.test.FakeRequest
import play.api.test.Helpers._
import utils.WithApplication

class GStatutoryMaternityPaternityAdoptionPaySpec extends Specification {
  section ("unit", models.domain.StatutoryMaternityPaternityAdoptionPay.id)
  "Statutory Maternity Paternity Adoption Pay - Controller" should {
    val whoPaysYou = "The Man"
    val howMuch = "12"
    val yes = "yes"

    val formInput = Seq(
      "paymentTypesForThisPay" -> "MaternityOrPaternityPay",
      "stillBeingPaidThisPay" -> yes,
      "whenDidYouLastGetPaid" -> "",
      "whoPaidYouThisPay" -> whoPaysYou,
      "amountOfThisPay" -> howMuch,
      "howOftenPaidThisPay" -> "Monthly",
      "howOftenPaidThisPayOther" -> ""
    )

    "present 'Statutory Maternity Paternity Adoption Pay '" in new WithApplication with Claiming {
      val request = FakeRequest()

      val result = GStatutoryMaternityPaternityAdoptionPay.present(request)

      status(result) mustEqual OK
    }

    "add submitted data to the cached claim" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody(formInput: _*)

      val result = GStatutoryMaternityPaternityAdoptionPay.submit(request)
      val claim = getClaimFromCache(result)
      val section: Section = claim.section(YourIncomeStatutoryMaternityPaternityAdoptionPay)

      section.questionGroup(StatutoryMaternityPaternityAdoptionPay) must beLike {
        case Some(f: StatutoryMaternityPaternityAdoptionPay) => {
          f.stillBeingPaidThisPay must equalTo(yes)
          f.whenDidYouLastGetPaid must equalTo(None)
          f.whoPaidYouThisPay must equalTo(whoPaysYou)
          f.amountOfThisPay must equalTo(howMuch)
          f.howOftenPaidThisPay must equalTo("Monthly")
          f.howOftenPaidThisPayOther must equalTo(None)
        }
      }
    }

    "return a bad request after an invalid submission" in new WithApplication {
      "reject invalid yesNo answers" in new WithApplication with Claiming {
        val request = FakeRequest()
          .withFormUrlEncodedBody(
            "stillBeingPaidThisPay" -> "INVALID",
            "whenDidYouLastGetPaid" -> "INVALID",
            "amountOfThisPay" -> "INVALID"
          )

        val result = GStatutoryMaternityPaternityAdoptionPay.submit(request)
        status(result) mustEqual BAD_REQUEST
      }

      "missing mandatory fields" in new WithApplication with Claiming {
        val request = FakeRequest()
          .withFormUrlEncodedBody("" -> "")

        val result = GStatutoryMaternityPaternityAdoptionPay.submit(request)
        status(result) mustEqual BAD_REQUEST
      }

      "reject a howOften frequency of other with no other text entered" in new WithApplication with Claiming {
        val request = FakeRequest()
          .withFormUrlEncodedBody(
            "paymentTypesForThisPay" -> "MaternityOrPaternityPay",
            "stillBeingPaidThisPay" -> yes,
            "whenDidYouLastGetPaid" -> "",
            "whoPaidYouThisPay" -> whoPaysYou,
            "amountOfThisPay" -> howMuch,
            "howOftenPaidThisPay" -> "Other",
            "howOftenPaidThisPayOther" -> ""
          )
        val result = GStatutoryMaternityPaternityAdoptionPay.submit(request)
        status(result) mustEqual BAD_REQUEST
      }
    }

    "redirect to the next page after a valid submission" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody(formInput: _*)

      val result = GStatutoryMaternityPaternityAdoptionPay.submit(request)

      status(result) mustEqual SEE_OTHER
    }
  }
  section ("unit", models.domain.StatutoryMaternityPaternityAdoptionPay.id)
}
