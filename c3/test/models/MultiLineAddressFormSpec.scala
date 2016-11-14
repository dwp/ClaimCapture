package models

import controllers.mappings.{AddressMappings, Mappings}
import org.specs2.mutable._
import play.api.data.Form
import controllers.mappings.AddressMappings._

class MultiLineAddressFormSpec extends Specification {
  section("unit")
  "Multi Line Address" should {
    "reject empty input - set carer address errors correctly" in {
      Form("address" -> address(AddressMappings.CARER)).bind(Map("address.lineOne" -> "", "address.lineTwo" -> "", "address.lineThree" -> "")).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.careraddress.lines.required"),
        address => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "reject empty input - set carer previous address errors correctly" in {
      Form("address" -> address(AddressMappings.CARERPREVIOUS)).bind(Map("address.lineOne" -> "", "address.lineTwo" -> "", "address.lineThree" -> "")).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.carerprevaddress.lines.required"),
        address => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "reject empty input - set carer new address errors correctly" in {
      Form("address" -> address(AddressMappings.CARERNEW)).bind(Map("address.lineOne" -> "", "address.lineTwo" -> "", "address.lineThree" -> "")).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.carernewaddress.lines.required"),
        address => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "reject empty input - set caree/dp address errors correctly" in {
      Form("address" -> address(AddressMappings.CAREE)).bind(Map("address.lineOne" -> "", "address.lineTwo" -> "", "address.lineThree" -> "")).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.careeaddress.lines.required"),
        address => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "reject empty input - set caree/dp new address errors correctly" in {
      Form("address" -> address(AddressMappings.CAREENEW)).bind(Map("address.lineOne" -> "", "address.lineTwo" -> "", "address.lineThree" -> "")).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.careenewaddress.lines.required"),
        address => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "reject empty input - set employment address errors correctly" in {
      Form("address" -> address(AddressMappings.EMPLOYMENT)).bind(Map("address.lineOne" -> "", "address.lineTwo" -> "", "address.lineThree" -> "")).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.empaddress.lines.required"),
        address => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "accept two address lines" in {
      Form("address" -> address(AddressMappings.CARER)).bind(Map("address.lineOne" -> "line1", "address.lineTwo" -> "line2", "address.lineThree" -> "")).fold(
        formWithErrors => "The mapping should not fail." must equalTo("Error"),
        address => {
            address must equalTo(MultiLineAddress(Some("line1"), Some("line2")))
          }
      )
    }

    "reject single lineTwo" in {
      Form("address" -> address(AddressMappings.CARER)).bind(Map("address.lineOne" -> "", "address.lineTwo" -> "line2", "address.lineThree" -> "")).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.careraddress.lines.required"),
        address => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "reject single lineThree" in {
      Form("address" -> address(AddressMappings.CARER)).bind(Map("address.lineOne" -> "", "address.lineTwo" -> "", "address.lineThree" -> "line3")).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.careraddress.lines.required"),
        address => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "reject if lineTwo is empty" in {
      Form("address" -> address(AddressMappings.CARER)).bind(Map("address.lineOne" -> "lineOne", "address.lineTwo" -> "", "address.lineThree" -> "")).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.careraddress.lines.required"),
        address => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "have a maxLength constraint for lineOne" in {
      Form("address" -> address(AddressMappings.CARER)).bind(
        Map("address.lineOne" -> "CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS", "address.lineTwo" -> "test")
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo(Mappings.maxLengthError),
        address => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "have a maxLength constraint for lineTwo" in {
      Form("address" -> address(AddressMappings.CARER)).bind(
        Map("address.lineOne" -> "somevalue","address.lineTwo" -> "CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS")
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo(Mappings.maxLengthError),
        address => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "have a maxLength constraint for lineThree" in {
      Form("address" -> address(AddressMappings.CARER)).bind(
        Map("address.lineOne" -> "somevalue","address.lineTwo" -> "somevalue","address.lineThree" -> "CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS")
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo(Mappings.maxLengthError),
        address => "This mapping should not happen." must equalTo("Valid")
      )
    }
  }
  section("unit")
}
