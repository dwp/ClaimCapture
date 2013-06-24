package integration.s4_care_you_provide

import org.specs2.mutable.Specification
import controllers.CareYouProvide
import models.Postcode

class G2TheirContactDetailsFormSpec extends Specification {
  val validPostcode: String = "SE1 6EH"
  
  "Their Contact Details Form" should {

    "map data into case class" in {
      CareYouProvide.theirContactDetailsForm.bind(
        Map("address.lineOne" -> "lineOne", "address.lineTwo" -> "lineTwo", "address.lineThree" -> "lineThree", "postcode" -> validPostcode, "phoneNumber" -> "020-76542059")
      ).fold(
        formWithErrors => "This mapping should not happen." must equalTo("Error"),
        theirContactDetails => {
          theirContactDetails.address.lineOne must equalTo(Some("lineOne"))
          theirContactDetails.address.lineTwo must equalTo(Some("lineTwo"))
          theirContactDetails.address.lineThree must equalTo(Some("lineThree"))
          theirContactDetails.postcode must equalTo(Some(validPostcode))
          theirContactDetails.phoneNumber must equalTo(Some("020-76542059"))
        }
      )
    }

    "have a mandatory address" in {
      CareYouProvide.theirContactDetailsForm.bind(
        Map("address.lineOne" -> "", "address.lineTwo" -> "", "address.lineThree" -> "", "postcode" -> "", "phoneNumber" -> "")
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.required"),
        theirContactDetails => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "reject an invalid postcode" in {
      CareYouProvide.theirContactDetailsForm.bind(
        Map("address.lineOne" -> "lineOne", "address.lineTwo" -> "", "address.lineThree" -> "", "postcode" -> "1234567890")
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.postcode"),
        theirContactDetails => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "reject an invalid phone number" in {
      CareYouProvide.theirContactDetailsForm.bind(
        Map("address.lineOne" -> "lineOne", "address.lineTwo" -> "", "address.lineThree" -> "", "phoneNumber" -> "abcdef")
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.invalid"),
        theirContactDetails => "This mapping should not happen." must equalTo("Valid")
      )
    }
  }

}
