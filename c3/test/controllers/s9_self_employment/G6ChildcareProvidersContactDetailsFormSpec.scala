package controllers.s9_self_employment

import org.specs2.mutable.{Tags, Specification}
import models.MultiLineAddress

class G6ChildcareProvidersContactDetailsFormSpec extends Specification with Tags {

  "Childcare provider's contact Details Form" should {
    val addressLineOne = "lineOne"
    val addressLineTwo = "lineTwo"
    val addressLineThree = "lineThree"
    val postcode = "SE1 6EH"
      
    "map data into case class" in {
      G6ChildcareProvidersContactDetails.form.bind(
        Map("address.lineOne" -> addressLineOne, 
            "address.lineTwo" -> addressLineTwo, 
            "address.lineThree" -> addressLineThree, 
            "postcode" -> postcode)
      ).fold(
        formWithErrors => "This mapping should not happen." must equalTo("Error"),
        f => {
          f.address must equalTo(Some(MultiLineAddress(Some(addressLineOne), Some(addressLineTwo), Some(addressLineThree))))
          f.postcode must equalTo(Some(postcode))
        }
      )
    }

    "allow optional fields to be left blank" in {
      G6ChildcareProvidersContactDetails.form.bind(
        Map("address.lineOne" -> "", "postcode" -> "")
      ).fold(
        formWithErrors => "This mapping should not happen." must equalTo("Error"),
        f => {
          f.address must equalTo(None)
          f.postcode must equalTo(None)
        }
      )
    }

    "reject an invalid postcode" in {
      G6ChildcareProvidersContactDetails.form.bind(
        Map("postcode" -> "INVALID")
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.postcode"),
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }
  } section "unit"

}
