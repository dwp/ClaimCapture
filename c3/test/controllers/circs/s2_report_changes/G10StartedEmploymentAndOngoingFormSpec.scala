package controllers.circs.s2_report_changes

import utils.WithApplication
import org.specs2.mutable._
import models.DayMonthYear

class G10StartedEmploymentAndOngoingFormSpec extends Specification {
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
  val doYouPayIntoPensionText = "pension text"
  val doYouPayForThingsText = "Some expenses to do the job"
  val doCareCostsForThisWorkText = "care text"
  val moreInfo = "more information"

  section("unit", models.domain.CircumstancesSelfEmployment.id)
  "Report an Employment change in your circumstances where the employment is ongoing - Employment Form" should {
    "map weekly paid with no pension/expenses paid when been paid set to 'yes'" in new WithApplication {
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
          "doYouPayForThings.answer" -> no,
          "doCareCostsForThisWork.answer" -> no
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
            f.payIntoPension.answer must equalTo(no)
            f.doYouPayForThings.answer must equalTo(no)
            f.careCostsForThisWork.answer must equalTo(no)
          }
        )
    }

    "map 'other' paid with pension/expenses paid and more information about changes when been paid set to 'no'" in new WithApplication {
      G10StartedEmploymentAndOngoing.form.bind(
        Map(
          "beenPaidYet" -> no,
          "howMuchPaid" -> amountPaid,
          "whatDatePaid.day" -> whatDatePaidDay.toString,
          "whatDatePaid.month" -> whatDatePaidMonth.toString,
          "whatDatePaid.year" -> whatDatePaidYear.toString,
          "howOften.frequency" -> other,
          "howOften.frequency.other" -> otherText,
          "usuallyPaidSameAmount" -> yes,
          "doYouPayIntoPension.answer" -> yes,
          "doYouPayIntoPension.whatFor" -> doYouPayIntoPensionText,
          "doYouPayForThings.answer" -> yes,
          "doYouPayForThings.whatFor" -> doYouPayForThingsText,
          "doCareCostsForThisWork.answer" -> yes,
          "doCareCostsForThisWork.whatCosts" -> doCareCostsForThisWorkText,
          "moreAboutChanges" -> moreInfo
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
            f.payIntoPension.answer must equalTo(yes)
            f.payIntoPension.text.get must equalTo(doYouPayIntoPensionText)
            f.doYouPayForThings.answer must equalTo(yes)
            f.doYouPayForThings.text.get must equalTo(doYouPayForThingsText)
            f.careCostsForThisWork.answer must equalTo(yes)
            f.careCostsForThisWork.text.get must equalTo(doCareCostsForThisWorkText)
            f.moreAboutChanges.get must equalTo(moreInfo)
          }
        )
    }

    "map monthly paid with no pension/expenses paid when been paid set to 'yes'" in new WithApplication {
      G10StartedEmploymentAndOngoing.form.bind(
        Map(
          "beenPaidYet" -> yes,
          "howMuchPaid" -> amountPaid,
          "whatDatePaid.day" -> whatDatePaidDay.toString,
          "whatDatePaid.month" -> whatDatePaidMonth.toString,
          "whatDatePaid.year" -> whatDatePaidYear.toString,
          "howOften.frequency" -> monthly,
          "monthlyPayDay" -> monthlyPayDay,
          "usuallyPaidSameAmount" -> no,
          "doYouPayIntoPension.answer" -> no,
          "doYouPayForThings.answer" -> no,
          "doCareCostsForThisWork.answer" -> no
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
            f.payIntoPension.answer must equalTo(no)
            f.doYouPayForThings.answer must equalTo(no)
            f.careCostsForThisWork.answer must equalTo(no)
          }
        )
    }
  }
  section("unit", models.domain.CircumstancesSelfEmployment.id)
}
