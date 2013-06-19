package controllers

import org.specs2.mutable.Specification
import org.specs2.mock.Mockito
import play.api.test.{FakeRequest, WithApplication}
import models.claim._
import play.api.cache.Cache
import play.api.test.Helpers._
import models.claim.Section
import models.claim.Claim
import scala.Some

class CareYouProvideSpec extends Specification with Mockito {

  "Care You Provide" should {
    "accept all initial mandatory data" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody(
        "title" -> "Mr",
        "firstName" -> "John",
        "surname" -> "Doo",
        "dateOfBirth.day" -> "5",
        "dateOfBirth.month" -> "12",
        "dateOfBirth.year" -> "1990",
        "liveAtSameAddress" -> "yes")

      val result = controllers.CareYouProvide.theirPersonalDetailsSubmit(request)
      //      redirectLocation(result) must beSome("/careYouProvide/theirContactDetails")

      val claim = Cache.getAs[Claim](claimKey).get
      val section: Section = claim.section(models.claim.CareYouProvide.id).get

      section.form(TheirPersonalDetails.id) must beLike {
        case Some(f: TheirPersonalDetails) => {
          f.title mustEqual "Mr"
          f.firstName mustEqual "John"
          f.surname mustEqual "Doo"
          f.liveAtSameAddress mustEqual "yes"
        }
      }
    }
  }

  "Care You Provide for breaks" should {
    """present "Have you had any breaks in caring for this person" """ in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)

      val result = controllers.CareYouProvide.breaks(request)
      status(result) mustEqual OK
    }

    """enforce answer to "Have you had any breaks in caring for this person" """ in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)

      val result = controllers.CareYouProvide.breaksSubmit(request)
      status(result) mustEqual BAD_REQUEST
    }

    """accept "yes" to "Have you had any breaks in caring for this person" """ in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey).withFormUrlEncodedBody("breaks" -> "yes")

      val result = controllers.CareYouProvide.breaksSubmit(request)
      redirectLocation(result) must beSome("/careYouProvide/breaksInCare")
    }

    """accept "no" to "Have you had any breaks in caring for this person" """ in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey).withFormUrlEncodedBody("breaks" -> "no")

      val result = controllers.CareYouProvide.breaksSubmit(request)
      redirectLocation(result) must beSome("/careYouProvide/completed")
    }
  }
}