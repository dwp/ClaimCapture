package integration.s4_careYouProvide

import org.specs2.mutable.Specification
import controllers.CareYouProvide

class G2TheirContactDetailsFormSpec extends Specification {

  "Their Contact Details Form" should {

    "map data into case class" in {
      CareYouProvide.theirContactDetailsForm.bind(
        Map("address.lineOne" -> "lineOne", "address.lineTwo" -> "lineTwo", "address.lineThree" -> "lineThree", "postcode" -> "SE1 6EH", "phoneNumber" -> "020-76542059")
      ).fold(
        formWithErrors => "This mapping should not happen." must equalTo("Error"),
        theirContactDetails => {
          theirContactDetails.address.lineOne must equalTo(Some("lineOne"))
          theirContactDetails.address.lineTwo must equalTo(Some("lineTwo"))
          theirContactDetails.address.lineThree must equalTo(Some("lineThree"))
          theirContactDetails.postcode must equalTo(Some("SE1 6EH"))
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
        Map("address.lineOne" -> "lineOne", "address.lineTwo" -> "", "address.lineThree" -> "", "postcode" -> "SE 6EH")
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
