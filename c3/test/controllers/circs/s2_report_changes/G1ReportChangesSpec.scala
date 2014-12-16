package controllers.circs.s2_report_changes

import org.specs2.mutable.{Tags, Specification}
import play.api.test.{FakeApplication, FakeRequest, WithApplication}
import models.domain._
import models.view.CachedChangeOfCircs
import play.api.test.Helpers._
import controllers.circs.s2_report_changes
import app.ReportChange._
import controllers.mappings.Mappings._
import app.ReportChange.AdditionalInfo
import app.ReportChange.SelfEmployment
import models.domain.Claim
import app.ReportChange
import play.api.cache.Cache

class G1ReportChangesSpec extends Specification with Tags {

  val startDateDay = 1
  val startDateMonth = 12
  val startDateYear = 2012
  val selfEmployed = "self-employed"
  val selfEmployedTypeOfWork = "IT Consultant"

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

  val validCaringAndOngoingSelfEmploymentStartedFormInput = Seq(
    "stillCaring.answer" -> yes,
    "hasWorkStartedYet.answer" -> yes,
    "hasWorkStartedYet.dateWhenStarted.day" -> startDateDay.toString,
    "hasWorkStartedYet.dateWhenStarted.month" -> startDateMonth.toString,
    "hasWorkStartedYet.dateWhenStarted.year" -> startDateYear.toString,
    "hasWorkFinishedYet.answer" -> no,
    "typeOfWork.answer" -> selfEmployed,
    "typeOfWork.selfEmployedTypeOfWork" -> selfEmployedTypeOfWork,
    "typeOfWork.selfEmployedTotalIncome" -> dontknow
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

    "claim should only contain 'other changes' and 'employment change' must be discarded" in new WithApplication(app = FakeApplication(additionalConfiguration = Map("circs.employment.active" -> "true"))) with MockForm {

      val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey)
        .withFormUrlEncodedBody(validCaringAndOngoingSelfEmploymentStartedFormInput: _*)

      val result = G9EmploymentChange.submit(request)

      val request1 = FakeRequest().withSession(CachedChangeOfCircs.key -> extractCacheKey(result,CachedChangeOfCircs.key))
        .withFormUrlEncodedBody(validAdditionalDetailsReportChangesFormInput: _*)

      val result1 = s2_report_changes.G1ReportChanges.submit(request1)
      redirectLocation(result1) must beSome("/circumstances/report-changes/other-change")

      val claimFromCache = getClaimFromCache(result,CachedChangeOfCircs.key)
      val section: Section = claimFromCache.section(models.domain.CircumstancesReportChanges)
      section.questionGroup(CircumstancesEmploymentChange) must beNone
    }

   } section("unit", models.domain.CircumstancesReportChanges.id)
 }
