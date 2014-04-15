package controllers.circs.s2_report_changes

import org.specs2.mutable.{Tags, Specification}
import play.api.test.{FakeRequest, WithApplication}
import models.domain.MockForm
import models.view.CachedChangeOfCircs
import play.api.test.Helpers._
import controllers.circs.s2_report_changes
import app.ReportChange._

class G1ReportChangesSpec extends Specification with Tags {

  val validStoppedCaringReportChangesFormInput = Seq(
    "reportChanges" -> StoppedCaring.name
  )

  val validAddressChangeFormInput = Seq(
    "reportChanges" -> AddressChange.name
  )

  val validSelfEmploymentReportChangesFormInput = Seq(
    "reportChanges" -> SelfEmployment.name
  )

  val validPaymentChangeFormInput = Seq(
    "reportChanges" -> PaymentChange.name
  )

  val validAdditionalDetailsReportChangesFormInput = Seq(
    "reportChanges" -> AdditionalInfo.name
  )

  val validBreakFromCaringFormInput = Seq(
    "reportChanges" -> BreakFromCaring.name
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
      redirectLocation(result) must beSome("/circumstances/report-changes/other-change")
    }

    "redirect to the next page after a valid self employment submission" in new WithApplication with MockForm {
      val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey)
        .withFormUrlEncodedBody(validSelfEmploymentReportChangesFormInput: _*)

      val result = s2_report_changes.G1ReportChanges.submit(request)
      redirectLocation(result) must beSome("/circumstances/report-changes/self-employment")
    }

    "redirect to the next page after a valid stopped caring submission" in new WithApplication with MockForm {
      val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey)
        .withFormUrlEncodedBody(validStoppedCaringReportChangesFormInput: _*)

      val result = s2_report_changes.G1ReportChanges.submit(request)
      redirectLocation(result) must beSome("/circumstances/report-changes/stopped-caring")
    }

    "redirect to the next page after a valid address change submission" in new WithApplication with MockForm {
      val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey)
        .withFormUrlEncodedBody(validAddressChangeFormInput: _*)

      val result = s2_report_changes.G1ReportChanges.submit(request)
      redirectLocation(result) must beSome("/circumstances/report-changes/address-change")
    }

    "redirect to the next page after a valid payment change submission" in new WithApplication with MockForm {
      val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey)
        .withFormUrlEncodedBody(validPaymentChangeFormInput: _*)

      val result = s2_report_changes.G1ReportChanges.submit(request)
      redirectLocation(result) must beSome("/circumstances/report-changes/payment-change")
    }

    "redirect to the next page after a valid break from caring submission" in new WithApplication with MockForm {
      val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey)
        .withFormUrlEncodedBody(validBreakFromCaringFormInput: _*)

      val result = s2_report_changes.G1ReportChanges.submit(request)
      redirectLocation(result) must beSome("/circumstances/report-changes/breaks-in-care")
    }

   } section("unit", models.domain.CircumstancesReportChanges.id)
 }
