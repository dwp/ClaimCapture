package models

import org.joda.time.DateTime
import org.joda.time.format.{DateTimeFormatter, DateTimeFormat}
import play.api.data.Form
import org.specs2.mutable._
import controllers.mappings.Mappings._
import utils.WithApplication

class DayMonthYearFormSpec extends Specification {
  section("unit")
  "DayMonthYear Validation" should {
    "accept valid input" in new WithApplication {
      Form("date" -> dayMonthYear.verifying(validDate)).bind(Map("date.day" -> "1", "date.month" -> "2", "date.year" -> "1980")).fold(
        formWithErrors => "The mapping should not fail." must equalTo("Error"),
        dateMonthYear => dateMonthYear must equalTo(DayMonthYear(Some(1), Some(2), Some(1980), None, None))
      )
    }

    "not accept invalid input" in new WithApplication {
      Form("date" -> dayMonthYear.verifying(validDate)).bind(Map("date.day" -> "50", "date.month" -> "90", "date.year" -> "1980")).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo(errorInvalid),
        dateMonthYear => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "only accept a numerical year" in new WithApplication {
      Form("date" -> dayMonthYear.verifying(validDate)).bind(Map("date.day" -> "1", "date.month" -> "2", "date.year" -> "bbbb")).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo(errorInvalid),
        dateMonthYear => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "not accept a 3 digits year" in new WithApplication {
      Form("date" -> dayMonthYear.verifying(validDate)).bind(Map("date.day" -> "1", "date.month" -> "2", "date.year" -> "123")).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo(errorInvalid),
        dateMonthYear => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "not accept a 5 digits year" in new WithApplication {
      Form("date" -> dayMonthYear.verifying(validDate)).bind(Map("date.day" -> "1", "date.month" -> "2", "date.year" -> "12345")).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo(errorInvalid),
        dateMonthYear => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "not allow date of birth before 1900" in new WithApplication {
      Form("date" -> dayMonthYear.verifying(validDateOfBirth)).bind(Map("date.day" -> "31", "date.month" -> "12", "date.year" -> "1899")).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo(errorInvalid),
        dmy => "This mapping should not happen." must equalTo("BrokenTestIfDropInHere")
      )
    }

    "not allow date of birth of tomorrow" in new WithApplication {
      val tomorrow = DateTime.now().plusDays(1)
      val d = tomorrow.getDayOfMonth.toString
      val m = tomorrow.getMonthOfYear.toString
      val y = tomorrow.getYear.toString
      Form("date" -> dayMonthYear.verifying(validDateOfBirth)).bind(Map("date.day" -> d, "date.month" -> m, "date.year" -> y)).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo(errorInvalid),
        dmy => "This mapping should not happen." must equalTo("BrokenTestIfDropInHere")
      )
    }

    "allow date of birth for 1st jan 1900" in new WithApplication {
      Form("date" -> dayMonthYear.verifying(validDateOfBirth)).bind(Map("date.day" -> "1", "date.month" -> "1", "date.year" -> "1900")).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("BrokenTestIfDropInHere"),
        dmy => dmy.`dd/MM/yyyy` must equalTo("01/01/1900")
      )
    }

    "allow date of birth for say 1980" in new WithApplication {
      Form("date" -> dayMonthYear.verifying(validDateOfBirth)).bind(Map("date.day" -> "1", "date.month" -> "1", "date.year" -> "1980")).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("BrokenTestIfDropInHere"),
        dmy => dmy.`dd/MM/yyyy` must equalTo("01/01/1980")
      )
    }

    "allow date of birth of today" in new WithApplication {
      val now = DateTime.now()
      val d = now.getDayOfMonth.toString
      val m = now.getMonthOfYear.toString
      val y = now.getYear.toString
      val dtf = DateTimeFormat.forPattern("dd/MM/yyyy")
      val ddmmyyyy = now.toString(dtf)
      Form("date" -> dayMonthYear.verifying(validDateOfBirth)).bind(Map("date.day" -> d, "date.month" -> m, "date.year" -> y)).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("BrokenTestIfDropInHere"),
        dmy => dmy.`dd/MM/yyyy` must equalTo(ddmmyyyy)
      )
    }
  }
  section("unit")
}
