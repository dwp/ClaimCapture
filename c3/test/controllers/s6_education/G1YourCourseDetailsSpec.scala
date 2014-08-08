package controllers.s6_education

import org.specs2.mutable.{Tags, Specification}
import play.api.test.{FakeRequest, WithApplication}
import models.domain._
import play.api.test.Helpers._
import play.api.cache.Cache
import models.{DayMonthYear, domain}
import models.domain.Claim
import scala.Some
import models.view.CachedClaim

class G1YourCourseDetailsSpec extends Specification with Tags {

  val nameOfSchoolCollegeOrUniversity = "MIT"
  val nameOfMainTeacherOrTutor = "Albert Einstein"
  val courseContactNumber = "02076541058"
  val title = "Law"

  val formInput = Seq(
    "beenInEducationSinceClaimDate" -> "yes",
    "courseTitle" -> title,
    "nameOfSchoolCollegeOrUniversity" -> nameOfSchoolCollegeOrUniversity,
    "nameOfMainTeacherOrTutor" -> nameOfMainTeacherOrTutor,
    "courseContactNumber" -> courseContactNumber,
    "startDate.day" -> "16",
    "startDate.month" -> "4",
    "startDate.year" -> "1992",
    "expectedEndDate.day" -> "30",
    "expectedEndDate.month" -> "9",
    "expectedEndDate.year" -> "1997"
    )

  "Your course details - Controller" should {
    "present 'Your course details'" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)

      val result = G1YourCourseDetails.present(request)
      status(result) mustEqual OK
    }

    "add submitted data to the cached claim" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)
        .withFormUrlEncodedBody(formInput: _*)

      val result = controllers.s6_education.G1YourCourseDetails.submit(request)
      val claim = Cache.getAs[Claim](claimKey).get
      val section: Section = claim.section(domain.Education)

      section.questionGroup(YourCourseDetails) must beLike {
        case Some(f: YourCourseDetails) => {
          f.beenInEducationSinceClaimDate mustEqual("yes")
          f.title mustEqual Some(title)
          f.nameOfSchoolCollegeOrUniversity mustEqual Some(nameOfSchoolCollegeOrUniversity)
          f.nameOfMainTeacherOrTutor mustEqual Some(nameOfMainTeacherOrTutor)
          f.courseContactNumber mustEqual Some(courseContactNumber)
          f.startDate must equalTo(Some(DayMonthYear(Some(16), Some(4), Some(1992), None, None)))
          f.expectedEndDate must equalTo(Some(DayMonthYear(Some(30), Some(9), Some(1997), None, None)))
        }
      }
    }

    "return bad request on invalid submission" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)
        .withFormUrlEncodedBody("finishedDate.day" -> "1")

      val result = controllers.s6_education.G1YourCourseDetails.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "redirect to the next page after a valid submission" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)
        .withFormUrlEncodedBody(formInput: _*)

      val result = controllers.s6_education.G1YourCourseDetails.submit(request)
      status(result) mustEqual SEE_OTHER
    }

  } section("unit", models.domain.Education.id)
}