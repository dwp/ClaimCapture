package controllers.circs.report_changes

import utils.WithApplication
import org.specs2.mutable._
import models.DayMonthYear

class GStartedAndFinishedEmploymentFormSpec extends Specification {
  val yes = "yes"
  val no = "no"
  val howmuch = "£50"
  val whatWasIncluded = "not enough"
  val paydateDay = 10
  val paydateMonth = 11
  val paydateYear = 2012
  val weekly = "weekly"
  val monthly = "monthly"
  val monthlyPayDay = "2nd Thursday every month"
  val other = "other"
  val otherText = "some other text"
  val owedMoneyInfo = "kick back for keeping my mouth shut"

  section("unit", models.domain.CircumstancesSelfEmployment.id)
  "Report an Employment change in your circumstances where the employment is finished - Employment Form" should {
    "map weekly paid with no pension/expenses paid when employer does not owe money" in new WithApplication {
      GEmploymentPay.form.bind(
        Map(
          "pastpresentfuture" -> "past",
          "paid" -> no,
          "howmuch" -> howmuch,
          "whatWasIncluded" -> whatWasIncluded,
          "paydate.day" -> paydateDay.toString,
          "paydate.month" -> paydateMonth.toString,
          "paydate.year" -> paydateYear.toString,
          "howOften.frequency" -> weekly,
          "sameAmount" -> no,
          "owedMoney" -> no
        )
      ).fold(
          formWithErrors => {
            "This mapping should not happen." must equalTo("Error")
          },
          f => {
            f.whatWasIncluded.get must equalTo(whatWasIncluded)
            f.payDate must equalTo(Some(DayMonthYear(Some(paydateDay), Some(paydateMonth), Some(paydateYear), None, None)))
            f.howOften.frequency must equalTo(weekly)
            f.sameAmount must equalTo(Some(no))
            f.owedMoney must equalTo(Some(no))
          }
        )
    }

    "map monthly paid with no pension/expenses paid when employer does not owe money" in new WithApplication {
      GEmploymentPay.form.bind(
        Map(
          "pastpresentfuture" -> "past",
          "paid" -> no,
          "howmuch" -> howmuch,
          "whatWasIncluded" -> whatWasIncluded,
          "paydate.day" -> paydateDay.toString,
          "paydate.month" -> paydateMonth.toString,
          "paydate.year" -> paydateYear.toString,
          "howOften.frequency" -> monthly,
          "monthlyPayDay" -> monthlyPayDay,
          "sameAmount" -> no,
          "owedMoney" -> no
        )
      ).fold(
          formWithErrors => {
            "This mapping should not happen." must equalTo("Error")
          },
          f => {
            f.whatWasIncluded.get must equalTo(whatWasIncluded)
            f.payDate must equalTo(Some(DayMonthYear(Some(paydateDay), Some(paydateMonth), Some(paydateYear), None, None)))
            f.howOften.frequency must equalTo(monthly)
            f.monthlyPayDay.get must equalTo(monthlyPayDay)
            f.sameAmount must equalTo(Some(no))
            f.owedMoney must equalTo(Some(no))
          }
        )
    }

    "map other paid with pension/expenses paid when employer does owes money and further information is provided" in new WithApplication {
      GEmploymentPay.form.bind(
        Map(
          "pastpresentfuture" -> "past",
          "paid" -> yes,
          "howmuch" -> howmuch,
          "whatWasIncluded" -> whatWasIncluded,
          "paydate.day" -> paydateDay.toString,
          "paydate.month" -> paydateMonth.toString,
          "paydate.year" -> paydateYear.toString,
          "howOften.frequency" -> other,
          "howOften.frequency.other" -> otherText,
          "sameAmount" -> yes,
          "owedMoney" -> yes,
          "owedMoneyInfo" -> owedMoneyInfo
        )
      ).fold(
          formWithErrors => {
            "This mapping should not happen." must equalTo("Error")
          },
          f => {
            f.whatWasIncluded.get must equalTo(whatWasIncluded)
            f.payDate must equalTo(Some(DayMonthYear(Some(paydateDay), Some(paydateMonth), Some(paydateYear), None, None)))
            f.howOften.frequency must equalTo(other)
            f.howOften.other.get must equalTo(otherText)
            f.sameAmount must equalTo(Some(yes))
            f.owedMoney must equalTo(Some(yes))
            f.owedMoneyInfo.get must equalTo(owedMoneyInfo)
          }
        )
    }
  }
  section("unit", models.domain.CircumstancesSelfEmployment.id)
}
