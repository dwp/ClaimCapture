package controllers.s_eligibility

import utils.WithApplication
import controllers.mappings.Mappings
import org.specs2.mutable._
import models.domain.Benefits

class GBenefitsFormSpec extends Specification {
  "Carer's Allowance - Benefits - Form" should {
    val benefitsAnswer = Benefits.aa

    "map data into case class" in new WithApplication {
      GBenefits.form.bind(
        Map("benefitsAnswer" -> benefitsAnswer)
      ).fold(
        formWithErrors => "This mapping should not happen." must equalTo("Error"),
        f => {
          f.benefitsAnswer must equalTo("AA")
        }
      )
    }

    "reject if mandatory field is not filled" in new WithApplication {
      GBenefits.form.bind(
        Map("benefitsAnswer" -> "")
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo(Mappings.errorRequired),
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }
  }
  section("unit", models.domain.CarersAllowance.id)
}
