package controllers.s_breaks_OLD

import controllers.s_breaks.{GOldBreak, GOldBreaksInCare}
import models.domain.{OldBreaksInCareSummary, OldBreaksInCare, Claim, Claiming}
import models.view.CachedClaim
import org.specs2.mutable._
import play.api.test.FakeRequest
import play.api.test.Helpers._
import utils.WithApplication

class GBreaksInCareSpec extends Specification {
  section("unit", models.domain.CareYouProvide.id)
  "Breaks from care" should {
    """present "Have you had any breaks in caring for this person".""" in new WithApplication with Claiming {
      val request = FakeRequest()

      val result = GOldBreaksInCare.present(request)
      status(result) shouldEqual OK
    }

    """enforce answer to "Have you had any breaks in caring for this person".""" in new WithApplication with Claiming {
      val request = FakeRequest()

      val result = GOldBreaksInCare.submit(request)
      status(result) shouldEqual BAD_REQUEST
    }

    """accept "yes" to "Have you had any breaks in caring for this person".""" in new WithApplication with Claiming {
      val request = FakeRequest().withFormUrlEncodedBody("answer" -> "yes")

      val result = GOldBreaksInCare.submit(request)
      redirectLocation(result).get must contain("/breaks/break/")
    }

    """accept "no" to "Have you had any breaks in caring for this person".""" in new WithApplication with Claiming {
      val request = FakeRequest().withFormUrlEncodedBody("answer" -> "no")

      val result = GOldBreaksInCare.submit(request)
      redirectLocation(result) should beSome("/education/your-course-details")
    }

    "complete upon indicating that there are no more breaks having provided zero break details" in new WithApplication with Claiming {
      val request = FakeRequest().withFormUrlEncodedBody("answer" -> "no")

      val result = GOldBreaksInCare.submit(request)
      redirectLocation(result) should beSome("/education/your-course-details")

      val claim = cache.get[Claim]("default"+extractCacheKey(result)).get

      val breaksInCare = claim.questionGroup[OldBreaksInCare].getOrElse(OldBreaksInCare())
      breaksInCare.breaks must beEmpty
    }

    "complete upon indicating that there are no more breaks having now provided one break" in new WithApplication with Claiming {
      val request1 = FakeRequest()
        .withFormUrlEncodedBody(
        "iterationID" -> "newID",
        "start.day" -> "1",
        "start.month" -> "1",
        "start.year" -> "2001",
        "whereYou.answer" -> "Holiday",
        "wherePerson.answer" -> "Holiday",
        "medicalDuringBreak" -> "no",
        "hasBreakEnded.answer" -> "no")

      val result1 = GOldBreak.submit(request1)
      redirectLocation(result1) should beSome("/breaks/breaks-in-care")

      val request2 = FakeRequest().withSession(CachedClaim.key -> extractCacheKey(result1)).withFormUrlEncodedBody("answer" -> "no")

      val result2 = GOldBreaksInCare.submit(request2)
      redirectLocation(result2) should beSome("/education/your-course-details")

      val claim = getClaimFromCache(result1)

      claim.questionGroup(OldBreaksInCare) should beLike { case Some(b: OldBreaksInCare) => b.breaks.size shouldEqual 1 }
    }

    "allow no more than 10 breaks" in new WithApplication with Claiming {
      val request1 = FakeRequest()
        .withFormUrlEncodedBody(
          "iterationID" -> 1.toString,
          "start.day" -> "1",
          "start.month" -> "1",
          "start.year" -> "2001",
          "whereYou.answer" -> "Holiday",
          "wherePerson.answer" -> "Holiday",
          "medicalDuringBreak" -> "no",
          "hasBreakEnded.answer" -> "no")
      val result1 = GOldBreak.submit(request1)
      redirectLocation(result1) should beSome("/breaks/breaks-in-care")

      for (i <- 2 to 10) {
        val request = FakeRequest().withSession(CachedClaim.key -> extractCacheKey(result1))
          .withFormUrlEncodedBody(
          "iterationID" -> i.toString,
          "start.day" -> "1",
          "start.month" -> "1",
          "start.year" -> "2001",
          "whereYou.answer" -> "Holiday",
          "wherePerson.answer" -> "Holiday",
          "medicalDuringBreak" -> "no",
          "hasBreakEnded.answer" -> "no")

        val result = GOldBreak.submit(request)
        redirectLocation(result) should beSome("/breaks/breaks-in-care")
      }

      getClaimFromCache(result1).questionGroup(OldBreaksInCare) should beLike {
        case Some(b: OldBreaksInCare) => b.breaks.size shouldEqual 10
      }

      val request2 = FakeRequest().withSession(CachedClaim.key -> extractCacheKey(result1))
        .withFormUrlEncodedBody(
        "iterationID" -> "999",
        "start.day" -> "1",
        "start.month" -> "1",
        "start.year" -> "2001",
        "whereYou.answer" -> "Holiday",
        "wherePerson.answer" -> "Holiday",
        "medicalDuringBreak" -> "no",
        "hasBreakEnded.answer" -> "no")

      val result2 = GOldBreak.submit(request2)
      redirectLocation(result2) should beSome("/breaks/breaks-in-care")

      getClaimFromCache(result2).questionGroup(OldBreaksInCare) should beLike {
        case Some(b: OldBreaksInCare) => b.breaks.size shouldEqual 10
      }
    }

    "have no breaks upon deleting a break" in new WithApplication with Claiming {
      val instanceID = "1"

      val request = FakeRequest()
        .withFormUrlEncodedBody(
        "iterationID" -> instanceID,
        "start.day" -> "1",
        "start.month" -> "1",
        "start.year" -> "2001",
        "whereYou.answer" -> "Holiday",
        "wherePerson.answer" -> "Holiday",
        "medicalDuringBreak" -> "no",
        "hasBreakEnded.answer" -> "no")

      val result = GOldBreak.submit(request)

      GOldBreaksInCare.delete(FakeRequest().withSession(CachedClaim.key -> extractCacheKey(result)).withFormUrlEncodedBody("deleteId"->instanceID))

      getClaimFromCache(result).questionGroup(OldBreaksInCare) should beLike {
        case Some(b: OldBreaksInCare) => b.breaks.size shouldEqual 0
      }
    }

    "issue an 'error' when deleting a non-existing break when there are no existing breaks" in new WithApplication with Claiming {
      val result = GOldBreaksInCare.delete(FakeRequest().withFormUrlEncodedBody("deleteId"->"nonExistingBreakID"))
      status(result) shouldEqual BAD_REQUEST
    }

    "issue an 'error' when deleting a non-existing break when there are existing breaks" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody(
        "iterationID" -> "1",
        "start.day" -> "1",
        "start.month" -> "1",
        "start.year" -> "2001",
        "whereYou.answer" -> "Holiday",
        "wherePerson.answer" -> "Holiday",
        "medicalDuringBreak" -> "no")

      val result1 = GOldBreak.submit(request)

      val result = GOldBreaksInCare.delete(FakeRequest().withSession(CachedClaim.key -> extractCacheKey(result1)).withFormUrlEncodedBody("deleteId"->"nonExistingBreakID"))
      status(result) shouldEqual BAD_REQUEST
    }

    "question reset afer deletion of break in care" in new WithApplication with Claiming {
      val instanceID = "1"
      val request1 = FakeRequest()
        .withFormUrlEncodedBody(
          "iterationID" -> instanceID,
          "start.day" -> "1",
          "start.month" -> "1",
          "start.year" -> "2001",
          "whereYou.answer" -> "Holiday",
          "wherePerson.answer" -> "Holiday",
          "medicalDuringBreak" -> "no",
          "hasBreakEnded.answer" -> "no")
      val result1 = GOldBreak.submit(request1)
      redirectLocation(result1) should beSome("/breaks/breaks-in-care")

      val request = FakeRequest().withSession(CachedClaim.key -> extractCacheKey(result1))
        .withFormUrlEncodedBody(
          "iterationID" -> "2",
          "start.day" -> "1",
          "start.month" -> "1",
          "start.year" -> "2001",
          "whereYou.answer" -> "Holiday",
          "wherePerson.answer" -> "Holiday",
          "medicalDuringBreak" -> "no",
          "hasBreakEnded.answer" -> "no")

      val result = GOldBreak.submit(request)
      redirectLocation(result) should beSome("/breaks/breaks-in-care")

      val request3 = FakeRequest().withSession(CachedClaim.key -> extractCacheKey(result1)).withFormUrlEncodedBody("answer" -> "no")

      val result3 = GOldBreaksInCare.submit(request3)
      redirectLocation(result3) should beSome("/education/your-course-details")

      val claim = getClaimFromCache(result1)
      claim.questionGroup(OldBreaksInCare) should beLike { case Some(b: OldBreaksInCare) => b.breaks.size shouldEqual 2 }
      claim.questionGroup(OldBreaksInCareSummary) should beLike { case Some(b: OldBreaksInCareSummary) => b.answer shouldEqual "no" }

      GOldBreaksInCare.delete(FakeRequest().withSession(CachedClaim.key -> extractCacheKey(result)).withFormUrlEncodedBody("deleteId"->instanceID))
      val claim1 = getClaimFromCache(result1)
      claim1.questionGroup(OldBreaksInCare) should beLike { case Some(b: OldBreaksInCare) => b.breaks.size shouldEqual 1 }
      claim1.questionGroup(OldBreaksInCareSummary) should beEmpty 
    }
  }
  section("unit", models.domain.CareYouProvide.id)
}
