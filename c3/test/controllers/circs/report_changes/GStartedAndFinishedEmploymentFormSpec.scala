package controllers.circs.report_changes

import utils.WithApplication
import org.specs2.mutable._
import models.DayMonthYear

class GStartedAndFinishedEmploymentFormSpec extends Specification {
  val yes = "yes"
  val no = "no"
  val howMuchPaid = "£50"
  val whatWasIncluded = "not enough"
  val dateLastPaidDay = 10
  val dateLastPaidMonth = 11
  val dateLastPaidYear = 2012
  val weekly = "weekly"
  val monthly = "monthly"
  val monthlyPayDay = "2nd Thursday every month"
  val other = "other"
  val otherText = "some other text"
  val employerOwesYouMoneyInfo = "kick back for keeping my mouth shut"

  section("unit", models.domain.CircumstancesSelfEmployment.id)
  "Report an Employment change in your circumstances where the employment is finished - Employment Form" should {
    "map weekly paid with no pension/expenses paid when employer does not owe money" in new WithApplication {
      GStartedAndFinishedEmployment.form.bind(
        Map(
          "beenPaidYet" -> no,
          "howMuchPaid" -> howMuchPaid,
          "whatWasIncluded" -> whatWasIncluded,
          "dateLastPaid.day" -> dateLastPaidDay.toString,
          "dateLastPaid.month" -> dateLastPaidMonth.toString,
          "dateLastPaid.year" -> dateLastPaidYear.toString,
          "howOften.frequency" -> weekly,
          "usuallyPaidSameAmount" -> no,
          "employerOwesYouMoney" -> no
        )
      ).fold(
          formWithErrors => {
            "This mapping should not happen." must equalTo("Error")
          },
          f => {
            f.whatWasIncluded.get must equalTo(whatWasIncluded)
            f.dateLastPaid must equalTo(DayMonthYear(Some(dateLastPaidDay), Some(dateLastPaidMonth), Some(dateLastPaidYear), None, None))
            f.howOften.frequency must equalTo(weekly)
            f.usuallyPaidSameAmount must equalTo(Some(no))
            f.employerOwesYouMoney must equalTo(no)
          }
        )
    }

    "map monthly paid with no pension/expenses paid when employer does not owe money" in new WithApplication {
      GStartedAndFinishedEmployment.form.bind(
        Map(
          "beenPaidYet" -> no,
          "howMuchPaid" -> howMuchPaid,
          "whatWasIncluded" -> whatWasIncluded,
          "dateLastPaid.day" -> dateLastPaidDay.toString,
          "dateLastPaid.month" -> dateLastPaidMonth.toString,
          "dateLastPaid.year" -> dateLastPaidYear.toString,
          "howOften.frequency" -> monthly,
          "monthlyPayDay" -> monthlyPayDay,
          "usuallyPaidSameAmount" -> no,
          "employerOwesYouMoney" -> no
        )
      ).fold(
          formWithErrors => {
            "This mapping should not happen." must equalTo("Error")
          },
          f => {
            f.whatWasIncluded.get must equalTo(whatWasIncluded)
            f.dateLastPaid must equalTo(DayMonthYear(Some(dateLastPaidDay), Some(dateLastPaidMonth), Some(dateLastPaidYear), None, None))
            f.howOften.frequency must equalTo(monthly)
            f.monthlyPayDay.get must equalTo(monthlyPayDay)
            f.usuallyPaidSameAmount must equalTo(Some(no))
            f.employerOwesYouMoney must equalTo(no)
          }
        )
    }

    "map other paid with pension/expenses paid when employer does owes money and further information is provided" in new WithApplication {
      GStartedAndFinishedEmployment.form.bind(
        Map(
          "beenPaidYet" -> yes,
          "howMuchPaid" -> howMuchPaid,
          "whatWasIncluded" -> whatWasIncluded,
          "dateLastPaid.day" -> dateLastPaidDay.toString,
          "dateLastPaid.month" -> dateLastPaidMonth.toString,
          "dateLastPaid.year" -> dateLastPaidYear.toString,
          "howOften.frequency" -> other,
          "howOften.frequency.other" -> otherText,
          "usuallyPaidSameAmount" -> yes,
          "employerOwesYouMoney" -> yes,
          "employerOwesYouMoneyInfo" -> employerOwesYouMoneyInfo
        )
      ).fold(
          formWithErrors => {
            "This mapping should not happen." must equalTo("Error")
          },
          f => {
            f.whatWasIncluded.get must equalTo(whatWasIncluded)
            f.dateLastPaid must equalTo(DayMonthYear(Some(dateLastPaidDay), Some(dateLastPaidMonth), Some(dateLastPaidYear), None, None))
            f.howOften.frequency must equalTo(other)
            f.howOften.other.get must equalTo(otherText)
            f.usuallyPaidSameAmount must equalTo(Some(yes))
            f.employerOwesYouMoney must equalTo(yes)
            f.employerOwesYouMoneyInfo.get must equalTo(employerOwesYouMoneyInfo)
          }
        )
    }
  }
  section("unit", models.domain.CircumstancesSelfEmployment.id)
}
