package controllers.s4_care_you_provide

import models.domain.{BreaksInCare, Claim, Claiming}
import models.view.CachedClaim
import org.specs2.mutable.{Specification, Tags}
import play.api.cache.Cache
import play.api.test.Helpers._
import play.api.test.{FakeRequest, WithApplication}

class G10BreaksInCareSpec extends Specification with Tags {
  "Breaks from care" should {
    """present "Have you had any breaks in caring for this person".""" in new WithApplication with Claiming {
      val request = FakeRequest()

      val result = G10BreaksInCare.present(request)
      status(result) shouldEqual OK
    }

    """enforce answer to "Have you had any breaks in caring for this person".""" in new WithApplication with Claiming {
      val request = FakeRequest()

      val result = G10BreaksInCare.submit(request)
      status(result) shouldEqual BAD_REQUEST
    }

    """accept "yes" to "Have you had any breaks in caring for this person".""" in new WithApplication with Claiming {
      val request = FakeRequest().withFormUrlEncodedBody("answer" -> "yes")

      val result = G10BreaksInCare.submit(request)
      redirectLocation(result) should beSome("/care-you-provide/break")
    }

    """accept "no" to "Have you had any breaks in caring for this person".""" in new WithApplication with Claiming {
      val request = FakeRequest().withFormUrlEncodedBody("answer" -> "no")

      val result = G10BreaksInCare.submit(request)
      redirectLocation(result) should beSome("/care-you-provide/their-personal-details")
    }

    "complete upon indicating that there are no more breaks having provided zero break details" in new WithApplication with Claiming {
      val request = FakeRequest().withFormUrlEncodedBody("answer" -> "no")

      val result = G10BreaksInCare.submit(request)
      redirectLocation(result) should beSome("/care-you-provide/their-personal-details")

      val claim = Cache.getAs[Claim](extractCacheKey(result)).get

      claim.questionGroup(BreaksInCare) should beLike { case Some(b: BreaksInCare) => b.breaks shouldEqual Nil }
    }

    "complete upon indicating that there are no more breaks having now provided one break" in new WithApplication with Claiming {
      val request1 = FakeRequest()
        .withFormUrlEncodedBody(
        "breakID" -> "newID",
        "start.day" -> "1",
        "start.month" -> "1",
        "start.year" -> "2001",
        "whereYou.location" -> "Holiday",
        "wherePerson.location" -> "Holiday",
        "medicalDuringBreak" -> "no")

      val result1 = G11Break.submit(request1)
      redirectLocation(result1) should beSome("/care-you-provide/breaks-in-care")

      val request2 = FakeRequest().withSession(CachedClaim.key -> extractCacheKey(result1)).withFormUrlEncodedBody("answer" -> "no")

      val result2 = G10BreaksInCare.submit(request2)
      redirectLocation(result2) should beSome("/education/your-course-details")

      val claim = getClaimFromCache(result1)

      claim.questionGroup(BreaksInCare) should beLike { case Some(b: BreaksInCare) => b.breaks.size shouldEqual 1 }
    }

    "allow no more than 10 breaks" in new WithApplication with Claiming {
      val request1 = FakeRequest()
        .withFormUrlEncodedBody(
          "breakID" -> 1.toString,
          "start.day" -> "1",
          "start.month" -> "1",
          "start.year" -> "2001",
          "whereYou.location" -> "Holiday",
          "wherePerson.location" -> "Holiday",
          "medicalDuringBreak" -> "no")
      val result1 = G11Break.submit(request1)
      redirectLocation(result1) should beSome("/care-you-provide/breaks-in-care")

      for (i <- 2 to 10) {
        val request = FakeRequest().withSession(CachedClaim.key -> extractCacheKey(result1))
          .withFormUrlEncodedBody(
          "breakID" -> i.toString,
          "start.day" -> "1",
          "start.month" -> "1",
          "start.year" -> "2001",
          "whereYou.location" -> "Holiday",
          "wherePerson.location" -> "Holiday",
          "medicalDuringBreak" -> "no")

        val result = G11Break.submit(request)
        redirectLocation(result) should beSome("/care-you-provide/breaks-in-care")
      }

      getClaimFromCache(result1).questionGroup(BreaksInCare) should beLike {
        case Some(b: BreaksInCare) => b.breaks.size shouldEqual 10
      }

      val request2 = FakeRequest().withSession(CachedClaim.key -> extractCacheKey(result1))
        .withFormUrlEncodedBody(
        "breakID" -> "999",
        "start.day" -> "1",
        "start.month" -> "1",
        "start.year" -> "2001",
        "whereYou.location" -> "Holiday",
        "wherePerson.location" -> "Holiday",
        "medicalDuringBreak" -> "no")

      val result2 = G11Break.submit(request2)
      redirectLocation(result2) should beSome("/care-you-provide/breaks-in-care")

      getClaimFromCache(result2).questionGroup(BreaksInCare) should beLike {
        case Some(b: BreaksInCare) => b.breaks.size shouldEqual 10
      }
    }

    "have no breaks upon deleting a break" in new WithApplication with Claiming {
      val breakID = "1"

      val request = FakeRequest()
        .withFormUrlEncodedBody(
        "breakID" -> breakID,
        "start.day" -> "1",
        "start.month" -> "1",
        "start.year" -> "2001",
        "whereYou.location" -> "Holiday",
        "wherePerson.location" -> "Holiday",
        "medicalDuringBreak" -> "no")

      val result = G11Break.submit(request)

      G10BreaksInCare.delete(FakeRequest().withSession(CachedClaim.key -> extractCacheKey(result)).withFormUrlEncodedBody("deleteId"->breakID))

      getClaimFromCache(result).questionGroup(BreaksInCare) should beLike {
        case Some(b: BreaksInCare) => b.breaks.size shouldEqual 0
      }
    }

    "issue an 'error' when deleting a non-existing break when there are no existing breaks" in new WithApplication with Claiming {
      val result = G10BreaksInCare.delete(FakeRequest().withFormUrlEncodedBody("deleteId"->"nonExistingBreakID"))
      status(result) shouldEqual BAD_REQUEST
    }

    "issue an 'error' when deleting a non-existing break when there are existing breaks" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody(
        "breakID" -> "1",
        "start.day" -> "1",
        "start.month" -> "1",
        "start.year" -> "2001",
        "whereYou.location" -> "Holiday",
        "wherePerson.location" -> "Holiday",
        "medicalDuringBreak" -> "no")

      val result1 = G11Break.submit(request)

      val result = G10BreaksInCare.delete(FakeRequest().withSession(CachedClaim.key -> extractCacheKey(result1)).withFormUrlEncodedBody("deleteId"->"nonExistingBreakID"))
      status(result) shouldEqual BAD_REQUEST
    }
  } section("unit", models.domain.CareYouProvide.id)
}