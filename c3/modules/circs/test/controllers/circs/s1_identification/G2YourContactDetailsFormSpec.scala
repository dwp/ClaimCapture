package controllers.circs.s1_identification

import org.specs2.mutable.{Tags, Specification}


class G2YourContactDetailsFormSpec extends Specification with Tags {

  val addressLineOne = "one"
  val addressLineTwo = "two"
  val addressLineThree = "three"

  val postCode = "PR2 8AE"
  val phoneNumber = "1234466"
  val mobileNumber = "444444"

  "Change of circumstances - Your contact details Form" should {

    "map data into case class" in {
      G2YourContactDetails.form.bind(
        Map("address.street.lineOne" -> addressLineOne,
          "address.town.lineTwo" -> addressLineTwo,
          "address.town.lineThree" -> addressLineThree,
          "postcode" -> postCode,
          "phoneNumber" -> phoneNumber,
          "mobileNumber" -> mobileNumber
        )
      ).fold(
        formWithErrors => "This mapping should not happen." must equalTo("Error"),
        f => {
          f.address.street.lineOne must equalTo(Some("one"))
        }
      )
    }

    "have 1 mandatory field" in {
      G2YourContactDetails.form.bind(
        Map("phoneNumber" -> phoneNumber)).fold(
        formWithErrors => {
          formWithErrors.errors.length must equalTo(1)
          formWithErrors.errors(0).message must equalTo("error.required")
        },
        f => "This mapping should not happen." must equalTo("Valid"))
    }

    "reject invalid postcode" in {
      G2YourContactDetails.form.bind(
        Map("address.street.lineOne" -> addressLineOne, "postcode" -> "e1234")).fold(
        formWithErrors => {
          formWithErrors.errors.length must equalTo(1)
          formWithErrors.errors(0).message must equalTo("error.postcode")
        },
        f => "This mapping should not happen." must equalTo("Valid"))
    }

    "reject invalid phonenumber" in {
      G2YourContactDetails.form.bind(
        Map("address.street.lineOne" -> addressLineOne, "phoneNumber" -> "123abc")).fold(
        formWithErrors => {
          formWithErrors.errors.length must equalTo(1)
          formWithErrors.errors(0).message must equalTo("error.invalid")
        },
        f => "This mapping should not happen." must equalTo("Valid"))
    }

    "reject invalid mobileNumber" in {
      G2YourContactDetails.form.bind(
        Map("address.street.lineOne" -> addressLineOne, "mobileNumber" -> "123def")).fold(
        formWithErrors => {
          formWithErrors.errors.length must equalTo(1)
          formWithErrors.errors(0).message must equalTo("error.invalid")
        },
        f => "This mapping should not happen." must equalTo("Valid"))
    }

  } section("unit", models.domain.CircumstancesYourContactDetails.id)
}
