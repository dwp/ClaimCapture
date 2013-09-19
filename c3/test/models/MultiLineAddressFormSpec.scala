package models

import org.specs2.mutable.Specification
import play.api.data.Form
import controllers.Mappings._

class MultiLineAddressFormSpec extends Specification {

  "Multi Line Address" should {

    "reject empty input" in {
      Form("address" -> address).bind(Map("address.street.lineOne" -> "", "address.town.lineTwo" -> "", "address.town.lineThree" -> "")).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.required"),
        address => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "accept single lineOne" in {
      Form("address" -> address).bind(Map("address.street.lineOne" -> "line1", "address.town.lineTwo" -> "", "address.town.lineThree" -> "")).fold(
        formWithErrors => "The mapping should not fail." must equalTo("Error"),
        address => address must equalTo(MultiLineAddress(Street(Some("line1"))))
      )
    }

    "reject single lineTwo" in {
      Form("address" -> address).bind(Map("address.street.lineOne" -> "", "address.town.lineTwo" -> "line2", "address.town.lineThree" -> "")).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.required"),
        address => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "reject single lineThree" in {
      Form("address" -> address).bind(Map("address.street.lineOne" -> "", "address.town.lineTwo" -> "", "address.town.lineThree" -> "line3")).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.required"),
        address => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "have a maxLength constraint for lineOne" in {
      Form("address" -> address).bind(
        Map("address.street.lineOne" -> "CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS")
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.maxLength"),
        address => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "have a maxLength constraint for lineTwo" in {
      Form("address" -> address).bind(
        Map("address.street.lineOne" -> "somevalue","address.town.lineTwo" -> "CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS")
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.maxLength"),
        address => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "have a maxLength constraint for lineThree" in {
      Form("address" -> address).bind(
        Map("address.street.lineOne" -> "somevalue","address.town.lineThree" -> "CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS")
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.maxLength"),
        address => "This mapping should not happen." must equalTo("Valid")
      )
    }
  }
}