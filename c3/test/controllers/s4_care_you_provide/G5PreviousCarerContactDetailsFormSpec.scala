package controllers.s4_care_you_provide

import org.specs2.mutable.{Tags, Specification}


class G5PreviousCarerContactDetailsFormSpec extends Specification with Tags {
  val addressLineOne = "123 Street"
  val postcode: String = "SE1 6EH"
  val phoneNumber = "02076541058"
  val mobileNumber = "01818118181"

  "Previous Carer Contact Details Form" should {

    "map data into case class" in {
      G5PreviousCarerContactDetails.form.bind(
        Map("address.lineOne" -> addressLineOne,
          "address.lineTwo" -> "lineTwo",
          "address.lineThree" -> "lineThree",
          "postcode" -> postcode,
          "phoneNumber" -> phoneNumber,
          "mobileNumber" -> mobileNumber)
      ).fold(
        formWithErrors => "This mapping should not happen." must equalTo("Error"),
        f => {
          f.address.get.lineOne must equalTo(Some(addressLineOne))
          f.address.get.lineTwo must equalTo(Some("lineTwo"))
          f.address.get.lineThree must equalTo(Some("lineThree"))
          f.postcode must equalTo(Some(postcode))
          f.phoneNumber must equalTo(Some(phoneNumber))
          f.mobileNumber must equalTo(Some(mobileNumber))
        }
      )
    }

    "allow optional fields to be left blank" in {
      G5PreviousCarerContactDetails.form.bind(
        Map("address.lineOne" -> "")
      ).fold(
        formWithErrors => "This mapping should not happen." must equalTo("Error"),
        f => {
          f.address must equalTo(None)
          f.postcode must equalTo(None)
          f.phoneNumber must equalTo(None)
          f.mobileNumber must equalTo(None)
        }
      )
    }

    "reject an invalid postcode" in {
      G5PreviousCarerContactDetails.form.bind(
        Map("postcode" -> "INVALID")
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.postcode"),
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "reject an invalid phone number" in {
      G5PreviousCarerContactDetails.form.bind(
        Map("phoneNumber" -> "INVALID")
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.invalid"),
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "reject an invalid mobile number" in {
      G5PreviousCarerContactDetails.form.bind(
        Map("mobileNumber" -> "INVALID")
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.invalid"),
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }
  }  section "unit"

}
