package controllers.s1_carers_allowance

import org.specs2.mutable.{Tags, Specification}

class G4LivesInGBFormSpec extends Specification with Tags {
  "Carer's Allowance - LivesInGB - Form" should {
    val answerYesNo = "yes"
      
    "map data into case class" in {
      G4LivesInGB.form.bind(
        Map("livesInGB.answer" -> answerYesNo)
      ).fold(
        formWithErrors => "This mapping should not happen." must equalTo("Error"),
        f => {
          f.answerYesNo must equalTo("yes")
        }
      )
    }

    "reject if mandatory field is not filled" in {
      G4LivesInGB.form.bind(
        Map("livesInGB.answer" -> "")
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.required"),
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }
  } section("unit", models.domain.CarersAllowance.id)
}