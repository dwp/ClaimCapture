package controllers.s_claim_date

import models.domain._
import models.{DayMonthYear, domain}
import org.specs2.mutable._
import play.api.test.Helpers._
import play.api.test.FakeRequest
import models.yesNo.YesNoWithDate
import controllers.mappings.Mappings._
import utils.WithApplication

class GClaimDateSpec extends Specification {
  val claimDateDay = 1
  val claimDateMonth = 1
  val claimDateYear = 2014

  val spent35HoursCaringBeforeClaimYesWithNoDate = YesNoWithDate(yes, None)
  val spent35HoursCaringBeforeClaimNo = YesNoWithDate(no, None)

  val claimDateInputSpent35HoursBeforeClaimNo = Seq(
    "dateOfClaim.day" -> claimDateDay.toString,
    "dateOfClaim.month" -> claimDateMonth.toString,
    "dateOfClaim.year" -> claimDateYear.toString,
    "beforeClaimCaring.answer" -> spent35HoursCaringBeforeClaimNo.answer)

  def claimDateInput() = {
    Seq(
      "dateOfClaim.day" -> claimDateDay.toString,
      "dateOfClaim.month" -> claimDateMonth.toString,
      "dateOfClaim.year" -> claimDateYear.toString,
      "beforeClaimCaring.answer" -> YesNoWithDate(yes, Some(DayMonthYear(Some(claimDateDay), Some(claimDateMonth), Some(claimDateYear), None, None))).answer,
      "beforeClaimCaring.date.day" -> claimDateDay.toString,
      "beforeClaimCaring.date.month" -> claimDateMonth.toString,
      "beforeClaimCaring.date.year" -> claimDateYear.toString)
  }

  section("unit", models.domain.YourClaimDate.id)
  "Your claim date" should {
    "present 'Your claim date' " in new WithApplication with Claiming {
      val request = FakeRequest()

      val result = GClaimDate.present(request)
      status(result) mustEqual OK
    }

    "add submitted form to the cached claim" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody(claimDateInput: _*)

      val result = GClaimDate.submit(request)
      val claim = getClaimFromCache(result)
      val section: Section = claim.section(domain.YourClaimDate)

      section.questionGroup(ClaimDate) must beLike {
        case Some(f: ClaimDate) =>
          f.dateOfClaim must equalTo(DayMonthYear(Some(claimDateDay), Some(claimDateMonth), Some(claimDateYear)))
          f.spent35HoursCaringBeforeClaim must equalTo(YesNoWithDate(yes, Some(DayMonthYear(Some(claimDateDay), Some(claimDateMonth), Some(claimDateYear), None, None))))
      }
    }

    "add submitted form to the cached claim when spent 35 hours before claim is no" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody(claimDateInputSpent35HoursBeforeClaimNo: _*)

      val result = GClaimDate.submit(request)
      val claim = getClaimFromCache(result)
      val section: Section = claim.section(domain.YourClaimDate)

      section.questionGroup(ClaimDate) must beLike {
        case Some(f: ClaimDate) =>
          f.dateOfClaim must equalTo(DayMonthYear(Some(claimDateDay), Some(claimDateMonth), Some(claimDateYear)))
          f.spent35HoursCaringBeforeClaim must equalTo(spent35HoursCaringBeforeClaimNo)
      }
    }

    "return a bad request after an invalid submission" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody("foo" -> "bar")

      val result = GClaimDate.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "redirect to the next page after a valid submission" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody(claimDateInput: _*)

      val result = GClaimDate.submit(request)
      status(result) mustEqual SEE_OTHER
    }
  }
  section("unit", models.domain.YourClaimDate.id)
}
