package controllers.s7_employment

import org.specs2.mutable.{Tags, Specification}
import models.DayMonthYear

class G3JobDetailsFormSpec extends Specification with Tags {
  "Employer Details - Employment History Form" should {
    val jobId = "1"
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

    "map data into case class" in {
      G3JobDetails.form.bind(
        Map(
          "jobID" -> jobId,
          "employerName" -> employerName,
          "phoneNumber" -> phoneNumber,
          "address.lineOne" -> addressLine,
          "address.lineTwo" -> addressLineTwo,
          "address.lineThree" -> addressLineThree,
          "postcode" -> postCode,
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
          f.jobID must equalTo(jobId)
          f.employerName must equalTo(employerName)
          f.phoneNumber must equalTo(phoneNumber)
          f.address.lineOne must equalTo(Some(addressLine))
          f.address.lineTwo must equalTo(Some(addressLineTwo))
          f.address.lineThree must equalTo(Some(addressLineThree))
          f.postcode must equalTo(Some(postCode))
          f.jobStartDate must equalTo(DayMonthYear())
          f.lastWorkDate must equalTo(Some(DayMonthYear()))

          f.hoursPerWeek must equalTo(Some(hrsPerWeek))
        }
      )
    }

    "have 5 mandatory fields" in {
      G3JobDetails.form.bind(
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
          formWithErrors.errors.length must equalTo(5)
          formWithErrors.errors(0).message must equalTo("error.required")
          formWithErrors.errors(1).message must equalTo("error.required")
          formWithErrors.errors(2).message must equalTo("error.required")
          formWithErrors.errors(3).message must equalTo("error.required")
          formWithErrors.errors(4).message must equalTo("error.required")
        },
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "reject if employerName is not filled" in {
      G3JobDetails.form.bind(
        Map(
          "jobID" -> jobId,
          "address.lineOne" -> addressLine,
          "jobStartDate.day" -> day,
          "jobStartDate.month" -> month,
          "jobStartDate.year" -> year1,
          "finishedThisJob" -> no)
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.required"),
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "reject if address is not filled" in {
      G3JobDetails.form.bind(
        Map(
          "jobID" -> jobId,
          "employerName" -> employerName,
          "jobStartDate.day" -> day,
          "jobStartDate.month" -> month,
          "jobStartDate.year" -> year1,
          "finishedThisJob" -> no)
      ).fold(
          formWithErrors => formWithErrors.errors.head.message must equalTo("error.required"),
          f => "This mapping should not happen." must equalTo("Valid")
        )
    }

    "reject if finishedThisJob is not filled" in {
      G3JobDetails.form.bind(
        Map(
          "jobID" -> jobId,
          "employerName" -> employerName,
          "address.lineOne" -> addressLine,
          "jobStartDate.day" -> day,
          "jobStartDate.month" -> month,
          "jobStartDate.year" -> year1)
      ).fold(
          formWithErrors => formWithErrors.errors.head.message must equalTo("error.required"),
          f => "This mapping should not happen." must equalTo("Valid")
        )
    }

    "have 1 expanded mandatory fields if finishedThisJob is yes" in {
      G3JobDetails.form.bind(
        Map(
          "jobID" -> jobId,
          "employerName" -> employerName,
          "phoneNumber" -> phoneNumber,
          "address.lineOne" -> addressLine,
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

    "reject if phoneNumber is not filled" in {
      G3JobDetails.form.bind(
        Map(
          "jobID" -> jobId,
          "employerName" -> employerName,
          "address.lineOne" -> addressLine,
          "jobStartDate.day" -> day,
          "jobStartDate.month" -> month,
          "jobStartDate.year" -> year1,
          "finishedThisJob" -> no)
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.required"),
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "reject if phoneNumber is not valid" in {
      G3JobDetails.form.bind(
        Map(
          "jobID" -> jobId,
          "employerName" -> employerName,
          "phoneNumber" -> "AB126789*",
          "address.lineOne" -> addressLine,
          "jobStartDate.day" -> day,
          "jobStartDate.month" -> month,
          "jobStartDate.year" -> year1,
          "finishedThisJob" -> no)
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.invalid"),
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }

  } section("unit", models.domain.SelfEmployment.id)
}