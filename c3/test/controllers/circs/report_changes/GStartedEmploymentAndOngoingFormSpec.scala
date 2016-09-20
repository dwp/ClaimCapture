package controllers.circs.report_changes

import utils.WithApplication
import org.specs2.mutable._
import models.DayMonthYear

class GStartedEmploymentAndOngoingFormSpec extends Specification {
  val yes = "yes"
  val no = "no"
  val amountPaid = "£199.98"
  val whatDatePaidDay = 10
  val whatDatePaidMonth = 11
  val whatDatePaidYear = 2012
  val weekly = "weekly"
  val monthly = "monthly"
  val monthlyPayDay = "2nd Thursday every month"
  val other = "other"
  val otherText = "some other text"

  section("unit", models.domain.CircumstancesSelfEmployment.id)
  "Report an Employment change in your circumstances where the employment is ongoing - Employment Form" should {
    "map weekly paid with no pension/expenses paid when been paid set to 'yes'" in new WithApplication {
      GStartedEmploymentAndOngoing.form.bind(
        Map(
          "beenPaidYet" -> yes,
          "howMuchPaid" -> amountPaid,
          "whatDatePaid.day" -> whatDatePaidDay.toString,
          "whatDatePaid.month" -> whatDatePaidMonth.toString,
          "whatDatePaid.year" -> whatDatePaidYear.toString,
          "howOften.frequency" -> weekly,
          "usuallyPaidSameAmount" -> no
        )
      ).fold(
          formWithErrors => {
            "This mapping should not happen." must equalTo("Error")
          },
          f => {
            f.beenPaid must equalTo(yes)
            f.date must equalTo(DayMonthYear(Some(whatDatePaidDay), Some(whatDatePaidMonth), Some(whatDatePaidYear), None, None))
            f.howOften.frequency must equalTo(weekly)
            f.usuallyPaidSameAmount must equalTo(no)
          }
        )
    }

    "map 'other' paid with pension/expenses paid and more information about changes when been paid set to 'no'" in new WithApplication {
      GStartedEmploymentAndOngoing.form.bind(
        Map(
          "beenPaidYet" -> no,
          "howMuchPaid" -> amountPaid,
          "whatDatePaid.day" -> whatDatePaidDay.toString,
          "whatDatePaid.month" -> whatDatePaidMonth.toString,
          "whatDatePaid.year" -> whatDatePaidYear.toString,
          "howOften.frequency" -> other,
          "howOften.frequency.other" -> otherText,
          "usuallyPaidSameAmount" -> yes
        )
      ).fold(
          formWithErrors => {
            "This mapping should not happen." must equalTo("Error")
          },
          f => {
            f.beenPaid must equalTo(no)
            f.date must equalTo(DayMonthYear(Some(whatDatePaidDay), Some(whatDatePaidMonth), Some(whatDatePaidYear), None, None))
            f.howOften.frequency must equalTo(other)
            f.howOften.other.get must equalTo(otherText)
            f.usuallyPaidSameAmount must equalTo(yes)
          }
        )
    }

    "map monthly paid with no pension/expenses paid when been paid set to 'yes'" in new WithApplication {
      GStartedEmploymentAndOngoing.form.bind(
        Map(
          "beenPaidYet" -> yes,
          "howMuchPaid" -> amountPaid,
          "whatDatePaid.day" -> whatDatePaidDay.toString,
          "whatDatePaid.month" -> whatDatePaidMonth.toString,
          "whatDatePaid.year" -> whatDatePaidYear.toString,
          "howOften.frequency" -> monthly,
          "monthlyPayDay" -> monthlyPayDay,
          "usuallyPaidSameAmount" -> no
        )
      ).fold(
          formWithErrors => {
            "This mapping should not happen." must equalTo("Error")
          },
          f => {
            f.beenPaid must equalTo(yes)
            f.date must equalTo(DayMonthYear(Some(whatDatePaidDay), Some(whatDatePaidMonth), Some(whatDatePaidYear), None, None))
            f.howOften.frequency must equalTo(monthly)
            f.monthlyPayDay.get must equalTo(monthlyPayDay)
            f.usuallyPaidSameAmount must equalTo(no)
          }
        )
    }
  }
  section("unit", models.domain.CircumstancesSelfEmployment.id)
}
