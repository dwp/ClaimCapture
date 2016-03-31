package controllers.your_income

import models.domain._
import org.specs2.mutable._
import play.api.test.FakeRequest
import utils.WithApplication
import play.api.test.Helpers._

class GStatutorySickPaySpec extends Specification {
  section ("unit", models.domain.StatutorySickPay.id)
  "Statutory Sick Pay - Controller" should {
    val whoPaysYou = "The Man"
    val howMuch = "12"
    val yes = "yes"

    val formInput = Seq(
      "stillBeingPaidStatutorySickPay" -> yes,
      "whenDidYouLastGetPaid" -> "",
      "whoPaidYouStatutorySickPay" -> whoPaysYou,
      "amountOfStatutorySickPay" -> howMuch,
      "howOftenPaidStatutorySickPay" -> "Monthly",
      "howOftenPaidStatutorySickPayOther" -> ""
    )

    "present 'Statutory Sick Pay '" in new WithApplication with Claiming {
      val request = FakeRequest()

      val result = GStatutorySickPay.present(request)

      status(result) mustEqual OK
    }

    "add submitted data to the cached claim" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody(formInput: _*)

      val result = GStatutorySickPay.submit(request)
      val claim = getClaimFromCache(result)
      val section: Section = claim.section(YourIncomeStatutorySickPay)

      section.questionGroup(StatutorySickPay) must beLike {
        case Some(f: StatutorySickPay) => {
          f.stillBeingPaidStatutorySickPay must equalTo(yes)
          f.whenDidYouLastGetPaid must equalTo(None)
          f.whoPaidYouStatutorySickPay must equalTo(whoPaysYou)
          f.amountOfStatutorySickPay must equalTo(howMuch)
          f.howOftenPaidStatutorySickPay must equalTo("Monthly")
          f.howOftenPaidStatutorySickPayOther must equalTo(None)
        }
      }
    }

    "return a bad request after an invalid submission" in new WithApplication {
      "reject invalid yesNo answers" in new WithApplication with Claiming {
        val request = FakeRequest()
          .withFormUrlEncodedBody(
            "stillBeingPaidStatutorySickPay" -> "INVALID",
            "whenDidYouLastGetPaid" -> "INVALID",
            "amountOfStatutorySickPay" -> "INVALID"
          )

        val result = GStatutorySickPay.submit(request)
        status(result) mustEqual BAD_REQUEST
      }

      "missing mandatory fields" in new WithApplication with Claiming {
        val request = FakeRequest()
          .withFormUrlEncodedBody("" -> "")

        val result = GStatutorySickPay.submit(request)
        status(result) mustEqual BAD_REQUEST
      }

      "reject a howOften frequency of other with no other text entered" in new WithApplication with Claiming {
        val request = FakeRequest()
          .withFormUrlEncodedBody(
            "stillBeingPaidStatutorySickPay" -> yes,
            "whenDidYouLastGetPaid" -> "",
            "whoPaidYouStatutorySickPay" -> whoPaysYou,
            "amountOfStatutorySickPay" -> howMuch,
            "howOftenPaidStatutorySickPay" -> "Other",
            "howOftenPaidStatutorySickPayOther" -> ""
          )
        val result = GStatutorySickPay.submit(request)
        status(result) mustEqual BAD_REQUEST
      }
    }

    "redirect to the next page after a valid submission" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody(formInput: _*)

      val result = GStatutorySickPay.submit(request)

      status(result) mustEqual SEE_OTHER
    }
  }
  section ("unit", models.domain.StatutorySickPay.id)
}
