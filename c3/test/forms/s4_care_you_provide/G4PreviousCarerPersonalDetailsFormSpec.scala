package forms.s4_care_you_provide

import org.specs2.mutable.Specification
import forms.CareYouProvide
import models.DayMonthYear
import models.NationalInsuranceNumber

class G4PreviousCarerPersonalDetailsFormSpec extends Specification {

  "More About The Person Form" should {

    "map data into case class" in {
      CareYouProvide.previousCarerPersonalDetailsForm.bind(
        Map("title" -> "Mr",
          "firstName" -> "Ronald",
          "middleName" -> "Mc",
          "surname" -> "Donald",
          "nationalInsuranceNumber.ni1" -> "AB",
          "nationalInsuranceNumber.ni2" -> "12",
          "nationalInsuranceNumber.ni3" -> "34",
          "nationalInsuranceNumber.ni4" -> "56",
          "nationalInsuranceNumber.ni5" -> "C",
          "dateOfBirth.day" -> "3",
          "dateOfBirth.month" -> "4",
          "dateOfBirth.year" -> "1980",
          "liveAtSameAddress" -> "yes")).fold(
          formWithErrors => "This mapping should not happen." must equalTo("Error"),
          f => {
            f.firstName must equalTo(Some("Ronald"))
            f.middleName must equalTo(Some("Mc"))
            f.surname must equalTo(Some("Donald"))
            f.dateOfBirth must equalTo(Some(DayMonthYear(Some(3), Some(4), Some(1980), None, None)))
            f.nationalInsuranceNumber must equalTo(Some(NationalInsuranceNumber(Some("AB"), Some("12"), Some("34"), Some("56"), Some("C"))))
          })
    }

    "reject too long first name" in {
      CareYouProvide.previousCarerPersonalDetailsForm.bind(
        Map("firstName" -> "CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS")).fold(
          formWithErrors => {
            formWithErrors.errors.head.message must equalTo("error.maxLength")
          },
          f => "This mapping should not happen." must equalTo("Valid"))
    }

    "reject too long middle name" in {
      CareYouProvide.previousCarerPersonalDetailsForm.bind(
        Map("middleName" -> "CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS")).fold(
          formWithErrors => {
            formWithErrors.errors.head.message must equalTo("error.maxLength")
          },
          f => "This mapping should not happen." must equalTo("Valid"))
    }

    "reject too long surname" in {
      CareYouProvide.previousCarerPersonalDetailsForm.bind(
        Map("surname" -> "CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS")).fold(
          formWithErrors => {
            formWithErrors.errors.head.message must equalTo("error.maxLength")
          },
          f => "This mapping should not happen." must equalTo("Valid"))
    }

    "reject invalid national insurance number" in {
      CareYouProvide.previousCarerPersonalDetailsForm.bind(
        Map("nationalInsuranceNumber.ni1" -> "INVALID")).fold(
          formWithErrors => {
            formWithErrors.errors.head.message must equalTo("error.nationalInsuranceNumber")
            formWithErrors.errors.length must equalTo(1)
          },
          f => "This mapping should not happen." must equalTo("Valid"))
    }

    "reject invalid date" in {
      CareYouProvide.previousCarerPersonalDetailsForm.bind(
        Map("dateOfBirth.day" -> "INVALID")).fold(
          formWithErrors => {
            formWithErrors.errors.head.message must equalTo("error.number")
            formWithErrors.errors.length must equalTo(1)
          },
          f => "This mapping should not happen." must equalTo("Valid"))
    }
  }
}
