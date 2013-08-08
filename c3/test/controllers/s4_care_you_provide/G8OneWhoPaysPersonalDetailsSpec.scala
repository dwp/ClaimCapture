package controllers.s4_care_you_provide

import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import org.specs2.mutable.{Tags, Specification}
import play.api.cache.Cache
import models.domain.{Claiming, OneWhoPaysPersonalDetails, Claim}

class G8OneWhoPaysPersonalDetailsSpec extends Specification with Tags {
  "G8OneWhoPaysPersonalDetails - Controller" should {
    "add 'one who pays personal details' to the cached claim" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey).withFormUrlEncodedBody(
        "firstName" -> "John",
        "surname" -> "Doe",
        "amount" -> "44.99",
        "startDatePayment.day" -> "1",
        "startDatePayment.month" -> "1",
        "startDatePayment.year" -> "1999")

      val result = G8OneWhoPaysPersonalDetails.submit(request)
      status(result) mustEqual SEE_OTHER

      val claim = Cache.getAs[Claim](claimKey).get
      val section= claim.section(models.domain.CareYouProvide)

      section.questionGroup(OneWhoPaysPersonalDetails) must beLike {
        case Some(o: OneWhoPaysPersonalDetails) => {
          o.firstName shouldEqual "John"
        }
      }
    }

    "return a BadRequest on an invalid submission" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody("startDatePayment.year" -> "12345")

      val result = G8OneWhoPaysPersonalDetails.submit(request)
      status(result) mustEqual BAD_REQUEST
    }
  } section("unit", models.domain.CareYouProvide.id)
}