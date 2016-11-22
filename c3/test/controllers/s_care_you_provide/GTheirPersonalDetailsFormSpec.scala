package controllers.s_care_you_provide

import play.api.test.FakeRequest
import utils.{LightFakeApplication, WithApplication}
import controllers.mappings.Mappings
import org.specs2.mutable._
import models.DayMonthYear

class GTheirPersonalDetailsFormSpec extends Specification {
  section("unit", models.domain.CareYouProvide.id)
  "Their Personal Details Form" should {
    "map data into case class" in new WithApplication {
      GTheirPersonalDetails.form(FakeRequest()).bind(
        Map(
          "relationship" -> "father",
          "title" -> "Mr",
          "firstName" -> "Ronald",
          "middleName" -> "Mc",
          "surname" -> "Donald",
          "dateOfBirth.day" -> "3",
          "dateOfBirth.month" -> "4",
          "dateOfBirth.year" -> "1980",
          "theirAddress.answer" -> "yes"
        )
      ).fold(
          formWithErrors => "This mapping should not happen." must equalTo("Error"),
          theirPersonalDetails => {
            theirPersonalDetails.relationship must equalTo("father")
            theirPersonalDetails.title must equalTo("Mr")
            theirPersonalDetails.firstName must equalTo("Ronald")
            theirPersonalDetails.middleName must equalTo(Some("Mc"))
            theirPersonalDetails.surname must equalTo("Donald")
            theirPersonalDetails.dateOfBirth must equalTo(DayMonthYear(Some(3), Some(4), Some(1980), None, None))
            theirPersonalDetails.theirAddress.answer must equalTo("yes")
          }
        )
    }

    val validPostcode: String = "SE1 6EH"

    val personalData = Map("relationship" -> "father",
      "title" -> "Mr",
      "firstName" -> "Ronald",
      "middleName" -> "Mc",
      "surname" -> "Donald",
      "dateOfBirth.day" -> "3",
      "dateOfBirth.month" -> "4",
      "dateOfBirth.year" -> "1980",
      "theirAddress.answer" -> "no"
    )

    "map address data into case class" in new WithApplication {
      GTheirPersonalDetails.form(FakeRequest()).bind(personalData++
        Map(
          "theirAddress.address.lineOne" -> "lineOne",
          "theirAddress.address.lineTwo" -> "lineTwo",
          "theirAddress.address.lineThree" -> "lineThree",
          "theirAddress.postCode" -> validPostcode
        )
      ).fold(
          formWithErrors => "This mapping should not happen." must equalTo("Error"),
          theirPersonalDetails => {
            theirPersonalDetails.theirAddress.answer must equalTo("no")
            theirPersonalDetails.theirAddress.address.get.lineOne must equalTo(Some("lineOne"))
            theirPersonalDetails.theirAddress.address.get.lineTwo must equalTo(Some("lineTwo"))
            theirPersonalDetails.theirAddress.address.get.lineThree must equalTo(Some("lineThree"))
            theirPersonalDetails.theirAddress.postCode must equalTo(Some(validPostcode))
          }
        )
    }

    "have a mandatory address" in new WithApplication {
      GTheirPersonalDetails.form(FakeRequest()).bind(personalData++
        Map("theirAddress.address.lineOne" -> "", "theirAddress.address.lineTwo" -> "", "theirAddress.address.lineThree" -> "", "theirAddress.postCode" -> "")).fold(
          formWithErrors => formWithErrors.errors.head.message must equalTo("theirAddress.address"),
          theirContactDetails => "This mapping should not happen." must equalTo("Valid"))
    }

    "reject an invalid postcode" in new WithApplication {
      GTheirPersonalDetails.form(FakeRequest()).bind(personalData++
        Map("theirAddress.address.lineOne" -> "lineOne", "theirAddress.address.lineTwo" -> "lineTwo", "theirAddress.address.lineThree" -> "", "theirAddress.postCode" -> "INVALID")).fold(
          formWithErrors => formWithErrors.errors.head.message must equalTo("error.postcode"),
          theirContactDetails => "This mapping should not happen." must equalTo("Valid"))
    }

    "reject an invalid address with empty second line" in new WithApplication {
      GTheirPersonalDetails.form(FakeRequest()).bind(personalData++
        Map("theirAddress.address.lineOne" -> "lineOne", "theirAddress.address.lineTwo" -> "", "theirAddress.address.lineThree" -> "", "theirAddress.postCode" -> "")).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.careeaddress.lines.required"),
        theirContactDetails => "This mapping should not happen." must equalTo("Valid"))
    }

    "reject too long firstName, middleName or surname" in new WithApplication {
      GTheirPersonalDetails.form(FakeRequest()).bind(
        Map(
          "relationship" -> "father",
          "title" -> "Mr",
          "firstName" -> "HARACTERS,CHARACTE",
          "middleName" -> "HARACTERS,CHARACTE",
          "surname" -> "CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS",
          "dateOfBirth.day" -> "1",
          "dateOfBirth.month" -> "1",
          "dateOfBirth.year" -> "1980",
          "theirAddress.answer" -> "yes"
        )
      ).fold(
          formWithErrors => {
            formWithErrors.errors(0).message must equalTo(Mappings.maxLengthError)
            formWithErrors.errors(1).message must equalTo(Mappings.maxLengthError)
            formWithErrors.errors(2).message must equalTo(Mappings.maxLengthError)
          },
          theirPersonalDetails => "This mapping should not happen." must equalTo("Valid")
        )
    }

    "have 5 mandatory fields" in new WithApplication {
      GTheirPersonalDetails.form(FakeRequest()).bind(
        Map("middleName" -> "middle optional")
      ).fold(
          formWithErrors => {
            formWithErrors.errors.length must equalTo(6)
            formWithErrors.errors(0).message must equalTo(Mappings.errorRequired)
            formWithErrors.errors(1).message must equalTo(Mappings.errorRequired)
            formWithErrors.errors(2).message must equalTo(Mappings.errorRequired)
            formWithErrors.errors(3).message must equalTo(Mappings.errorRequired)
            formWithErrors.errors(4).message must equalTo(Mappings.errorRequired)
            formWithErrors.errors(5).message must equalTo(Mappings.errorRequired)
          },
          theirPersonalDetails => "This mapping should not happen." must equalTo("Valid")
        )
    }

    "reject special characters pre drs schema" in new WithApplication {
      GTheirPersonalDetails.form(FakeRequest()).bind(
        Map(
          "relationship" -> "father",
          "title" -> "Mr",
          "firstName" -> "Fir>name;",
          "middleName" -> "Mc{",
          "surname" -> "Surname<",
          "dateOfBirth.day" -> "3",
          "dateOfBirth.month" -> "4",
          "dateOfBirth.year" -> "1980",
          "theirAddress.answer" -> "yes"
        )
      ).fold(formWithErrors => {
        formWithErrors.errors.length must equalTo(3)
        formWithErrors.errors.head.message must equalTo(Mappings.errorRestrictedCharacters)
      },
          f => "This mapping should not happen." must equalTo("Valid"))
    }

    "reject special characters" in new WithApplication (app = LightFakeApplication(additionalConfiguration = Map("surname-drs-regex" -> "true"))){
      GTheirPersonalDetails.form(FakeRequest()).bind(
        Map(
          "relationship" -> "father",
          "title" -> "Mr",
          "firstName" -> "Fir>name;",
          "middleName" -> "Mc{",
          "surname" -> "Surname<",
          "dateOfBirth.day" -> "3",
          "dateOfBirth.month" -> "4",
          "dateOfBirth.year" -> "1980",
          "theirAddress.answer" -> "yes"
        )
      ).fold(formWithErrors => {
        formWithErrors.errors.length must equalTo(3)
        formWithErrors.errors.head.message must equalTo(Mappings.errorNameRestrictedCharacters)
      },
        f => "This mapping should not happen." must equalTo("Valid"))
    }
  }
  section("unit", models.domain.CareYouProvide.id)
}
