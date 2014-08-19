package controllers.s4_care_you_provide

import org.specs2.mutable.{Tags, Specification}
import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import play.api.cache.Cache
import models.domain.{Claiming, BreaksInCare, Claim}
import models.view.CachedClaim
import app.Whereabouts._

class G11BreakSpec extends Specification with Tags {
  "Break" should {
    val breakId1 = "1"

    "present" in new WithApplication with Claiming {
      val request = FakeRequest()

      val result = G11Break.present(request)
      status(result) mustEqual OK
    }

    "reject when submitted with missing mandatory data" in new WithApplication with Claiming {
      val request = FakeRequest()

      val result = G11Break.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "redirect to the next page after a valid submission" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody(
        "breakID" -> breakId1,
        "start.day" -> "1",
        "start.month" -> "1",
        "start.year" -> "2001",
        "whereYou.location" -> Holiday,
        "wherePerson.location" -> Holiday,
        "medicalDuringBreak" -> "no")

      val result = G11Break.submit(request)
      status(result) mustEqual SEE_OTHER
    }

    "reject when submitted with other selected but missing other data" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody(
        "breakID" -> breakId1,
        "start.day" -> "1",
        "start.month" -> "1",
        "start.year" -> "2001",
        "whereYou.location" -> Other,
        "whereYou.location.other" -> "",
        "wherePerson.location" -> Other,
        "wherePerson.location.other" -> "",
        "medicalDuringBreak" -> "no")

      val result = G11Break.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "redirect to the next page after a valid submission with other selected" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody(
        "breakID" -> breakId1,
        "start.day" -> "1",
        "start.month" -> "1",
        "start.year" -> "2001",
        "whereYou.location" -> Other,
        "whereYou.location.other" -> "Outer space",
        "wherePerson.location" -> Other,
        "wherePerson.location.other" -> "Underwater",
        "medicalDuringBreak" -> "no")

      val result = G11Break.submit(request)
      status(result) mustEqual SEE_OTHER
    }

    "add 2 breaks" in new WithApplication with Claiming {
      val request1 = FakeRequest()
        .withFormUrlEncodedBody(
        "breakID" -> breakId1,
        "start.day" -> "1",
        "start.month" -> "1",
        "start.year" -> "2001",
        "whereYou.location" -> Holiday,
        "wherePerson.location" -> Holiday,
        "medicalDuringBreak" -> "no")

      val result = G11Break.submit(request1)

      val request2 = FakeRequest().withSession(CachedClaim.key -> extractCacheKey(result))
        .withFormUrlEncodedBody(
        "breakID" -> "2",
        "start.day" -> "1",
        "start.month" -> "1",
        "start.year" -> "2001",
        "whereYou.location" -> Holiday,
        "wherePerson.location" -> Holiday,
        "medicalDuringBreak" -> "no")

      G11Break.submit(request2)

      val claim = getClaimFromCache(result)

      claim.questionGroup(BreaksInCare) must beLike { case Some(b: BreaksInCare) => b.breaks.size mustEqual 2 }
    }

    "update existing break" in new WithApplication with Claiming {
      val requestNew = FakeRequest()
        .withFormUrlEncodedBody(
        "breakID" -> breakId1,
        "start.day" -> "1",
        "start.month" -> "1",
        "start.year" -> "2001",
        "whereYou.location" -> Holiday,
        "wherePerson.location" -> Holiday,
        "medicalDuringBreak" -> "no")

      val result = G11Break.submit(requestNew)

      val yearUpdate = 2005

      val requestUpdate = FakeRequest().withSession(CachedClaim.key -> extractCacheKey(result))
        .withFormUrlEncodedBody(
        "breakID" -> breakId1,
        "start.day" -> "1",
        "start.month" -> "1",
        "start.year" -> yearUpdate.toString,
        "whereYou.location" -> Holiday,
        "wherePerson.location" -> Holiday,
        "medicalDuringBreak" -> "no")

      G11Break.submit(requestUpdate)

      getClaimFromCache(result).questionGroup(BreaksInCare) must beLike {
        case Some(b: BreaksInCare) =>
          b.breaks.head.start.year.get shouldEqual yearUpdate
      }
    }
  } section("unit", models.domain.CareYouProvide.id)
}