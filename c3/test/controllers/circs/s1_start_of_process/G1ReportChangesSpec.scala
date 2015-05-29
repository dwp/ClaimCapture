package controllers.circs.s1_start_of_process

import app.ReportChange.{AdditionalInfo, SelfEmployment, _}
import controllers.circs.{s1_start_of_process, s2_report_changes}
import controllers.mappings.Mappings._
import models.domain._
import models.view.CachedChangeOfCircs
import org.specs2.mutable.{Specification, Tags}
import play.api.test.Helpers._
import play.api.test.{FakeApplication, FakeRequest, WithApplication}
import s2_report_changes._

class G1ReportChangesSpec extends Specification with Tags {

  val startDateDay = 1
  val startDateMonth = 12
  val startDateYear = 2012
  val selfEmployed = "self-employed"
  val selfEmployedTypeOfWork = "IT Consultant"



  val validAdditionalDetailsReportChangesFormInput = Seq(
    "reportChanges" -> AdditionalInfo.name
  )




  "Report a change in your circumstances - Change in circumstances - Controller" should {
    "present 'CoC Report Changes' " in new WithApplication with MockForm {
      val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey)

      val result = G1ReportChanges.present(request)
      status(result) mustEqual OK
    }

    "redirect to next page"in new WithApplication with MockForm {
      val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey)
        .withFormUrlEncodedBody(validAdditionalDetailsReportChangesFormInput: _*)

      val result = s1_start_of_process.G1ReportChanges.submit(request)
      redirectLocation(result) must beSome("/circumstances/identification/about-you")
    }

   } section("unit", models.domain.CircumstancesReportChanges.id)
 }
