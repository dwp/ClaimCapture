package controllers.s9_other_money

import org.specs2.mutable.{Tags, Specification}

class G1AboutOtherMoneyFormSpec extends Specification with Tags {
  "About Other Money Form" should {
    val yourBenefits = "yes"
    val anyPaymentsSinceClaimDate = "yes"


    "map data into case class" in {
      G1AboutOtherMoney.form.bind(
        Map("yourBenefits.answer" -> yourBenefits,
          "anyPaymentsSinceClaimDate.answer" -> anyPaymentsSinceClaimDate
        )
      ).fold(
        formWithErrors => "This mapping should not happen." must equalTo("Error"),
        f => {
          f.yourBenefits.answer must equalTo(yourBenefits)
          f.anyPaymentsSinceClaimDate.answer must equalTo(anyPaymentsSinceClaimDate)
        }
      )
    }
    
    "reject invalid yesNo answer" in {
      G1AboutOtherMoney.form.bind(
        Map("yourBenefits.answer" -> "INVALID",
            "anyPaymentsSinceClaimDate.answer" -> "INVALID")
      ).fold(
        formWithErrors => {
          formWithErrors.errors.length must equalTo(2)
          formWithErrors.errors(0).message must equalTo("yesNo.invalid")
          formWithErrors.errors(1).message must equalTo("yesNo.invalid")
        },
        f => "This mapping should not happen." must equalTo("Valid"))
    }
  } section("unit", models.domain.OtherMoney.id)
}