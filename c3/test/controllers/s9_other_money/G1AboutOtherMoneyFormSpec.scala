package controllers.s9_other_money

import org.specs2.mutable.{ Tags, Specification }
import models.PaymentFrequency

class G1AboutOtherMoneyFormSpec extends Specification with Tags {
  "About Other Money Form" should {
    val yourBenefits = "yes"
    val anyPaymentsSinceClaimDate = "yes"
    val whoPaysYou = "The Man"
    val howMuch = "Not much"
    val howOften_frequency = app.PensionPaymentFrequency.Other
    val howOften_frequency_other = "Every day and twice on Sundays"

    "map data into case class" in {
      G1AboutOtherMoney.form.bind(
        Map("yourBenefits.answer" -> yourBenefits,
          "anyPaymentsSinceClaimDate.answer" -> anyPaymentsSinceClaimDate,
          "whoPaysYou" -> whoPaysYou,
          "howMuch" -> howMuch,
          "howOften.frequency" -> howOften_frequency,
          "howOften.frequency.other" -> howOften_frequency_other)).fold(
          formWithErrors => "This mapping should not happen." must equalTo("Error"),
          f => {
            f.yourBenefits.answer must equalTo(yourBenefits)
            f.anyPaymentsSinceClaimDate.answer must equalTo(anyPaymentsSinceClaimDate)
            f.whoPaysYou must equalTo(Some(whoPaysYou))
            f.howMuch must equalTo(Some(howMuch))
            f.howOften must equalTo(Some(PaymentFrequency(howOften_frequency, Some(howOften_frequency_other))))
          })
    }

    "return a bad request after an invalid submission" in {
      "reject invalid yesNo answers" in {
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

      "reject a howOften frequency of other with no other text entered" in {
        G1AboutOtherMoney.form.bind(
          Map("yourBenefits.answer" -> yourBenefits,
            "anyPaymentsSinceClaimDate.answer" -> anyPaymentsSinceClaimDate,
            "whoPaysYou" -> whoPaysYou,
            "howMuch" -> howMuch,
            "howOften.frequency" -> howOften_frequency,
            "howOften.frequency.other" -> "")).fold(
            formWithErrors => {
              formWithErrors.errors.length must equalTo(1)
              formWithErrors.errors(0).message must equalTo("error.paymentFrequency")
            },
            f => "This mapping should not happen." must equalTo("Valid"))
      }
    }
  } section ("unit", models.domain.OtherMoney.id)
}