package controllers.s9_other_money

import org.specs2.mutable.{ Tags, Specification }
import models.{Street, Town, PaymentFrequency, MultiLineAddress}
import scala.Some

class G6OtherStatutoryPayFormSpec extends Specification with Tags {
  "Other Statutory Form" should {
    val yes = "yes"
    val no = "no"
    val howMuch = "howMuch"
    val howOften_frequency = app.PensionPaymentFrequency.Other
    val howOften_frequency_other = "Every day and twice on Sundays"
    val employersName = "Johny B Good"
    val employersAddressLineOne = "lineOne"
    val employersAddressLineTwo = "lineTwo"
    val employersAddressLineThree = "lineThree"
    val employersPostcode = "SE1 6EH"

    "map data into case class" in {
      G6OtherStatutoryPay.form.bind(
        Map(
          "otherPay" -> yes,
          "howMuch" -> howMuch,
          "howOften.frequency" -> howOften_frequency,
          "howOften.frequency.other" -> howOften_frequency_other,
          "employersName" -> employersName,
          "employersAddress.street.lineOne" -> employersAddressLineOne,
          "employersAddress.town.lineTwo" -> employersAddressLineTwo,
          "employersAddress.town.lineThree" -> employersAddressLineThree,
          "employersPostcode" -> employersPostcode)).fold(
          formWithErrors => "This mapping should not happen." must equalTo("Error"),
          f => {
            f.otherPay must equalTo(yes)
            f.howMuch must equalTo(Some(howMuch))
            f.howOften must equalTo(Some(PaymentFrequency(howOften_frequency, Some(howOften_frequency_other))))
            f.employersName must equalTo(Some(employersName))
            f.employersAddress must equalTo(Some(MultiLineAddress(Street(Some(employersAddressLineOne)), Some(Town(Some(employersAddressLineTwo), Some(employersAddressLineThree))))))
            f.employersPostcode must equalTo(Some(employersPostcode))
          })
    }

    "allow optional fields to be left blank when answer is no" in {
      G6OtherStatutoryPay.form.bind(
        Map("otherPay" -> no)).fold(
          formWithErrors => formWithErrors.errors must equalTo("Error"),
          f => f.otherPay must equalTo(no))
    }

    "allow optional fields to be left blank when answer is yes" in {
      G6OtherStatutoryPay.form.bind(
        Map("otherPay" -> yes,
          "employersName" -> employersName)).fold(
          formWithErrors => "This mapping should not happen." must equalTo("Error"),
          f => {
            f.otherPay must equalTo(yes)
            f.employersName must equalTo(Some(employersName))
          })
    }

    "return a bad request after an invalid submission" in {
      "reject an invalid postcode" in {
        G6OtherStatutoryPay.form.bind(
          Map("otherPay" -> yes,
            "employersName" -> employersName,
            "employersPostcode" -> "INVALID")).fold(
            formWithErrors => {
              formWithErrors.errors.length must equalTo(1)
              formWithErrors.errors(0).message must equalTo("error.postcode")
            },
            f => "This mapping should not happen." must equalTo("Valid"))
      }

      "reject answer yes but other mandatory fields not filled in" in {
        G6OtherStatutoryPay.form.bind(
          Map("otherPay" -> yes)).fold(
            formWithErrors => {
              formWithErrors.errors.length must equalTo(1)
              formWithErrors.errors(0).message must equalTo("employersName.required")
            },
            f => "This mapping should not happen." must equalTo("Valid"))
      }

      "reject a howOften frequency if other with no other text entered" in {
        G6OtherStatutoryPay.form.bind(
          Map("otherPay" -> yes,
            "howMuch" -> howMuch,
            "howOften.frequency" -> howOften_frequency,
            "howOften.frequency.other" -> "",
            "employersName" -> employersName,
            "employersAddress.street.lineOne" -> employersAddressLineOne,
            "employersAddress.town.lineTwo" -> employersAddressLineTwo,
            "employersAddress.town.lineThree" -> employersAddressLineThree,
            "employersPostcode" -> employersPostcode)).fold(
            formWithErrors => {
              println(formWithErrors.errors)
              formWithErrors.errors.length must equalTo(1)
              formWithErrors.errors(0).message must equalTo("error.paymentFrequency")
            },
            f => "This mapping should not happen." must equalTo("Valid"))
      }
    }
  } section ("unit", models.domain.OtherMoney.id)
}