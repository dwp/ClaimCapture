package controllers.circs.s2_report_changes

import org.specs2.mutable.{Tags, Specification}
import play.api.test.{FakeRequest, FakeApplication, WithApplication}
import models.domain.MockForm
import models.view.CachedChangeOfCircs
import play.api.test.Helpers._
import play.api.test.FakeApplication

class G11StartedAndFinishedEmploymentSpec extends Specification with Tags {
  val yes = "yes"
  val no = "no"
  val howMuchPaid = "£35"
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
  val doYouPayIntoPensionText = "pension text"
  val doCareCostsForThisWorkText = "care text"
  val moreInfo = "more information"

  val validFinishedWeeklyPaymentEmployment = Seq(
    "beenPaidYet" -> no,
    "howMuchPaid" -> howMuchPaid,
    "whatWasIncluded" -> whatWasIncluded,
    "dateLastPaid.day" -> dateLastPaidDay.toString,
    "dateLastPaid.month" -> dateLastPaidMonth.toString,
    "dateLastPaid.year" -> dateLastPaidYear.toString,
    "howOften.frequency" -> weekly,
    "usuallyPaidSameAmount" -> no,
    "employerOwesYouMoney" -> no,
    "doYouPayIntoPension.answer" -> no,
    "doCareCostsForThisWork.answer" -> no
  )

  val validFinishedMonthlyPaymentEmployment = Seq(
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
    "doYouPayIntoPension.answer" -> no,
    "doCareCostsForThisWork.answer" -> no
  )

  val validFinishedOtherPaymentEmployment = Seq(
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
    "doYouPayIntoPension.answer" -> yes,
    "doYouPayIntoPension.whatFor" -> doYouPayIntoPensionText,
    "doCareCostsForThisWork.answer" -> yes,
    "doCareCostsForThisWork.whatCosts" -> doCareCostsForThisWorkText,
    "moreAboutChanges" -> moreInfo
  )

  "Report an Employment change in your circumstances where the employment is finished - Employment Controller" should {
    "present 'CoC Finished Employment Change'" in new WithApplication(app = FakeApplication(additionalConfiguration = Map("circs.employment.active" -> "true"))) with MockForm {
      val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey)

      val result = G11StartedAndFinishedEmployment.present(request)
      status(result) mustEqual OK
    }

    "redirect to the next page after valid submission of weekly on going employment" in new WithApplication(app = FakeApplication(additionalConfiguration = Map("circs.employment.active" -> "true"))) with MockForm {
      val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey)
        .withFormUrlEncodedBody(validFinishedWeeklyPaymentEmployment: _*)

      val result = G11StartedAndFinishedEmployment.submit(request)
      redirectLocation(result) must beSome("/circumstances/consent-and-declaration/declaration")
    }

    "redirect to the next page after valid submission of monthly on going employment" in new WithApplication(app = FakeApplication(additionalConfiguration = Map("circs.employment.active" -> "true"))) with MockForm {
      pending
      val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey)
        .withFormUrlEncodedBody(validFinishedMonthlyPaymentEmployment: _*)

      val result = G11StartedAndFinishedEmployment.submit(request)
      redirectLocation(result) must beSome("/circumstances/consent-and-declaration/declaration")
    }

    "redirect to the next page after valid submission of other on going employment" in new WithApplication(app = FakeApplication(additionalConfiguration = Map("circs.employment.active" -> "true"))) with MockForm {
      pending
      val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey)
        .withFormUrlEncodedBody(validFinishedOtherPaymentEmployment: _*)

      val result = G11StartedAndFinishedEmployment.submit(request)
      redirectLocation(result) must beSome("/circumstances/consent-and-declaration/declaration")
    }

  } section("unit", models.domain.CircumstancesReportChanges.id)
}
