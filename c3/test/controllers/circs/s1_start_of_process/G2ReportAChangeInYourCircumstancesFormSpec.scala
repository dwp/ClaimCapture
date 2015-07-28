package controllers.circs.s1_start_of_process

import app.ReportChange
import app.ReportChange.AdditionalInfo
import app.ReportChange.SelfEmployment
import controllers.circs.s1_start_of_process
import controllers.circs.s2_report_changes.G9EmploymentChange
import controllers.mappings.Mappings
import models.domain._
import models.view.CachedChangeOfCircs
import org.specs2.mutable.{Tags, Specification}
import play.api.cache.Cache
import play.api.test.Helpers._
import play.api.test.{FakeApplication, FakeRequest, WithApplication}
import Mappings._


class G2ReportAChangeInYourCircumstancesFormSpec extends Specification with Tags {

  "Change of circumstances - About You Form" should {

    val fullName = "Mr John Joe Smith"
    val nino = "AB123456C"
    val dateOfBirthDay = 5
    val dateOfBirthMonth = 12
    val dateOfBirthYear = 1990
    val theirFullName = "Mrs Jane Smith"
    val theirRelationshipToYou = "Wife"

    "map data into case class" in {
      G2ReportAChangeInYourCircumstances.form.bind(
        Map(
          "fullName" -> fullName,
          "nationalInsuranceNumber.nino" -> nino,
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
      G2ReportAChangeInYourCircumstances.form.bind(
        Map(
          "fullName" -> "HARACTERS,CHARACTE,HARACTERS,CHARACTE",
          "nationalInsuranceNumber.nino" -> nino,
          "dateOfBirth.day" -> dateOfBirthDay.toString,
          "dateOfBirth.month" -> dateOfBirthMonth.toString,
          "dateOfBirth.year" -> dateOfBirthYear.toString,
          "theirFullName" -> "HARACTERS,CHARACTE,HARACTERS,CHARACTE",
          "theirRelationshipToYou" -> "HARACTERS,CHARACTE,HARACTERS,CHARACTE")).fold(
        formWithErrors => {
          formWithErrors.errors.length must equalTo(3)
          formWithErrors.errors(0).message must equalTo(Mappings.maxLengthError)
          formWithErrors.errors(1).message must equalTo(Mappings.maxLengthError)
          formWithErrors.errors(2).message must equalTo(Mappings.maxLengthError)
        },
        f => "This mapping should not happen." must equalTo("Valid"))
    }

    "reject special characters in text fields" in {
      G2ReportAChangeInYourCircumstances.form.bind(
        Map(
          "fullName" -> "John >",
          "nationalInsuranceNumber.nino" -> nino,
          "dateOfBirth.day" -> dateOfBirthDay.toString,
          "dateOfBirth.month" -> dateOfBirthMonth.toString,
          "dateOfBirth.year" -> dateOfBirthYear.toString,
          "theirFullName" -> "Jane >",
          "theirRelationshipToYou" -> "Wife >")).fold(
        formWithErrors => {
          formWithErrors.errors.length must equalTo(3)
          formWithErrors.errors(0).message must equalTo(Mappings.errorRestrictedCharacters)
          formWithErrors.errors(1).message must equalTo(Mappings.errorRestrictedCharacters)
          formWithErrors.errors(2).message must equalTo(Mappings.errorRestrictedCharacters)
        },
        f => "This mapping should not happen." must equalTo("Valid"))
    }

    "have 5 mandatory fields (plus invalid Nino)" in {
      G2ReportAChangeInYourCircumstances.form.bind(
        Map("fullName" -> "")).fold(
        formWithErrors => {
          formWithErrors.errors.length must equalTo(6)
          formWithErrors.errors(0).message must equalTo(Mappings.errorRequired)
          formWithErrors.errors(1).message must equalTo(Mappings.errorRequired)
          formWithErrors.errors(2).message must equalTo("error.nationalInsuranceNumber")
          formWithErrors.errors(3).message must equalTo(Mappings.errorRequired)
          formWithErrors.errors(4).message must equalTo(Mappings.errorRequired)
          formWithErrors.errors(5).message must equalTo(Mappings.errorRequired)
        },
        f => "This mapping should not happen." must equalTo("Valid"))
    }

    "reject invalid national insurance number" in {
      G2ReportAChangeInYourCircumstances.form.bind(
        Map(
          "fullName" -> fullName,
          "nationalInsuranceNumber.nino" -> "INVALID",
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
      G2ReportAChangeInYourCircumstances.form.bind(
        Map(
          "fullName" -> fullName,
          "nationalInsuranceNumber.nino" -> nino.toString,
          "dateOfBirth.day" -> dateOfBirthDay.toString,
          "dateOfBirth.month" -> dateOfBirthMonth.toString,
          "dateOfBirth.year" -> "12345",
          "theirFullName" -> theirFullName,
          "theirRelationshipToYou" -> theirRelationshipToYou
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

    val validCaringAndOngoingSelfEmploymentStartedFormInput = Seq(
      "stillCaring.answer" -> yes,
      "hasWorkStartedYet.answer" -> yes,
      "hasWorkStartedYet.dateWhenStarted.day" -> startDateDay.toString,
      "hasWorkStartedYet.dateWhenStarted.month" -> startDateMonth.toString,
      "hasWorkStartedYet.dateWhenStarted.year" -> startDateYear.toString,
      "hasWorkFinishedYet.answer" -> no,
      "typeOfWork.answer" -> selfEmployed,
      "typeOfWork.selfEmployedTypeOfWork" -> selfEmployedTypeOfWork,
      "typeOfWork.selfEmployedTotalIncome" -> dontknow
    )

    def g2FakeRequest(claimKey:String) = {
      FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey).withFormUrlEncodedBody(
        "fullName" -> fullName,
        "nationalInsuranceNumber.nino" -> nino,
        "dateOfBirth.day" -> dateOfBirthDay.toString,
        "dateOfBirth.month" -> dateOfBirthMonth.toString,
        "dateOfBirth.year" -> dateOfBirthYear.toString,
        "theirFullName" -> theirFullName,
        "theirRelationshipToYou" -> theirRelationshipToYou
      )
    }

    "Controller flow " should {
      "redirect to the next page after a valid additional info submission" in new WithApplication with MockForm {
        val claim = Claim(claimKey)

        Cache.set(claimKey,claim.update(ReportChanges(false,ReportChange.AdditionalInfo.name)))

        val result = G2ReportAChangeInYourCircumstances.submit(g2FakeRequest(claimKey))

        redirectLocation(result) must beSome("/circumstances/report-changes/other-change")
      }

      "redirect to the next page after a valid self employment submission" in new WithApplication with MockForm {
        val claim = Claim(claimKey)

        Cache.set(claimKey,claim.update(ReportChanges(false,ReportChange.SelfEmployment.name)))

        val result = G2ReportAChangeInYourCircumstances.submit(g2FakeRequest(claimKey))

        redirectLocation(result) must beSome("/circumstances/report-changes/self-employment")
      }

      "redirect to the next page after a valid stopped caring submission" in new WithApplication with MockForm {
        val claim = Claim(claimKey)

        Cache.set(claimKey,claim.update(ReportChanges(false,ReportChange.StoppedCaring.name)))

        val result = G2ReportAChangeInYourCircumstances.submit(g2FakeRequest(claimKey))

        redirectLocation(result) must beSome("/circumstances/report-changes/stopped-caring")
      }

      "redirect to the next page after a valid address change submission" in new WithApplication with MockForm {
        val claim = Claim(claimKey)

        Cache.set(claimKey,claim.update(ReportChanges(false,ReportChange.AddressChange.name)))

        val result = G2ReportAChangeInYourCircumstances.submit(g2FakeRequest(claimKey))

        redirectLocation(result) must beSome("/circumstances/report-changes/address-change")
      }

      "redirect to the next page after a valid payment change submission" in new WithApplication with MockForm {
        val claim = Claim(claimKey)

        Cache.set(claimKey,claim.update(ReportChanges(false,ReportChange.PaymentChange.name)))

        val result = G2ReportAChangeInYourCircumstances.submit(g2FakeRequest(claimKey))

        redirectLocation(result) must beSome("/circumstances/report-changes/payment-change")
      }

      "redirect to the next page after a valid break from caring submission" in new WithApplication with MockForm {
        val claim = Claim(claimKey)

        Cache.set(claimKey,claim.update(ReportChanges(false,ReportChange.BreakFromCaring.name)))

        val result = G2ReportAChangeInYourCircumstances.submit(g2FakeRequest(claimKey))

        redirectLocation(result) must beSome("/circumstances/report-changes/breaks-in-care")
      }

      "redirect to the next page after a valid break from caring submission because of you" in new WithApplication with MockForm {
        val claim = Claim(claimKey)

        Cache.set(claimKey,claim.update(ReportChanges(false,ReportChange.BreakFromCaringYou.name)))

        val result = G2ReportAChangeInYourCircumstances.submit(g2FakeRequest(claimKey))

        redirectLocation(result) must beSome("/circumstances/report-changes/breaks-in-care")
      }

    }


  } section ("unit", models.domain.CircumstancesIdentification.id)
}
