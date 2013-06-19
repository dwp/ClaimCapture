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

      val result = controllers.CareYouProvide.hasBreaks(request)
      status(result) mustEqual OK
    }

    """enforce answer to "Have you had any breaks in caring for this person" """ in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)

      val result = controllers.CareYouProvide.hasBreaksSubmit(request)
      status(result) mustEqual BAD_REQUEST
    }

    """accept "yes" to "Have you had any breaks in caring for this person" """ in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey).withFormUrlEncodedBody("answer" -> "yes")

      val result = controllers.CareYouProvide.hasBreaksSubmit(request)
      redirectLocation(result) must beSome("/careYouProvide/breaksInCare")
    }

    """accept "no" to "Have you had any breaks in caring for this person" """ in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey).withFormUrlEncodedBody("answer" -> "no")

      val result = controllers.CareYouProvide.hasBreaksSubmit(request)
      redirectLocation(result) must beSome("/careYouProvide/completed")
    }

    """present "breaks in care" """ in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)

      val result = controllers.CareYouProvide.breaksInCare(request)
      status(result) mustEqual OK
    }

    "complete upon indicating that there are no more breaks having provided zero break details" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey).withFormUrlEncodedBody("moreBreaks" -> "no")

      val result = controllers.CareYouProvide.breaksInCareSubmit(request)
      redirectLocation(result) must beSome("/careYouProvide/completed")

      val claim = Cache.getAs[Claim](claimKey).get

      claim.form(BreaksInCare.id) must beLike {
        case Some(b: BreaksInCare) => b.breaks mustEqual Nil
      }
    }

    "complete upon indicating that there are no more breaks having now provided one break" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody(
          "moreBreaks" -> "no",
          "break.start.day" -> "1",
          "break.start.month" -> "1",
          "break.start.year" -> "2001",
          "break.end.day" -> "1",
          "break.end.month" -> "1",
          "break.end.year" -> "2001")

      val result = controllers.CareYouProvide.breaksInCareSubmit(request)
      redirectLocation(result) must beSome("/careYouProvide/completed")

      val claim = Cache.getAs[Claim](claimKey).get

      claim.form(BreaksInCare.id) must beLike {
        case Some(b: BreaksInCare) => b.breaks.size mustEqual 1
      }
    }

    /*"""allow more breaks to be added (answer "yes" to ""Have you had any more breaks) """ in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey).withFormUrlEncodedBody("moreBreaks" -> "yes")

      val result = controllers.CareYouProvide.breaksInCareSubmit(request)
      status(result) mustEqual OK
    }*/
  }
}