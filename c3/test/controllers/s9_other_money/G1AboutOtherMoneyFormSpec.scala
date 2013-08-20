package controllers.s9_other_money

import org.specs2.mutable.{Tags, Specification}

class G1AboutOtherMoneyFormSpec extends Specification with Tags {
  "About Other Money Form" should {
    val yourBenefits = "yes"
    val yourBenefitsText = "bar"
    val yourPartnerBenefitsText = "claimed"

    "map data into case class" in {
      G1AboutOtherMoney.form.bind(
        Map("yourBenefits.answer" -> yourBenefits,
            "yourBenefits.text1" -> yourBenefitsText,
            "yourBenefits.text2" -> yourPartnerBenefitsText)
      ).fold(
        formWithErrors => "This mapping should not happen." must equalTo("Error"),
        f => {
          f.yourBenefits.answer must equalTo(yourBenefits)
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
  } section("unit", models.domain.OtherMoney.id)
}