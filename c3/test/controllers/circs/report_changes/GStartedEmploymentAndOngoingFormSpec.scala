package controllers.circs.report_changes

import utils.WithApplication
import org.specs2.mutable._
import models.DayMonthYear

class GStartedEmploymentAndOngoingFormSpec extends Specification {
  val yes = "yes"
  val no = "no"
  val amountPaid = "£199.98"
  val paydateDay = 10
  val paydateMonth = 11
  val paydateYear = 2012
  val weekly = "weekly"
  val monthly = "monthly"
  val monthlyPayDay = "2nd Thursday every month"
  val other = "other"
  val otherText = "some other text"

  section("unit", models.domain.CircumstancesSelfEmployment.id)
  "Report an Employment change in your circumstances where the employment is ongoing - Employment Form" should {
    "map weekly paid with no pension/expenses paid when been paid set to 'yes'" in new WithApplication {
      GEmploymentPay.form.bind(
        Map(
          "pastpresentfuture" -> "present",
          "paid" -> yes,
          "howmuch" -> amountPaid,
          "paydate.day" -> paydateDay.toString,
          "paydate.month" -> paydateMonth.toString,
          "paydate.year" -> paydateYear.toString,
          "howOften.frequency" -> weekly,
          "sameAmount" -> no
        )
      ).fold(
          formWithErrors => {
            "This mapping should not happen." must equalTo("Error")
          },
          f => {
            f.paid must equalTo(Some(yes))
            f.payDate must equalTo(Some(DayMonthYear(Some(paydateDay), Some(paydateMonth), Some(paydateYear), None, None)))
            f.howOften.frequency must equalTo(weekly)
            f.sameAmount must equalTo(Some(no))
          }
        )
    }

    "map 'other' paid with pension/expenses paid and more information about changes when been paid set to 'no'" in new WithApplication {
      GEmploymentPay.form.bind(
        Map(
          "pastpresentfuture" -> "present",
          "paid" -> no,
          "howmuch" -> amountPaid,
          "paydate.day" -> paydateDay.toString,
          "paydate.month" -> paydateMonth.toString,
          "paydate.year" -> paydateYear.toString,
          "howOften.frequency" -> other,
          "howOften.frequency.other" -> otherText,
          "sameAmount" -> yes
        )
      ).fold(
          formWithErrors => {
            "This mapping should not happen." must equalTo("Error")
          },
          f => {
            f.paid must equalTo(Some(no))
            f.payDate must equalTo(Some(DayMonthYear(Some(paydateDay), Some(paydateMonth), Some(paydateYear), None, None)))
            f.howOften.frequency must equalTo(other)
            f.howOften.other.get must equalTo(otherText)
            f.sameAmount must equalTo(Some(yes))
          }
        )
    }

    "map monthly paid with no pension/expenses paid when been paid set to 'yes'" in new WithApplication {
      GEmploymentPay.form.bind(
        Map(
          "pastpresentfuture" -> "present",
          "paid" -> yes,
          "howmuch" -> amountPaid,
          "paydate.day" -> paydateDay.toString,
          "paydate.month" -> paydateMonth.toString,
          "paydate.year" -> paydateYear.toString,
          "howOften.frequency" -> monthly,
          "monthlyPayDay" -> monthlyPayDay,
          "sameAmount" -> no
        )
      ).fold(
          formWithErrors => {
            "This mapping should not happen." must equalTo("Error")
          },
          f => {
            f.paid must equalTo(Some(yes))
            f.payDate must equalTo(Some(DayMonthYear(Some(paydateDay), Some(paydateMonth), Some(paydateYear), None, None)))
            f.howOften.frequency must equalTo(monthly)
            f.monthlyPayDay.get must equalTo(monthlyPayDay)
            f.sameAmount must equalTo(Some(no))
          }
        )
    }
  }
  section("unit", models.domain.CircumstancesSelfEmployment.id)
}
