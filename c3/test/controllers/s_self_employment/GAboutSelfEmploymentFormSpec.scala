package controllers.s_self_employment

import controllers.mappings.Mappings
import org.specs2.mutable._
import models.DayMonthYear
import utils.WithApplication

class GAboutSelfEmploymentFormSpec extends Specification {

  section("unit", models.domain.SelfEmployment.id)
  "About Self Employment - About Self Employment Form" should {
    val areYouSelfEmployedNow = "no"
    val whenDidYouStartThisJob_day = 11
    val whenDidYouStartThisJob_month = 9
    val whenDidYouStartThisJob_year = 2001
    val whenDidTheJobFinish_day = 7
    val whenDidTheJobFinish_month = 7
    val whenDidTheJobFinish_year = 2005
    val haveYouCeasedTrading = "no"
    val natureOfYourBusiness = "Consulting"

    "map data into case class" in new WithApplication {
      GSelfEmploymentDates.form.bind(
        Map("stillSelfEmployed" -> areYouSelfEmployedNow,
          "whenDidYouStartThisJob.day" -> whenDidYouStartThisJob_day.toString,
          "whenDidYouStartThisJob.month" -> whenDidYouStartThisJob_month.toString,
          "whenDidYouStartThisJob.year" -> whenDidYouStartThisJob_year.toString,
          "whenDidTheJobFinish.day" -> whenDidTheJobFinish_day.toString,
          "whenDidTheJobFinish.month" -> whenDidTheJobFinish_month.toString,
          "whenDidTheJobFinish.year" -> whenDidTheJobFinish_year.toString,
          "haveYouCeasedTrading" -> haveYouCeasedTrading)
      ).fold(
        formWithErrors => "This mapping should not happen." must equalTo("Error"),
        f => {
          f.stillSelfEmployed must equalTo(areYouSelfEmployedNow)
          f.startThisWork.getOrElse(None) should beLike {
            case dmy: DayMonthYear =>
              dmy.day must equalTo(Some(whenDidYouStartThisJob_day))
              dmy.month must equalTo(Some(whenDidYouStartThisJob_month))
              dmy.year must equalTo(Some(whenDidYouStartThisJob_year))
          }
          f.finishThisWork.getOrElse(None) should beLike {
            case Some(dmy: DayMonthYear) =>
              dmy.day must equalTo(Some(whenDidTheJobFinish_day))
              dmy.month must equalTo(Some(whenDidTheJobFinish_month))
              dmy.year must equalTo(Some(whenDidTheJobFinish_year))
          }
        }
      )
    }

    "reject if areYouSelfEmployedNow is not filled" in new WithApplication {
      GSelfEmploymentDates.form.bind(
        Map("areYouSelfEmployedNow" -> "no",
          "whenDidYouStartThisJob.day" -> whenDidYouStartThisJob_day.toString,
          "whenDidYouStartThisJob.month" -> whenDidYouStartThisJob_month.toString,
          "whenDidYouStartThisJob.year" -> whenDidYouStartThisJob_year.toString)
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo(Mappings.errorRequired),
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "reject if areYouSelfEmployedNow answered no but whenDidTheJobFinish not filled in" in new WithApplication {
      GSelfEmploymentDates.form.bind(
        Map("areYouSelfEmployedNow" -> "no",
          "whenDidYouStartThisJob.day" -> whenDidYouStartThisJob_day.toString,
          "whenDidYouStartThisJob.month" -> whenDidYouStartThisJob_month.toString,
          "whenDidYouStartThisJob.year" -> whenDidYouStartThisJob_year.toString)
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo(Mappings.errorRequired),
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "allow optional field to be left blank" in new WithApplication {
      GSelfEmploymentDates.form.bind(
        Map("areYouSelfEmployedNow" -> "no",
          "whenDidYouStartThisJob.day" -> whenDidYouStartThisJob_day.toString,
          "whenDidYouStartThisJob.month" -> whenDidYouStartThisJob_month.toString,
          "whenDidYouStartThisJob.year" -> whenDidYouStartThisJob_year.toString,
          "whenDidTheJobFinish.day" -> whenDidTheJobFinish_day.toString,
          "whenDidTheJobFinish.month" -> whenDidTheJobFinish_month.toString,
          "whenDidTheJobFinish.year" -> whenDidTheJobFinish_year.toString,
          "natureOfYourBusiness" -> natureOfYourBusiness)
      ).fold(
        formWithErrors => "This mapping should not happen." must equalTo("Error"),
        f => {
          f.stillSelfEmployed must equalTo(areYouSelfEmployedNow)
          f.startThisWork.getOrElse(None) should beLike {
            case dmy: DayMonthYear =>
              dmy.day must equalTo(Some(whenDidYouStartThisJob_day))
              dmy.month must equalTo(Some(whenDidYouStartThisJob_month))
              dmy.year must equalTo(Some(whenDidYouStartThisJob_year))
          }
          f.finishThisWork.getOrElse(None) should beLike {
            case Some(dmy: DayMonthYear) =>
              dmy.day must equalTo(Some(whenDidTheJobFinish_day))
              dmy.month must equalTo(Some(whenDidTheJobFinish_month))
              dmy.year must equalTo(Some(whenDidTheJobFinish_year))
          }
        }
      )
    }
  }
  section("unit", models.domain.SelfEmployment.id)
}
