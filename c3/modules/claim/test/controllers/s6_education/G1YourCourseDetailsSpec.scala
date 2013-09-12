package controllers.s6_education

import org.specs2.mutable.{Tags, Specification}
import play.api.test.{FakeRequest, WithApplication}
import models.domain._
import play.api.test.Helpers._
import play.api.cache.Cache
import models.{DayMonthYear, domain}
import models.domain.Claim
import scala.Some
import models.view.CachedDigitalForm

class G1YourCourseDetailsSpec extends Specification with Tags {

  val formInput = Seq("courseType" -> "University", "courseTitle" -> "Law",
    "startDate.day" -> "16", "startDate.month" -> "4", "startDate.year" -> "1992",
    "expectedEndDate.day" -> "30", "expectedEndDate.month" -> "9", "expectedEndDate.year" -> "1997",
    "finishedDate.day" -> "1", "finishedDate.month" -> "1", "finishedDate.year" -> "2000",
    "studentReferenceNumber" -> "ST-2828281")

  "Your course details - Controller" should {
    "present 'Your course details'" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedDigitalForm.claimKey -> claimKey)

      val result = G1YourCourseDetails.present(request)
      status(result) mustEqual OK
    }

    "add submitted data to the cached claim" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedDigitalForm.claimKey -> claimKey)
        .withFormUrlEncodedBody(formInput: _*)

      val result = controllers.s6_education.G1YourCourseDetails.submit(request)
      val claim = Cache.getAs[Claim](claimKey).get
      val section: Section = claim.section(domain.Education)

      section.questionGroup(YourCourseDetails) must beLike {
        case Some(f: YourCourseDetails) => {
          f.courseType must equalTo(Some("University"))
          f.startDate must equalTo(Some(DayMonthYear(Some(16), Some(4), Some(1992), None, None)))
          f.expectedEndDate must equalTo(Some(DayMonthYear(Some(30), Some(9), Some(1997), None, None)))
          f.finishedDate must equalTo(Some(DayMonthYear(Some(1), Some(1), Some(2000), None, None)))
          f.studentReferenceNumber must equalTo(Some("ST-2828281"))
        }
      }
    }

    "return bad request on invalid submission" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedDigitalForm.claimKey -> claimKey)
        .withFormUrlEncodedBody("finishedDate.day" -> "1")

      val result = controllers.s6_education.G1YourCourseDetails.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "redirect to the next page after a valid submission" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedDigitalForm.claimKey -> claimKey)
        .withFormUrlEncodedBody(formInput: _*)

      val result = controllers.s6_education.G1YourCourseDetails.submit(request)
      status(result) mustEqual SEE_OTHER
    }

  } section("unit", models.domain.Education.id)
}