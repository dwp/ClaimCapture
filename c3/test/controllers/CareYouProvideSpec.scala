package controllers

import org.specs2.mutable.Specification
import org.specs2.mock.Mockito
import play.api.test.{FakeRequest, WithApplication}
import models.view._
import play.api.cache.Cache
import play.api.test.Helpers._
import scala.Some
import models.{DayMonthYear, domain}
import models.domain.{BreaksInCare, TheirPersonalDetails, Section, Claim}

class CareYouProvideSpec extends Specification with Mockito {

  val theirPersonalDetailsInput = Seq("title" -> "Mr", "firstName" -> "John", "surname" -> "Doo",
    "dateOfBirth.day" -> "5", "dateOfBirth.month" -> "12", "dateOfBirth.year" -> "1990", "liveAtSameAddress" -> "yes")

  "Care You Provide - Their Personal Details" should {

    "add their personal details to the cached claim" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody(theirPersonalDetailsInput:_*)

      val result = controllers.CareYouProvide.theirPersonalDetailsSubmit(request)
      val claim = Cache.getAs[Claim](claimKey).get
      val section: Section = claim.section(domain.CareYouProvide.id).get

      section.questionGroup(TheirPersonalDetails.id) must beLike {
        case Some(f: TheirPersonalDetails) => {
          f.title mustEqual "Mr"
          f.firstName mustEqual "John"
          f.surname mustEqual "Doo"
          f.dateOfBirth mustEqual(DayMonthYear(Some(5), Some(12), Some(1990), None, None))
          f.liveAtSameAddress mustEqual "yes"
        }
      }
    }

    "return a bad request after an invalid submission" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody("title" -> "Mr")

      val result = controllers.CareYouProvide.theirPersonalDetailsSubmit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "redirect to the next page after a valid submission" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody(theirPersonalDetailsInput:_*)

      val result = controllers.CareYouProvide.theirPersonalDetailsSubmit(request)
      redirectLocation(result) must beSome("/careYouProvide/theirContactDetails")
    }
  }

  "Care You Provide with breaks" should {
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
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody("moreBreaks" -> "no")

      val result = controllers.CareYouProvide.breaksInCareSubmit(request)
      redirectLocation(result) must beSome("/careYouProvide/completed")

      val claim = Cache.getAs[Claim](claimKey).get

      claim.questionGroup(BreaksInCare.id) must beLike {
        case Some(b: BreaksInCare) => b.breaks mustEqual Nil
      }
    }

    "complete upon indicating that there are no more breaks having now provided one break" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody(
          "break.start.day" -> "1",
          "break.start.month" -> "1",
          "break.start.year" -> "2001",
          "break.whereYou.location" -> "Holiday",
          "break.wherePerson.location" -> "Holiday",
          "moreBreaks" -> "no")

      val result = controllers.CareYouProvide.breaksInCareSubmit(request)
      redirectLocation(result) must beSome("/careYouProvide/completed")

      val claim = Cache.getAs[Claim](claimKey).get

      claim.questionGroup(BreaksInCare.id) must beLike {
        case Some(b: BreaksInCare) => b.breaks.size mustEqual 1
      }
    }

    """allow more breaks to be added (answer "yes" to "Have you had any more breaks")""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey).withFormUrlEncodedBody("moreBreaks" -> "yes")

      val result = controllers.CareYouProvide.breaksInCareSubmit(request)
      redirectLocation(result) must beSome("/careYouProvide/breaksInCare")

      val claim = Cache.getAs[Claim](claimKey).get

      claim.questionGroup(BreaksInCare.id) must beLike {
        case Some(b: BreaksInCare) => b.breaks mustEqual Nil
      }
    }

    "add 2 breaks" in new WithApplication with Claiming {
      val request1 = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody(
        "break.start.day" -> "1",
        "break.start.month" -> "1",
        "break.start.year" -> "2001",
        "break.whereYou.location" -> "Holiday",
        "break.wherePerson.location" -> "Holiday",
        "moreBreaks" -> "yes")

      val result1 = controllers.CareYouProvide.breaksInCareSubmit(request1)
      redirectLocation(result1) must beSome("/careYouProvide/breaksInCare")

      val request2 = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody(
        "break.start.day" -> "1",
        "break.start.month" -> "1",
        "break.start.year" -> "2001",
        "break.whereYou.location" -> "Holiday",
        "break.wherePerson.location" -> "Holiday",
        "moreBreaks" -> "no")

      val result2 = controllers.CareYouProvide.breaksInCareSubmit(request2)
      redirectLocation(result2) must beSome("/careYouProvide/completed")

      val claim = Cache.getAs[Claim](claimKey).get

      claim.questionGroup(BreaksInCare.id) must beLike {
        case Some(b: BreaksInCare) => b.breaks.size mustEqual 2
      }
    }

    "allow no more than 10 breaks" in new WithApplication with Claiming {
      for (i <- 1 to 9) {
        val request = FakeRequest().withSession("connected" -> claimKey)
          .withFormUrlEncodedBody(
          "break.start.day" -> "1",
          "break.start.month" -> "1",
          "break.start.year" -> "2001",
          "break.end.day" -> "1",
          "break.end.month" -> "1",
          "break.end.year" -> "2001",
          "break.whereYou.location" -> "Holiday",
          "break.wherePerson.location" -> "Holiday",
          "moreBreaks" -> "yes")

        val result = controllers.CareYouProvide.breaksInCareSubmit(request)
        redirectLocation(result) must beSome("/careYouProvide/breaksInCare")
      }

      Cache.getAs[Claim](claimKey).get.questionGroup(BreaksInCare.id) must beLike {
        case Some(b: BreaksInCare) => b.breaks.size mustEqual 9
      }

      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody(
        "break.start.day" -> "1",
        "break.start.month" -> "1",
        "break.start.year" -> "2001",
        "break.end.day" -> "1",
        "break.end.month" -> "1",
        "break.end.year" -> "2001",
        "break.whereYou.location" -> "Holiday",
        "break.wherePerson.location" -> "Holiday",
        "moreBreaks" -> "yes")

      val result = controllers.CareYouProvide.breaksInCareSubmit(request)
      redirectLocation(result) must beSome("/careYouProvide/completed")

      Cache.getAs[Claim](claimKey).get.questionGroup(BreaksInCare.id) must beLike {
        case Some(b: BreaksInCare) => b.breaks.size mustEqual 10
      }
    }

    "have no breaks upon deleting a break" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody(
        "break.start.day" -> "1",
        "break.start.month" -> "1",
        "break.start.year" -> "2001",
        "break.whereYou.location" -> "Holiday",
        "break.wherePerson.location" -> "Holiday",
        "moreBreaks" -> "yes")

      controllers.CareYouProvide.breaksInCareSubmit(request)

      Cache.getAs[Claim](claimKey).get.questionGroup(BreaksInCare.id) must beLike {
        case Some(b: BreaksInCare) =>
          val breakID = b.breaks.head.id
          controllers.CareYouProvide.deleteBreak(breakID)(FakeRequest().withSession("connected" -> claimKey))

          Cache.getAs[Claim](claimKey).get.questionGroup(BreaksInCare.id) must beLike {
            case Some(b: BreaksInCare) => b.breaks.size mustEqual 0
          }
      }
    }
  }
}