package controllers.s4_care_you_provide

import org.specs2.mutable.{Tags, Specification}
import play.api.test.{FakeRequest, WithApplication}
import play.api.cache.Cache
import models.domain._
import models.{DayMonthYear, domain}
import play.api.test.Helpers._
import models.domain.Claim
import models.view.CachedClaim

class G1TheirPersonalDetailsSpec extends Specification with Tags {

  val theirPersonalDetailsInput = Seq("relationship" -> "father", "title" -> "Mr", "firstName" -> "John", "surname" -> "Doo",
    "dateOfBirth.day" -> "5", "dateOfBirth.month" -> "12", "dateOfBirth.year" -> "1990", "armedForcesPayment" -> "yes", "liveAtSameAddressCareYouProvide" -> "yes")

  "Their Personal Details - Controller" should {

    "present 'Their Personal Details'." in new WithApplication with Claiming {
      val request = FakeRequest()

      val result = G1TheirPersonalDetails.present(request)
      status(result) mustEqual OK
    }

    "add 'Their Personal Details' to the cached claim" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody(theirPersonalDetailsInput: _*)

      val result = G1TheirPersonalDetails.submit(request)
      val claim = getClaimFromCache(result)
      val section: Section = claim.section(domain.CareYouProvide)

      section.questionGroup(TheirPersonalDetails) must beLike {
        case Some(t: TheirPersonalDetails) => {
          t.relationship mustEqual "father"
          t.title mustEqual "Mr"
          t.firstName mustEqual "John"
          t.surname mustEqual "Doo"
          t.dateOfBirth mustEqual DayMonthYear(Some(5), Some(12), Some(1990), None, None)
          t.armedForcesPayment mustEqual "yes"
          t.liveAtSameAddressCareYouProvide mustEqual "yes"
        }
      }
    }

    "return a bad request after an invalid submission" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody("title" -> "Mr")

      val result = G1TheirPersonalDetails.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "redirect to the next page after a valid submission" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody(theirPersonalDetailsInput: _*)

      val result = G1TheirPersonalDetails.submit(request)
      redirectLocation(result) must beSome("/care-you-provide/their-contact-details")
    }
  } section("unit", models.domain.CareYouProvide.id)
}