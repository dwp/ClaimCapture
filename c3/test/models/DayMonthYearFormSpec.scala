package models

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
  }
  section("unit")
}
