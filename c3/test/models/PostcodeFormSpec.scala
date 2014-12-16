package models

import org.specs2.mutable.Specification
import play.api.data.Form
import play.api.data.Forms._
import controllers.mappings.Mappings._

class PostcodeFormSpec extends Specification {

  "Postcode" should {

    "accept valid input with space" in {
      val validPostcode = "RM111DA"

      createPostcodeForm(validPostcode).fold(
        formWithErrors => {
          "The mapping should not fail." must equalTo("Error")
        }, {
          postcode =>
            postcode must equalTo(Some(validPostcode))
        })
    }

    "accept valid input" in {
      val validPostcodeWithSpace = "RM11 1DA"

      createPostcodeForm(validPostcodeWithSpace).fold(
        formWithErrors => {
          "The mapping should not fail." must equalTo("Error")
        }, {
          postcode =>
            postcode must equalTo(Some(validPostcodeWithSpace))
        })
    }

    "accept valid input in lowercase" in {
      val validPostcodeLowercase = "rm11 1da"

      createPostcodeForm(validPostcodeLowercase).fold(
        formWithErrors => {
          "The mapping should not fail." must equalTo("Error")
        }, {
          postcode =>
            postcode must equalTo(Some(validPostcodeLowercase))
        })
    }

    "reject invalid input" in {
      createPostcodeForm("1111 111").fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.postcode"),
        postcode => "This mapping should not happen." must equalTo("Valid"))
    }

    "reject input that is too short" in {
      createPostcodeForm("RM11").fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.postcode"),
        postcode => "This mapping should not happen." must equalTo("Valid"))
    }

    "reject input that is too long" in {
      createPostcodeForm("RM11 1DAAAAAAAA").fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.postcode"),
        postcode => "This mapping should not happen." must equalTo("Valid"))
    }

    "reject input that fails regex" in {
      createPostcodeForm("LE2 OAJ").fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.postcode"),
        postcode => "This mapping should not happen." must equalTo("Valid"))
    }
  }

  private def createPostcodeForm(postcode: String)
    = Form("postcode" -> optional(play.api.data.Forms.text verifying validPostcode)).bind(Map("postcode" -> postcode))
}