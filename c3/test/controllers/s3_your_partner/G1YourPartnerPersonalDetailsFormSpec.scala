package controllers.s3_your_partner

import org.specs2.mutable.{ Tags, Specification }
import models.{ NationalInsuranceNumber, DayMonthYear }
import scala.Some

class G1YourPartnerPersonalDetailsFormSpec extends Specification with Tags {

  val title = "Mr"
  val firstName = "John"
  val middleName = "Mc"
  val surname = "Doe"
  val otherNames = "Duck"
  val ni1 = "AB"
  val ni2 = 12
  val ni3 = 34
  val ni4 = 56
  val ni5 = "C"
  val dateOfBirthDay = 5
  val dateOfBirthMonth = 12
  val dateOfBirthYear = 1990
  val nationality = "British"
  val separatedFromPartner = "yes"

  "Your Partner Personal Details Form" should {
    "map data into case class" in {
      G1YourPartnerPersonalDetails.form.bind(
        Map("title" -> title,
          "firstName" -> firstName,
          "middleName" -> middleName,
          "surname" -> surname,
          "otherNames" -> otherNames,
          "nationalInsuranceNumber.ni1" -> ni1,
          "nationalInsuranceNumber.ni2" -> ni2.toString,
          "nationalInsuranceNumber.ni3" -> ni3.toString,
          "nationalInsuranceNumber.ni4" -> ni4.toString,
          "nationalInsuranceNumber.ni5" -> ni5,
          "dateOfBirth.day" -> dateOfBirthDay.toString,
          "dateOfBirth.month" -> dateOfBirthMonth.toString,
          "dateOfBirth.year" -> dateOfBirthYear.toString,
          "nationality" -> nationality,
          "separated.fromPartner" -> separatedFromPartner)).fold(
          formWithErrors => "This mapping should not happen." must equalTo("Error"),
          f => {
            f.title must equalTo(title)
            f.firstName must equalTo(firstName)
            f.middleName must equalTo(Some(middleName))
            f.surname must equalTo(surname)
            f.otherSurnames must equalTo(Some(otherNames))
            f.nationalInsuranceNumber must equalTo(Some(NationalInsuranceNumber(Some(ni1), Some(ni2.toString), Some(ni3.toString), Some(ni4.toString), Some(ni5))))
            f.dateOfBirth must equalTo(DayMonthYear(Some(dateOfBirthDay), Some(dateOfBirthMonth), Some(dateOfBirthYear), None, None))
            f.nationality must equalTo(Some(nationality))
            f.separatedFromPartner must equalTo(separatedFromPartner)
          })
    }

    "reject too many characters in text fields" in {
      G1YourPartnerPersonalDetails.form.bind(
        Map("title" -> title,
          "firstName" -> "CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS",
          "middleName" -> "CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS",
          "surname" -> "CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS",
          "otherNames" -> "CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS",
          "dateOfBirth.day" -> dateOfBirthDay.toString,
          "dateOfBirth.month" -> dateOfBirthMonth.toString,
          "dateOfBirth.year" -> dateOfBirthYear.toString,
          "nationality" -> "CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS",
          "separated.fromPartner" -> separatedFromPartner)).fold(
          formWithErrors => {
            formWithErrors.errors.length must equalTo(5)
            formWithErrors.errors(0).message must equalTo("error.maxLength")
            formWithErrors.errors(1).message must equalTo("error.maxLength")
            formWithErrors.errors(2).message must equalTo("error.maxLength")
            formWithErrors.errors(3).message must equalTo("error.maxLength")
            formWithErrors.errors(4).message must equalTo("error.nationality")
          },
          theirPersonalDetails => "This mapping should not happen." must equalTo("Valid"))
    }

    "have 5 mandatory fields" in {
      G1YourPartnerPersonalDetails.form.bind(
        Map("middleName" -> "middle name is optional")).fold(
          formWithErrors => {
            formWithErrors.errors.length must equalTo(5)
            formWithErrors.errors(0).message must equalTo("error.required")
            formWithErrors.errors(1).message must equalTo("error.required")
            formWithErrors.errors(2).message must equalTo("error.required")
            formWithErrors.errors(3).message must equalTo("error.required")
            formWithErrors.errors(4).message must equalTo("error.required")
          },
          theirPersonalDetails => "This mapping should not happen." must equalTo("Valid"))
    }

    "reject invalid national insurance number" in {
      G1YourPartnerPersonalDetails.form.bind(
        Map("title" -> title,
          "firstName" -> firstName,
          "middleName" -> middleName,
          "surname" -> surname,
          "otherNames" -> otherNames,
          "nationalInsuranceNumber.ni1" -> "INVALID",
          "nationalInsuranceNumber.ni2" -> ni2.toString,
          "nationalInsuranceNumber.ni3" -> ni3.toString,
          "nationalInsuranceNumber.ni4" -> ni4.toString,
          "nationalInsuranceNumber.ni5" -> ni5,
          "dateOfBirth.day" -> dateOfBirthDay.toString,
          "dateOfBirth.month" -> dateOfBirthMonth.toString,
          "dateOfBirth.year" -> dateOfBirthYear.toString,
          "nationality" -> nationality,
          "separated.fromPartner" -> separatedFromPartner)).fold(
          formWithErrors => {
            formWithErrors.errors.head.message must equalTo("error.nationalInsuranceNumber")
            formWithErrors.errors.length must equalTo(1)
          },
          f => "This mapping should not happen." must equalTo("Valid"))
    }

    "reject invalid date" in {
      G1YourPartnerPersonalDetails.form.bind(
        Map("title" -> title,
          "firstName" -> firstName,
          "middleName" -> middleName,
          "surname" -> surname,
          "otherNames" -> otherNames,
          "nationalInsuranceNumber.ni1" -> ni1,
          "nationalInsuranceNumber.ni2" -> ni2.toString,
          "nationalInsuranceNumber.ni3" -> ni3.toString,
          "nationalInsuranceNumber.ni4" -> ni4.toString,
          "nationalInsuranceNumber.ni5" -> ni5,
          "dateOfBirth.day" -> dateOfBirthDay.toString,
          "dateOfBirth.month" -> dateOfBirthMonth.toString,
          "dateOfBirth.year" -> "12345",
          "nationality" -> nationality,
          "separated.fromPartner" -> separatedFromPartner)).fold(
          formWithErrors => {
            formWithErrors.errors.head.message must equalTo("error.invalid")
            formWithErrors.errors.length must equalTo(1)
          },
          f => "This mapping should not happen." must equalTo("Valid"))
    }

    "accept nationality with space character, uppercase and lowercase" in {
      G1YourPartnerPersonalDetails.form.bind(
        Map("title" -> title,
          "firstName" -> firstName,
          "middleName" -> middleName,
          "surname" -> surname,
          "otherNames" -> otherNames,
          "nationalInsuranceNumber.ni1" -> ni1.toString,
          "nationalInsuranceNumber.ni2" -> ni2.toString,
          "nationalInsuranceNumber.ni3" -> ni3.toString,
          "nationalInsuranceNumber.ni4" -> ni4.toString,
          "nationalInsuranceNumber.ni5" -> ni5,
          "dateOfBirth.day" -> dateOfBirthDay.toString,
          "dateOfBirth.month" -> dateOfBirthMonth.toString,
          "dateOfBirth.year" -> dateOfBirthYear.toString,
          "nationality" -> "United States",
          "separated.fromPartner" -> separatedFromPartner)).fold(
          formWithErrors => "This mapping should not happen." must equalTo("Error"),
          f => {
            f.nationality must equalTo(Some("United States"))
          })
    }

    "reject invalid nationality with numbers" in {
      G1YourPartnerPersonalDetails.form.bind(
        Map("title" -> title,
          "firstName" -> firstName,
          "middleName" -> middleName,
          "surname" -> surname,
          "otherNames" -> otherNames,
          "nationalInsuranceNumber.ni1" -> ni1.toString,
          "nationalInsuranceNumber.ni2" -> ni2.toString,
          "nationalInsuranceNumber.ni3" -> ni3.toString,
          "nationalInsuranceNumber.ni4" -> ni4.toString,
          "nationalInsuranceNumber.ni5" -> ni5,
          "dateOfBirth.day" -> dateOfBirthDay.toString,
          "dateOfBirth.month" -> dateOfBirthMonth.toString,
          "dateOfBirth.year" -> dateOfBirthYear.toString,
          "nationality" -> "a123456",
          "separated.fromPartner" -> separatedFromPartner)).fold(
          formWithErrors => {
            formWithErrors.errors.length must equalTo(1)
            formWithErrors.errors.head.message must equalTo("error.nationality")
          },
          f => "This mapping should not happen." must equalTo("Valid"))
    }

    "reject invalid nationality with special characters" in {
      G1YourPartnerPersonalDetails.form.bind(
        Map("title" -> title,
          "firstName" -> firstName,
          "middleName" -> middleName,
          "surname" -> surname,
          "otherNames" -> otherNames,
          "nationalInsuranceNumber.ni1" -> ni1.toString,
          "nationalInsuranceNumber.ni2" -> ni2.toString,
          "nationalInsuranceNumber.ni3" -> ni3.toString,
          "nationalInsuranceNumber.ni4" -> ni4.toString,
          "nationalInsuranceNumber.ni5" -> ni5,
          "dateOfBirth.day" -> dateOfBirthDay.toString,
          "dateOfBirth.month" -> dateOfBirthMonth.toString,
          "dateOfBirth.year" -> dateOfBirthYear.toString,
          "nationality" -> "a!@£$%^&*(){}",
          "separated.fromPartner" -> separatedFromPartner)).fold(
          formWithErrors => {
            formWithErrors.errors.length must equalTo(1)
            formWithErrors.errors.head.message must equalTo("error.nationality")
          },
          f => "This mapping should not happen." must equalTo("Valid"))
    }

    "reject first and last names with forbidden characters" in {
      G1YourPartnerPersonalDetails.form.bind(
        Map("title" -> title,
          "firstName" -> "MyNa>me",
          "middleName" -> middleName,
          "surname" -> ";My Surname;",
          "otherNames" -> otherNames,
          "nationalInsuranceNumber.ni1" -> ni1.toString,
          "nationalInsuranceNumber.ni2" -> ni2.toString,
          "nationalInsuranceNumber.ni3" -> ni3.toString,
          "nationalInsuranceNumber.ni4" -> ni4.toString,
          "nationalInsuranceNumber.ni5" -> ni5,
          "dateOfBirth.day" -> dateOfBirthDay.toString,
          "dateOfBirth.month" -> dateOfBirthMonth.toString,
          "dateOfBirth.year" -> dateOfBirthYear.toString,
          "nationality" -> "United States",
          "separated.fromPartner" -> separatedFromPartner)).fold(
        formWithErrors => {
          formWithErrors.errors.length must equalTo(2)
          formWithErrors.errors.head.message must equalTo("error.forbidden.characters")
        },
        f => "This mapping should not happen." must equalTo("Valid"))
    }
  } section ("unit", models.domain.YourPartner.id)
}