package controllers.s8_self_employment

import play.api.test.{FakeRequest, WithApplication}
import models.domain._
import play.api.test.Helpers._
import org.specs2.mutable.{Tags, Specification}
import play.api.cache.Cache
import scala.Some

class G5ChildcareExpensesWhileAtWorkSpec extends Specification with Tags {

  "Self Employment - Child care expenses while at work - Controller" should {
    val howMuchYouPay = "123445"
    val whoLooksAfterChildren = "myself"
    val whatRelationIsToYou = "son"
    val relationToPartner = "son"
    val whatRelationIsTothePersonYouCareFor = "parent"

    val selfEmploymentChildCareExpensesInput = Seq("howMuchYouPay" -> howMuchYouPay,
      "whoLooksAfterChildren" -> whoLooksAfterChildren,
      "whatRelationIsToYou" -> whatRelationIsToYou,
      "relationToPartner" -> relationToPartner,
      "whatRelationIsTothePersonYouCareFor" -> whatRelationIsTothePersonYouCareFor
    )

    "present 'Child Care Expenses while at work' " in new WithApplication with Claiming {
      val request = FakeRequest().withSession(models.view.CachedClaim.CLAIM_KEY -> claimKey)
        .withFormUrlEncodedBody("doYouPayToPensionScheme.answer" -> "no", "doYouPayToLookAfterYourChildren" -> "yes","didYouPayToLookAfterThePersonYouCaredFor" -> "yes")

      val result = G4SelfEmploymentPensionsAndExpenses.submit(request)

      val request2 = FakeRequest().withSession(models.view.CachedClaim.CLAIM_KEY -> claimKey)

      val result2 = G5ChildcareExpensesWhileAtWork.present(request)
      status(result2) mustEqual OK
    }

    "add submitted form to the cached claim" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(models.view.CachedClaim.CLAIM_KEY -> claimKey)
        .withFormUrlEncodedBody(selfEmploymentChildCareExpensesInput: _*)

      val result = controllers.s8_self_employment.G5ChildcareExpensesWhileAtWork.submit(request)
      val claim = Cache.getAs[Claim](claimKey).get
      val section: Section = claim.section(models.domain.SelfEmployment)
      section.questionGroup(ChildcareExpensesWhileAtWork) must beLike {
        case Some(f: ChildcareExpensesWhileAtWork) => {
          f.howMuchYouPay must equalTo(howMuchYouPay)
          f.nameOfPerson must equalTo(whoLooksAfterChildren)
          f.whatRelationIsToYou must equalTo(whatRelationIsToYou)
          f.relationToPartner must equalTo(Some(relationToPartner))
          f.whatRelationIsTothePersonYouCareFor must equalTo(whatRelationIsTothePersonYouCareFor)
        }
      }
    }

    "missing mandatory field" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(models.view.CachedClaim.CLAIM_KEY -> claimKey)
        .withFormUrlEncodedBody(
        "whatRelationIsToYou" -> whatRelationIsToYou)

      val result = controllers.s8_self_employment.G5ChildcareExpensesWhileAtWork.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "redirect to the next page after a valid submission" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(models.view.CachedClaim.CLAIM_KEY -> claimKey)
        .withFormUrlEncodedBody(selfEmploymentChildCareExpensesInput: _*)

      val result = controllers.s8_self_employment.G5ChildcareExpensesWhileAtWork.submit(request)
      status(result) mustEqual SEE_OTHER
    }
  } section("unit", models.domain.SelfEmployment.id)
}