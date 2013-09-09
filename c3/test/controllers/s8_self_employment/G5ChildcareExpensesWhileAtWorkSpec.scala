package controllers.s8_self_employment

import play.api.test.{FakeRequest, WithApplication}
import models.domain._
import play.api.test.Helpers._
import org.specs2.mutable.{Tags, Specification}
import play.api.cache.Cache
import models.view.CachedClaim
import app.PensionPaymentFrequency._
import scala.Some

class G5ChildcareExpensesWhileAtWorkSpec extends Specification with Tags {

  "Self Employment - Child care expenses while at work - Controller" should {
    val whoLooksAfterChildren = "myself"
    val howMuchYouPay = "123.45"
    val howOften_frequency = Weekly //Other
    val howOften_frequency_other = "Every day and twice on Sundays"
    val whatRelationIsToYou = "son"
    val relationToPartner = "married"
    val whatRelationIsTothePersonYouCareFor = "mother"

    val selfEmploymentChildCareExpensesInput = Seq(
      "whoLooksAfterChildren" -> whoLooksAfterChildren,
      "howMuchYouPay" -> howMuchYouPay,
      "howOftenPayChildCare" -> howOften_frequency,
      //"howOften.frequency.other" -> howOften_frequency_other,
      "whatRelationIsToYou" -> whatRelationIsToYou,
      "relationToPartner" -> relationToPartner,
      "whatRelationIsTothePersonYouCareFor" -> whatRelationIsTothePersonYouCareFor
    )

    "present 'Child Care Expenses while at work' " in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.claimKey -> claimKey)
        .withFormUrlEncodedBody("doYouPayToPensionScheme" -> "no", "doYouPayToLookAfterYourChildren" -> "yes", "didYouPayToLookAfterThePersonYouCaredFor" -> "yes")

      val result = G4SelfEmploymentPensionsAndExpenses.submit(request)

      val request2 = FakeRequest().withSession(CachedClaim.claimKey -> claimKey)

      val result2 = G5ChildcareExpensesWhileAtWork.present(request)
      status(result2) mustEqual OK
    }

    "add submitted form to the cached claim" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.claimKey -> claimKey)
        .withFormUrlEncodedBody(selfEmploymentChildCareExpensesInput: _*)

      val result = controllers.s8_self_employment.G5ChildcareExpensesWhileAtWork.submit(request)
      val claim = Cache.getAs[Claim](claimKey).get
      val section: Section = claim.section(models.domain.SelfEmployment)

      section.questionGroup(ChildcareExpensesWhileAtWork) must beLike {
        case Some(f: ChildcareExpensesWhileAtWork) => {
          f.howMuchYouPay must equalTo(howMuchYouPay)
          f.howOftenPayChildCare must equalTo(howOften_frequency)
          f.nameOfPerson must equalTo(whoLooksAfterChildren)
          f.whatRelationIsToYou must equalTo(whatRelationIsToYou)
          f.relationToPartner must equalTo(Some(relationToPartner))
          f.whatRelationIsTothePersonYouCareFor must equalTo(whatRelationIsTothePersonYouCareFor)
        }
      }
    }

    "reject when missing mandatory field whoLooksAfterChildren" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.claimKey -> claimKey)
        .withFormUrlEncodedBody(
        "whoLooksAfterChildren" -> "",
        "howMuchYouPay" -> howMuchYouPay,
        "howOftenPayChildCare" -> howOften_frequency,
        //"howOften.frequency.other" -> howOften_frequency_other,
        "whatRelationIsToYou" -> whatRelationIsToYou,
        "relationToPartner" -> relationToPartner,
        "whatRelationIsTothePersonYouCareFor" -> whatRelationIsTothePersonYouCareFor
      )

      val result = controllers.s8_self_employment.G5ChildcareExpensesWhileAtWork.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "reject missing mandatory field howMuchYouPay" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.claimKey -> claimKey)
        .withFormUrlEncodedBody(
        "whoLooksAfterChildren" -> whoLooksAfterChildren,
        "howMuchYouPay" -> "",
        "howOftenPayChildCare" -> howOften_frequency,
        //"howOften.frequency.other" -> howOften_frequency_other,
        "whatRelationIsToYou" -> whatRelationIsToYou,
        "relationToPartner" -> relationToPartner,
        "whatRelationIsTothePersonYouCareFor" -> whatRelationIsTothePersonYouCareFor
      )

      val result = controllers.s8_self_employment.G5ChildcareExpensesWhileAtWork.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "reject missing mandatory field howOftenPayChildCare" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.claimKey -> claimKey)
        .withFormUrlEncodedBody(
        "whoLooksAfterChildren" -> whoLooksAfterChildren,
        "howMuchYouPay" -> howMuchYouPay,
        "howOftenPayChildCare" -> "",
        //"howOften.frequency.other" -> howOften_frequency_other,
        "whatRelationIsToYou" -> whatRelationIsToYou,
        "relationToPartner" -> relationToPartner,
        "whatRelationIsTothePersonYouCareFor" -> whatRelationIsTothePersonYouCareFor
      )

      val result = controllers.s8_self_employment.G5ChildcareExpensesWhileAtWork.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

/*
    "reject other selected but other not filled on" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.claimKey -> claimKey)
        .withFormUrlEncodedBody(
        "whoLooksAfterChildren" -> whoLooksAfterChildren,
        "howMuchYouPay" -> howMuchYouPay,
        "howOftenPayChildCare" -> howOften_frequency,
        "howOften.frequency.other" -> "",
        "whatRelationIsToYou" -> whatRelationIsToYou,
        "relationToPartner" -> relationToPartner,
        "whatRelationIsTothePersonYouCareFor" -> whatRelationIsTothePersonYouCareFor
      )

      val result = controllers.s8_self_employment.G5ChildcareExpensesWhileAtWork.submit(request)
      status(result) mustEqual BAD_REQUEST
    }
*/
    "reject missing mandatory field whatRelationIsToYou" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.claimKey -> claimKey)
        .withFormUrlEncodedBody(
        "whoLooksAfterChildren" -> whoLooksAfterChildren,
        "howMuchYouPay" -> howMuchYouPay,
        "howOftenPayChildCare" -> howOften_frequency,
        //"howOften.frequency.other" -> howOften_frequency_other,
        "whatRelationIsToYou" -> "",
        "relationToPartner" -> relationToPartner,
        "whatRelationIsTothePersonYouCareFor" -> whatRelationIsTothePersonYouCareFor
      )

      val result = controllers.s8_self_employment.G5ChildcareExpensesWhileAtWork.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "reject missing mandatory field whatRelationIsTothePersonYouCareFor" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.claimKey -> claimKey)
        .withFormUrlEncodedBody(
        "whoLooksAfterChildren" -> whoLooksAfterChildren,
        "howMuchYouPay" -> howMuchYouPay,
        "howOftenPayChildCare" -> howOften_frequency,
        //"howOften.frequency.other" -> howOften_frequency_other,
        "whatRelationIsToYou" -> whatRelationIsToYou,
        "relationToPartner" -> relationToPartner,
        "whatRelationIsTothePersonYouCareFor" -> ""
      )

      val result = controllers.s8_self_employment.G5ChildcareExpensesWhileAtWork.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "redirect to the next page after a valid submission" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.claimKey -> claimKey)
        .withFormUrlEncodedBody(selfEmploymentChildCareExpensesInput: _*)

      val result = controllers.s8_self_employment.G5ChildcareExpensesWhileAtWork.submit(request)
      status(result) mustEqual SEE_OTHER
    }
  } section("unit", models.domain.SelfEmployment.id)
}