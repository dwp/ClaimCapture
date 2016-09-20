package controllers.circs.report_changes

import org.specs2.mutable._
import play.api.test.FakeRequest
import models.domain.MockForm
import utils.pageobjects.circumstances.report_changes.GEmploymentPensionExpensesPage
import utils.pageobjects.circumstances.start_of_process.GCircsYourDetailsPage
import utils.{WithApplication}
import models.view.CachedChangeOfCircs
import play.api.test.Helpers._

class GStartedAndFinishedEmploymentSpec extends Specification {
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
  val startedAndFinishedPath = "DWPCAChangeOfCircumstances//EmploymentChange//StartedEmploymentAndFinished//MoreAboutChanges//Answer"
  val nextPageUrl = GEmploymentPensionExpensesPage.url

  val validFinishedWeeklyPaymentEmployment = Seq(
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
    "employerOwesYouMoney" -> no
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
    "employerOwesYouMoneyInfo" -> employerOwesYouMoneyInfo
  )

  section("unit", models.domain.CircumstancesReportChanges.id)
  "Report an Employment change in your circumstances where the employment is finished - Employment Controller" should {
    "present 'CoC Finished Employment Change'" in new WithApplication with MockForm {

      val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey)

      val result = GStartedAndFinishedEmployment.present(request)
      status(result) mustEqual OK
    }

    "redirect to the next page after valid submission of weekly on going employment" in new WithApplication with MockForm {
      val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey)
        .withFormUrlEncodedBody(validFinishedWeeklyPaymentEmployment: _*)

      val result = GStartedAndFinishedEmployment.submit(request)
      redirectLocation(result) must beSome(nextPageUrl)
    }

    "redirect to the next page after valid submission of monthly on going employment" in new WithApplication with MockForm {
      val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey)
        .withFormUrlEncodedBody(validFinishedMonthlyPaymentEmployment: _*)

      val result = GStartedAndFinishedEmployment.submit(request)
      redirectLocation(result) must beSome(nextPageUrl)
    }

    "redirect to the next page after valid submission of other on going employment" in new WithApplication with MockForm {
      val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey)
        .withFormUrlEncodedBody(validFinishedOtherPaymentEmployment: _*)

      val result = GStartedAndFinishedEmployment.submit(request)
      redirectLocation(result) must beSome(nextPageUrl)
    }
  }
  section("unit", models.domain.CircumstancesReportChanges.id)
}
