package controllers.s_about_you

import utils.WithApplication
import controllers.mappings.Mappings
import org.specs2.mutable._

class GContactDetailsFormSpec extends Specification {
  section ("unit", models.domain.ContactDetails.id)
  "Contact Details Form" should {
    val addressLineOne = "101 Clifton Street"
    val addressLineTwo = "Blackpool"
    val postcode = "FY1 2RW"
    val howWeContactYou = "01772888901"
    val wantsEmailContact = Mappings.no

    "map data into case class" in new WithApplication {
      GContactDetails.form.bind(
        Map(
          "address.lineOne" -> addressLineOne,
          "address.lineTwo" -> addressLineTwo,
          "postcode" -> postcode,
          "howWeContactYou" -> howWeContactYou,
          "wantsEmailContact" -> wantsEmailContact
          )).fold(
          formWithErrors => "This mapping should not happen." must equalTo("Error"),
          f => {
            f.address.lineOne must equalTo(Some(addressLineOne))
            f.address.lineTwo must equalTo(Some(addressLineTwo))
            f.postcode must equalTo(Some(postcode))
            f.howWeContactYou must equalTo(Some(howWeContactYou))
          })
    }

    "reject too many digits in contact number field" in new WithApplication {
      GContactDetails.form.bind(
        Map("address.lineOne" -> addressLineOne,
          "address.lineTwo" -> addressLineTwo,
          "postcode" -> postcode,
          "howWeContactYou" -> "012345678901234567890",
          "wantsEmailContact" -> wantsEmailContact)).fold(
          formWithErrors => {
            formWithErrors.errors.length must equalTo(1)
            formWithErrors.errors(0).message must equalTo(Mappings.errorInvalid)
          },
          f => "This mapping should not happen." must equalTo("Valid"))
    }

    "have 1 mandatory fields" in new WithApplication {
      GContactDetails.form.bind(
        Map("howWeContactYou" -> "01234567890")).fold(
        formWithErrors => {
          formWithErrors.errors.length must equalTo(1)
          formWithErrors.errors(0).message must equalTo(Mappings.errorRequired)
        },
        f => "This mapping should not happen." must equalTo("Valid"))
    }
  }
  section ("unit", models.domain.ContactDetails.id)
}
