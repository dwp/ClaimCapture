package controllers.circs.s_permanently_stopped_caring

import org.specs2.mutable.{Tags, Specification}
import models.DayMonthYear


class G1PermanentlyStoppedCaringFormSpec extends Specification with Tags {

  val moreAboutChanges = "This is more about the change"
  val stoppedCaringDateDay = 23
  val stoppedCaringDateMonth = 12
  val stoppedCaringDateYear = 2013

  "Report a change in your circumstances - Permanently stopped caring Form" should {
    "map data into case class" in {
      G1PermanentlyStoppedCaring.form.bind(
        Map("stoppedCaringDate.day" -> stoppedCaringDateDay.toString,
          "stoppedCaringDate.month" -> stoppedCaringDateMonth.toString,
          "stoppedCaringDate.year" -> stoppedCaringDateYear.toString,
          "moreAboutChanges"->moreAboutChanges)
      ).fold(
        formWithErrors => "This mapping should not happen." must equalTo("Error"),
        f => {
          f.moreAboutChanges must equalTo(Some(moreAboutChanges))
          f.stoppedCaringDate must equalTo(DayMonthYear(Some(stoppedCaringDateDay), Some(stoppedCaringDateMonth), Some(stoppedCaringDateYear), None, None))
        }
      )
    }

    "have 1 mandatory field" in {
      G1PermanentlyStoppedCaring.form.bind(
        Map("moreAboutChanges" -> moreAboutChanges)
      ).fold(
        formWithErrors => {
          formWithErrors.errors(0).message must equalTo("error.required")
        },
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "reject special characters in text field" in {
      G1PermanentlyStoppedCaring.form.bind(
        Map("moreAboutChanges" -> "<>",
          "stoppedCaringDate.day" -> stoppedCaringDateDay.toString,
          "stoppedCaringDate.month" -> stoppedCaringDateMonth.toString,
          "stoppedCaringDate.year" -> stoppedCaringDateYear.toString)
      ).fold(
        formWithErrors => {
          formWithErrors.errors.length must equalTo(1)
          formWithErrors.errors(0).message must equalTo("error.restricted.characters")
        },
        f => "This mapping should not happen." must equalTo("Valid"))
    }

    "reject invalid date" in {
      G1PermanentlyStoppedCaring.form.bind(
        Map("moreAboutChanges" -> moreAboutChanges,
          "stoppedCaringDate.day" -> stoppedCaringDateDay.toString,
          "stoppedCaringDate.month" -> stoppedCaringDateMonth.toString,
          "stoppedCaringDate.year" -> "12345")).fold(
          formWithErrors => {
            formWithErrors.errors.length must equalTo(1)
            formWithErrors.errors.head.message must equalTo("error.invalid")
          },
          f => "This mapping should not happen." must equalTo("Valid"))
    }
  } section("unit", models.domain.CircumstancesAdditionalInfo.id)
}
