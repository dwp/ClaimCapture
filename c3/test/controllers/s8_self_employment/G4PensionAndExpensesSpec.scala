package controllers.s8_self_employment

import org.specs2.mutable.{Tags, Specification}
import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import models.domain._
import models.view.CachedClaim
import scala.Some

class G4PensionAndExpensesSpec extends Specification with Tags {


  "Pension and expenses" should {
    "present" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)
      val result = G4SelfEmploymentPensionsAndExpenses.present(request)
      status(result) mustEqual OK
    }

    "require all mandatory data" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)

      val result = G4SelfEmploymentPensionsAndExpenses.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "accept all mandatory data" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey).withFormUrlEncodedBody(
        "payPensionScheme.answer" -> "yes",
        "haveExpensesForJob.answer" -> "yes",
        "payPensionScheme.text" -> "some pension expense",
        "haveExpensesForJob.text" -> "some job expense"
      )
      val result = G4SelfEmploymentPensionsAndExpenses.submit(request)
      status(result) mustEqual SEE_OTHER
    }

  } section("unit", models.domain.SelfEmployment.id)
}