package controllers.s8_other_money

import org.specs2.mutable.{Tags, Specification}
import models.MultiLineAddress

class G4PersonContactDetailsFormSpec extends Specification with Tags {

  "Other Money - Person Contact Details Form" should {

    "map data into case class" in {
      G4PersonContactDetails.form.bind(
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
      G4PersonContactDetails.form.bind(
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
      G4PersonContactDetails.form.bind(
        Map("postcode" -> "INVALID")
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.postcode"),
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }
  } section "unit"
}