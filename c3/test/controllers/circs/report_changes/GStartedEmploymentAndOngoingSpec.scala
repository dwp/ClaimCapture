package controllers.circs.report_changes

import controllers.mappings.Mappings
import org.specs2.mutable._
import play.api.test.FakeRequest
import models.domain.MockForm
import models.view.CachedChangeOfCircs
import play.api.test.Helpers._
import utils.pageobjects.circumstances.report_changes.GEmploymentPensionExpensesPage
import utils.pageobjects.circumstances.start_of_process.GCircsYourDetailsPage
import utils.{LightFakeApplication, WithApplication}
import play.api.i18n.{MMessages, MessagesApi}
import play.api.Play.current

class GStartedEmploymentAndOngoingSpec extends Specification {
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
  val nextPageUrl = GEmploymentPensionExpensesPage.url

  val validOngoingWeeklyPaymentEmployment = Seq(
    "pastpresentfuture" -> "present",
    "paid" -> yes,
    "howmuch" -> amountPaid,
    "paydate.day" -> paydateDay.toString,
    "paydate.month" -> paydateMonth.toString,
    "paydate.year" -> paydateYear.toString,
    "howOften.frequency" -> weekly,
    "sameAmount" -> no
  )

  val validOngoingMonthlyPaymentEmployment = Seq(
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

  val validOngoingOtherPaymentEmployment = Seq(
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

  val invalidOngoingOtherPaymentEmployment = Seq(
    "pastpresentfuture" -> "present",
    "paid" -> no,
    "howmuch" -> amountPaid,
    "paydate.month" -> paydateMonth.toString,
    "paydate.year" -> paydateYear.toString,
    "sameAmount" -> yes
  )

  section("unit", models.domain.CircumstancesReportChanges.id)
  "Report an Employment change in your circumstances where the employment is ongoing - Employment - Controller" should {
    "present 'CoC Ongoing Employment Change'" in new WithApplication(app = LightFakeApplication.faCEATrue) with MockForm {
      val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey)

      val result = GEmploymentPay.present(request)
      status(result) mustEqual OK
    }

    "redirect to the next page after valid submission of weekly on going employment" in new WithApplication(app = LightFakeApplication.faCEATrue) with MockForm {
      val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey)
        .withFormUrlEncodedBody(validOngoingWeeklyPaymentEmployment: _*)

      val result = GEmploymentPay.submit(request)
      redirectLocation(result) must beSome(nextPageUrl)
    }

    "redirect to the next page after valid submission of monthly on going employment" in new WithApplication(app = LightFakeApplication.faCEATrue) with MockForm {
      val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey)
        .withFormUrlEncodedBody(validOngoingMonthlyPaymentEmployment: _*)

      val result = GEmploymentPay.submit(request)
      redirectLocation(result) must beSome(nextPageUrl)
    }

    "redirect to the next page after valid submission of other on going employment" in new WithApplication(app = LightFakeApplication.faCEATrue) with MockForm {
      val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey)
        .withFormUrlEncodedBody(validOngoingOtherPaymentEmployment: _*)

      val result = GEmploymentPay.submit(request)
      redirectLocation(result) must beSome(nextPageUrl)
    }

    "raise errors and stay same page if mandatory fields missing" in new WithApplication(app = LightFakeApplication.faCEATrue) with MockForm {
      val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey)
        .withFormUrlEncodedBody(invalidOngoingOtherPaymentEmployment: _*)

      val result = GEmploymentPay.submit(request)
      val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
      contentAsString(result) must contain(messagesApi(Mappings.errorRequired))
    }
  }
  section("unit", models.domain.CircumstancesReportChanges.id)
}
