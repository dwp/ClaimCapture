package controllers.s8_self_employment

import app.PensionPaymentFrequency._
import models.PensionPaymentFrequency
import models.domain._
import models.view.CachedClaim
import org.specs2.mutable.{Specification, Tags}
import play.api.test.Helpers._
import play.api.test.{FakeRequest, WithApplication}

class G5ChildcareExpensesWhileAtWorkSpec extends Specification with Tags {

  "Self Employment - Child care expenses while at work - Controller" should {
    val whoLooksAfterChildren = "myself"
    val howMuchYouPay = "123.45"
    val howOften_frequency = Other
    val howOften_frequency_other = "Every day and twice on Sundays"
    val whatRelationIsToYou = "Son"
    val relationToPartner = "Married"
    val whatRelationIsTothePersonYouCareFor = "mother"

    val selfEmploymentChildCareExpensesInput = Seq(
      "whoLooksAfterChildren" -> whoLooksAfterChildren,
      "howMuchYouPay" -> howMuchYouPay,
      "howOftenPayChildCare.frequency" -> howOften_frequency,
      "howOftenPayChildCare.frequency.other" -> howOften_frequency_other,
      "whatRelationIsToYou" -> whatRelationIsToYou,
      "relationToPartner" -> relationToPartner,
      "whatRelationIsTothePersonYouCareFor" -> whatRelationIsTothePersonYouCareFor
    )

    "present 'Child Care Expenses while at work' " in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody("doYouPayToPensionScheme" -> "no", "doYouPayToLookAfterYourChildren" -> "yes", "didYouPayToLookAfterThePersonYouCaredFor" -> "yes")

      val result = G4SelfEmploymentPensionsAndExpenses.submit(request)

      val request2 = FakeRequest().withSession(CachedClaim.key -> extractCacheKey(result))

      val result2 = G5ChildcareExpensesWhileAtWork.present(request2)
      status(result2) mustEqual OK
    }

    "add submitted form to the cached claim" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody(selfEmploymentChildCareExpensesInput: _*)

      val result = controllers.s8_self_employment.G5ChildcareExpensesWhileAtWork.submit(request)
      val claim = getClaimFromCache(result)
      val section: Section = claim.section(models.domain.SelfEmployment)

      section.questionGroup(ChildcareExpensesWhileAtWork) must beLike {
        case Some(f: ChildcareExpensesWhileAtWork) => {
          f.howMuchYouPay must equalTo(howMuchYouPay)
          f.howOftenPayChildCare must equalTo(PensionPaymentFrequency(howOften_frequency, Some(howOften_frequency_other))) // TODO
          f.nameOfPerson must equalTo(whoLooksAfterChildren)
          f.whatRelationIsToYou must equalTo(whatRelationIsToYou)
          f.relationToPartner must equalTo(Some(relationToPartner))
          f.whatRelationIsTothePersonYouCareFor must equalTo(whatRelationIsTothePersonYouCareFor)
        }
      }
    }

    "reject when missing mandatory field whoLooksAfterChildren" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody(
        "whoLooksAfterChildren" -> "",
        "howMuchYouPay" -> howMuchYouPay,
        "howOftenPayChildCare.frequency" -> howOften_frequency,
        "howOftenPayChildCare.frequency.other" -> howOften_frequency_other,
        "whatRelationIsToYou" -> whatRelationIsToYou,
        "relationToPartner" -> relationToPartner,
        "whatRelationIsTothePersonYouCareFor" -> whatRelationIsTothePersonYouCareFor
      )

      val result = controllers.s8_self_employment.G5ChildcareExpensesWhileAtWork.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "reject missing mandatory field howMuchYouPay" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody(
        "whoLooksAfterChildren" -> whoLooksAfterChildren,
        "howMuchYouPay" -> "",
        "howOftenPayChildCare.frequency" -> howOften_frequency,
        "howOftenPayChildCare.frequency.other" -> howOften_frequency_other,
        "whatRelationIsToYou" -> whatRelationIsToYou,
        "relationToPartner" -> relationToPartner,
        "whatRelationIsTothePersonYouCareFor" -> whatRelationIsTothePersonYouCareFor
      )

      val result = controllers.s8_self_employment.G5ChildcareExpensesWhileAtWork.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "reject missing mandatory field howOftenPayChildCare" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody(
        "whoLooksAfterChildren" -> whoLooksAfterChildren,
        "howMuchYouPay" -> howMuchYouPay,
        "howOftenPayChildCare.frequency" -> "",
        "howOftenPayChildCare.frequency.other" -> howOften_frequency_other,
        "whatRelationIsToYou" -> whatRelationIsToYou,
        "relationToPartner" -> relationToPartner,
        "whatRelationIsTothePersonYouCareFor" -> whatRelationIsTothePersonYouCareFor
      )

      val result = controllers.s8_self_employment.G5ChildcareExpensesWhileAtWork.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "reject other selected but other not filled on" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody(
        "whoLooksAfterChildren" -> whoLooksAfterChildren,
        "howMuchYouPay" -> howMuchYouPay,
        "howOftenPayChildCare.frequency" -> howOften_frequency,
        "howOftenPayChildCare.frequency.other" -> "",
        "whatRelationIsToYou" -> whatRelationIsToYou,
        "relationToPartner" -> relationToPartner,
        "whatRelationIsTothePersonYouCareFor" -> whatRelationIsTothePersonYouCareFor
      )

      val result = controllers.s8_self_employment.G5ChildcareExpensesWhileAtWork.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "reject missing mandatory field whatRelationIsToYou" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody(
        "whoLooksAfterChildren" -> whoLooksAfterChildren,
        "howMuchYouPay" -> howMuchYouPay,
        "howOftenPayChildCare.frequency" -> howOften_frequency,
        "howOftenPayChildCare.frequency.other" -> howOften_frequency_other,
        "whatRelationIsToYou" -> "",
        "relationToPartner" -> relationToPartner,
        "whatRelationIsTothePersonYouCareFor" -> whatRelationIsTothePersonYouCareFor
      )

      val result = controllers.s8_self_employment.G5ChildcareExpensesWhileAtWork.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "reject missing mandatory field whatRelationIsTothePersonYouCareFor" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody(
        "whoLooksAfterChildren" -> whoLooksAfterChildren,
        "howMuchYouPay" -> howMuchYouPay,
        "howOftenPayChildCare.frequency" -> howOften_frequency,
        "howOftenPayChildCare.frequency.other" -> howOften_frequency_other,
        "whatRelationIsToYou" -> whatRelationIsToYou,
        "relationToPartner" -> relationToPartner,
        "whatRelationIsTothePersonYouCareFor" -> ""
      )

      val result = controllers.s8_self_employment.G5ChildcareExpensesWhileAtWork.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "redirect to the next page after a valid submission" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody(selfEmploymentChildCareExpensesInput: _*)

      val result = controllers.s8_self_employment.G5ChildcareExpensesWhileAtWork.submit(request)
      status(result) mustEqual SEE_OTHER
    }
  } section("unit", models.domain.SelfEmployment.id)
}