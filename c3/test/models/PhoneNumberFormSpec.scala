package models

import org.specs2.mutable.Specification
import play.api.data.Form
import play.api.data.Forms._
import controllers.mappings.Mappings._

class PhoneNumberFormSpec extends Specification {

  "Phone Number" should {

    "accept valid input " in {
      val validPhoneNumber = "02076541058"

      createPhoneNumberForm(validPhoneNumber).fold(
        formWithErrors => "The mapping should not fail." must equalTo("Error"),
        phoneNumber => phoneNumber must equalTo(Some(validPhoneNumber))
      )
    }

    "accept number with a dash (-)" in {
      val validPhoneNumber = "020-76541058"

      createPhoneNumberForm(validPhoneNumber).fold(
        formWithErrors => "The mapping should not fail." must equalTo("Error"),
        phoneNumber => phoneNumber must equalTo(Some(validPhoneNumber))
      )
    }

    "accept number with spaces" in {
      val validPhoneNumber = "020 765 410 58"

      createPhoneNumberForm(validPhoneNumber).fold(
        formWithErrors => "The mapping should not fail." must equalTo("Error"),
        phoneNumber => phoneNumber must equalTo(Some(validPhoneNumber))
      )
    }

    "reject characters" in {
      val validPhoneNumber = "abcdefgh"

      createPhoneNumberForm(validPhoneNumber).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo(errorInvalid),
        phoneNumber => "The mapping should not happen." must equalTo("Error")
      )
    }
  }

  private def createPhoneNumberForm(phoneNumber: String)
    = Form("phoneNumber" -> optional(play.api.data.Forms.text verifying validPhoneNumber)).bind(Map("phoneNumber" -> phoneNumber))
}