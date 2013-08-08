package controllers.s4_care_you_provide

import org.specs2.mutable.{Tags, Specification}
import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import play.api.cache.Cache
import models.domain.{Claiming, BreaksInCare, Claim}

class G10BreaksInCareSpec extends Specification with Tags {
  "Breaks in care" should {
    """present "Have you had any breaks in caring for this person".""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)

      val result = G10BreaksInCare.present(request)
      status(result) mustEqual OK
    }

    """enforce answer to "Have you had any breaks in caring for this person".""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)

      val result = G10BreaksInCare.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    """accept "yes" to "Have you had any breaks in caring for this person".""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey).withFormUrlEncodedBody("answer" -> "yes")

      val result = G10BreaksInCare.submit(request)
      redirectLocation(result) must beSome("/careYouProvide/break")
    }

    """accept "no" to "Have you had any breaks in caring for this person".""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey).withFormUrlEncodedBody("answer" -> "no")

      val result = G10BreaksInCare.submit(request)
      redirectLocation(result) must beSome("/careYouProvide/completed")
    }

    "complete upon indicating that there are no more breaks having provided zero break details" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey).withFormUrlEncodedBody("answer" -> "no")

      val result = G10BreaksInCare.submit(request)
      redirectLocation(result) must beSome("/careYouProvide/completed")

      val claim = Cache.getAs[Claim](claimKey).get

      claim.questionGroup(BreaksInCare) must beLike { case Some(b: BreaksInCare) => b.breaks mustEqual Nil }
    }

    "complete upon indicating that there are no more breaks having now provided one break" in new WithApplication with Claiming {
      val request1 = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody(
        "breakID" -> "newID",
        "start.day" -> "1",
        "start.month" -> "1",
        "start.year" -> "2001",
        "whereYou.location" -> "Holiday",
        "wherePerson.location" -> "Holiday",
        "medicalDuringBreak" -> "no")

      val result1 = G11Break.submit(request1)
      redirectLocation(result1) must beSome("/careYouProvide/breaksInCare")

      val request2 = FakeRequest().withSession("connected" -> claimKey).withFormUrlEncodedBody("answer" -> "no")

      val result2 = G10BreaksInCare.submit(request2)
      redirectLocation(result2) must beSome("/careYouProvide/completed")

      val claim = Cache.getAs[Claim](claimKey).get

      claim.questionGroup(BreaksInCare) must beLike { case Some(b: BreaksInCare) => b.breaks.size mustEqual 1 }
    }

    "allow no more than 10 breaks" in new WithApplication with Claiming {
      for (i <- 1 to 10) {
        val request = FakeRequest().withSession("connected" -> claimKey)
          .withFormUrlEncodedBody(
          "breakID" -> i.toString,
          "start.day" -> "1",
          "start.month" -> "1",
          "start.year" -> "2001",
          "whereYou.location" -> "Holiday",
          "wherePerson.location" -> "Holiday",
          "medicalDuringBreak" -> "no")

        val result = G11Break.submit(request)
        redirectLocation(result) must beSome("/careYouProvide/breaksInCare")
      }

      Cache.getAs[Claim](claimKey).get.questionGroup(BreaksInCare) must beLike {
        case Some(b: BreaksInCare) => b.breaks.size mustEqual 10
      }

      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody(
        "breakID" -> "999",
        "start.day" -> "1",
        "start.month" -> "1",
        "start.year" -> "2001",
        "whereYou.location" -> "Holiday",
        "wherePerson.location" -> "Holiday",
        "medicalDuringBreak" -> "no")

      val result = G11Break.submit(request)
      redirectLocation(result) must beSome("/careYouProvide/breaksInCare")

      Cache.getAs[Claim](claimKey).get.questionGroup(BreaksInCare) must beLike {
        case Some(b: BreaksInCare) => b.breaks.size mustEqual 10
      }
    }

    "have no breaks upon deleting a break" in new WithApplication with Claiming {
      val breakID = "1"

      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody(
        "breakID" -> breakID,
        "start.day" -> "1",
        "start.month" -> "1",
        "start.year" -> "2001",
        "whereYou.location" -> "Holiday",
        "wherePerson.location" -> "Holiday",
        "medicalDuringBreak" -> "no")

      G11Break.submit(request)

      G10BreaksInCare.delete(breakID)(FakeRequest().withSession("connected" -> claimKey))

      Cache.getAs[Claim](claimKey).get.questionGroup(BreaksInCare) must beLike {
        case Some(b: BreaksInCare) => b.breaks.size mustEqual 0
      }
    }
  } section("unit",models.domain.CareYouProvide.id)
}