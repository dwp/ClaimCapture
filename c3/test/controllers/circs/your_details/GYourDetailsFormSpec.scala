package controllers.circs.your_details

import app.ReportChange
import controllers.circs.report_changes.GOtherChangeInfo
import controllers.mappings.Mappings
import models.domain._
import models.view.CachedChangeOfCircs
import org.specs2.mutable._
import play.api.test.Helpers._
import play.api.test.FakeRequest
import utils.{LightFakeApplication, WithApplication}
import utils.pageobjects.circumstances.report_changes.GOtherChangeInfoPage
import utils.pageobjects.circumstances.start_of_process.GCircsYourDetailsPage

class GYourDetailsFormSpec extends Specification {
  val firstName = "John"
  val surname = "Smith"
  val nino = "AB123456C"
  val dateOfBirthDay = 5
  val dateOfBirthMonth = 12
  val dateOfBirthYear = 1990
  val theirFirstName = "Jane"
  val theirSurname = "Jones"
  val theirRelationshipToYou = "Wife"

  val byTelephone = "01254897675"
  val wantsEmailContactCircs = "no"
  val nextPageUrl = GOtherChangeInfoPage.url

  section("unit", models.domain.CircumstancesReportChanges.id)
  "Change of circumstances - About You Form" should {
    "map data into case class" in new WithApplication {
      GYourDetails.form.bind(
        Map(
          "firstName" -> firstName,
          "surname" -> surname,
          "nationalInsuranceNumber.nino" -> nino,
          "dateOfBirth.day" -> dateOfBirthDay.toString,
          "dateOfBirth.month" -> dateOfBirthMonth.toString,
          "dateOfBirth.year" -> dateOfBirthYear.toString,
          "theirFirstName" -> theirFirstName,
          "theirSurname" -> theirSurname,
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
          f.firstName must equalTo("John")
          f.surname must equalTo("Smith")
        }
      )
    }

    "map data into case class no contact info" in new WithApplication {
      GYourDetails.form.bind(
        Map(
          "firstName" -> firstName,
          "surname" -> surname,
          "nationalInsuranceNumber.nino" -> nino,
          "dateOfBirth.day" -> dateOfBirthDay.toString,
          "dateOfBirth.month" -> dateOfBirthMonth.toString,
          "dateOfBirth.year" -> dateOfBirthYear.toString,
          "theirFirstName" -> theirFirstName,
          "theirSurname" -> theirSurname,
          "theirRelationshipToYou" -> theirRelationshipToYou,
          "wantsEmailContactCircs" -> wantsEmailContactCircs
        )
      ).fold(
        formWithErrors => "This mapping should not happen." must equalTo("Error"),
        f => {
          f.firstName must equalTo("John")
          f.surname must equalTo("Smith")
        }
      )
    }

    "reject too many characters in text fields" in new WithApplication {
      GYourDetails.form.bind(
        Map(
          "firstName" -> "a really long first name greater than max",
          "surname" -> "a really long surname greater than max",
          "nationalInsuranceNumber.nino" -> nino,
          "dateOfBirth.day" -> dateOfBirthDay.toString,
          "dateOfBirth.month" -> dateOfBirthMonth.toString,
          "dateOfBirth.year" -> dateOfBirthYear.toString,
          "theirFirstName" -> "HARACTERS,CHARACTE,HARACTERS,CHARACTE,HARACTERS,CHARACTE,HARACTERS,CHARACTE",
          "theirSurname" -> "HARACTERS,CHARACTE,HARACTERS,CHARACTE,HARACTERS,CHARACTE,HARACTERS,CHARACTE",
          "theirRelationshipToYou" -> "HARACTERS,CHARACTE,HARACTERS,CHARACTE",
          "furtherInfoContact" -> byTelephone,
          "wantsEmailContactCircs" -> wantsEmailContactCircs
        )).fold(
        formWithErrors => {
          println(formWithErrors)
          formWithErrors.errors.length must equalTo(5)
          formWithErrors.errors(0).message must equalTo(Mappings.maxLengthError)
          formWithErrors.errors(1).message must equalTo(Mappings.maxLengthError)
          formWithErrors.errors(2).message must equalTo(Mappings.maxLengthError)
          formWithErrors.errors(3).message must equalTo(Mappings.maxLengthError)
          formWithErrors.errors(4).message must equalTo(Mappings.maxLengthError)
        },
        f => "This mapping should not happen." must equalTo("Valid"))
    }

    "reject characters in contact number field" in new WithApplication {
      GYourDetails.form.bind(
        Map(
          "firstName" -> firstName,
          "surname" -> surname,
          "nationalInsuranceNumber.nino" -> nino,
          "dateOfBirth.day" -> dateOfBirthDay.toString,
          "dateOfBirth.month" -> dateOfBirthMonth.toString,
          "dateOfBirth.year" -> dateOfBirthYear.toString,
          "theirFirstName" -> theirFirstName,
          "theirSurname" -> theirSurname,
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
          "firstName" -> firstName,
          "surname" -> surname,
          "nationalInsuranceNumber.nino" -> nino,
          "dateOfBirth.day" -> dateOfBirthDay.toString,
          "dateOfBirth.month" -> dateOfBirthMonth.toString,
          "dateOfBirth.year" -> dateOfBirthYear.toString,
          "theirFirstName" -> theirFirstName,
          "theirSurname" -> theirSurname,
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
          "firstName" -> firstName,
          "surname" -> surname,
          "nationalInsuranceNumber.nino" -> nino,
          "dateOfBirth.day" -> dateOfBirthDay.toString,
          "dateOfBirth.month" -> dateOfBirthMonth.toString,
          "dateOfBirth.year" -> dateOfBirthYear.toString,
          "theirFirstName" -> theirFirstName,
          "theirSurname" -> theirSurname,
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

    "reject special characters in text fields pre drs schema" in new WithApplication (app = LightFakeApplication(additionalConfiguration = Map("surname-drs-regex" -> "false"))){
      GYourDetails.form.bind(
        Map(
          "firstName" -> "John >",
          "surname" -> "Smith $",
          "nationalInsuranceNumber.nino" -> nino,
          "dateOfBirth.day" -> dateOfBirthDay.toString,
          "dateOfBirth.month" -> dateOfBirthMonth.toString,
          "dateOfBirth.year" -> dateOfBirthYear.toString,
          "theirFirstName" -> "Jane >",
          "theirSurname" -> "Evans $",
          "theirRelationshipToYou" -> "Wife >",
          "furtherInfoContact" -> byTelephone,
          "wantsEmailContactCircs" -> wantsEmailContactCircs
        )).fold(
        formWithErrors => {
          println("FORMWITHERRORS:" + formWithErrors)
          formWithErrors.errors.length must equalTo(5)
          formWithErrors.errors(0).message must equalTo(Mappings.errorRestrictedCharacters)
          formWithErrors.errors(1).message must equalTo(Mappings.errorRestrictedCharacters)
          formWithErrors.errors(2).message must equalTo(Mappings.errorRestrictedCharacters)
          formWithErrors.errors(3).message must equalTo(Mappings.errorRestrictedCharacters)
          formWithErrors.errors(4).message must equalTo(Mappings.errorRestrictedCharacters)
        },
        f => "This mapping should not happen." must equalTo("Valid"))
    }

    "reject special characters in text fields" in new WithApplication (app = LightFakeApplication(additionalConfiguration = Map("surname-drs-regex" -> "true"))){
      GYourDetails.form.bind(
        Map(
          "firstName" -> "John >",
          "surname" -> "Smith $",
          "nationalInsuranceNumber.nino" -> nino,
          "dateOfBirth.day" -> dateOfBirthDay.toString,
          "dateOfBirth.month" -> dateOfBirthMonth.toString,
          "dateOfBirth.year" -> dateOfBirthYear.toString,
          "theirFirstName" -> "Jane >",
          "theirSurname" -> "Evans $",
          "theirRelationshipToYou" -> "Wife >",
          "furtherInfoContact" -> byTelephone,
          "wantsEmailContactCircs" -> wantsEmailContactCircs
        )).fold(
        formWithErrors => {
          formWithErrors.errors.length must equalTo(5)
          formWithErrors.errors(0).message must equalTo(Mappings.errorNameRestrictedCharacters)
          formWithErrors.errors(1).message must equalTo(Mappings.errorNameRestrictedCharacters)
          formWithErrors.errors(2).message must equalTo(Mappings.errorNameRestrictedCharacters)
          formWithErrors.errors(3).message must equalTo(Mappings.errorNameRestrictedCharacters)
          formWithErrors.errors(4).message must equalTo(Mappings.errorNameRestrictedCharacters)
        },
        f => "This mapping should not happen." must equalTo("Valid"))
    }

    "have 8 mandatory fields (plus invalid Nino)" in new WithApplication {
      GYourDetails.form.bind(
        Map("fullName" -> "")).fold(
        formWithErrors => {
          formWithErrors.errors.length must equalTo(8)
          formWithErrors.errors(0).message must equalTo(Mappings.errorRequired)
          formWithErrors.errors(1).message must equalTo(Mappings.errorRequired)
          formWithErrors.errors(3).message must equalTo(Mappings.errorRequired)
          formWithErrors.errors(4).message must equalTo(Mappings.errorRequired)
          formWithErrors.errors(5).message must equalTo(Mappings.errorRequired)
          formWithErrors.errors(6).message must equalTo(Mappings.errorRequired)
          formWithErrors.errors(7).message must equalTo(Mappings.errorRequired)
        },
        f => "This mapping should not happen." must equalTo("Valid"))
    }


    "reject invalid national insurance number" in new WithApplication {
      GYourDetails.form.bind(
        Map(
          "firstName" -> firstName,
          "surname" -> surname,
          "nationalInsuranceNumber.nino" -> "INVALID",
          "dateOfBirth.day" -> dateOfBirthDay.toString,
          "dateOfBirth.month" -> dateOfBirthMonth.toString,
          "dateOfBirth.year" -> dateOfBirthYear.toString,
          "theirFirstName" -> theirFirstName,
          "theirSurname" -> theirSurname,
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
          "firstName" -> firstName,
          "surname" -> surname,
          "nationalInsuranceNumber.nino" -> nino.toString,
          "dateOfBirth.day" -> dateOfBirthDay.toString,
          "dateOfBirth.month" -> dateOfBirthMonth.toString,
          "dateOfBirth.year" -> "12345",
          "theirFirstName" -> theirFirstName,
          "theirSurname" -> theirSurname,
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

    "allow maximum spaces in and around nino totalling 19 chars" in new WithApplication {
      GYourDetails.form.bind(
        Map(
          "firstName" -> firstName,
          "surname" -> surname,
          "nationalInsuranceNumber.nino" -> " N R 0 1 0 2 0 3 A ",
          "dateOfBirth.day" -> dateOfBirthDay.toString,
          "dateOfBirth.month" -> dateOfBirthMonth.toString,
          "dateOfBirth.year" -> dateOfBirthYear.toString,
          "theirFirstName" -> theirFirstName,
          "theirSurname" -> theirSurname,
          "theirRelationshipToYou" -> theirRelationshipToYou,
          "furtherInfoContact" -> byTelephone,
          "wantsEmailContactCircs" -> wantsEmailContactCircs
        )).fold(
        formWithErrors => {
          formWithErrors.errors.length must equalTo(0)
        },
        f => {
          f.nationalInsuranceNumber.nino.getOrElse("").length mustEqual 19
          f.nationalInsuranceNumber.nino mustEqual Some(" N R 0 1 0 2 0 3 A ")
          f.nationalInsuranceNumber.stringify mustEqual "NR010203A"
        })
    }

    "enforce usual validation on nino" in new WithApplication {
      GYourDetails.form.bind(
        Map(
          "firstName" -> firstName,
          "surname" -> surname,
          "nationalInsuranceNumber.nino" -> " N R X 1 0 2 0 3 A ",
          "dateOfBirth.day" -> dateOfBirthDay.toString,
          "dateOfBirth.month" -> dateOfBirthMonth.toString,
          "dateOfBirth.year" -> dateOfBirthYear.toString,
          "theirFirstName" -> theirFirstName,
          "theirSurname" -> theirSurname,
          "theirRelationshipToYou" -> theirRelationshipToYou,
          "furtherInfoContact" -> byTelephone,
          "wantsEmailContactCircs" -> wantsEmailContactCircs
        )).fold(
        formWithErrors => {
          formWithErrors.errors.length must equalTo(1)
        },
        f => {
          "This mapping should not happen." must equalTo("Valid")
        })
    }

    def g2FakeRequest(claimKey: String) = {
      FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey).withFormUrlEncodedBody(
        "firstName" -> firstName,
        "surname" -> surname,
        "nationalInsuranceNumber.nino" -> nino,
        "dateOfBirth.day" -> dateOfBirthDay.toString,
        "dateOfBirth.month" -> dateOfBirthMonth.toString,
        "dateOfBirth.year" -> dateOfBirthYear.toString,
        "theirFirstName" -> theirFirstName,
        "theirSurname" -> theirSurname,
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
