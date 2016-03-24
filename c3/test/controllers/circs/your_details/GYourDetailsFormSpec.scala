package controllers.circs.your_details

import app.ReportChange
import controllers.mappings.Mappings
import models.domain._
import models.view.CachedChangeOfCircs
import org.specs2.mutable._
import play.api.test.Helpers._
import play.api.test.FakeRequest
import utils.WithApplication
import utils.pageobjects.circumstances.consent_and_declaration.GCircsDeclarationPage

class GYourDetailsFormSpec extends Specification {
  val fullName = "Mr John Joe Smith"
  val nino = "AB123456C"
  val dateOfBirthDay = 5
  val dateOfBirthMonth = 12
  val dateOfBirthYear = 1990
  val theirFullName = "Mrs Jane Smith"
  val theirRelationshipToYou = "Wife"

  val byTelephone = "01254897675"
  val wantsEmailContactCircs = "no"
  val nextPageUrl = GCircsDeclarationPage.url

  section("unit", models.domain.CircumstancesReportChanges.id)
  "Change of circumstances - About You Form" should {
    "map data into case class" in new WithApplication {
      GYourDetails.form.bind(
        Map(
          "fullName" -> fullName,
          "nationalInsuranceNumber.nino" -> nino,
          "dateOfBirth.day" -> dateOfBirthDay.toString,
          "dateOfBirth.month" -> dateOfBirthMonth.toString,
          "dateOfBirth.year" -> dateOfBirthYear.toString,
          "theirFullName" -> theirFullName,
          "theirRelationshipToYou" -> theirRelationshipToYou,
          "furtherInfoContact" -> byTelephone,
          "wantsEmailContactCircs" -> wantsEmailContactCircs
        )
      ).fold(
        formWithErrors => {
          println(s"errors $formWithErrors.errors")
          "This mapping should not happen." must equalTo("Error")
        },
        f => {
          f.fullName must equalTo("Mr John Joe Smith")
        }
      )
    }

    "map data into case class no contact info" in new WithApplication {
      GYourDetails.form.bind(
        Map(
          "fullName" -> fullName,
          "nationalInsuranceNumber.nino" -> nino,
          "dateOfBirth.day" -> dateOfBirthDay.toString,
          "dateOfBirth.month" -> dateOfBirthMonth.toString,
          "dateOfBirth.year" -> dateOfBirthYear.toString,
          "theirFullName" -> theirFullName,
          "theirRelationshipToYou" -> theirRelationshipToYou,
          "wantsEmailContactCircs" -> wantsEmailContactCircs
        )
      ).fold(
        formWithErrors => "This mapping should not happen." must equalTo("Error"),
        f => {
          f.fullName must equalTo("Mr John Joe Smith")
        }
      )
    }

    "reject too many characters in text fields" in new WithApplication {
      GYourDetails.form.bind(
        Map(
          "fullName" -> "HARACTERS,CHARACTE,HARACTERS,CHARACTE,HARACTERS,CHARACTE,HARACTERS,CHARACTE",
          "nationalInsuranceNumber.nino" -> nino,
          "dateOfBirth.day" -> dateOfBirthDay.toString,
          "dateOfBirth.month" -> dateOfBirthMonth.toString,
          "dateOfBirth.year" -> dateOfBirthYear.toString,
          "theirFullName" -> "HARACTERS,CHARACTE,HARACTERS,CHARACTE,HARACTERS,CHARACTE,HARACTERS,CHARACTE",
          "theirRelationshipToYou" -> "HARACTERS,CHARACTE,HARACTERS,CHARACTE",
          "furtherInfoContact" -> byTelephone,
          "wantsEmailContactCircs" -> wantsEmailContactCircs
        )).fold(
        formWithErrors => {
          formWithErrors.errors.length must equalTo(3)
          formWithErrors.errors(0).message must equalTo(Mappings.maxLengthError)
          formWithErrors.errors(1).message must equalTo(Mappings.maxLengthError)
          formWithErrors.errors(2).message must equalTo(Mappings.maxLengthError)
        },
        f => "This mapping should not happen." must equalTo("Valid"))
    }

    "reject characters in contact number field" in new WithApplication {
      GYourDetails.form.bind(
        Map(
          "fullName" -> fullName,
          "nationalInsuranceNumber.nino" -> nino,
          "dateOfBirth.day" -> dateOfBirthDay.toString,
          "dateOfBirth.month" -> dateOfBirthMonth.toString,
          "dateOfBirth.year" -> dateOfBirthYear.toString,
          "theirFullName" -> theirFullName,
          "theirRelationshipToYou" -> theirRelationshipToYou,
          "furtherInfoContact" -> "dhjahskdk",
          "wantsEmailContactCircs" -> wantsEmailContactCircs
        )).fold(
        formWithErrors => {
          formWithErrors.errors.length must equalTo(1)
          formWithErrors.errors(0).message must equalTo(Mappings.errorInvalid)
        },
        f => "This mapping should not happen." must equalTo("Valid"))
    }

    "reject too many digits in contact number field" in new WithApplication {
      GYourDetails.form.bind(
        Map(
          "fullName" -> fullName,
          "nationalInsuranceNumber.nino" -> nino,
          "dateOfBirth.day" -> dateOfBirthDay.toString,
          "dateOfBirth.month" -> dateOfBirthMonth.toString,
          "dateOfBirth.year" -> dateOfBirthYear.toString,
          "theirFullName" -> theirFullName,
          "theirRelationshipToYou" -> theirRelationshipToYou,
          "furtherInfoContact" -> "012345678901234567890",
          "wantsEmailContactCircs" -> wantsEmailContactCircs
        )).fold(
        formWithErrors => {
          formWithErrors.errors.length must equalTo(1)
          formWithErrors.errors(0).message must equalTo(Mappings.errorInvalid)
        },
        f => "This mapping should not happen." must equalTo("Valid"))
    }

    "reject too few digits in contact number field" in new WithApplication {
      GYourDetails.form.bind(
        Map(
          "fullName" -> fullName,
          "nationalInsuranceNumber.nino" -> nino,
          "dateOfBirth.day" -> dateOfBirthDay.toString,
          "dateOfBirth.month" -> dateOfBirthMonth.toString,
          "dateOfBirth.year" -> dateOfBirthYear.toString,
          "theirFullName" -> theirFullName,
          "theirRelationshipToYou" -> theirRelationshipToYou,
          "furtherInfoContact" -> "012345",
          "wantsEmailContactCircs" -> wantsEmailContactCircs
        )).fold(
        formWithErrors => {
          formWithErrors.errors.length must equalTo(1)
          formWithErrors.errors(0).message must equalTo(Mappings.errorInvalid)
        },
        f => "This mapping should not happen." must equalTo("Valid"))
    }

    "reject special characters in text fields" in new WithApplication {
      GYourDetails.form.bind(
        Map(
          "fullName" -> "John >",
          "nationalInsuranceNumber.nino" -> nino,
          "dateOfBirth.day" -> dateOfBirthDay.toString,
          "dateOfBirth.month" -> dateOfBirthMonth.toString,
          "dateOfBirth.year" -> dateOfBirthYear.toString,
          "theirFullName" -> "Jane >",
          "theirRelationshipToYou" -> "Wife >",
          "furtherInfoContact" -> byTelephone,
          "wantsEmailContactCircs" -> wantsEmailContactCircs
        )).fold(
        formWithErrors => {
          formWithErrors.errors.length must equalTo(3)
          formWithErrors.errors(0).message must equalTo(Mappings.errorRestrictedCharacters)
          formWithErrors.errors(1).message must equalTo(Mappings.errorRestrictedCharacters)
          formWithErrors.errors(2).message must equalTo(Mappings.errorRestrictedCharacters)
        },
        f => "This mapping should not happen." must equalTo("Valid"))
    }

    "have 5 mandatory fields (plus invalid Nino)" in new WithApplication {
      GYourDetails.form.bind(
        Map("fullName" -> "")).fold(
        formWithErrors => {
          formWithErrors.errors.length must equalTo(7)
          formWithErrors.errors(0).message must equalTo(Mappings.errorRequired)
          formWithErrors.errors(1).message must equalTo(Mappings.errorRequired)
          formWithErrors.errors(2).message must equalTo("error.nationalInsuranceNumber")
          formWithErrors.errors(3).message must equalTo(Mappings.errorRequired)
          formWithErrors.errors(4).message must equalTo(Mappings.errorRequired)
          formWithErrors.errors(5).message must equalTo(Mappings.errorRequired)
        },
        f => "This mapping should not happen." must equalTo("Valid"))
    }

    "reject invalid national insurance number" in new WithApplication {
      GYourDetails.form.bind(
        Map(
          "fullName" -> fullName,
          "nationalInsuranceNumber.nino" -> "INVALID",
          "dateOfBirth.day" -> dateOfBirthDay.toString,
          "dateOfBirth.month" -> dateOfBirthMonth.toString,
          "dateOfBirth.year" -> dateOfBirthYear.toString,
          "theirFullName" -> theirFullName,
          "theirRelationshipToYou" -> theirRelationshipToYou,
          "furtherInfoContact" -> byTelephone,
          "wantsEmailContactCircs" -> wantsEmailContactCircs
        )).fold(
        formWithErrors => {
          formWithErrors.errors.length must equalTo(1)
          formWithErrors.errors.head.message must equalTo("error.nationalInsuranceNumber")
        },
        f => "This mapping should not happen." must equalTo("Valid"))
    }

    "reject invalid date" in new WithApplication {
      GYourDetails.form.bind(
        Map(
          "fullName" -> fullName,
          "nationalInsuranceNumber.nino" -> nino.toString,
          "dateOfBirth.day" -> dateOfBirthDay.toString,
          "dateOfBirth.month" -> dateOfBirthMonth.toString,
          "dateOfBirth.year" -> "12345",
          "theirFullName" -> theirFullName,
          "theirRelationshipToYou" -> theirRelationshipToYou,
          "furtherInfoContact" -> byTelephone,
          "wantsEmailContactCircs" -> wantsEmailContactCircs
        )).fold(
        formWithErrors => {
          formWithErrors.errors.length must equalTo(1)
          formWithErrors.errors.head.message must equalTo(Mappings.errorInvalid)
        },
        f => "This mapping should not happen." must equalTo("Valid"))
    }

    val startDateDay = 1
    val startDateMonth = 12
    val startDateYear = 2012
    val selfEmployed = "self-employed"
    val selfEmployedTypeOfWork = "IT Consultant"


    def g2FakeRequest(claimKey: String) = {
      FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey).withFormUrlEncodedBody(
        "fullName" -> fullName,
        "nationalInsuranceNumber.nino" -> nino,
        "dateOfBirth.day" -> dateOfBirthDay.toString,
        "dateOfBirth.month" -> dateOfBirthMonth.toString,
        "dateOfBirth.year" -> dateOfBirthYear.toString,
        "theirFullName" -> theirFullName,
        "theirRelationshipToYou" -> theirRelationshipToYou,
        "furtherInfoContact" -> byTelephone,
        "wantsEmailContactCircs" -> wantsEmailContactCircs
      )
    }

    "Controller flow " should {
      "redirect to the next page after a valid additional info submission" in new WithApplication with MockForm {
        val claim = Claim(claimKey)
        cache.set("default" + claimKey, claim.update(ReportChangeReason(false, ReportChange.AdditionalInfo.name)))
        val result = GYourDetails.submit(g2FakeRequest(claimKey))
        redirectLocation(result) must beSome(nextPageUrl)
      }
    }
  }
  section("unit", models.domain.CircumstancesReportChanges.id)
}
