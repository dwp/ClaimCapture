package controllers.s_employment

import controllers.mappings.Mappings
import org.specs2.mutable._
import models.DayMonthYear
import utils.WithApplication

class GJobDetailsFormSpec extends Specification {
  section("unit", models.domain.JobDetails.id)
  "Employer Details - Employment History Form" should {
    val iterationID = "1"
    val yes = "yes"
    val no = "no"
    val addressLine = "test 1 rd"
    val addressLineTwo = "line two"
    val addressLineThree = "line three"
    val employerName = "Toys r not us"
    val hrsPerWeek =  "25"
    val phoneNumber = "12345678"
    val postCode = "PR1 7UP"
    val day = "1"
    val month = "1"
    val year1 = "1970"
    val year2 = "1970"

    "map data into case class" in new WithApplication {
      GJobDetails.form.bind(
        Map(
          "iterationID" -> iterationID,
          "employerName" -> employerName,
          "phoneNumber" -> phoneNumber,
          "address.lineOne" -> addressLine,
          "address.lineTwo" -> addressLineTwo,
          "address.lineThree" -> addressLineThree,
          "postcode" -> postCode,
          "startJobBeforeClaimDate" -> no,
          "jobStartDate.day" -> day,
          "jobStartDate.month" -> month,
          "jobStartDate.year" -> year1,
          "finishedThisJob" -> yes,
          "lastWorkDate.day" -> day,
          "lastWorkDate.month" -> month,
          "lastWorkDate.year" -> year2,
          "p45LeavingDate.day" -> day,
          "p45LeavingDate.month" -> month,
          "p45LeavingDate.year" -> year2,
          "hoursPerWeek" -> hrsPerWeek
          )
      ).fold(
        formWithErrors => "This mapping should not happen." must equalTo("Error"),
        f => {
          f.iterationID must equalTo(iterationID)
          f.employerName must equalTo(employerName)
          f.phoneNumber must equalTo(phoneNumber)
          f.address.lineOne must equalTo(Some(addressLine))
          f.address.lineTwo must equalTo(Some(addressLineTwo))
          f.address.lineThree must equalTo(Some(addressLineThree))
          f.postcode must equalTo(Some(postCode))
          f.jobStartDate must equalTo(Some(DayMonthYear()))
          f.lastWorkDate must equalTo(Some(DayMonthYear()))

          f.hoursPerWeek must equalTo(Some(hrsPerWeek))
        }
      )
    }

    "have 6 mandatory fields" in new WithApplication {
      GJobDetails.form.bind(
        Map(
          "postcode" -> postCode,
          "jobStartDate.day" -> day,
          "jobStartDate.month" -> month,
          "jobStartDate.year" -> year1,
          "lastWorkDate.day" -> day,
          "lastWorkDate.month" -> month,
          "lastWorkDate.year" -> year2,
          "p45LeavingDate.day" -> day,
          "p45LeavingDate.month" -> month,
          "p45LeavingDate.year" -> year2,
          "hoursPerWeek" -> hrsPerWeek)
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
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "reject if employerName is not filled" in new WithApplication {
      GJobDetails.form.bind(
        Map(
          "iterationID" -> iterationID,
          "address.lineOne" -> addressLine,
          "startJobBeforeClaimDate" -> no,
          "jobStartDate.day" -> day,
          "jobStartDate.month" -> month,
          "jobStartDate.year" -> year1,
          "finishedThisJob" -> no)
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo(Mappings.errorRequired),
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "reject if address is not filled" in new WithApplication {
      GJobDetails.form.bind(
        Map(
          "iterationID" -> iterationID,
          "employerName" -> employerName,
          "startJobBeforeClaimDate" -> no,
          "jobStartDate.day" -> day,
          "jobStartDate.month" -> month,
          "jobStartDate.year" -> year1,
          "finishedThisJob" -> no)
      ).fold(
          formWithErrors => formWithErrors.errors.head.message must equalTo(Mappings.errorRequired),
          f => "This mapping should not happen." must equalTo("Valid")
        )
    }

    "reject if finishedThisJob is not filled" in new WithApplication {
      GJobDetails.form.bind(
        Map(
          "iterationID" -> iterationID,
          "employerName" -> employerName,
          "address.lineOne" -> addressLine,
          "startJobBeforeClaimDate" -> no,
          "jobStartDate.day" -> day,
          "jobStartDate.month" -> month,
          "jobStartDate.year" -> year1)
      ).fold(
          formWithErrors => formWithErrors.errors.head.message must equalTo(Mappings.errorRequired),
          f => "This mapping should not happen." must equalTo("Valid")
        )
    }

    "reject if startJobBeforeClaimDate is not filled" in new WithApplication {
      GJobDetails.form.bind(
        Map(
          "iterationID" -> iterationID,
          "employerName" -> employerName,
          "address.lineOne" -> addressLine,
          "address.lineTwo" -> addressLineTwo,
          "phoneNumber" -> phoneNumber,
          "finishedThisJob" -> no)
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo(Mappings.errorRequired),
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "have 1 expanded mandatory fields if startJobBeforeClaimDate is no" in new WithApplication {
      GJobDetails.form.bind(
        Map(
          "iterationID" -> iterationID,
          "employerName" -> employerName,
          "phoneNumber" -> phoneNumber,
          "address.lineOne" -> addressLine,
          "address.lineTwo" -> addressLineTwo,
          "startJobBeforeClaimDate" -> no,
          "finishedThisJob" -> no)
      ).fold(
        formWithErrors => {
          formWithErrors.errors.length must equalTo(1)
          formWithErrors.errors(0).message must equalTo("jobStartDate.required")
        },
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "have 1 expanded mandatory fields if finishedThisJob is yes" in new WithApplication {
      GJobDetails.form.bind(
        Map(
          "iterationID" -> iterationID,
          "employerName" -> employerName,
          "phoneNumber" -> phoneNumber,
          "address.lineOne" -> addressLine,
          "address.lineTwo" -> addressLineTwo,
          "startJobBeforeClaimDate" -> no,
          "jobStartDate.day" -> day,
          "jobStartDate.month" -> month,
          "jobStartDate.year" -> year1,
          "finishedThisJob" -> yes)
      ).fold(
          formWithErrors => {
            formWithErrors.errors.length must equalTo(1)
            formWithErrors.errors(0).message must equalTo("lastWorkDate.required")
          },
          f => "This mapping should not happen." must equalTo("Valid")
        )
    }

    "reject if phoneNumber is not filled" in new WithApplication {
      GJobDetails.form.bind(
        Map(
          "iterationID" -> iterationID,
          "employerName" -> employerName,
          "address.lineOne" -> addressLine,
          "startJobBeforeClaimDate" -> no,
          "jobStartDate.day" -> day,
          "jobStartDate.month" -> month,
          "jobStartDate.year" -> year1,
          "finishedThisJob" -> no)
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo(Mappings.errorRequired),
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "reject if phoneNumber is not valid" in new WithApplication {
      GJobDetails.form.bind(
        Map(
          "iterationID" -> iterationID,
          "employerName" -> employerName,
          "phoneNumber" -> "AB126789*",
          "address.lineOne" -> addressLine,
          "startJobBeforeClaimDate" -> no,
          "jobStartDate.day" -> day,
          "jobStartDate.month" -> month,
          "jobStartDate.year" -> year1,
          "finishedThisJob" -> no)
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo(Mappings.errorInvalid),
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "reject if address first line is empty" in new WithApplication {
      GJobDetails.form.bind(
        Map(
          "iterationID" -> iterationID,
          "employerName" -> employerName,
          "phoneNumber" -> "AB126789",
          "address.lineOne" -> "",
          "address.lineTwo" -> "lineTwo",
          "startJobBeforeClaimDate" -> no,
          "jobStartDate.day" -> day,
          "jobStartDate.month" -> month,
          "jobStartDate.year" -> year1,
          "finishedThisJob" -> no)
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo(Mappings.errorInvalid),
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "reject if address second line is empty" in new WithApplication {
      GJobDetails.form.bind(
        Map(
          "iterationID" -> iterationID,
          "employerName" -> employerName,
          "phoneNumber" -> "12345678",
          "address.lineOne" -> "lineOne",
          "address.lineTwo" -> "",
          "startJobBeforeClaimDate" -> no,
          "jobStartDate.day" -> day,
          "jobStartDate.month" -> month,
          "jobStartDate.year" -> year1,
          "finishedThisJob" -> no)
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.addressLines.required"),
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }
  }
  section("unit", models.domain.JobDetails.id)
}
