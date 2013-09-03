package controllers.circs.s1_identification

import org.specs2.mutable.{Tags, Specification}


class G3DetailsOfThePersonYouCareForFormSpec extends Specification with Tags {

  "Change of circumstances - About You Form" should {

    val title = "Mr"
    val firstName = "John"
    val middelName = ""
    val lastName = "Smith"
    val ni1 = "AB"
    val ni2 = 12
    val ni3 = 34
    val ni4 = 56
    val ni5 = "C"

    val dateOfBirthDay = 5
    val dateOfBirthMonth = 12
    val dateOfBirthYear = 1990

    "map data into case class" in {
      G3DetailsOfThePersonYouCareFor.form.bind(
        Map("title" -> title,
          "firstName" -> firstName,
          "middleName" -> middelName,
          "lastName" -> lastName,
          "nationalInsuranceNumber.ni1" -> ni1,
          "nationalInsuranceNumber.ni2" -> ni2.toString,
          "nationalInsuranceNumber.ni3" -> ni3.toString,
          "nationalInsuranceNumber.ni4" -> ni4.toString,
          "nationalInsuranceNumber.ni5" -> ni5,
          "dateOfBirth.day" -> dateOfBirthDay.toString,
          "dateOfBirth.month" -> dateOfBirthMonth.toString,
          "dateOfBirth.year" -> dateOfBirthYear.toString
        )
      ).fold(
        formWithErrors => "This mapping should not happen." must equalTo("Error"),
        f => {
          f.firstName must equalTo("John")
        }
      )
    }

    "reject too many characters in text fields" in {
      G3DetailsOfThePersonYouCareFor.form.bind(
        Map("title" -> title,
          "firstName" -> "CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS",
          "middleName" -> "CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS",
          "lastName" -> "CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS",
          "nationalInsuranceNumber.ni1" -> ni1,
          "nationalInsuranceNumber.ni2" -> ni2.toString,
          "nationalInsuranceNumber.ni3" -> ni3.toString,
          "nationalInsuranceNumber.ni4" -> ni4.toString,
          "nationalInsuranceNumber.ni5" -> ni5,
          "dateOfBirth.day" -> dateOfBirthDay.toString,
          "dateOfBirth.month" -> dateOfBirthMonth.toString,
          "dateOfBirth.year" -> dateOfBirthYear.toString)).fold(
        formWithErrors => {
          formWithErrors.errors.length must equalTo(3)
          formWithErrors.errors(0).message must equalTo("error.maxLength")
          formWithErrors.errors(1).message must equalTo("error.maxLength")
          formWithErrors.errors(2).message must equalTo("error.maxLength")
        },
        f => "This mapping should not happen." must equalTo("Valid"))
    }

    "have 6 mandatory fields" in {
      G3DetailsOfThePersonYouCareFor.form.bind(
        Map("middleName" -> "middle name is optional")).fold(
        formWithErrors => {
          formWithErrors.errors.length must equalTo(6)
          formWithErrors.errors(0).message must equalTo("error.required")
          formWithErrors.errors(1).message must equalTo("error.required")
          formWithErrors.errors(2).message must equalTo("error.required")
          formWithErrors.errors(3).message must equalTo("error.required")
          formWithErrors.errors(4).message must equalTo("error.nationalInsuranceNumber")
          formWithErrors.errors(5).message must equalTo("error.required")
        },
        f => "This mapping should not happen." must equalTo("Valid"))
    }

    "reject invalid national insurance number" in {
      G3DetailsOfThePersonYouCareFor.form.bind(
        Map("title" -> title,
          "firstName" -> firstName,
          "middleName" -> middelName,
          "lastName" -> lastName,
          "nationalInsuranceNumber.ni1" -> "INVALID",
          "nationalInsuranceNumber.ni2" -> ni2.toString,
          "nationalInsuranceNumber.ni3" -> ni3.toString,
          "nationalInsuranceNumber.ni4" -> ni4.toString,
          "nationalInsuranceNumber.ni5" -> ni5,
          "dateOfBirth.day" -> dateOfBirthDay.toString,
          "dateOfBirth.month" -> dateOfBirthMonth.toString,
          "dateOfBirth.year" -> dateOfBirthYear.toString
        )).fold(
        formWithErrors => {
          formWithErrors.errors.length must equalTo(1)
          formWithErrors.errors.head.message must equalTo("error.nationalInsuranceNumber")
        },
        f => "This mapping should not happen." must equalTo("Valid"))
    }

    "reject invalid date" in {
      G3DetailsOfThePersonYouCareFor.form.bind(
        Map("title" -> title,
          "firstName" -> firstName,
          "middleName" -> middelName,
          "lastName" -> lastName,
          "nationalInsuranceNumber.ni1" -> ni1.toString,
          "nationalInsuranceNumber.ni2" -> ni2.toString,
          "nationalInsuranceNumber.ni3" -> ni3.toString,
          "nationalInsuranceNumber.ni4" -> ni4.toString,
          "nationalInsuranceNumber.ni5" -> ni5,
          "dateOfBirth.day" -> dateOfBirthDay.toString,
          "dateOfBirth.month" -> dateOfBirthMonth.toString,
          "dateOfBirth.year" -> "12345"
        )).fold(
        formWithErrors => {
          formWithErrors.errors.length must equalTo(1)
          formWithErrors.errors.head.message must equalTo("error.invalid")
        },
        f => "This mapping should not happen." must equalTo("Valid"))
    }
  } section ("unit", models.domain.DetailsOfThePersonYouCareFor.id)
}
