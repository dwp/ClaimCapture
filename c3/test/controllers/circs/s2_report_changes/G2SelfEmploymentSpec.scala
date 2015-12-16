package controllers.circs.s2_report_changes

import org.specs2.mutable._
import play.api.test.FakeRequest
import models.domain.MockForm
import models.view.CachedChangeOfCircs
import play.api.test.Helpers._
import controllers.circs.s2_report_changes
import utils.WithApplication

class G2SelfEmploymentSpec extends Specification {
  val yes = "yes"
  val no = "no"
  val stillCaringDateDay = 10
  val stillCaringDateMonth = 11
  val stillCaringDateYear = 2012
  val whenThisSelfEmploymentStartedDateDay = 11
  val whenThisSelfEmploymentStartedDateMonth = 12
  val whenThisSelfEmploymentStartedDateYear = 2013
  val typeOfBusiness = "Plumber"
  val moreAboutChanges = "This is more about the change"
  val invalidYear = 99999

  val validStillCaringFormInput = Seq(
    "stillCaring.answer" -> yes,
    "whenThisSelfEmploymentStarted.day" -> whenThisSelfEmploymentStartedDateDay.toString,
    "whenThisSelfEmploymentStarted.month" -> whenThisSelfEmploymentStartedDateMonth.toString,
    "whenThisSelfEmploymentStarted.year" -> whenThisSelfEmploymentStartedDateYear.toString,
    "typeOfBusiness" -> typeOfBusiness,
    "totalOverWeeklyIncomeThreshold" -> yes,
    "moreAboutChanges" -> moreAboutChanges
  )

  val validNotCaringFormInput = Seq(
    "stillCaring.answer" -> no,
    "stillCaring.date.day" -> stillCaringDateDay.toString,
    "stillCaring.date.month" -> stillCaringDateMonth.toString,
    "stillCaring.date.year" -> stillCaringDateYear.toString,
    "whenThisSelfEmploymentStarted.day" -> whenThisSelfEmploymentStartedDateDay.toString,
    "whenThisSelfEmploymentStarted.month" -> whenThisSelfEmploymentStartedDateMonth.toString,
    "whenThisSelfEmploymentStarted.year" -> whenThisSelfEmploymentStartedDateYear.toString,
    "typeOfBusiness" -> typeOfBusiness,
    "totalOverWeeklyIncomeThreshold" -> yes,
    "moreAboutChanges" -> moreAboutChanges
  )

  section("unit", models.domain.CircumstancesSelfEmployment.id)
  "Circumstances - Self Employment - Controller" should {
    "present 'CoC Self Employment' " in new WithApplication with MockForm {
      val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey)

      val result = G2SelfEmployment.present(request)
      status(result) mustEqual OK
    }

    "redirect to the next page after a valid still caring submission" in new WithApplication with MockForm {
      val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey)
        .withFormUrlEncodedBody(validStillCaringFormInput: _*)

      val result = s2_report_changes.G2SelfEmployment.submit(request)
      status(result) mustEqual SEE_OTHER
    }

    "redirect to the next page after a valid still caring submission" in new WithApplication with MockForm {
      val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey)
        .withFormUrlEncodedBody(validNotCaringFormInput: _*)

      val result = s2_report_changes.G2SelfEmployment.submit(request)
      status(result) mustEqual SEE_OTHER
    }
  }
  section("unit", models.domain.CircumstancesSelfEmployment.id)
}
