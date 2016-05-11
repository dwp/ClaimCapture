package controllers.your_income

import models.domain._
import org.specs2.mutable._
import play.api.test.FakeRequest
import play.api.test.Helpers._
import utils.WithApplication

class GYourIncomesSpec extends Specification {
  section ("unit", models.domain.YourIncomes.id)
  "Your Incomes - Controller" should {
    val yes = "yes"

    val formInput = Seq(
      "beenSelfEmployedSince1WeekBeforeClaim" -> yes,
      "beenEmployedSince6MonthsBeforeClaim" -> yes,
      "yourIncome_sickpay" -> "true",
      "yourIncome_patmatadoppay" -> "true",
      "yourIncome_fostering" -> "true",
      "yourIncome_directpay" -> "true",
      "yourIncome_anyother" -> "true"
    )

    "present 'Your Incomes '" in new WithApplication with Claiming {
      val request = FakeRequest()

      val result = GYourIncomes.present(request)

      status(result) mustEqual OK
    }

    "add submitted data to the cached claim" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody(formInput: _*)

      val result = GYourIncomes.submit(request)
      val claim = getClaimFromCache(result)
      val section: Section = claim.section(YourIncome)

      section.questionGroup(YourIncomes) must beLike {
        case Some(f: YourIncomes) => {
          f.beenSelfEmployedSince1WeekBeforeClaim must equalTo(yes)
          f.beenEmployedSince6MonthsBeforeClaim must equalTo(yes)
          f.yourIncome_sickpay must equalTo(Some("true"))
          f.yourIncome_patmatadoppay must equalTo(Some("true"))
          f.yourIncome_fostering must equalTo(Some("true"))
          f.yourIncome_directpay must equalTo(Some("true"))
          f.yourIncome_anyother must equalTo(Some("true"))
        }
      }
    }

    "reject invalid yesNo answers" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody(
          "beenSelfEmployedSince1WeekBeforeClaim" -> "INVALID",
          "beenEmployedSince6MonthsBeforeClaim" -> "INVALID"
        )

      val result = GYourIncomes.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "missing mandatory fields" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody("" -> "")

      val result = GYourIncomes.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "reject a when all other income fields selected" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody(
          "beenSelfEmployedSince1WeekBeforeClaim" -> yes,
          "beenEmployedSince6MonthsBeforeClaim" -> yes,
          "yourIncome_sickpay" -> "true",
          "yourIncome_patmatadoppay" -> "true",
          "yourIncome_fostering" -> "true",
          "yourIncome_directpay" -> "true",
          "yourIncome_anyother" -> "true",
          "yourIncome_none" -> "true"
        )
      val result = GYourIncomes.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "reject a when no other income fields selected" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody(
          "beenSelfEmployedSince1WeekBeforeClaim" -> yes,
          "beenEmployedSince6MonthsBeforeClaim" -> yes
        )
      val result = GYourIncomes.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "redirect to the next page after a valid submission" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody(formInput: _*)

      val result = GYourIncomes.submit(request)

      status(result) mustEqual SEE_OTHER
    }
  }
  section ("unit", models.domain.YourIncomes.id)
}
