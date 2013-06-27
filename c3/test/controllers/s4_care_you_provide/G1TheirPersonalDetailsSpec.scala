package controllers.s4_care_you_provide

import org.specs2.mutable.Specification
import org.specs2.mock.Mockito
import play.api.test.{FakeRequest, WithApplication}
import models.view.Claiming
import play.api.cache.Cache
import models.domain._
import models.{DayMonthYear, domain}
import play.api.test.Helpers._
import models.domain.Section
import scala.Some

class G1TheirPersonalDetailsSpec extends Specification with Mockito {

  val theirPersonalDetailsInput = Seq("title" -> "Mr", "firstName" -> "John", "surname" -> "Doo",
    "dateOfBirth.day" -> "5", "dateOfBirth.month" -> "12", "dateOfBirth.year" -> "1990", "liveAtSameAddress" -> "yes")

  "Their Personal Details - Controller" should {


    "present 'Their Personal Details' " in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)

      val result = controllers.s4_care_you_provide.G1TheirPersonalDetails.present(request)
      status(result) mustEqual OK
    }


    "add 'Their Personal Details' to the cached claim" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody(theirPersonalDetailsInput: _*)

      val result = controllers.s4_care_you_provide.G1TheirPersonalDetails.submit(request)
      val claim = Cache.getAs[Claim](claimKey).get
      val section: Section = claim.section(domain.CareYouProvide.id).get

      section.questionGroup(TheirPersonalDetails.id) must beLike {
        case Some(f: TheirPersonalDetails) => {
          f.title mustEqual "Mr"
          f.firstName mustEqual "John"
          f.surname mustEqual "Doo"
          f.dateOfBirth mustEqual DayMonthYear(Some(5), Some(12), Some(1990), None, None)
          f.liveAtSameAddress mustEqual "yes"
        }
      }
    }

    "return a bad request after an invalid submission" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody("title" -> "Mr")

      val result = controllers.s4_care_you_provide.G1TheirPersonalDetails.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "redirect to the next page after a valid submission" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody(theirPersonalDetailsInput: _*)

      val result = controllers.s4_care_you_provide.G1TheirPersonalDetails.submit(request)
      redirectLocation(result) must beSome("/careYouProvide/theirContactDetails")
    }
  }


}
