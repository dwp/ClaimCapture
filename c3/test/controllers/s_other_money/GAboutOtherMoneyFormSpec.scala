package controllers.s_other_money

import org.specs2.mutable._
import models.{MultiLineAddress, PaymentFrequency}
import utils.WithApplication

class GAboutOtherMoneyFormSpec extends Specification {
  section ("unit", models.domain.OtherMoney.id)
  "Other Money Form" should {
    val anyPaymentsSinceClaimDate = "yes"
    val whoPaysYou = "The Man"
    val howMuch = "12"
    val howOften_frequency = app.PensionPaymentFrequency.Other
    val howOften_frequency_other = "Every day and twice on Sundays"
    val employersName = "Toys R Us"
    val employersAddressLineOne = "Address line 1"
    val employersAddressLineTwo = "Address line 2"
    val employersAddressLineThree = "Address line 3"
    val employersPostcode = "PR1A4JQ"
    val yes = "yes"
    val no = "no"

    "map data into case class" in new WithApplication {
      GAboutOtherMoney.form.bind(
        Map("anyPaymentsSinceClaimDate.answer" -> anyPaymentsSinceClaimDate,
          "whoPaysYou" -> whoPaysYou,
          "howMuch" -> howMuch,
          "howOften.frequency" -> howOften_frequency,
          "howOften.frequency.other" -> howOften_frequency_other,
          "statutorySickPay.answer" -> yes,
          "statutorySickPay.howMuch" -> howMuch,
          "statutorySickPay.howOften.frequency" -> howOften_frequency,
          "statutorySickPay.howOften.frequency.other" -> howOften_frequency_other,
          "statutorySickPay.employersName" -> employersName,
          "statutorySickPay.employersAddress.lineOne" -> employersAddressLineOne,
          "statutorySickPay.employersAddress.lineTwo" -> employersAddressLineTwo,
          "statutorySickPay.employersAddress.lineThree" -> employersAddressLineThree,
          "statutorySickPay.employersPostcode" -> employersPostcode,
          "otherStatutoryPay.answer" -> yes,
          "otherStatutoryPay.howMuch" -> howMuch,
          "otherStatutoryPay.howOften.frequency" -> howOften_frequency,
          "otherStatutoryPay.howOften.frequency.other" -> howOften_frequency_other,
          "otherStatutoryPay.employersName" -> employersName,
          "otherStatutoryPay.address.lineOne" -> employersAddressLineOne,
          "otherStatutoryPay.address.lineTwo" -> employersAddressLineTwo,
          "otherStatutoryPay.address.lineThree" -> employersAddressLineThree,
          "otherStatutoryPay.postCode" -> employersPostcode
        )
      ).fold(
          formWithErrors => "This mapping should not happen." must equalTo("Error"),
          f => {
            f.anyPaymentsSinceClaimDate.answer must equalTo(anyPaymentsSinceClaimDate)
            f.whoPaysYou must equalTo(Some(whoPaysYou))
            f.howMuch must equalTo(Some(howMuch))
            f.howOften must equalTo(Some(PaymentFrequency(howOften_frequency, Some(howOften_frequency_other))))
            f.statutorySickPay.answer must equalTo(yes)
            f.statutorySickPay.howMuch must equalTo(Some(howMuch))
            f.statutorySickPay.howOften must equalTo(Some(PaymentFrequency(howOften_frequency, Some(howOften_frequency_other))))
            f.statutorySickPay.employersName must equalTo(Some(employersName))
            f.statutorySickPay.address must equalTo(Some(MultiLineAddress(Some(employersAddressLineOne), Some(employersAddressLineTwo), Some(employersAddressLineThree))))
            f.statutorySickPay.postCode must equalTo(Some(employersPostcode))
            f.otherStatutoryPay.answer must equalTo(yes)
            f.otherStatutoryPay.howMuch must equalTo(Some(howMuch))
            f.otherStatutoryPay.howOften must equalTo(Some(PaymentFrequency(howOften_frequency, Some(howOften_frequency_other))))
            f.otherStatutoryPay.employersName must equalTo(Some(employersName))
          })
    }

    "return a bad request after an invalid submission" in new WithApplication {
      "reject invalid yesNo answers" in new WithApplication {
        GAboutOtherMoney.form.bind(
          Map("anyPaymentsSinceClaimDate.answer" -> "INVALID",
            "statutorySickPay.answer" -> "INVALID", "otherStatutoryPay.answer" -> "INVALID")).fold(
            formWithErrors => {
              formWithErrors.errors.length must equalTo(3)
              formWithErrors.errors(0).message must equalTo("yesNo.invalid")
              formWithErrors.errors(1).message must equalTo("yesNo.invalid")
              formWithErrors.errors(2).message must equalTo("yesNo.invalid")
            },
            f => "This mapping should not happen." must equalTo("Valid"))
      }

      "reject a howOften frequency of other with no other text entered - other money" in new WithApplication {
        GAboutOtherMoney.form.bind(
          Map("anyPaymentsSinceClaimDate.answer" -> anyPaymentsSinceClaimDate,
            "whoPaysYou" -> whoPaysYou,
            "howMuch" -> howMuch,
            "howOften.frequency" -> "Other",
            "howOften.frequency.other" -> "",
            "statutorySickPay.answer" -> no,
            "otherStatutoryPay.answer" -> no)).fold(
            formWithErrors => {
              formWithErrors.errors.length must equalTo(1)
              formWithErrors.errors(0).message must equalTo("error.paymentFrequency")
            },
            f => "This mapping should not happen." must equalTo("Valid"))
      }

      "reject a howOften frequency of other with no other text entered - statutory sick pay" in new WithApplication {
        GAboutOtherMoney.form.bind(
          Map("anyPaymentsSinceClaimDate.answer" -> no,
            "statutorySickPay.answer" -> yes,
            "statutorySickPay.whoPaysYou" -> whoPaysYou,
            "statutorySickPay.howMuch" -> howMuch,
            "statutorySickPay.howOften.frequency" -> "Other",
            "statutorySickPay.howOften.frequency.other" -> "",
            "otherStatutoryPay.answer" -> no)).fold(
            formWithErrors => {
              formWithErrors.errors.length must equalTo(1)
              formWithErrors.errors(0).message must equalTo("error.paymentFrequency")
            },
            f => "This mapping should not happen." must equalTo("Valid"))
      }

      "reject a howOften frequency of other with no other text entered - other pay" in new WithApplication {
        GAboutOtherMoney.form.bind(
          Map("anyPaymentsSinceClaimDate.answer" -> no,
            "otherStatutoryPay.answer" -> yes,
            "otherStatutoryPay.whoPaysYou" -> whoPaysYou,
            "otherStatutoryPay.howMuch" -> howMuch,
            "otherStatutoryPay.howOften.frequency" -> "Other",
            "otherStatutoryPay.howOften.frequency.other" -> "",
            "statutorySickPay.answer" -> no)).fold(
            formWithErrors => {
              formWithErrors.errors.length must equalTo(1)
              formWithErrors.errors(0).message must equalTo("error.paymentFrequency")
            },
            f => "This mapping should not happen." must equalTo("Valid"))
      }

      "allow optional fields to be left blank when answer is yes - other money" in new WithApplication {
        GAboutOtherMoney.form.bind(
          Map("anyPaymentsSinceClaimDate.answer" -> yes,
            "statutorySickPay.answer" -> no,
            "otherStatutoryPay.answer" -> no,
            "whoPaysYou" -> whoPaysYou,
            "howMuch" -> howMuch)).fold(
            formWithErrors => "This mapping should not happen." must equalTo("Error"),
            f => {
              f.anyPaymentsSinceClaimDate.answer must equalTo("yes")
              f.howOften must equalTo(None)
              f.otherStatutoryPay.answer must equalTo(no)
              f.otherStatutoryPay.answer must equalTo(no)
              f.whoPaysYou must equalTo(Some(whoPaysYou))
              f.howMuch must equalTo(Some(howMuch))
            })
      }
    }
  }
  section ("unit", models.domain.OtherMoney.id)
}
