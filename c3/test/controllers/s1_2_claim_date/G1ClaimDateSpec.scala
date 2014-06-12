package controllers.s1_2_claim_date

import org.specs2.mutable.{Tags, Specification}
import play.api.test.{FakeRequest, WithApplication}
import models.domain._
import models.view.CachedClaim
import play.api.test.Helpers._
import play.api.cache.Cache
import models.{DayMonthYear, domain}
import models.domain.Claim
import scala.Some

class G1ClaimDateSpec extends Specification with Tags {

  val claimDateDay = 1
  val claimDateMonth = 1
  val claimDateYear = 2014

  val claimDateInput = Seq(
    "dateOfClaim.day" -> claimDateDay.toString,
    "dateOfClaim.month" -> claimDateMonth.toString,
    "dateOfClaim.year" -> claimDateYear.toString)

  "Your claim date" should {

    "present 'Your claim date' " in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)

      val result = G1ClaimDate.present(request)
      status(result) mustEqual OK
    }

    "add submitted form to the cached claim" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)
        .withFormUrlEncodedBody(claimDateInput: _*)

      val result = G1ClaimDate.submit(request)
      val claim = Cache.getAs[Claim](claimKey).get
      val section: Section = claim.section(domain.YourClaimDate)

      section.questionGroup(ClaimDate) must beLike {
        case Some(f: ClaimDate) =>
          f.dateOfClaim must equalTo(DayMonthYear(Some(claimDateDay), Some(claimDateMonth), Some(claimDateYear)))
      }
    }

    "return a bad request after an invalid submission" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)
        .withFormUrlEncodedBody("foo" -> "bar")

      val result = G1ClaimDate.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "redirect to the next page after a valid submission" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)
        .withFormUrlEncodedBody(claimDateInput: _*)

      val result = G1ClaimDate.submit(request)
      status(result) mustEqual SEE_OTHER
    }

  }  section("unit", models.domain.YourClaimDate.id)
}
