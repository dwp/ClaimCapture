package controllers.s1_carers_allowance

import controllers.mappings.Mappings
import org.specs2.mutable.{Tags, Specification}

class G2EligibilityFormSpec extends Specification with Tags {
  "Carer's Allowance - Elegibility - Form" should {
    val answerHours = "yes"
    val answerOver16 = "no"
    val answerLivesInGB = "no"

    "map data into case class" in {
      G2Eligibility.form.bind(
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

    "reject if mandatory field is not filled" in {
      G2Eligibility.form.bind(
        Map("hours.answer" -> "")
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo(Mappings.errorRequired),
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }
  } section("unit", models.domain.CarersAllowance.id)
}