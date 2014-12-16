package models

import org.specs2.mutable.Specification
import play.api.data.Form
import controllers.mappings.Mappings._
import controllers.mappings.AddressMappings._

class MultiLineAddressFormSpec extends Specification {

  "Multi Line Address" should {

    "reject empty input" in {
      Form("address" -> address.verifying(requiredAddress)).bind(Map("address.lineOne" -> "", "address.lineTwo" -> "", "address.lineThree" -> "")).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.required"),
        address => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "accept single lineOne" in {
      Form("address" -> address.verifying(requiredAddress)).bind(Map("address.lineOne" -> "line1", "address.lineTwo" -> "", "address.lineThree" -> "")).fold(
        formWithErrors => "The mapping should not fail." must equalTo("Error"),
        address => address must equalTo(MultiLineAddress(Some("line1")))
      )
    }

    "reject single lineTwo" in {
      Form("address" -> address.verifying(requiredAddress)).bind(Map("address.lineOne" -> "", "address.lineTwo" -> "line2", "address.lineThree" -> "")).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.required"),
        address => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "reject single lineThree" in {
      Form("address" -> address.verifying(requiredAddress)).bind(Map("address.lineOne" -> "", "address.lineTwo" -> "", "address.lineThree" -> "line3")).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.required"),
        address => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "have a maxLength constraint for lineOne" in {
      Form("address" -> address.verifying(requiredAddress)).bind(
        Map("address.lineOne" -> "CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS")
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.maxLength"),
        address => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "have a maxLength constraint for lineTwo" in {
      Form("address" -> address.verifying(requiredAddress)).bind(
        Map("address.lineOne" -> "somevalue","address.lineTwo" -> "CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS")
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.maxLength"),
        address => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "have a maxLength constraint for lineThree" in {
      Form("address" -> address.verifying(requiredAddress)).bind(
        Map("address.lineOne" -> "somevalue","address.lineThree" -> "CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS")
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.maxLength"),
        address => "This mapping should not happen." must equalTo("Valid")
      )
    }
  }
}