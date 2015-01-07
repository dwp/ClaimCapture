package controllers.circs.s1_identification

import org.specs2.mutable.{Tags, Specification}


class G1ReportAChangeInYourCircumstancesFormSpec extends Specification with Tags {

  "Change of circumstances - About You Form" should {

    val fullName = "Mr John Joe Smith"
    val ni1 = "AB123456C"
    val dateOfBirthDay = 5
    val dateOfBirthMonth = 12
    val dateOfBirthYear = 1990
    val theirFullName = "Mrs Jane Smith"
    val theirRelationshipToYou = "Wife"

    "map data into case class" in {
      G1ReportAChangeInYourCircumstances.form.bind(
        Map(
          "fullName" -> fullName,
          "nationalInsuranceNumber.ni1" -> ni1,
          "dateOfBirth.day" -> dateOfBirthDay.toString,
          "dateOfBirth.month" -> dateOfBirthMonth.toString,
          "dateOfBirth.year" -> dateOfBirthYear.toString,
          "theirFullName" -> theirFullName,
          "theirRelationshipToYou" -> theirRelationshipToYou
        )
      ).fold(
        formWithErrors => "This mapping should not happen." must equalTo("Error"),
        f => {
          f.fullName must equalTo("Mr John Joe Smith")
        }
      )
    }

    "reject too many characters in text fields" in {
      G1ReportAChangeInYourCircumstances.form.bind(
        Map(
          "fullName" -> "HARACTERS,CHARACTE,HARACTERS,CHARACTE",
          "nationalInsuranceNumber.ni1" -> ni1,
          "dateOfBirth.day" -> dateOfBirthDay.toString,
          "dateOfBirth.month" -> dateOfBirthMonth.toString,
          "dateOfBirth.year" -> dateOfBirthYear.toString,
          "theirFullName" -> "HARACTERS,CHARACTE,HARACTERS,CHARACTE",
          "theirRelationshipToYou" -> "HARACTERS,CHARACTE,HARACTERS,CHARACTE")).fold(
        formWithErrors => {
          formWithErrors.errors.length must equalTo(3)
          formWithErrors.errors(0).message must equalTo("error.maxLength")
          formWithErrors.errors(1).message must equalTo("error.maxLength")
          formWithErrors.errors(2).message must equalTo("error.maxLength")
        },
        f => "This mapping should not happen." must equalTo("Valid"))
    }

    "reject special characters in text fields" in {
      G1ReportAChangeInYourCircumstances.form.bind(
        Map(
          "fullName" -> "John >",
          "nationalInsuranceNumber.ni1" -> ni1,
          "dateOfBirth.day" -> dateOfBirthDay.toString,
          "dateOfBirth.month" -> dateOfBirthMonth.toString,
          "dateOfBirth.year" -> dateOfBirthYear.toString,
          "theirFullName" -> "Jane >",
          "theirRelationshipToYou" -> "Wife >")).fold(
        formWithErrors => {
          formWithErrors.errors.length must equalTo(3)
          formWithErrors.errors(0).message must equalTo("error.restricted.characters")
          formWithErrors.errors(1).message must equalTo("error.restricted.characters")
          formWithErrors.errors(2).message must equalTo("error.restricted.characters")
        },
        f => "This mapping should not happen." must equalTo("Valid"))
    }

    "have 5 mandatory fields (plus invalid Nino)" in {
      G1ReportAChangeInYourCircumstances.form.bind(
        Map("fullName" -> "")).fold(
        formWithErrors => {
          formWithErrors.errors.length must equalTo(6)
          formWithErrors.errors(0).message must equalTo("error.required")
          formWithErrors.errors(1).message must equalTo("error.required")
          formWithErrors.errors(2).message must equalTo("error.nationalInsuranceNumber")
          formWithErrors.errors(3).message must equalTo("error.required")
          formWithErrors.errors(4).message must equalTo("error.required")
          formWithErrors.errors(5).message must equalTo("error.required")
        },
        f => "This mapping should not happen." must equalTo("Valid"))
    }

    "reject invalid national insurance number" in {
      G1ReportAChangeInYourCircumstances.form.bind(
        Map(
          "fullName" -> fullName,
          "nationalInsuranceNumber.ni1" -> "INVALID",
          "dateOfBirth.day" -> dateOfBirthDay.toString,
          "dateOfBirth.month" -> dateOfBirthMonth.toString,
          "dateOfBirth.year" -> dateOfBirthYear.toString,
          "theirFullName" -> theirFullName,
          "theirRelationshipToYou" -> theirRelationshipToYou
        )).fold(
        formWithErrors => {
          formWithErrors.errors.length must equalTo(1)
          formWithErrors.errors.head.message must equalTo("error.nationalInsuranceNumber")
        },
        f => "This mapping should not happen." must equalTo("Valid"))
    }

    "reject invalid date" in {
      G1ReportAChangeInYourCircumstances.form.bind(
        Map(
          "fullName" -> fullName,
          "nationalInsuranceNumber.ni1" -> ni1.toString,
          "dateOfBirth.day" -> dateOfBirthDay.toString,
          "dateOfBirth.month" -> dateOfBirthMonth.toString,
          "dateOfBirth.year" -> "12345",
          "theirFullName" -> theirFullName,
          "theirRelationshipToYou" -> theirRelationshipToYou
        )).fold(
        formWithErrors => {
          formWithErrors.errors.length must equalTo(1)
          formWithErrors.errors.head.message must equalTo("error.invalid")
        },
        f => "This mapping should not happen." must equalTo("Valid"))
    }
  } section ("unit", models.domain.CircumstancesIdentification.id)
}
