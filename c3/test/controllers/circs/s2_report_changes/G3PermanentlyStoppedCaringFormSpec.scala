package controllers.circs.s2_report_changes

import utils.WithApplication
import controllers.mappings.Mappings
import org.specs2.mutable._
import models.DayMonthYear

class G3PermanentlyStoppedCaringFormSpec extends Specification {

  val moreAboutChanges = "This is more about the change"
  val stoppedCaringDateDay = 23
  val stoppedCaringDateMonth = 12
  val stoppedCaringDateYear = 2013

  "Report a change in your circumstances - Permanently stopped caring Form" should {
    "map data into case class" in new WithApplication {
      G3PermanentlyStoppedCaring.form.bind(
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

    "have 1 mandatory field" in new WithApplication {
      G3PermanentlyStoppedCaring.form.bind(
        Map("moreAboutChanges" -> moreAboutChanges)
      ).fold(
        formWithErrors => {
          formWithErrors.errors(0).message must equalTo(Mappings.errorRequired)
        },
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "reject special characters in text field" in new WithApplication {
      G3PermanentlyStoppedCaring.form.bind(
        Map("moreAboutChanges" -> "<>",
          "stoppedCaringDate.day" -> stoppedCaringDateDay.toString,
          "stoppedCaringDate.month" -> stoppedCaringDateMonth.toString,
          "stoppedCaringDate.year" -> stoppedCaringDateYear.toString)
      ).fold(
        formWithErrors => {
          formWithErrors.errors.length must equalTo(1)
          formWithErrors.errors(0).message must equalTo(Mappings.errorRestrictedCharacters)
        },
        f => "This mapping should not happen." must equalTo("Valid"))
    }

    "reject invalid date" in new WithApplication {
      G3PermanentlyStoppedCaring.form.bind(
        Map("moreAboutChanges" -> moreAboutChanges,
          "stoppedCaringDate.day" -> stoppedCaringDateDay.toString,
          "stoppedCaringDate.month" -> stoppedCaringDateMonth.toString,
          "stoppedCaringDate.year" -> "12345")).fold(
          formWithErrors => {
            formWithErrors.errors.length must equalTo(1)
            formWithErrors.errors.head.message must equalTo(Mappings.errorInvalid)
          },
          f => "This mapping should not happen." must equalTo("Valid"))
    }
  }
  section("unit", models.domain.CircumstancesStoppedCaring.id)
}
