package controllers.s9_other_money

import org.specs2.mutable.{ Tags, Specification }
import models.{MultiLineAddress, PaymentFrequency}

class G5StatutorySickPayFormSpec extends Specification with Tags {
  "Statutory Sick Pay Form" should {
    val haveYouHadAnyStatutorySickPay = "yes"
    val howMuch = "123.45"
    val howOften_frequency = "other"
    val howOften_frequency_other = "Every day and twice on Sundays"
    val employersName = "Johny B Good"
    val employersAddressLineOne = "lineOne"
    val employersAddressLineTwo = "lineTwo"
    val employersAddressLineThree = "lineThree"
    val employersPostcode = "SE1 6EH"

    "map data into case class" in {
      G5StatutorySickPay.form.bind(
        Map("haveYouHadAnyStatutorySickPay" -> haveYouHadAnyStatutorySickPay,
          "howMuch" -> howMuch,
          "howOften.frequency" -> howOften_frequency,
          "howOften.frequency.other" -> howOften_frequency_other,
          "employersName" -> employersName,
          "employersAddress.lineOne" -> employersAddressLineOne,
          "employersAddress.lineTwo" -> employersAddressLineTwo,
          "employersAddress.lineThree" -> employersAddressLineThree,
          "employersPostcode" -> employersPostcode)).fold(
          formWithErrors => "This mapping should not happen." must equalTo("Error"),
          f => {
            f.haveYouHadAnyStatutorySickPay must equalTo(haveYouHadAnyStatutorySickPay)
            f.howMuch must equalTo(Some(howMuch))
            f.howOften must equalTo(Some(PaymentFrequency(howOften_frequency, Some(howOften_frequency_other))))
            f.employersName must equalTo(Some(employersName))
            f.employersAddress must equalTo(Some(MultiLineAddress(Some(employersAddressLineOne), Some(employersAddressLineTwo), Some(employersAddressLineThree))))
            f.employersPostcode must equalTo(Some(employersPostcode))
          })
    }

    "allow optional fields to be left blank when answer is no" in {
      G5StatutorySickPay.form.bind(
        Map("haveYouHadAnyStatutorySickPay" -> "no")).fold(
          formWithErrors => "This mapping should not happen." must equalTo("Error"),
          f => {
            f.haveYouHadAnyStatutorySickPay must equalTo("no")
            f.howMuch must equalTo(None)
            f.howOften must equalTo(None)
            f.employersName must equalTo(None)
            f.employersAddress must equalTo(None)
            f.employersPostcode must equalTo(None)
          })
    }

    "allow optional fields to be left blank when answer is yes" in {
      G5StatutorySickPay.form.bind(
        Map("haveYouHadAnyStatutorySickPay" -> haveYouHadAnyStatutorySickPay,
          "howMuch" -> howMuch,
          "employersName" -> employersName)).fold(
          formWithErrors => "This mapping should not happen." must equalTo("Error"),
          f => {
            f.haveYouHadAnyStatutorySickPay must equalTo(haveYouHadAnyStatutorySickPay)
            f.howOften must equalTo(None)
            f.employersName must equalTo(Some(employersName))
            f.employersAddress must equalTo(None)
            f.employersPostcode must equalTo(None)
          })
    }

    "return a bad request after an invalid submission" in {
      "reject an invalid postcode" in {
        G5StatutorySickPay.form.bind(
          Map("haveYouHadAnyStatutorySickPay" -> haveYouHadAnyStatutorySickPay,
            "employersName" -> employersName,
            "employersPostcode" -> "INVALID")).fold(
            formWithErrors => {
              formWithErrors.errors.length must equalTo(1)
              formWithErrors.errors(0).message must equalTo("error.postcode")},
            f => "This mapping should not happen." must equalTo("Valid"))
      }

      "reject answer yes but other mandatory fields not filled in" in {
        G5StatutorySickPay.form.bind(
          Map("haveYouHadAnyStatutorySickPay" -> haveYouHadAnyStatutorySickPay)).fold(
            formWithErrors => {
              formWithErrors.errors.length must equalTo(2)
              formWithErrors.errors(0).message must equalTo("employersName.required")
            },
            f => "This mapping should not happen." must equalTo("Valid"))
      }
      
      "reject a howOften frequency of other with no other text entered" in {
        G5StatutorySickPay.form.bind(
          Map("haveYouHadAnyStatutorySickPay" -> haveYouHadAnyStatutorySickPay,
          "howMuch" -> howMuch,
          "howOften.frequency" -> howOften_frequency,
          "howOften.frequency.other" -> "",
          "employersName" -> employersName,
          "employersAddress.lineOne" -> employersAddressLineOne,
          "employersAddress.lineTwo" -> employersAddressLineTwo,
          "employersAddress.lineThree" -> employersAddressLineThree,
          "employersPostcode" -> employersPostcode)).fold(
            formWithErrors => {
              formWithErrors.errors.length must equalTo(1)
              formWithErrors.errors(0).message must equalTo("error.paymentFrequency")
            },
            f => "This mapping should not happen." must equalTo("Valid"))
      }
    }
  } section ("unit", models.domain.OtherMoney.id)
}