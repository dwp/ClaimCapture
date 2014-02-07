package controllers.circs.s2_report_changes

import org.specs2.mutable.{Tags, Specification}
import play.api.test.{FakeRequest, WithApplication}
import models.domain.MockForm
import models.view.CachedChangeOfCircs
import play.api.test.Helpers._
import controllers.circs.s2_report_changes
import app.ReportChange._

class G1ReportChangesSpec extends Specification with Tags {
  val validAdditionalDetailsReportChangesFormInput = Seq(
    "reportChanges" -> AdditionalInfo.name
  )

  val validSelfEmploymentReportChangesFormInput = Seq(
    "reportChanges" -> AdditionalInfo.name
  )

  val validStoppedCaringReportChangesFormInput = Seq(
    "reportChanges" -> AdditionalInfo.name
  )

  "Report a change in your circumstances - Change in circumstances - Controller" should {
    "present 'CoC Report Changes' " in new WithApplication with MockForm {
      val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey)

      val result = G1ReportChanges.present(request)
      status(result) mustEqual OK
    }

    "redirect to the next page after a valid additional info submission" in new WithApplication with MockForm {
      val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey)
        .withFormUrlEncodedBody(validAdditionalDetailsReportChangesFormInput: _*)

      val result = s2_report_changes.G1ReportChanges.submit(request)
      status(result) mustEqual SEE_OTHER
    }

    "redirect to the next page after a valid self employment submission" in new WithApplication with MockForm {
      val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey)
        .withFormUrlEncodedBody(validSelfEmploymentReportChangesFormInput: _*)

      val result = s2_report_changes.G1ReportChanges.submit(request)
      status(result) mustEqual SEE_OTHER
    }

    "redirect to the next page after a valid stopped caring submission" in new WithApplication with MockForm {
      val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey)
        .withFormUrlEncodedBody(validStoppedCaringReportChangesFormInput: _*)

      val result = s2_report_changes.G1ReportChanges.submit(request)
      status(result) mustEqual SEE_OTHER
    }
   } section("unit", models.domain.CircumstancesReportChanges.id)
 }
