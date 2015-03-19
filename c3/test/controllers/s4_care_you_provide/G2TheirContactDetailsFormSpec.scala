package controllers.s4_care_you_provide

import controllers.mappings.Mappings
import org.specs2.mutable.{ Tags, Specification }

class G2TheirContactDetailsFormSpec extends Specification with Tags {
  val validPostcode: String = "SE1 6EH"

  "Their Contact Details Form" should {

    "map data into case class" in {
      G2TheirContactDetails.form.bind(
        Map("address.lineOne" -> "lineOne",
          "address.lineTwo" -> "lineTwo",
          "address.lineThree" -> "lineThree",
          "postcode" -> validPostcode)).fold(
          formWithErrors => "This mapping should not happen." must equalTo("Error"),
          theirContactDetails => {
            theirContactDetails.address.lineOne must equalTo(Some("lineOne"))
            theirContactDetails.address.lineTwo must equalTo(Some("lineTwo"))
            theirContactDetails.address.lineThree must equalTo(Some("lineThree"))
            theirContactDetails.postcode must equalTo(Some(validPostcode))
          })
    }

    "have a mandatory address" in {
      G2TheirContactDetails.form.bind(
        Map("address.lineOne" -> "", "address.lineTwo" -> "", "address.lineThree" -> "", "postcode" -> "")).fold(
          formWithErrors => formWithErrors.errors.head.message must equalTo(Mappings.errorRequired),
          theirContactDetails => "This mapping should not happen." must equalTo("Valid"))
    }

    "reject an invalid postcode" in {
      G2TheirContactDetails.form.bind(
        Map("address.lineOne" -> "lineOne", "address.lineTwo" -> "lineTwo", "address.lineThree" -> "", "postcode" -> "INVALID")).fold(
          formWithErrors => formWithErrors.errors.head.message must equalTo("error.postcode"),
          theirContactDetails => "This mapping should not happen." must equalTo("Valid"))
    }

    "reject an invalid address with empty second line" in {
      G2TheirContactDetails.form.bind(
        Map("address.lineOne" -> "lineOne", "address.lineTwo" -> "", "address.lineThree" -> "", "postcode" -> "")).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.addressLines.required"),
        theirContactDetails => "This mapping should not happen." must equalTo("Valid"))
    }

  } section ("unit", models.domain.CareYouProvide.id)
}