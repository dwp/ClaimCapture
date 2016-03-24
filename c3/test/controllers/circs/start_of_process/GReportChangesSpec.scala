package controllers.circs.start_of_process

import app.ReportChange.AdditionalInfo
import controllers.circs.report_changes.GOtherChangeInfo
import models.domain._
import models.view.CachedChangeOfCircs
import org.specs2.mutable.Specification
import play.api.test.Helpers._
import utils.WithApplication
import play.api.test.FakeRequest
import utils.pageobjects.circumstances.start_of_process.GGoToCircsPage

class GReportChangesSpec extends Specification {
  val startDateDay = 1
  val startDateMonth = 12
  val startDateYear = 2012
  val selfEmployed = "self-employed"
  val selfEmployedTypeOfWork = "IT Consultant"
  val validAdditionalDetailsReportChangesFormInput = Seq(
    "reportChanges" -> AdditionalInfo.name
  )
  val nextPageUrl = "/circumstances/report-changes/other-change"

  section("unit", models.domain.CircumstancesReportChanges.id)
  "Report a change in your circumstances - Change in circumstances - Controller" should {
    "present 'CoC Report Changes' " in new WithApplication with MockForm {
      val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey)

      val result = GReportChangeReason.present(request)
      status(result) mustEqual OK
    }

    "redirect to next page"in new WithApplication with MockForm {
      val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey)
        .withFormUrlEncodedBody(validAdditionalDetailsReportChangesFormInput: _*)

      val result = GReportChangeReason.submit(request)
      redirectLocation(result) must beSome(nextPageUrl)
    }
  }
  section("unit", models.domain.CircumstancesReportChanges.id)
}
