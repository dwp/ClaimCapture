package controllers.s_education

import utils.WithApplication
import controllers.mappings.Mappings
import org.specs2.mutable._
import models.DayMonthYear

class GYourCourseDetailsFormSpec extends Specification {

  val overHundredChars = "CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS"
  val nameOfSchoolCollegeOrUniversity = "MIT"
  val nameOfMainTeacherOrTutor = "Albert Einstein"
  val courseContactNumber = "02076541058"
  val title = "Law"
  val dateDay = 5
  val dateMonth = 12
  val dateYear = 1990

  section("unit", models.domain.Education.id)
  "Your course details Form" should {
    "map data into case class" in new WithApplication {
      GYourCourseDetails.form.bind(
        Map(
          "beenInEducationSinceClaimDate" -> "yes",
          "courseTitle" -> title,
          "nameOfSchoolCollegeOrUniversity" -> nameOfSchoolCollegeOrUniversity,
          "nameOfMainTeacherOrTutor" -> nameOfMainTeacherOrTutor,
          "courseContactNumber" -> courseContactNumber,
          "startDate.day" -> "16", "startDate.month" -> "4", "startDate.year" -> "1992",
          "expectedEndDate.day" -> "30", "expectedEndDate.month" -> "9", "expectedEndDate.year" -> "1997"
        )
      ).fold(
        formWithErrors => "This mapping should not happen." must equalTo("Valid"),
        success = f => {
          f.beenInEducationSinceClaimDate mustEqual("yes")
          f.title mustEqual(Some(title))
          f.nameOfSchoolCollegeOrUniversity must equalTo(Some(nameOfSchoolCollegeOrUniversity))
          f.nameOfMainTeacherOrTutor must equalTo(Some(nameOfMainTeacherOrTutor))
          f.courseContactNumber must equalTo(Some(courseContactNumber))
          f.startDate must equalTo(Some(DayMonthYear(Some(16), Some(4), Some(1992), None, None)))
          f.expectedEndDate must equalTo(Some(DayMonthYear(Some(30), Some(9), Some(1997), None, None)))
        }
      )
    }

    "have 1 mandatory field on initial load" in new WithApplication {
      GYourCourseDetails.form.bind(
        Map("courseTitle" -> title,
          "nameOfSchoolCollegeOrUniversity" -> nameOfSchoolCollegeOrUniversity,
          "nameOfMainTeacherOrTutor" -> nameOfMainTeacherOrTutor,
          "courseContactNumber" -> courseContactNumber,
          "startDate.day" -> "16", "startDate.month" -> "4", "startDate.year" -> "1992",
          "expectedEndDate.day" -> "30", "expectedEndDate.month" -> "9", "expectedEndDate.year" -> "1997")
      ).fold(
          formWithErrors => {
            formWithErrors.errors.length must equalTo(1)
            formWithErrors.errors(0).message must equalTo(Mappings.errorRequired)
          },
          f => "This mapping should not happen." must equalTo("Valid")
        )
    }

    "have 5 mandatory fields if beenInEducationSinceClaimDate is yes" in new WithApplication {
      GYourCourseDetails.form.bind(
        Map("beenInEducationSinceClaimDate" -> "yes")
      ).fold(
          formWithErrors => {
            formWithErrors.errors.length must equalTo(5)
            formWithErrors.errors(0).message must equalTo("courseTitle.required")
            formWithErrors.errors(1).message must equalTo("nameOfSchoolCollegeOrUniversity.required")
            formWithErrors.errors(2).message must equalTo("nameOfMainTeacherOrTutor.required")
            formWithErrors.errors(3).message must equalTo("startDate.required")
            formWithErrors.errors(4).message must equalTo("expectedEndDate.required")
          },
          f => "This mapping should not happen." must equalTo("Valid")
        )
    }

    "reject too many characters in text fields" in new WithApplication {
      GYourCourseDetails.form.bind(
        Map(
          "beenInEducationSinceClaimDate" -> "yes",
          "courseTitle" -> overHundredChars,
          "nameOfSchoolCollegeOrUniversity" -> overHundredChars,
          "nameOfMainTeacherOrTutor" -> overHundredChars,
          "courseContactNumber" -> courseContactNumber,
          "startDate.day" -> "16", "startDate.month" -> "4", "startDate.year" -> "1992",
          "expectedEndDate.day" -> "30", "expectedEndDate.month" -> "9", "expectedEndDate.year" -> "1997")
        ).fold(
          formWithErrors => {
            formWithErrors.errors.length must equalTo(3)
            formWithErrors.errors(0).message must equalTo(Mappings.maxLengthError)
            formWithErrors.errors(1).message must equalTo(Mappings.maxLengthError)
            formWithErrors.errors(2).message must equalTo(Mappings.maxLengthError)
          },
          f => "This mapping should not happen." must equalTo("Valid"))
    }

    "reject invalid dates" in new WithApplication {
      GYourCourseDetails.form.bind(
        Map(
          "beenInEducationSinceClaimDate" -> "yes",
          "courseTitle" -> title,
          "nameOfSchoolCollegeOrUniversity" -> nameOfSchoolCollegeOrUniversity,
          "nameOfMainTeacherOrTutor" -> nameOfMainTeacherOrTutor,
          "courseContactNumber" -> courseContactNumber,
          "startDate.day" -> dateDay.toString, "startDate.month" -> dateMonth.toString, "startDate.year" -> "12345",
          "expectedEndDate.day" -> dateDay.toString, "expectedEndDate.month" -> dateMonth.toString, "expectedEndDate.year" -> "20146"
        )).fold(
          formWithErrors => {
            formWithErrors.errors.length must equalTo(2)
            formWithErrors.errors(0).message must equalTo(Mappings.errorInvalid)
            formWithErrors.errors(0).message must equalTo(Mappings.errorInvalid)
          },
          f => "This mapping should not happen." must equalTo("Valid"))
    }
  }
  section("unit", models.domain.Education.id)
}
