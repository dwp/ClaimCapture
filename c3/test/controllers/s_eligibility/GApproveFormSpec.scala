package controllers.s_eligibility

import utils.WithApplication
import controllers.mappings.Mappings
import org.specs2.mutable._

class GApproveFormSpec extends Specification {
  "Carer's Allowance - Can you get Carer's Allowance - Form" should {
    val answerYes = "yes"

    "map data into case class when allowed to continue" in new WithApplication {
      CarersAllowance.form.bind(
        Map(
          "allowedToContinue" -> "true",
          "jsEnabled" -> "true"
        )
      ).fold(
          formWithErrors => "This mapping should not happen." must equalTo("Error"),
          f => {
            f.allowedToContinue must equalTo(true)
            f.jsEnabled must equalTo(true)
          }
        )
    }

    "map data into case class when allowed to continue" in new WithApplication {
      CarersAllowance.form.bind(
        Map(
          "allowedToContinue" -> "false",
          "answer" -> answerYes,
          "jsEnabled" -> "true"
        )
      ).fold(
          formWithErrors => "This mapping should not happen." must equalTo("Error"),
          f => {
            f.allowedToContinue must equalTo(false)
            f.answerYesNo.get must equalTo(answerYes)
            f.jsEnabled must equalTo(true)
          }
        )
    }

    "reject if not directly allowed to continue and answer is empty" in new WithApplication {
      CarersAllowance.form.bind(
        Map(
          "allowedToContinue" -> "false",
          "answer" -> ""
        )
      ).fold(
          formWithErrors => formWithErrors.errors.head.message must equalTo(Mappings.errorRequired),
          f => "This mapping should not happen." must equalTo("Valid")
        )
    }
  }
  section("unit", models.domain.CarersAllowance.id)

}
