package controllers.s3_your_partner

import org.specs2.mutable.Specification
import models.DayMonthYear
import scala.Some

class G3MoreAboutYourPartnerFormSpec extends Specification {
  val dateStartedLivingTogetherDay = 5
  val dateStartedLivingTogetherMonth = 12
  val dateStartedLivingTogetherYear = 1990
  val separatedFromPartner = "yes"

  "More About Your Partner Form" should {
    "map data into case class" in {
      G3MoreAboutYourPartner.form.bind(
        Map(
          "dateStartedLivingTogether.day" -> dateStartedLivingTogetherDay.toString,
          "dateStartedLivingTogether.month" -> dateStartedLivingTogetherMonth.toString,
          "dateStartedLivingTogether.year" -> dateStartedLivingTogetherYear.toString,
          "separatedFromPartner" -> separatedFromPartner
        )
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("Error"),
        f => {
          f.dateStartedLivingTogether must equalTo(Some(DayMonthYear(Some(dateStartedLivingTogetherDay), Some(dateStartedLivingTogetherMonth), Some(dateStartedLivingTogetherYear), None, None)))
          f.separatedFromPartner must equalTo(separatedFromPartner)
        }
      )
    }

    "have 1 mandatory field" in {
      G3MoreAboutYourPartner.form.bind(
        Map("foo" -> "bar")
      ).fold(
        formWithErrors => {
          formWithErrors.errors.length must equalTo(1)
          formWithErrors.errors(0).message must equalTo("error.required")
        },
        theirPersonalDetails => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "reject invalid date" in {
      G3MoreAboutYourPartner.form.bind(
        Map("dateStartedLivingTogether.day" -> dateStartedLivingTogetherDay.toString,
          "dateStartedLivingTogether.month" -> dateStartedLivingTogetherMonth.toString,
          "dateStartedLivingTogether.year" -> "123456789",
          "separatedFromPartner" -> separatedFromPartner)
      ).fold(
        formWithErrors => {
          formWithErrors.errors.head.message must equalTo("error.invalid")
          formWithErrors.errors.length must equalTo(1)
        },
        f => "This mapping should not happen." must equalTo("Valid"))
    }
  }
}