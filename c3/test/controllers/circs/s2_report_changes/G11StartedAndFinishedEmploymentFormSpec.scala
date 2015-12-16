package controllers.circs.s2_report_changes

import utils.WithApplication
import org.specs2.mutable._
import models.DayMonthYear

class G11StartedAndFinishedEmploymentFormSpec extends Specification {
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
  val didYouPayIntoPensionText = "pension text"
  val didYouPayForThingsText = "Some things needed to do the job"
  val didCareCostsForThisWorkText = "care text"
  val moreInfo = "more information"

  section("unit", models.domain.CircumstancesSelfEmployment.id)
  "Report an Employment change in your circumstances where the employment is finished - Employment Form" should {
    "map weekly paid with no pension/expenses paid when employer does not owe money" in new WithApplication {
      G11StartedAndFinishedEmployment.form.bind(
        Map(
          "beenPaidYet" -> no,
          "howMuchPaid" -> howMuchPaid,
          "whatWasIncluded" -> whatWasIncluded,
          "dateLastPaid.day" -> dateLastPaidDay.toString,
          "dateLastPaid.month" -> dateLastPaidMonth.toString,
          "dateLastPaid.year" -> dateLastPaidYear.toString,
          "howOften.frequency" -> weekly,
          "usuallyPaidSameAmount" -> no,
          "employerOwesYouMoney" -> no,
          "didYouPayIntoPension.answer" -> no,
          "didYouPayForThings.answer" -> no,
          "didCareCostsForThisWork.answer" -> no
        )
      ).fold(
          formWithErrors => {
            "This mapping should not happen." must equalTo("Error")
          },
          f => {
            f.whatWasIncluded.get must equalTo(whatWasIncluded)
            f.dateLastPaid must equalTo(DayMonthYear(Some(dateLastPaidDay), Some(dateLastPaidMonth), Some(dateLastPaidYear), None, None))
            f.howOften.frequency must equalTo(weekly)
            f.usuallyPaidSameAmount must equalTo(no)
            f.employerOwesYouMoney must equalTo(no)
            f.payIntoPension.answer must equalTo(no)
            f.didYouPayForThings.answer must equalTo(no)
            f.careCostsForThisWork.answer must equalTo(no)
          }
        )
    }

    "map monthly paid with no pension/expenses paid when employer does not owe money" in new WithApplication {
      G11StartedAndFinishedEmployment.form.bind(
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
          "employerOwesYouMoney" -> no,
          "didYouPayIntoPension.answer" -> no,
          "didYouPayForThings.answer" -> no,
          "didCareCostsForThisWork.answer" -> no
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
            f.usuallyPaidSameAmount must equalTo(no)
            f.employerOwesYouMoney must equalTo(no)
            f.payIntoPension.answer must equalTo(no)
            f.didYouPayForThings.answer must equalTo(no)
            f.careCostsForThisWork.answer must equalTo(no)
          }
        )
    }

    "map other paid with pension/expenses paid when employer does owes money and further information is provided" in new WithApplication {
      G11StartedAndFinishedEmployment.form.bind(
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
          "employerOwesYouMoneyInfo" -> employerOwesYouMoneyInfo,
          "didYouPayIntoPension.answer" -> yes,
          "didYouPayIntoPension.whatFor" -> didYouPayIntoPensionText,
          "didYouPayForThings.answer" -> yes,
          "didYouPayForThings.whatFor" -> didYouPayForThingsText,
          "didCareCostsForThisWork.answer" -> yes,
          "didCareCostsForThisWork.whatCosts" -> didCareCostsForThisWorkText,
          "moreAboutChanges" -> moreInfo
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
            f.usuallyPaidSameAmount must equalTo(yes)
            f.employerOwesYouMoney must equalTo(yes)
            f.employerOwesYouMoneyInfo.get must equalTo(employerOwesYouMoneyInfo)
            f.payIntoPension.answer must equalTo(yes)
            f.payIntoPension.text.get must equalTo(didYouPayIntoPensionText)
            f.didYouPayForThings.answer must equalTo(yes)
            f.didYouPayForThings.text.get must equalTo(didYouPayForThingsText)
            f.careCostsForThisWork.answer must equalTo(yes)
            f.careCostsForThisWork.text.get must equalTo(didCareCostsForThisWorkText)
            f.moreAboutChanges.get must equalTo(moreInfo)
          }
        )
    }
  }
  section("unit", models.domain.CircumstancesSelfEmployment.id)
}
