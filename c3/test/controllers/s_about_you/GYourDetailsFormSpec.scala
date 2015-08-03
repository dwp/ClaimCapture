package controllers.s_about_you

import controllers.mappings.Mappings
import org.specs2.mutable.{ Tags, Specification }
import models.DayMonthYear
import models.NationalInsuranceNumber

class GYourDetailsFormSpec extends Specification with Tags {
  "Your Details Form" should {
    val title = "Mr"
    val firstName = "John"
    val middleName = "Mc"
    val surname = "Doe"
    val nino = "AB123456C"
    val nationality = "British"
    val dateOfBirthDay = 5
    val dateOfBirthMonth = 12
    val dateOfBirthYear = 1990
    val alwaysLivedUK = "yes"

    "map data into case class" in {
      GYourDetails.form.bind(
        Map("title" -> title,
          "firstName" -> firstName,
          "middleName" -> middleName,
          "surname" -> surname,
          "nationalInsuranceNumber.nino" -> nino,
          "nationality" -> nationality,
          "dateOfBirth.day" -> dateOfBirthDay.toString,
          "dateOfBirth.month" -> dateOfBirthMonth.toString,
          "dateOfBirth.year" -> dateOfBirthYear.toString,
          "alwaysLivedUK" -> alwaysLivedUK
          )).fold(
          formWithErrors => "This mapping should not happen." must equalTo("Error"),
          f => {
            f.title must equalTo(title)
            f.firstName must equalTo(firstName)
            f.middleName must equalTo(Some(middleName))
            f.surname must equalTo(surname)
            f.nationalInsuranceNumber must equalTo(NationalInsuranceNumber(Some(nino)))
            f.dateOfBirth must equalTo(DayMonthYear(Some(dateOfBirthDay), Some(dateOfBirthMonth), Some(dateOfBirthYear), None, None))
          })
    }

    "reject too many characters in text fields" in {
      GYourDetails.form.bind(
        Map("title" -> title,
          "firstName" -> "HARACTERS,CHARACTE",
          "middleName" -> "HARACTERS,CHARACTE",
          "surname" -> "CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS",
          "nationalInsuranceNumber.nino" -> nino,
          "nationality" -> "CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS",
          "dateOfBirth.day" -> dateOfBirthDay.toString,
          "dateOfBirth.month" -> dateOfBirthMonth.toString,
          "dateOfBirth.year" -> dateOfBirthYear.toString,
          "alwaysLivedUK" -> alwaysLivedUK)).fold(
          formWithErrors => {
            formWithErrors.errors.length must equalTo(3)
            formWithErrors.errors(0).message must equalTo(Mappings.maxLengthError)
            formWithErrors.errors(1).message must equalTo(Mappings.maxLengthError)
            formWithErrors.errors(2).message must equalTo(Mappings.maxLengthError)
          },
          f => "This mapping should not happen." must equalTo("Valid"))
    }

    "reject special characters in text fields" in {
      GYourDetails.form.bind(
        Map("title" -> title,
          "firstName" -> "kk~",
          "middleName" -> "<>",
          "surname" -> "éugene[]",
          "nationalInsuranceNumber.nino" -> nino,
          "nationality" -> "€",
          "dateOfBirth.day" -> dateOfBirthDay.toString,
          "dateOfBirth.month" -> dateOfBirthMonth.toString,
          "dateOfBirth.year" -> dateOfBirthYear.toString,
          "alwaysLivedUK" -> alwaysLivedUK)).fold(
          formWithErrors => {
            formWithErrors.errors.length must equalTo(3)
            formWithErrors.errors(0).message must equalTo(Mappings.errorRestrictedCharacters)
            formWithErrors.errors(1).message must equalTo(Mappings.errorRestrictedCharacters)
            formWithErrors.errors(2).message must equalTo(Mappings.errorRestrictedCharacters)
          },
          f => "This mapping should not happen." must equalTo("Valid"))
    }

    "have 6 mandatory fields" in {
      GYourDetails.form.bind(
        Map("middleName" -> "middle optional")).fold(
          formWithErrors => {
            formWithErrors.errors.length must equalTo(6)
            formWithErrors.errors(0).message must equalTo(Mappings.errorRequired)
            formWithErrors.errors(1).message must equalTo(Mappings.errorRequired)
            formWithErrors.errors(2).message must equalTo(Mappings.errorRequired)
            formWithErrors.errors(3).message must equalTo(Mappings.errorRequired)
            formWithErrors.errors(4).message must equalTo("error.nationalInsuranceNumber")
            formWithErrors.errors(5).message must equalTo(Mappings.errorRequired)
          },
          f => "This mapping should not happen." must equalTo("Valid"))
    }

    "reject invalid national insurance number" in {
      GYourDetails.form.bind(
        Map("title" -> title,
          "firstName" -> firstName,
          "middleName" -> middleName,
          "surname" -> surname,
          "nationalInsuranceNumber.nino" -> "INVALID",
          "nationality" -> nationality,
          "dateOfBirth.day" -> dateOfBirthDay.toString,
          "dateOfBirth.month" -> dateOfBirthMonth.toString,
          "dateOfBirth.year" -> dateOfBirthYear.toString,
          "alwaysLivedUK" -> alwaysLivedUK)).fold(
          formWithErrors => {
            formWithErrors.errors.length must equalTo(1)
            formWithErrors.errors.head.message must equalTo("error.nationalInsuranceNumber")
          },
          f => "This mapping should not happen." must equalTo("Valid"))
    }

    "reject invalid date" in {
      GYourDetails.form.bind(
        Map("title" -> title,
          "firstName" -> firstName,
          "middleName" -> middleName,
          "surname" -> surname,
          "nationalInsuranceNumber.nino" -> nino.toString,
          "nationality" -> nationality,
          "dateOfBirth.day" -> dateOfBirthDay.toString,
          "dateOfBirth.month" -> dateOfBirthMonth.toString,
          "dateOfBirth.year" -> "12345",
          "alwaysLivedUK" -> alwaysLivedUK)).fold(
          formWithErrors => {
            formWithErrors.errors.length must equalTo(1)
            formWithErrors.errors.head.message must equalTo(Mappings.errorInvalid)
          },
          f => "This mapping should not happen." must equalTo("Valid"))
    }
  } section ("unit", models.domain.YourDetails.id)
}