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
  val howmuch = "£35"
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
  val startedAndFinishedPath = "DWPCAChangeOfCircumstances//EmploymentChange//StartedEmploymentAndFinished//MoreAboutChanges//Answer"
  val nextPageUrl = GEmploymentPensionExpensesPage.url

  val validFinishedWeeklyPaymentEmployment = Seq(
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

  val validFinishedMonthlyPaymentEmployment = Seq(
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

  val validFinishedOtherPaymentEmployment = Seq(
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

  section("unit", models.domain.CircumstancesReportChanges.id)
  "Report an Employment change in your circumstances where the employment is finished - Employment Controller" should {
    "present 'CoC Finished Employment Change'" in new WithApplication with MockForm {

      val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey)

      val result = GEmploymentPay.present(request)
      status(result) mustEqual OK
    }

    "redirect to the next page after valid submission of weekly on going employment" in new WithApplication with MockForm {
      val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey)
        .withFormUrlEncodedBody(validFinishedWeeklyPaymentEmployment: _*)

      val result = GEmploymentPay.submit(request)
      println("PAge result follows:")
      println(contentAsString(result))
      redirectLocation(result) must beSome(nextPageUrl)
    }

    "redirect to the next page after valid submission of monthly on going employment" in new WithApplication with MockForm {
      val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey)
        .withFormUrlEncodedBody(validFinishedMonthlyPaymentEmployment: _*)

      val result = GEmploymentPay.submit(request)
      redirectLocation(result) must beSome(nextPageUrl)
    }

    "redirect to the next page after valid submission of other on going employment" in new WithApplication with MockForm {
      val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey)
        .withFormUrlEncodedBody(validFinishedOtherPaymentEmployment: _*)

      val result = GEmploymentPay.submit(request)
      redirectLocation(result) must beSome(nextPageUrl)
    }
  }
  section("unit", models.domain.CircumstancesReportChanges.id)
}
