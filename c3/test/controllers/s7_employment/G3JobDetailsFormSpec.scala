package controllers.s7_employment

import org.specs2.mutable.{Tags, Specification}
import models.DayMonthYear

class G3JobDetailsFormSpec extends Specification with Tags {
  "Employer Details - Employment History Form" should {
    val jobId = "1"
    val yes = "yes"
    val no = "no"
    val addressLine = "1 Brackenbury Rd"
    val employerName = "Toys r not us"
    val hrsPerWeek =  "25"
    val phoneNumber = "12345678"
    val postCode = "PR1 7UP"
    val payrollNumber = "445566"
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
          "payrollEmployeeNumber" -> payrollNumber,
          "address.lineOne" -> "test 1 rd",
          "address.lineTwo" -> "lineTwo",
          "address.lineThree" -> "lineThree",
          "postcode" -> postCode,
          "jobStartDate.day" -> day,
          "jobStartDate.month" -> month,
          "jobStartDate.year" -> year1,
          "finishedThisJob" -> yes,
          "lastWorkDate.day" -> day,
          "lastWorkDate.month" -> month,
          "lastWorkDate.year" -> year2,
          "hoursPerWeek" -> hrsPerWeek
          )
      ).fold(
        formWithErrors => "This mapping should not happen." must equalTo("Error"),
        f => {
          f.jobID must equalTo(jobId)
          f.employerName must equalTo(employerName)
          f.phoneNumber must equalTo(Some(phoneNumber))
          f.payrollEmployeeNumber must equalTo(Some(payrollNumber))
          f.address.lineOne must equalTo(Some("test 1 rd"))
          f.address.lineTwo must equalTo(Some("lineTwo"))
          f.address.lineThree must equalTo(Some("lineThree"))
          f.postcode must equalTo(Some(postCode))
          f.jobStartDate must equalTo(DayMonthYear())
          f.lastWorkDate must equalTo(Some(DayMonthYear()))

          f.hoursPerWeek must equalTo(Some(hrsPerWeek))
        }
      )
    }

    "have 4 mandatory fields" in {
      G8AboutExpenses.form.bind(
        Map(
          "phoneNumber" -> phoneNumber,
          "payrollEmployeeNumber" -> payrollNumber,
          "postcode" -> postCode,
          "jobStartDate.day" -> "1",
          "jobStartDate.month" -> "1",
          "jobStartDate.year" -> "2000",
          "lastWorkDate.day" -> "1",
          "lastWorkDate.month" -> "1",
          "lastWorkDate.year" -> "2001",
          "hoursPerWeek" -> hrsPerWeek)
      ).fold(
          formWithErrors => {
            formWithErrors.errors.length must equalTo(4)
            formWithErrors.errors(0).message must equalTo("error.required")
            formWithErrors.errors(1).message must equalTo("error.required")
            formWithErrors.errors(2).message must equalTo("error.required")
            formWithErrors.errors(3).message must equalTo("error.required")
          },
          f => "This mapping should not happen." must equalTo("Valid")
        )
    }

    "reject if employerName is not filled" in {
      G8AboutExpenses.form.bind(
        Map(
          "jobID" -> jobId,
          "address.lineOne" -> addressLine,
          "jobStartDate.day" -> "1",
          "jobStartDate.month" -> "1",
          "jobStartDate.year" -> "2000",
          "finishedThisJob" -> no)
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.required"),
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "reject if address is not filled" in {
      G8AboutExpenses.form.bind(
        Map(
          "jobID" -> jobId,
          "employerName" -> employerName,
          "jobStartDate.day" -> "1",
          "jobStartDate.month" -> "1",
          "jobStartDate.year" -> "2000",
          "finishedThisJob" -> no)
      ).fold(
          formWithErrors => formWithErrors.errors.head.message must equalTo("error.required"),
          f => "This mapping should not happen." must equalTo("Valid")
        )
    }

    "reject if finishedThisJob is not filled" in {
      G8AboutExpenses.form.bind(
        Map(
          "jobID" -> jobId,
          "employerName" -> employerName,
          "address.lineOne" -> addressLine,
          "jobStartDate.day" -> "1",
          "jobStartDate.month" -> "1",
          "jobStartDate.year" -> "2000")
      ).fold(
          formWithErrors => formWithErrors.errors.head.message must equalTo("error.required"),
          f => "This mapping should not happen." must equalTo("Valid")
        )
    }


    "have 2 expanded mandatory fields if finishedThisJob is yes" in {
      G8AboutExpenses.form.bind(
        Map(
          "jobID" -> jobId,
          "employerName" -> employerName,
          "address.lineOne" -> addressLine,
          "jobStartDate.day" -> "1",
          "jobStartDate.month" -> "1",
          "jobStartDate.year" -> "2000",
          "finishedThisJob" -> yes)
      ).fold(
          formWithErrors => {
            formWithErrors.errors.length must equalTo(3)
            formWithErrors.errors(0).message must equalTo("error.required")
            formWithErrors.errors(1).message must equalTo("error.required")
          },
          f => "This mapping should not happen." must equalTo("Valid")
        )
    }

  } section("unit", models.domain.SelfEmployment.id)
}