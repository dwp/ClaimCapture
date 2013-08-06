package controllers.s1_carers_allowance

import org.specs2.mutable.{Tags, Specification}


class G1BenefitsMandatoryFormSpec extends Specification with Tags {

  "Carer's Allowance - Benefits - Form" should {

    "map data into case class" in {
      G1BenefitsMandatory.form.bind(
        Map("answer" -> "yes")
      ).fold(
        formWithErrors => "This mapping should not happen." must equalTo("Error"),
        f => {
          f.answerYesNo must equalTo("yes")
        }
      )
    }

    "reject if mandatory field is not filled" in {
      G1BenefitsMandatory.form.bind(
        Map("answer" -> "")
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.required"),
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }
  } section "unit"

}
