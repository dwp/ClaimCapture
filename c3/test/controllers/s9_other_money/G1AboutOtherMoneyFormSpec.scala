package controllers.s9_other_money

import org.specs2.mutable.{Tags, Specification}

class G1AboutOtherMoneyFormSpec extends Specification with Tags {
  "About Other Money Form" should {
    val yourBenefits = "yes"
    val yourBenefitsText = "bar"
      
    "map data into case class" in {
      G1AboutOtherMoney.form.bind(
        Map("yourBenefits.answer" -> yourBenefits,
            "yourBenefits.text" -> yourBenefitsText)
      ).fold(
        formWithErrors => "This mapping should not happen." must equalTo("Error"),
        f => {
          f.yourBenefits.answer must equalTo(yourBenefits)
          f.yourBenefits.text must equalTo(Some(yourBenefitsText))
        }
      )
    }
    
    "reject invalid yesNo answer" in {
      G1AboutOtherMoney.form.bind(
        Map("yourBenefits.answer" -> "INVALID")
      ).fold(
        formWithErrors => {
          formWithErrors.errors.head.message must equalTo("yesNo.invalid")
          formWithErrors.errors.length must equalTo(1)
        },
        f => "This mapping should not happen." must equalTo("Valid"))
    }
    
    "reject text enabled but text not filled in" in {
      G1AboutOtherMoney.form.bind(
        Map("yourBenefits.answer" -> yourBenefits)
      ).fold(
        formWithErrors => {
          formWithErrors.errors.head.message must equalTo("required")
          formWithErrors.errors.length must equalTo(1)
        },
        f => "This mapping should not happen." must equalTo("Valid"))
    }
  }
}