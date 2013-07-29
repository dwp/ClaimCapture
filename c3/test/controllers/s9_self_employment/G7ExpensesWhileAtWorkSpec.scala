package controllers.s9_self_employment

import org.specs2.mutable.{Tags, Specification}
import play.api.test.{FakeRequest, WithApplication}
import models.domain._
import play.api.test.Helpers._
import models.DayMonthYear
import play.api.cache.Cache
import models.domain.Claim
import scala.Some


class G7ExpensesWhileAtWorkSpec extends Specification with Tags {
  "Expenses related to the person you care for while at work - Self Employment - Controller" should {
    val howMuchYouPay = "123"
    val nameOfPerson = "b"
    val whatRelationIsToYou = "c"
    val whatRelationIsTothePersonYouCareFor = "d"

    val expensesWhileAtWorkInput = Seq("howMuchYouPay" -> howMuchYouPay,
          "nameOfPerson" -> nameOfPerson,
          "whatRelationIsToYou" -> whatRelationIsToYou,
          "whatRelationIsTothePersonYouCareFor" -> whatRelationIsTothePersonYouCareFor
      )

    "present 'Expenses related to the person you care for while at work' " in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)

      val result = controllers.s9_self_employment.G7ExpensesWhileAtWork.present(request)
      status(result) mustEqual OK
    }

    "add submitted form to the cached claim" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody(expensesWhileAtWorkInput: _*)

      val result = controllers.s9_self_employment.G7ExpensesWhileAtWork.submit(request)
      val claim = Cache.getAs[Claim](claimKey).get
      val section: Section = claim.section(models.domain.SelfEmployment)
      section.questionGroup(ExpensesWhileAtWork) must beLike {
        case Some(f: ExpensesWhileAtWork) => {
          f.howMuchYouPay must equalTo(Some(howMuchYouPay))
          f.nameOfPerson must equalTo(nameOfPerson)
          f.whatRelationIsToYou must equalTo(Some(whatRelationIsToYou))
          f.whatRelationIsTothePersonYouCareFor must equalTo(Some(whatRelationIsTothePersonYouCareFor))
        }
      }
    }

    "missing mandatory field" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody("nameOfPerson" -> "")

      val result = controllers.s9_self_employment.G7ExpensesWhileAtWork.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "redirect to the next page after a valid submission" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody(expensesWhileAtWorkInput: _*)

      val result = controllers.s9_self_employment.G7ExpensesWhileAtWork.submit(request)
      status(result) mustEqual SEE_OTHER
    }

  } section "unit"
}
