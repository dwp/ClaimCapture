package controllers.circs.s1_identification

import org.specs2.mutable.{Tags, Specification}


class G1ReportAChangeInYourCircumstancesFormSpec extends Specification with Tags {

  "Change of circumstances - About You Form" should {

    val fullName = "Mr John Joe Smith"
    val ni1 = "AB"
    val ni2 = 12
    val ni3 = 34
    val ni4 = 56
    val ni5 = "C"

    val dateOfBirthDay = 5
    val dateOfBirthMonth = 12
    val dateOfBirthYear = 1990

    "map data into case class" in {
      G1ReportAChangeInYourCircumstances.form.bind(
        Map(
          "fullName" -> fullName,
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
          f.fullName must equalTo("Mr John Joe Smith")
        }
      )
    }

    "reject too many characters in text fields" in {
      G1ReportAChangeInYourCircumstances.form.bind(
        Map(
          "fullName" -> "HARACTERS,CHARACTE,HARACTERS,CHARACTE",
          "nationalInsuranceNumber.ni1" -> ni1,
          "nationalInsuranceNumber.ni2" -> ni2.toString,
          "nationalInsuranceNumber.ni3" -> ni3.toString,
          "nationalInsuranceNumber.ni4" -> ni4.toString,
          "nationalInsuranceNumber.ni5" -> ni5,
          "dateOfBirth.day" -> dateOfBirthDay.toString,
          "dateOfBirth.month" -> dateOfBirthMonth.toString,
          "dateOfBirth.year" -> dateOfBirthYear.toString)).fold(
        formWithErrors => {
          formWithErrors.errors.length must equalTo(1)
          formWithErrors.errors(0).message must equalTo("error.maxLength")
        },
        f => "This mapping should not happen." must equalTo("Valid"))
    }

    "reject special characters in text fields" in {
      G1ReportAChangeInYourCircumstances.form.bind(
        Map(
          "fullName" -> "John >",
          "nationalInsuranceNumber.ni1" -> ni1,
          "nationalInsuranceNumber.ni2" -> ni2.toString,
          "nationalInsuranceNumber.ni3" -> ni3.toString,
          "nationalInsuranceNumber.ni4" -> ni4.toString,
          "nationalInsuranceNumber.ni5" -> ni5,
          "dateOfBirth.day" -> dateOfBirthDay.toString,
          "dateOfBirth.month" -> dateOfBirthMonth.toString,
          "dateOfBirth.year" -> dateOfBirthYear.toString)).fold(
        formWithErrors => {
          formWithErrors.errors.length must equalTo(1)
          formWithErrors.errors(0).message must equalTo("error.restricted.characters")
        },
        f => "This mapping should not happen." must equalTo("Valid"))
    }

    "have 3 mandatory fields" in {
      G1ReportAChangeInYourCircumstances.form.bind(
        Map("fullName" -> "")).fold(
        formWithErrors => {
          formWithErrors.errors.length must equalTo(4)
          formWithErrors.errors(0).message must equalTo("error.required")
          formWithErrors.errors(1).message must equalTo("error.required")
          formWithErrors.errors(2).message must equalTo("error.nationalInsuranceNumber")
          formWithErrors.errors(3).message must equalTo("error.required")
        },
        f => "This mapping should not happen." must equalTo("Valid"))
    }

    "reject invalid national insurance number" in {
      G1ReportAChangeInYourCircumstances.form.bind(
        Map(
          "fullName" -> fullName,
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
      G1ReportAChangeInYourCircumstances.form.bind(
        Map(
          "fullName" -> fullName,
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
  } section ("unit", models.domain.CircumstancesIdentification.id)
}
