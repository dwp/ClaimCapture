package controllers.circs.s2_report_changes

import org.specs2.mutable.{Tags, Specification}
import models.DayMonthYear

class G10StartedEmploymentAndOngoingFormSpec extends Specification with Tags {
  val yes = "yes"
  val no = "no"
  val amountPaid = "£199.98"
  val whatDatePaidDay = 10
  val whatDatePaidMonth = 11
  val whatDatePaidYear = 2012
  val weekly = "weekly"

  "Report an Employment change in your circumstances where the employment is ongoing - Employment Form" should {
    "map weekly paid with no pension/expenses paid when been paid set to 'yes'" in {
      G10StartedEmploymentAndOngoing.form.bind(
        Map(
          "beenPaidYet" -> yes,
          "howMuchPaid" -> amountPaid,
          "whatDatePaid.day" -> whatDatePaidDay.toString,
          "whatDatePaid.month" -> whatDatePaidMonth.toString,
          "whatDatePaid.year" -> whatDatePaidYear.toString,
          "howOften.frequency" -> weekly,
          "usuallyPaidSameAmount" -> no,
          "doYouPayIntoPension.answer" -> no,
          "doCareCostsForThisWork.answer" -> no
        )
      ).fold(
          formWithErrors => {
            println(formWithErrors)
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

    "map 'other' paid with pension/expenses paid and more information about changes when been paid set to 'no'" in {
      pending
    }
  }
}
