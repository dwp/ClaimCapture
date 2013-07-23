package controllers.s8_other_money

import org.specs2.mutable.{Tags, Specification}
import models.domain.{NoRouting, MoreAboutYou}
import models.MultiLineAddress

class G5StatutorySickPayFormSpec extends Specification with Tags {
  "Statutory Sick Pay Form" should {
    val haveYouHadAnyStatutorySickPay = "yes"
    val howMuch = "bar"
    val howOften_frequency = "fizz"
    val employersName = "Johny B Good"
    val employersAddressLineOne =  "lineOne"
    val employersAddressLineTwo = "lineTwo"
    val employersAddressLineThree = "lineThree"
    val employersPostcode = "SE1 6EH"
      
    "map data into case class" in {
      G5StatutorySickPay.form.bind(
        Map("haveYouHadAnyStatutorySickPay" -> haveYouHadAnyStatutorySickPay,
            "howMuch" -> howMuch,
            "howOften_frequency" -> howOften_frequency,
            "employersName" -> employersName,
            "employersAddress.lineOne" -> employersAddressLineOne, 
            "employersAddress.lineTwo" -> employersAddressLineTwo, 
            "employersAddress.lineThree" -> employersAddressLineThree,
            "employersPostcode" -> employersPostcode
        )
      ).fold(
        formWithErrors => "This mapping should not happen." must equalTo("Error"),
        f => {
          f.haveYouHadAnyStatutorySickPay must equalTo(haveYouHadAnyStatutorySickPay)
          f.howMuch must equalTo(Some(howMuch))
          f.howOften_frequency must equalTo(Some(howOften_frequency))
          f.employersName must equalTo(Some(employersName))
          f.employersAddress must equalTo(Some(MultiLineAddress(Some(employersAddressLineOne), Some(employersAddressLineTwo), Some(employersAddressLineThree))))
          f.employersPostcode must equalTo(Some(employersPostcode))
        }
      )
    }
    
    "allow optional fields to be left blank when answer is no" in {
      G5StatutorySickPay.form.bind(
        Map("haveYouHadAnyStatutorySickPay" -> "no")
      ).fold(
        formWithErrors => "This mapping should not happen." must equalTo("Error"),
        f => {
          f.haveYouHadAnyStatutorySickPay must equalTo("no")
          f.howMuch must equalTo(None)
          f.howOften_frequency must equalTo(None)
          f.employersName must equalTo(None)
          f.employersAddress must equalTo(None)
          f.employersPostcode must equalTo(None)
        }
      )
    }
    
    "allow optional fields to be left blank when answer is yes" in {
      G5StatutorySickPay.form.bind(
        Map("haveYouHadAnyStatutorySickPay" -> haveYouHadAnyStatutorySickPay, 
            "employersName" -> employersName
            )
      ).fold(
        formWithErrors => "This mapping should not happen." must equalTo("Error"),
        f => {
          f.haveYouHadAnyStatutorySickPay must equalTo(haveYouHadAnyStatutorySickPay)
          f.howMuch must equalTo(None)
          f.howOften_frequency must equalTo(None)
          f.employersName must equalTo(Some(employersName))
          f.employersAddress must equalTo(None)
          f.employersPostcode must equalTo(None)
        }
      )
    }

    "reject an invalid postcode" in {
      G5StatutorySickPay.form.bind(
        Map("haveYouHadAnyStatutorySickPay" -> haveYouHadAnyStatutorySickPay, 
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
        Map("haveYouHadAnyStatutorySickPay" -> haveYouHadAnyStatutorySickPay)
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("employersName.required"),
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }
  } section "unit"
}