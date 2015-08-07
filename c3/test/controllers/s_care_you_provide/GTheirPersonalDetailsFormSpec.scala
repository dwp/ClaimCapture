package controllers.s_care_you_provide

import controllers.mappings.Mappings
import org.specs2.mutable.{Tags, Specification}
import models.DayMonthYear

class GTheirPersonalDetailsFormSpec extends Specification with Tags {

  "Their Personal Details Form" should {

    "map data into case class" in {
      GTheirPersonalDetails.form.bind(
        Map(
          "relationship" -> "father",
          "title" -> "Mr",
          "firstName" -> "Ronald",
          "middleName" -> "Mc",
          "surname" -> "Donald",
          "dateOfBirth.day" -> "3",
          "dateOfBirth.month" -> "4",
          "dateOfBirth.year" -> "1980",
          "liveAtSameAddressCareYouProvide" -> "yes"
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
          theirPersonalDetails.liveAtSameAddressCareYouProvide must equalTo("yes")
        }
      )
    }

    "reject too long firstName, middleName or surname" in {
      GTheirPersonalDetails.form.bind(
        Map(
          "relationship" -> "father",
          "title" -> "Mr",
          "firstName" -> "HARACTERS,CHARACTE",
          "middleName" -> "HARACTERS,CHARACTE",
          "surname" -> "CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS",
          "dateOfBirth.day" -> "1",
          "dateOfBirth.month" -> "1",
          "dateOfBirth.year" -> "1980",
          "liveAtSameAddressCareYouProvide" -> "yes"
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

    "have 5 mandatory fields" in {
      GTheirPersonalDetails.form.bind(
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

    "reject special characters" in {
      GTheirPersonalDetails.form.bind(
        Map(
          "relationship" -> "father",
          "title" -> "Mr",
          "firstName" -> "Fir>name;",
          "middleName" -> "Mc{",
          "surname" -> "Surname<",
          "dateOfBirth.day" -> "3",
          "dateOfBirth.month" -> "4",
          "dateOfBirth.year" -> "1980",
          "liveAtSameAddressCareYouProvide" -> "yes"
        )
      ).fold(formWithErrors => {
        formWithErrors.errors.length must equalTo(3)
        formWithErrors.errors.head.message must equalTo(Mappings.errorRestrictedCharacters)
      },
        f => "This mapping should not happen." must equalTo("Valid"))
    }
  } section("unit", models.domain.CareYouProvide.id)
}