package controllers.circs.s2_report_changes

import org.specs2.mutable.{Tags, Specification}
import models.DayMonthYear

class G2SelfEmploymentFormSpec extends Specification with Tags {
  "Report a change in your circumstances - Self employment Form" should {
    val yes = "yes"
    val no = "no"
    val dontknow = "dontknow"
    val stillCaringDateDay = 10
    val stillCaringDateMonth = 11
    val stillCaringDateYear = 2012
    val whenThisSelfEmploymentStartedDateDay = 11
    val whenThisSelfEmploymentStartedDateMonth = 12
    val whenThisSelfEmploymentStartedDateYear = 2013
    val typeOfBusiness = "Plumber"
    val moreAboutChanges = "This is more about the change"
    val invalidYear = 99999

    "map data into case class" in {
      G2SelfEmployment.form.bind(
        Map(
          "stillCaring.answer" -> yes,
          "whenThisSelfEmploymentStarted.day" -> whenThisSelfEmploymentStartedDateDay.toString,
          "whenThisSelfEmploymentStarted.month" -> whenThisSelfEmploymentStartedDateMonth.toString,
          "whenThisSelfEmploymentStarted.year" -> whenThisSelfEmploymentStartedDateYear.toString,
          "typeOfBusiness" -> typeOfBusiness,
          "totalOverWeeklyIncomeThreshold" -> dontknow,
          "moreAboutChanges" -> moreAboutChanges
        )
      ).fold(
        formWithErrors => "This mapping should not happen." must equalTo("Error"),
        f => {
          f.stillCaring.answer must equalTo(yes)
          f.whenThisSelfEmploymentStarted must equalTo(DayMonthYear(Some(whenThisSelfEmploymentStartedDateDay), Some(whenThisSelfEmploymentStartedDateMonth), Some(whenThisSelfEmploymentStartedDateYear), None, None))
          f.typeOfBusiness must equalTo(typeOfBusiness)
          f.totalOverWeeklyIncomeThreshold must equalTo(dontknow)
          f.moreAboutChanges must equalTo(Some(moreAboutChanges))
        }
      )
    }

    "mandatory fields must be populated when caring is not set" in {
      G2SelfEmployment.form.bind(
        Map("moreAboutChanges" -> moreAboutChanges)
      ).fold(
          formWithErrors => {
            formWithErrors.errors(0).message must equalTo("error.required")
            formWithErrors.errors(1).message must equalTo("error.required")
            formWithErrors.errors(2).message must equalTo("error.required")
            formWithErrors.errors(3).message must equalTo("error.required")
          },
          f => "This mapping should not happen." must equalTo("Valid")
        )
    }

    "mandatory fields must be populated when still caring is set to 'no'" in {
      G2SelfEmployment.form.bind(
        Map(
          "stillCaring.answer" -> no,
          "moreAboutChanges" -> moreAboutChanges
        )
      ).fold(
          formWithErrors => {
            formWithErrors.errors(0).message must equalTo("dateRequired")
            formWithErrors.errors(1).message must equalTo("error.required")
            formWithErrors.errors(2).message must equalTo("error.required")
            formWithErrors.errors(3).message must equalTo("error.required")
          },
          f => "This mapping should not happen." must equalTo("Valid")
        )
    }

    "mandatory fields must be populated when still caring is set to 'yes'" in {
      G2SelfEmployment.form.bind(
        Map(
          "stillCaring.answer" -> yes,
          "moreAboutChanges" -> moreAboutChanges
        )
      ).fold(
          formWithErrors => {
            formWithErrors.errors(0).message must equalTo("error.required")
            formWithErrors.errors(1).message must equalTo("error.required")
            formWithErrors.errors(2).message must equalTo("error.required")
          },
          f => "This mapping should not happen." must equalTo("Valid")
        )
    }

    "reject special characters in text field" in {
      G2SelfEmployment.form.bind(
        Map(
          "stillCaring.answer" -> yes,
          "stillCaring.date.day" -> stillCaringDateDay.toString,
          "stillCaring.date.month" -> stillCaringDateMonth.toString,
          "stillCaring.date.year" -> stillCaringDateYear.toString,
          "whenThisSelfEmploymentStarted.day" -> whenThisSelfEmploymentStartedDateDay.toString,
          "whenThisSelfEmploymentStarted.month" -> whenThisSelfEmploymentStartedDateMonth.toString,
          "whenThisSelfEmploymentStarted.year" -> whenThisSelfEmploymentStartedDateYear.toString,
          "typeOfBusiness" -> typeOfBusiness,
          "totalOverWeeklyIncomeThreshold" -> yes,
          "moreAboutChanges" -> "<>"
        )
      ).fold(
        formWithErrors => {
          formWithErrors.errors.length must equalTo(1)
          formWithErrors.errors(0).message must equalTo("error.restricted.characters")
        },
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "reject invalid still caring date" in {
      G2SelfEmployment.form.bind(
        Map(
          "stillCaring.answer" -> no,
          "stillCaring.date.day" -> stillCaringDateDay.toString,
          "stillCaring.date.month" -> stillCaringDateMonth.toString,
          "stillCaring.date.year" -> invalidYear.toString,
          "whenThisSelfEmploymentStarted.day" -> whenThisSelfEmploymentStartedDateDay.toString,
          "whenThisSelfEmploymentStarted.month" -> whenThisSelfEmploymentStartedDateMonth.toString,
          "whenThisSelfEmploymentStarted.year" -> whenThisSelfEmploymentStartedDateYear.toString,
          "typeOfBusiness" -> typeOfBusiness,
          "totalOverWeeklyIncomeThreshold" -> yes,
          "moreAboutChanges" -> moreAboutChanges
        )
      ).fold(
          formWithErrors => {
            formWithErrors.errors.length must equalTo(1)
            formWithErrors.errors.head.message must equalTo("error.invalid")
          },
          f => "This mapping should not happen." must equalTo("Valid")
        )
    }

    "reject invalid when this Self Employment started  date" in {
      G2SelfEmployment.form.bind(
        Map(
          "stillCaring.answer" -> yes,
          "whenThisSelfEmploymentStarted.day" -> whenThisSelfEmploymentStartedDateDay.toString,
          "whenThisSelfEmploymentStarted.month" -> whenThisSelfEmploymentStartedDateMonth.toString,
          "whenThisSelfEmploymentStarted.year" -> invalidYear.toString,
          "typeOfBusiness" -> typeOfBusiness,
          "totalOverWeeklyIncomeThreshold" -> yes,
          "moreAboutChanges" -> moreAboutChanges
        )
      ).fold(
          formWithErrors => {
            formWithErrors.errors.length must equalTo(1)
            formWithErrors.errors.head.message must equalTo("error.invalid")
          },
          f => "This mapping should not happen." must equalTo("Valid")
        )
    }

  } section("unit", models.domain.CircumstancesSelfEmployment.id)
}
