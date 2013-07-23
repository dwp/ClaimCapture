package controllers.s8_other_money

import org.specs2.mutable.{Tags, Specification}
import models.{PaymentFrequency, MultiLineAddress}
import scala.Some

class G6OtherStatutoryPayFormSpec extends Specification with Tags {
  "Other Statutory Form" should {
    val yes = "yes"
    val no = "no"
    val howMuch = "howMuch"
    val howOften_frequency = "frequency"
    val howOften_other = "other"
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
          "howOften.other" -> howOften_other,
          "employersName" -> employersName,
          "employersAddress.lineOne" -> employersAddressLineOne,
          "employersAddress.lineTwo" -> employersAddressLineTwo,
          "employersAddress.lineThree" -> employersAddressLineThree,
          "employersPostcode" -> employersPostcode
        )
      ).fold(
        formWithErrors => "This mapping should not happen." must equalTo("Error"),
        f => {
          f.otherPay must equalTo(yes)
          f.howMuch must equalTo(Some(howMuch))
          f.howOften must equalTo(Some(PaymentFrequency(howOften_frequency, Some(howOften_other))))
          f.employersName must equalTo(Some(employersName))
          f.employersAddress must equalTo(Some(MultiLineAddress(Some(employersAddressLineOne), Some(employersAddressLineTwo), Some(employersAddressLineThree))))
          f.employersPostcode must equalTo(Some(employersPostcode))
        }
      )
    }

    "allow optional fields to be left blank when answer is no" in {
      G6OtherStatutoryPay.form.bind(
        Map("otherPay" -> no)
      ).fold(
        formWithErrors => formWithErrors.errors must equalTo("Error"),
        f => f.otherPay must equalTo(no)
      )
    }

    "allow optional fields to be left blank when answer is yes" in {
      G6OtherStatutoryPay.form.bind(
        Map("otherPay" -> yes,
          "employersName" -> employersName
        )
      ).fold(
        formWithErrors => "This mapping should not happen." must equalTo("Error"),
        f => {
          f.otherPay must equalTo(yes)
          f.employersName must equalTo(Some(employersName))
        }
      )
    }

    "reject an invalid postcode" in {
      G6OtherStatutoryPay.form.bind(
        Map("otherPay" -> yes,
          "employersName" -> employersName,
          "employersPostcode" -> "INVALID"
        )
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.postcode"),
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "reject answer yes but other mandatory fields not filled in" in {
      G5StatutorySickPay.form.bind(
        Map("otherPay" -> yes)
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.required"),
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }
  } section "unit"
}