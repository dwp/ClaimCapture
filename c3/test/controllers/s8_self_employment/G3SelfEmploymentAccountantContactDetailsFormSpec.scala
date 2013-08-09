package controllers.s8_self_employment

import org.specs2.mutable.{Tags, Specification}

class G3SelfEmploymentAccountantContactDetailsFormSpec extends Specification with Tags {

  "About Self Employment - Accountant's contact details Form" should {

    "map data into case class" in {
      G3SelfEmploymentAccountantContactDetails.form.bind(
        Map("accountantsName" -> "Hello123",
          "address.lineOne" -> "lineOne",
          "address.lineTwo" -> "lineTwo",
          "address.lineThree" -> "lineThree",
          "postCode" -> "EC1A 4JQ",
          "telephoneNumber" -> "111111",
          "faxNumber" -> "222222")
      ).fold(
        formWithErrors => "This mapping should not happen." must equalTo("Error"),
        f => {
          f.accountantsName must equalTo("Hello123")
        }
      )
    }

    "reject if accountantsName is not filled" in {
      G3SelfEmploymentAccountantContactDetails.form.bind(
        Map("address.lineOne" -> "lineOne",
          "address.lineTwo" -> "lineTwo",
          "address.lineThree" -> "lineThree")
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.required"),
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "reject if address is not filled" in {
      G3SelfEmploymentAccountantContactDetails.form.bind(
        Map("accountantsName" -> "Hello123")
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.missingLineOne"),
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "Accountant Contact Details - Allow optional fields to be left blank" in {
      G3SelfEmploymentAccountantContactDetails.form.bind(
        Map("accountantsName" -> "Hello123",
          "address.lineOne" -> "lineOne",
          "address.lineTwo" -> "lineTwo",
          "address.lineThree" -> "lineThree")
      ).fold(
        formWithErrors => "This mapping should not happen." must equalTo("Error"),
        f => {
          f.accountantsName must equalTo("Hello123")
        }
      )
    }
  } section("unit", models.domain.SelfEmployment.id)
}