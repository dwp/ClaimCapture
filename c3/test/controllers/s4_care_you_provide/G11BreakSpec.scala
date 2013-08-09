package controllers.s4_care_you_provide

import org.specs2.mutable.{Tags, Specification}
import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import play.api.cache.Cache
import models.domain.{Claiming, BreaksInCare, Claim}

class G11BreakSpec extends Specification with Tags {
  "Break" should {
    "present" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)

      val result = G11Break.present(request)
      status(result) mustEqual OK
    }

    "be rejected for missing mandatory data" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)

      val result = G11Break.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "add 2 breaks" in new WithApplication with Claiming {
      val request1 = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody(
        "breakID" -> "1",
        "start.day" -> "1",
        "start.month" -> "1",
        "start.year" -> "2001",
        "whereYou.location" -> "Holiday",
        "wherePerson.location" -> "Holiday",
        "medicalDuringBreak" -> "no")

      G11Break.submit(request1)

      val request2 = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody(
        "breakID" -> "2",
        "start.day" -> "1",
        "start.month" -> "1",
        "start.year" -> "2001",
        "whereYou.location" -> "Holiday",
        "wherePerson.location" -> "Holiday",
        "medicalDuringBreak" -> "no")

      G11Break.submit(request2)

      val claim = Cache.getAs[Claim](claimKey).get

      claim.questionGroup(BreaksInCare) must beLike { case Some(b: BreaksInCare) => b.breaks.size mustEqual 2 }
    }

    "update existing break" in new WithApplication with Claiming {
      val breakID = "1"

      val requestNew = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody(
        "breakID" -> breakID,
        "start.day" -> "1",
        "start.month" -> "1",
        "start.year" -> "2001",
        "whereYou.location" -> "Holiday",
        "wherePerson.location" -> "Holiday",
        "medicalDuringBreak" -> "no")

      G11Break.submit(requestNew)

      val yearUpdate = 2005

      val requestUpdate = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody(
        "breakID" -> breakID,
        "start.day" -> "1",
        "start.month" -> "1",
        "start.year" -> yearUpdate.toString,
        "whereYou.location" -> "Holiday",
        "wherePerson.location" -> "Holiday",
        "medicalDuringBreak" -> "no")

      G11Break.submit(requestUpdate)

      Cache.getAs[Claim](claimKey).get.questionGroup(BreaksInCare) must beLike {
        case Some(b: BreaksInCare) =>
          b.breaks.head.start.year must beSome(yearUpdate)
      }
    }
  } section("unit", models.domain.CareYouProvide.id)
}