package controllers.s3_your_partner

import org.specs2.mutable.Specification
import models.MultiLineAddress

class G2YourPartnerContactDetailsFormSpec extends Specification {

  "Your Partner Contact Details Form" should {

    "map data into case class" in {
      G2YourPartnerContactDetails.form.bind(
        Map("address.lineOne" -> "lineOne", "address.lineTwo" -> "lineTwo", "address.lineThree" -> "lineThree", "postcode" -> "SE1 6EH")
      ).fold(
        formWithErrors => "This mapping should not happen." must equalTo("Error"),
        f => {
          f.address must equalTo(Some(MultiLineAddress(Some("lineOne"), Some("lineTwo"), Some("lineThree"))))
          f.postcode must equalTo(Some("SE1 6EH"))
        }
      )
    }

    "allow optional fields to be left blank" in {
      G2YourPartnerContactDetails.form.bind(
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
      G2YourPartnerContactDetails.form.bind(
        Map("postcode" -> "INVALID")
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.postcode"),
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }

  }

}
