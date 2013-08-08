package controllers.s8_self_employment

import org.specs2.mutable.{Tags, Specification}
import play.api.test.{FakeRequest, WithApplication}
import models.domain._
import play.api.test.Helpers._
import play.api.cache.Cache
import scala.Some


class G4SelfEmploymentPensionsAndExpensesSpec extends Specification with Tags {

  "Self Employment - Pensions and Expenses - Controller" should {
    val doYouPayToPensionScheme = "yes"
    val howMuchDidYouPay = "11"
    val doYouPayToLookAfterYourChildren = "yes"
    val didYouPayToLookAfterThePersonYouCaredFor = "yes"

    val selfEmploymentPensionsAndExpensesInput = Seq("doYouPayToPensionScheme.answer" -> doYouPayToPensionScheme,
      "doYouPayToPensionScheme.howMuchDidYouPay" -> howMuchDidYouPay,
      "doYouPayToLookAfterYourChildren" -> doYouPayToLookAfterYourChildren,
      "didYouPayToLookAfterThePersonYouCaredFor" -> didYouPayToLookAfterThePersonYouCaredFor
    )

    "present 'Pensions and Expenses' " in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)

      val result = controllers.s8_self_employment.G4SelfEmploymentPensionsAndExpenses.present(request)
      status(result) mustEqual OK
    }

    "add submitted form to the cached claim" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody(selfEmploymentPensionsAndExpensesInput: _*)

      val result = controllers.s8_self_employment.G4SelfEmploymentPensionsAndExpenses.submit(request)
      val claim = Cache.getAs[Claim](claimKey).get
      val section: Section = claim.section(models.domain.SelfEmployment)
      section.questionGroup(SelfEmploymentPensionsAndExpenses) must beLike {
        case Some(f: SelfEmploymentPensionsAndExpenses) => {
          f.pensionSchemeMapping.answer must equalTo(doYouPayToPensionScheme)
          f.pensionSchemeMapping.text must equalTo(Some(howMuchDidYouPay))
          f.doYouPayToLookAfterYourChildren must equalTo(doYouPayToLookAfterYourChildren)
          f.didYouPayToLookAfterThePersonYouCaredFor must equalTo(didYouPayToLookAfterThePersonYouCaredFor)
        }
      }
    }

    "missing mandatory field" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody(
        "doYouPayToPensionScheme.answer" -> "no",
        "doYouPayToLookAfterYourChildren.answer" -> "yes",
        "didYouPayToLookAfterThePersonYouCaredFor.answer" -> "")

      val result = controllers.s8_self_employment.G4SelfEmploymentPensionsAndExpenses.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "missing mandatory field doYouPayToLookAfterYourChildren" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody(
        "doYouPayToPensionScheme.answer" -> "no",
        "doYouPayToLookAfterYourChildren.answer" -> "",
        "didYouPayToLookAfterThePersonYouCaredFor.answer" -> "yes")

      val result = controllers.s8_self_employment.G4SelfEmploymentPensionsAndExpenses.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "missing mandatory field doYouPayToPensionScheme" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody(
        "doYouPayToPensionScheme.answer" -> "",
        "doYouPayToLookAfterYourChildren.answer" -> "yes",
        "didYouPayToLookAfterThePersonYouCaredFor.answer" -> "yes")

      val result = controllers.s8_self_employment.G4SelfEmploymentPensionsAndExpenses.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "redirect to the next page after a valid submission" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody(selfEmploymentPensionsAndExpensesInput: _*)

      val result = controllers.s8_self_employment.G4SelfEmploymentPensionsAndExpenses.submit(request)
      status(result) mustEqual SEE_OTHER
    }

  }
}
