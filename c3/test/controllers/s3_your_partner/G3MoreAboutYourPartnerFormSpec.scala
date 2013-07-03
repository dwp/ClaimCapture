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
        formWithErrors => "This mapping should not happen." must equalTo("Error"),
        f => {
          f.dateStartedLivingTogether must equalTo(DayMonthYear(Some(dateStartedLivingTogetherDay), Some(dateStartedLivingTogetherMonth), Some(dateStartedLivingTogetherYear), None, None))
          f.separatedFromPartner must equalTo(separatedFromPartner)
        }
      )
    }

    "have 2 mandatory fields" in {
      G3MoreAboutYourPartner.form.bind(
        Map("foo" -> "bar")
      ).fold(
        formWithErrors => {
          formWithErrors.errors.length must equalTo(2)
          formWithErrors.errors(0).message must equalTo("error.required")
          formWithErrors.errors(1).message must equalTo("error.required")
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
