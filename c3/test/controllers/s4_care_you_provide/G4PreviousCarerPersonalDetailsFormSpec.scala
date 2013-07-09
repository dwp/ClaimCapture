package controllers.s4_care_you_provide

import org.specs2.mutable.{Tags, Specification}
import models.DayMonthYear
import models.NationalInsuranceNumber

class G4PreviousCarerPersonalDetailsFormSpec extends Specification with Tags {
  val firstName = "John"
  val middleName = "Mc"
  val surname = "Doe"
  val ni1 = "AB"
  val ni2 = 12
  val ni3 = 34
  val ni4 = 56
  val ni5 = "C"
  val dateOfBirthDay = 5
  val dateOfBirthMonth = 12
  val dateOfBirthYear = 1990

  "More About The Person Form" should {

    "map data into case class" in {
      G4PreviousCarerPersonalDetails.form.bind(
        Map("firstName" -> firstName,
          "middleName" -> middleName,
          "surname" -> surname,
          "nationalInsuranceNumber.ni1" -> "AB",
          "nationalInsuranceNumber.ni2" -> "12",
          "nationalInsuranceNumber.ni3" -> "34",
          "nationalInsuranceNumber.ni4" -> "56",
          "nationalInsuranceNumber.ni5" -> "C",
          "dateOfBirth.day" -> dateOfBirthDay.toString,
          "dateOfBirth.month" -> dateOfBirthMonth.toString,
          "dateOfBirth.year" -> dateOfBirthYear.toString)).fold(
        formWithErrors => "This mapping should not happen." must equalTo("Error"),
        f => {
          f.firstName must equalTo(Some(firstName))
          f.middleName must equalTo(Some(middleName))
          f.surname must equalTo(Some(surname))
          f.dateOfBirth must equalTo(Some(DayMonthYear(Some(dateOfBirthDay), Some(dateOfBirthMonth), Some(dateOfBirthYear), None, None)))
          f.nationalInsuranceNumber must equalTo(Some(NationalInsuranceNumber(Some(ni1), Some(ni2.toString), Some(ni3.toString), Some(ni4.toString), Some(ni5))))
        })
    }

    "reject too long first name" in {
      G4PreviousCarerPersonalDetails.form.bind(
        Map("firstName" -> "CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS")).fold(
        formWithErrors => {
          formWithErrors.errors.head.message must equalTo("error.maxLength")
        },
        f => "This mapping should not happen." must equalTo("Valid"))
    }

    "reject too long middle name" in {
      G4PreviousCarerPersonalDetails.form.bind(
        Map("middleName" -> "CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS")).fold(
        formWithErrors => {
          formWithErrors.errors.head.message must equalTo("error.maxLength")
        },
        f => "This mapping should not happen." must equalTo("Valid"))
    }

    "reject too long surname" in {
      G4PreviousCarerPersonalDetails.form.bind(
        Map("surname" -> "CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS")).fold(
        formWithErrors => {
          formWithErrors.errors.head.message must equalTo("error.maxLength")
        },
        f => "This mapping should not happen." must equalTo("Valid"))
    }

    "reject invalid national insurance number" in {
      G4PreviousCarerPersonalDetails.form.bind(
        Map("nationalInsuranceNumber.ni1" -> "INVALID")).fold(
        formWithErrors => {
          formWithErrors.errors.head.message must equalTo("error.nationalInsuranceNumber")
          formWithErrors.errors.length must equalTo(1)
        },
        f => "This mapping should not happen." must equalTo("Valid"))
    }

    "reject invalid date that has characters instead of numbers" in {
      G4PreviousCarerPersonalDetails.form.bind(
        Map("dateOfBirth.day" -> "INVALID")).fold(
        formWithErrors => {
          formWithErrors.errors.head.message must equalTo("error.number")
          formWithErrors.errors.length must equalTo(1)
        },
        f => "This mapping should not happen." must equalTo("Valid"))
    }
    
    "reject invalid date that has year higher than maximum allowed" in {
      G4PreviousCarerPersonalDetails.form.bind(
        Map("dateOfBirth.day" -> dateOfBirthDay.toString,
          "dateOfBirth.month" -> dateOfBirthMonth.toString,
          "dateOfBirth.year" -> "123456789")).fold(
        formWithErrors => {
          formWithErrors.errors.head.message must equalTo("error.invalid")
          formWithErrors.errors.length must equalTo(1)
        },
        f => "This mapping should not happen." must equalTo("Valid"))
    }
  } section "unit"
}