package controllers.s9_other_money

import org.specs2.mutable.{ Tags, Specification }
import models.PaymentFrequency

class G1AboutOtherMoneyFormSpec extends Specification with Tags {
  "About Other Money Form" should {
    val yourBenefits = "yes"
    val anyPaymentsSinceClaimDate = "yes"
    val whoPaysYou = "The Man"
    val howMuch = "Not much"
    val howOften_frequency = "Weekly"
    val howOften_other = "other"

    "map data into case class" in {
      G1AboutOtherMoney.form.bind(
        Map("yourBenefits.answer" -> yourBenefits,
          "anyPaymentsSinceClaimDate.answer" -> anyPaymentsSinceClaimDate,
          "whoPaysYou" -> whoPaysYou,
          "howMuch" -> howMuch,
          "howOften.frequency" -> howOften_frequency,
          "howOften.other" -> howOften_other)).fold(
          formWithErrors => "This mapping should not happen." must equalTo("Error"),
          f => {
            f.yourBenefits.answer must equalTo(yourBenefits)
            f.anyPaymentsSinceClaimDate.answer must equalTo(anyPaymentsSinceClaimDate)
            f.whoPaysYou must equalTo(Some(whoPaysYou))
            f.howMuch must equalTo(Some(howMuch))
            f.howOften must equalTo(Some(PaymentFrequency(howOften_frequency, Some(howOften_other))))
          })
    }

    "reject invalid yesNo answer" in {
      G1AboutOtherMoney.form.bind(
        Map("yourBenefits.answer" -> "INVALID",
          "anyPaymentsSinceClaimDate.answer" -> "INVALID")).fold(
          formWithErrors => {
            formWithErrors.errors.length must equalTo(2)
            formWithErrors.errors(0).message must equalTo("yesNo.invalid")
            formWithErrors.errors(1).message must equalTo("yesNo.invalid")
          },
          f => "This mapping should not happen." must equalTo("Valid"))
    }
    
    "reject howOften frequency other but no other text" in {
      G1AboutOtherMoney.form.bind(
        Map("yourBenefits.answer" -> yourBenefits,
          "anyPaymentsSinceClaimDate.answer" -> anyPaymentsSinceClaimDate,
          "whoPaysYou" -> whoPaysYou,
          "howMuch" -> howMuch,
          "howOften.frequency" -> "other",
          "howOften_other" -> "")).fold(
          formWithErrors => {
            formWithErrors.errors.length must equalTo(1)
            formWithErrors.errors(0).message must equalTo("error.paymentFrequency")
          },
          f => "This mapping should not happen." must equalTo("Valid"))
    }
  } section ("unit", models.domain.OtherMoney.id)
}