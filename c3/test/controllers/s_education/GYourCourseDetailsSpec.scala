package controllers.s_education

import models.domain._
import models.{DayMonthYear, domain}
import org.specs2.mutable._
import play.api.test.Helpers._
import play.api.test.FakeRequest
import utils.WithApplication

class GYourCourseDetailsSpec extends Specification {

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
      val request = FakeRequest()

      val result = GYourCourseDetails.present(request)
      status(result) mustEqual OK
    }

    "add submitted data to the cached claim" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody(formInput: _*)

      val result = controllers.s_education.GYourCourseDetails.submit(request)
      val claim = getClaimFromCache(result)
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
      val request = FakeRequest()
        .withFormUrlEncodedBody("finishedDate.day" -> "1")

      val result = controllers.s_education.GYourCourseDetails.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "redirect to the next page after a valid submission" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody(formInput: _*)

      val result = controllers.s_education.GYourCourseDetails.submit(request)
      status(result) mustEqual SEE_OTHER
    }

  }
  section("unit", models.domain.Education.id)
}
