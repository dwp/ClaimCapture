package controllers.s1_carers_allowance

import org.specs2.mutable.{Tags, Specification}

class G2HoursFormSpec extends Specification with Tags {
  "Carer's Allowance - Hours - Form" should {
    val answerYesNo = "yes"
      
    "map data into case class" in {
      G2Hours.form.bind(
        Map("hours.answer" -> answerYesNo)
      ).fold(
        formWithErrors => "This mapping should not happen." must equalTo("Error"),
        f => {
          f.answerYesNo must equalTo("yes")
        }
      )
    }

    "reject if mandatory field is not filled" in {
      G2Hours.form.bind(
        Map("hours.answer" -> "")
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.required"),
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }
  } section("unit", models.domain.CarersAllowance.id)
}