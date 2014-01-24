package controllers.s6_education

import org.specs2.mutable.{Tags, Specification}
import models.DayMonthYear
import scala.Some

class G1YourCourseDetailsFormSpec extends Specification with Tags {

  val overHundredChars = "CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS"
  val nameOfSchoolCollegeOrUniversity = "MIT"
  val nameOfMainTeacherOrTutor = "Albert Einstein"
  val courseContactNumber = "02076541058"
  val title = "Law"

  "Your course details Form" should {
    "map data into case class" in {
      G1YourCourseDetails.form.bind(
        Map("courseTitle" -> title,
          "nameOfSchoolCollegeOrUniversity" -> nameOfSchoolCollegeOrUniversity,
          "nameOfMainTeacherOrTutor" -> nameOfMainTeacherOrTutor,
          "courseContactNumber" -> courseContactNumber,
          "startDate.day" -> "16", "startDate.month" -> "4", "startDate.year" -> "1992",
          "expectedEndDate.day" -> "30", "expectedEndDate.month" -> "9", "expectedEndDate.year" -> "1997"
        )
      ).fold(
        formWithErrors => "This mapping should not happen." must equalTo("Valid"),
        success = f => {
          f.title mustEqual(title)
          f.nameOfSchoolCollegeOrUniversity must equalTo(nameOfSchoolCollegeOrUniversity)
          f.nameOfMainTeacherOrTutor must equalTo(nameOfMainTeacherOrTutor)
          f.courseContactNumber must equalTo(Some(courseContactNumber))
          f.startDate must equalTo(DayMonthYear(Some(16), Some(4), Some(1992), None, None))
          f.expectedEndDate must equalTo(DayMonthYear(Some(30), Some(9), Some(1997), None, None))
        }
      )
    }

    "have a max length for course title" in {
      G1YourCourseDetails.form.bind(
        Map("courseTitle" -> overHundredChars)
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.maxLength"),
        f => "This mapping should not happen." must equalTo("Error")
      )
    }

    "have 5 mandatory fields" in {
      G1YourCourseDetails.form.bind(
        Map("courseContactNumber" -> courseContactNumber)
      ).fold(
          formWithErrors => {
            formWithErrors.errors.length must equalTo(5)
            formWithErrors.errors(0).message must equalTo("error.required")
            formWithErrors.errors(1).message must equalTo("error.required")
            formWithErrors.errors(2).message must equalTo("error.required")
            formWithErrors.errors(3).message must equalTo("error.required")
            formWithErrors.errors(4).message must equalTo("error.required")
          },
          f => "This mapping should not happen." must equalTo("Valid")
        )
    }

  } section("unit", models.domain.Education.id)
}