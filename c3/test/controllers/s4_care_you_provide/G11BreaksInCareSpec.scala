package controllers.s4_care_you_provide

import org.specs2.mutable.Specification
import play.api.test.{FakeRequest, WithApplication}
import models.view.Claiming
import play.api.test.Helpers._
import play.api.cache.Cache
import models.domain.{BreaksInCare, Claim}
import org.specs2.mock.Mockito

class G11BreaksInCareSpec extends Specification with Mockito {
  "Breaks in care" should {
    "present" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)

      val result = G11BreaksInCare.present(request)
      status(result) mustEqual OK
    }

    "complete upon indicating that there are no more breaks having provided zero break details" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody("moreBreaks" -> "no")

      val result = G11BreaksInCare.submit(request)
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

      val result = G11BreaksInCare.submit(request)
      redirectLocation(result) must beSome("/careYouProvide/completed")

      val claim = Cache.getAs[Claim](claimKey).get

      claim.questionGroup(BreaksInCare.id) must beLike {
        case Some(b: BreaksInCare) => b.breaks.size mustEqual 1
      }
    }

    """allow more breaks to be added (answer "yes" to "Have you had any more breaks")""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey).withFormUrlEncodedBody("moreBreaks" -> "yes")

      val result = G11BreaksInCare.submit(request)
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

      val result1 = G11BreaksInCare.submit(request1)
      redirectLocation(result1) must beSome("/careYouProvide/breaksInCare")

      val request2 = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody(
        "break.start.day" -> "1",
        "break.start.month" -> "1",
        "break.start.year" -> "2001",
        "break.whereYou.location" -> "Holiday",
        "break.wherePerson.location" -> "Holiday",
        "moreBreaks" -> "no")

      val result2 = G11BreaksInCare.submit(request2)
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

        val result = G11BreaksInCare.submit(request)
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

      val result = G11BreaksInCare.submit(request)
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

      G11BreaksInCare.submit(request)

      Cache.getAs[Claim](claimKey).get.questionGroup(BreaksInCare.id) must beLike {
        case Some(b: BreaksInCare) =>
          val breakID = b.breaks.head.id
          G11BreaksInCare.deleteBreak(breakID)(FakeRequest().withSession("connected" -> claimKey))

          Cache.getAs[Claim](claimKey).get.questionGroup(BreaksInCare.id) must beLike {
            case Some(b: BreaksInCare) => b.breaks.size mustEqual 0
          }
      }
    }

    "update existing break" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody(
        "break.start.day" -> "1",
        "break.start.month" -> "1",
        "break.start.year" -> "2001",
        "break.whereYou.location" -> "Holiday",
        "break.wherePerson.location" -> "Holiday",
        "moreBreaks" -> "yes")

      G11BreaksInCare.submit(request)

      Cache.getAs[Claim](claimKey).get.questionGroup(BreaksInCare.id) must beLike {
        case Some(b: BreaksInCare) =>
          val breakID = b.breaks.head.id
          val updatedYear = 2005

          val request = FakeRequest().withSession("connected" -> claimKey)
            .withFormUrlEncodedBody(
            "breakID" -> breakID,
            "start.day" -> "1",
            "start.month" -> "1",
            "start.year" -> updatedYear.toString,
            "whereYou.location" -> "Holiday",
            "wherePerson.location" -> "Holiday")

          G11BreaksInCare.breakSubmit(request)

          Cache.getAs[Claim](claimKey).get.questionGroup(BreaksInCare.id) must beLike {
            case Some(b: BreaksInCare) =>
              b.breaks.head.start.year must beSome(updatedYear)
          }
      }
    }
  }
}