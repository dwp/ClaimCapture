package controllers.s_eligibility

import utils.WithApplication
import controllers.mappings.Mappings
import org.specs2.mutable._

class GEligibilityFormSpec extends Specification {
  section("unit", models.domain.CarersAllowance.id)
  "Carer's Allowance - Elegibility - Form" should {
    val answerHours = "yes"
    val answerOver16 = "no"
    val answerLivesInGB = "no"

    "map data into case class" in new WithApplication {
      GEligibility.form.bind(
        Map("hours.answer" -> answerHours, "over16.answer" -> answerOver16, "origin" -> "GB")
      ).fold(
        formWithErrors => "This mapping should not happen." must equalTo("Error"),
        f => {
          f.hours must equalTo(answerHours)
          f.over16 must equalTo(answerOver16)
          f.origin must equalTo("GB")
          f.livesInGB must equalTo("yes")
        }
      )
    }

    "reject if 35hours mandatory field is not filled" in new WithApplication {
      GEligibility.form.bind(
        Map("over16.answer" -> answerOver16, "origin" -> "GB")
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo(Mappings.errorRequired),
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "reject if over16 mandatory field is not filled" in new WithApplication {
      GEligibility.form.bind(
        Map("hours.answer" -> answerHours, "origin" -> "GB")
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo(Mappings.errorRequired),
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "reject if origin mandatory field is not filled" in new WithApplication {
      GEligibility.form.bind(
        Map("hours.answer" -> answerHours, "over16.answer" -> answerOver16)
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo(Mappings.errorRequired),
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }
  }
  section("unit", models.domain.CarersAllowance.id)
}
