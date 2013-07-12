package controllers.s3_your_partner

import org.specs2.mutable.{Tags, Specification}
import models.DayMonthYear
import scala.Some

class G3MoreAboutYourPartnerFormSpec extends Specification with Tags {
  val day = 5
  val month = 12
  val year = 1990
  val yes = "yes"

  "More About Your Partner Form" should {
    "map data into case class" in {
      G3MoreAboutYourPartner.form.bind(
        Map(
          "startedLivingTogether.afterClaimDate" -> yes,
          "startedLivingTogether.date.day" -> day.toString,
          "startedLivingTogether.date.month" -> month.toString,
          "startedLivingTogether.date.year" -> year.toString,
          "separated.fromPartner" -> yes,
          "separated.date.day" -> day.toString,
          "separated.date.month" -> month.toString,
          "separated.date.year" -> year.toString
        )
      ).fold(
        formWithErrors => "This mapping should not happen."  must equalTo("Error"),
        f => {
          f.startedLivingTogether.get.answer must equalTo(yes)
          f.startedLivingTogether.get.date must equalTo(Some(DayMonthYear(Some(day), Some(month), Some(year), None, None)))
          f.separated.answer must equalTo(yes)
          f.separated.date must equalTo(Some(DayMonthYear(Some(day), Some(month), Some(year), None, None)))
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
        Map(
          "startedLivingTogether.afterClaimDate" -> yes,
          "startedLivingTogether.date.day" -> day.toString,
          "startedLivingTogether.date.month" -> month.toString,
          "startedLivingTogether.date.year" -> "0",
          "separated.fromPartner" -> "no")
      ).fold(
        formWithErrors => {
          formWithErrors.errors.head.message must equalTo("error.invalid")
          formWithErrors.errors.length must equalTo(1)
        },
        f => "This mapping should not happen." must equalTo("Valid"))
    }

    "have mandatory separation date when separated" in {
      G3MoreAboutYourPartner.form.bind(
        Map("separated.fromPartner" -> yes)
      ).fold(
        formWithErrors => {
          formWithErrors.errors.head.key must equalTo("separated")
          formWithErrors.errors.length must equalTo(1)
        },
        f => "This mapping should not happen." must equalTo("Valid"))
    }

  } section "unit"
}