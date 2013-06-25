package forms.s4_care_you_provide

import org.specs2.mutable.Specification
import forms.CareYouProvide.previousCarerContactDetailsForm

class G5PreviousCarerContactDetailsFormSpec extends Specification {
  val validPostcode: String = "SE1 6EH"
  
  "Previous Carer Contact Details Form" should {

    "map data into case class" in {
      previousCarerContactDetailsForm.bind(
        Map("address.lineOne" -> "lineOne", 
            "address.lineTwo" -> "lineTwo", 
            "address.lineThree" -> "lineThree", 
            "postcode" -> validPostcode, 
            "phoneNumber" -> "020-76542059",
            "mobileNumber" -> "020-76542059")
      ).fold(
        formWithErrors => "This mapping should not happen." must equalTo("Error"),
        f => {
          f.address.get.lineOne must equalTo(Some("lineOne"))
          f.address.get.lineTwo must equalTo(Some("lineTwo"))
          f.address.get.lineThree must equalTo(Some("lineThree"))
          f.postcode must equalTo(Some(validPostcode))
          f.phoneNumber must equalTo(Some("020-76542059"))
          f.mobileNumber must equalTo(Some("020-76542059"))
        }
      )
    }

    "allow optional fields to be left blank" in {
      previousCarerContactDetailsForm.bind(
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
      previousCarerContactDetailsForm.bind(
        Map("postcode" -> "INVALID")
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.postcode"),
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "reject an invalid phone number" in {
      previousCarerContactDetailsForm.bind(
        Map("phoneNumber" -> "INVALID")
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.invalid"),
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }
    
    "reject an invalid mobile number" in {
      previousCarerContactDetailsForm.bind(
        Map("mobileNumber" -> "INVALID")
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.invalid"),
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }
  }

}
