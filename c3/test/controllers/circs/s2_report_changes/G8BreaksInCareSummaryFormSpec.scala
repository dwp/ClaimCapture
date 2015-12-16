package controllers.circs.s2_report_changes

import utils.WithApplication
import org.specs2.mutable._
import scala.Predef._

class G8BreaksInCareSummaryFormSpec extends Specification {
  section("unit", models.domain.CircumstancesAddressChange.id)
  "Report a change in your circumstances - Breaks from caring summary Form" should {
    val yes = "yes"
    val no = "no"

    "map data into case class with additional breaks" in new WithApplication {
       G8BreaksInCareSummary.form.bind(
          Map(
            "additionalBreaks.answer" -> yes,
            "additionalBreaks.text" -> "Some break I haven't told you about"
          )
       ).fold(
         formWithErrors => "This mapping should not happen." must equalTo("Error"),
         form => {
           form.additionalBreaks.answer must equalTo(yes)
           form.additionalBreaks.text must equalTo(Some("Some break I haven't told you about"))
         }
       )
    }

    "map data into case class with no additional breaks" in new WithApplication {
       G8BreaksInCareSummary.form.bind(
          Map(
            "additionalBreaks.answer" -> no
          )
       ).fold(
         formWithErrors => "This mapping should not happen." must equalTo("Error"),
         form => {
           form.additionalBreaks.answer must equalTo(no)
           form.additionalBreaks.text must equalTo(None)
         }
       )
    }

    "check errors for mandatory fields with additional breaks" in new WithApplication {
      G8BreaksInCareSummary.form.bind(
        Map(
          "additionalBreaks.answer" -> yes
        )
      ).fold(
        formWithErrors => {
            formWithErrors.errors.size must equalTo(1)
        },
        form => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "check errors for mandatory fields with additional breaks" in new WithApplication {
      G8BreaksInCareSummary.form.bind(
        Map(
          "additionalBreaks.answer" -> ""
        )
      ).fold(
        formWithErrors => {
            formWithErrors.errors.size must equalTo(2)
        },
        form => "This mapping should not happen." must equalTo("Valid")
      )
    }
  }
  section("unit", models.domain.CircumstancesAddressChange.id)
}
