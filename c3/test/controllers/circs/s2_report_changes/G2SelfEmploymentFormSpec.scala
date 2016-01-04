package controllers.circs.s2_report_changes

import utils.WithApplication
import controllers.mappings.Mappings
import org.specs2.mutable._
import models.DayMonthYear

class G2SelfEmploymentFormSpec extends Specification {
  "Report a change in your circumstances - Self-employment Form" should {
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

    "map data into case class" in new WithApplication {
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

    "mandatory fields must be populated when caring is not set" in new WithApplication {
      G2SelfEmployment.form.bind(
        Map("moreAboutChanges" -> moreAboutChanges)
      ).fold(
          formWithErrors => {
            formWithErrors.errors(0).message must equalTo(Mappings.errorRequired)
            formWithErrors.errors(1).message must equalTo(Mappings.errorRequired)
            formWithErrors.errors(2).message must equalTo(Mappings.errorRequired)
            formWithErrors.errors(3).message must equalTo(Mappings.errorRequired)
          },
          f => "This mapping should not happen." must equalTo("Valid")
        )
    }

    "mandatory fields must be populated when still caring is set to 'no'" in new WithApplication {
      G2SelfEmployment.form.bind(
        Map(
          "stillCaring.answer" -> no,
          "moreAboutChanges" -> moreAboutChanges
        )
      ).fold(
          formWithErrors => {
            formWithErrors.errors(0).message must equalTo("dateRequired")
            formWithErrors.errors(1).message must equalTo(Mappings.errorRequired)
            formWithErrors.errors(2).message must equalTo(Mappings.errorRequired)
            formWithErrors.errors(3).message must equalTo(Mappings.errorRequired)
          },
          f => "This mapping should not happen." must equalTo("Valid")
        )
    }

    "mandatory fields must be populated when still caring is set to 'yes'" in new WithApplication {
      G2SelfEmployment.form.bind(
        Map(
          "stillCaring.answer" -> yes,
          "moreAboutChanges" -> moreAboutChanges
        )
      ).fold(
          formWithErrors => {
            formWithErrors.errors(0).message must equalTo(Mappings.errorRequired)
            formWithErrors.errors(1).message must equalTo(Mappings.errorRequired)
            formWithErrors.errors(2).message must equalTo(Mappings.errorRequired)
          },
          f => "This mapping should not happen." must equalTo("Valid")
        )
    }

    "reject special characters in text field" in new WithApplication {
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
          formWithErrors.errors(0).message must equalTo(Mappings.errorRestrictedCharacters)
        },
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "reject invalid still caring date" in new WithApplication {
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
            formWithErrors.errors.head.message must equalTo(Mappings.errorInvalid)
          },
          f => "This mapping should not happen." must equalTo("Valid")
        )
    }

    "reject invalid when this Self-Employment started  date" in new WithApplication {
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
            formWithErrors.errors.head.message must equalTo(Mappings.errorInvalid)
          },
          f => "This mapping should not happen." must equalTo("Valid")
        )
    }

  }
  section("unit", models.domain.CircumstancesSelfEmployment.id)
}
