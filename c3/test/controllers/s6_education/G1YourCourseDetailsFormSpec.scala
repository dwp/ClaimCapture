package controllers.s6_education

import org.specs2.mutable.{Tags, Specification}
import models.DayMonthYear
import scala.Some

class G1YourCourseDetailsFormSpec extends Specification with Tags {

  val overHundredChars = "CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS"

  "Your Course Details Form" should {
    "map data into case class" in {
      G1YourCourseDetails.form.bind(
        Map("courseType" -> "University", "courseTitle" -> "Law",
          "startDate.day" -> "16", "startDate.month" -> "4", "startDate.year" -> "1992",
          "expectedEndDate.day" -> "30", "expectedEndDate.month" -> "9", "expectedEndDate.year" -> "1997",
          "finishedDate.day" -> "1", "finishedDate.month" -> "1", "finishedDate.year" -> "2000",
          "studentReferenceNumber" -> "ST-2828281"
        )
      ).fold(
        formWithErrors => "This mapping should not happen." must equalTo("Error"),
        f => {
          f.courseType must equalTo(Some("University"))
          f.startDate must equalTo(Some(DayMonthYear(Some(16), Some(4), Some(1992), None, None)))
          f.expectedEndDate must equalTo(Some(DayMonthYear(Some(30), Some(9), Some(1997), None, None)))
          f.finishedDate must equalTo(Some(DayMonthYear(Some(1), Some(1), Some(2000), None, None)))
          f.studentReferenceNumber must equalTo(Some("ST-2828281"))
        }
      )
    }

    "have a max length for course type" in {
      G1YourCourseDetails.form.bind(
        Map("courseType" -> overHundredChars)
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.maxLength"),
        f => "This mapping should not happen." must equalTo("Error")
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

    "have a max length for student reference number" in {
      G1YourCourseDetails.form.bind(
        Map("studentReferenceNumber" -> overHundredChars)
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.maxLength"),
        f => "This mapping should not happen." must equalTo("Error")
      )
    }

    "reject invalid start date" in {
      G1YourCourseDetails.form.bind(
        Map("startDate.day" -> "1")
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.invalid"),
        f => "This mapping should not happen." must equalTo("Error")
      )
    }

    "reject invalid expected end date" in {
      G1YourCourseDetails.form.bind(
        Map("expectedEndDate.day" -> "1")
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.invalid"),
        f => "This mapping should not happen." must equalTo("Error")
      )
    }

    "reject invalid finished date" in {
      G1YourCourseDetails.form.bind(
        Map("finishedDate.day" -> "1")
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.invalid"),
        f => "This mapping should not happen." must equalTo("Error")
      )
    }

    "no mandatory fields" in {
      G1YourCourseDetails.form.bind(
        Map("" -> "")
      ).fold(
        formWithErrors => "This mapping should not happen." must equalTo("Error"),
        f => "Ok" must equalTo("Ok")
      )
    }
  } section "unit"
}
