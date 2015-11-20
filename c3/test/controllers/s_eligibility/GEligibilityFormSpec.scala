package controllers.s_eligibility

import utils.WithApplication
import controllers.mappings.Mappings
import org.specs2.mutable._

class GEligibilityFormSpec extends Specification {
  "Carer's Allowance - Elegibility - Form" should {
    val answerHours = "yes"
    val answerOver16 = "no"
    val answerLivesInGB = "no"

    "map data into case class" in new WithApplication {
      GEligibility.form.bind(
        Map("hours.answer" -> answerHours, "over16.answer" -> answerOver16, "livesInGB.answer" -> answerLivesInGB)
      ).fold(
        formWithErrors => "This mapping should not happen." must equalTo("Error"),
        f => {
          f.hours must equalTo(answerHours)
          f.over16 must equalTo(answerOver16)
          f.livesInGB must equalTo(answerLivesInGB)
        }
      )
    }

    "reject if mandatory field is not filled" in new WithApplication {
      GEligibility.form.bind(
        Map("hours.answer" -> "")
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo(Mappings.errorRequired),
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }
  }
  section("unit", models.domain.CarersAllowance.id)
}
