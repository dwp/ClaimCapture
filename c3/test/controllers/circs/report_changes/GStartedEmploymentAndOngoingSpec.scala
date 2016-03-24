package controllers.circs.report_changes

import app.ConfigProperties._
import controllers.mappings.Mappings
import org.specs2.mutable._
import play.api.test.FakeRequest
import models.domain.MockForm
import models.view.CachedChangeOfCircs
import play.api.test.Helpers._
import utils.pageobjects.circumstances.report_changes.GStartedEmploymentAndOngoingPage
import utils.pageobjects.circumstances.start_of_process.GCircsYourDetailsPage
import utils.{WithBrowser, LightFakeApplication, WithApplication}
import play.api.i18n.{MMessages, MessagesApi}
import play.api.Play.current

class GStartedEmploymentAndOngoingSpec extends Specification {
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
  val ongoingPath = "DWPCAChangeOfCircumstances//EmploymentChange//StartedEmploymentAndOngoing//MoreAboutChanges//Answer"
  val nextPageUrl = GCircsYourDetailsPage.url

  val validOngoingWeeklyPaymentEmployment = Seq(
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

  val validOngoingMonthlyPaymentEmployment = Seq(
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

  val validOngoingOtherPaymentEmployment = Seq(
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

  val invalidOngoingOtherPaymentEmployment = Seq(
    "beenPaidYet" -> no,
    "howMuchPaid" -> amountPaid,
    "whatDatePaid.month" -> whatDatePaidMonth.toString,
    "whatDatePaid.year" -> whatDatePaidYear.toString,
    "usuallyPaidSameAmount" -> yes,
    "moreAboutChanges" -> moreInfo
  )

  section("unit", models.domain.CircumstancesReportChanges.id)
  "Report an Employment change in your circumstances where the employment is ongoing - Employment - Controller" should {
    "present 'CoC Ongoing Employment Change'" in new WithApplication(app = LightFakeApplication.faCEATrue) with MockForm {
      val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey)

      val result = GStartedEmploymentAndOngoing.present(request)
      status(result) mustEqual OK
    }

    "redirect to the next page after valid submission of weekly on going employment" in new WithApplication(app = LightFakeApplication.faCEATrue) with MockForm {
      val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey)
        .withFormUrlEncodedBody(validOngoingWeeklyPaymentEmployment: _*)

      val result = GStartedEmploymentAndOngoing.submit(request)
      redirectLocation(result) must beSome(nextPageUrl)
    }

    "redirect to the next page after valid submission of monthly on going employment" in new WithApplication(app = LightFakeApplication.faCEATrue) with MockForm {
      val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey)
        .withFormUrlEncodedBody(validOngoingMonthlyPaymentEmployment: _*)

      val result = GStartedEmploymentAndOngoing.submit(request)
      redirectLocation(result) must beSome(nextPageUrl)
    }

    "redirect to the next page after valid submission of other on going employment" in new WithApplication(app = LightFakeApplication.faCEATrue) with MockForm {
      val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey)
        .withFormUrlEncodedBody(validOngoingOtherPaymentEmployment: _*)

      val result = GStartedEmploymentAndOngoing.submit(request)
      redirectLocation(result) must beSome(nextPageUrl)
    }

    "raise errors and stay same page if mandatory fields missing" in new WithApplication(app = LightFakeApplication.faCEATrue) with MockForm {
      val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey)
        .withFormUrlEncodedBody(invalidOngoingOtherPaymentEmployment: _*)

      val result = GStartedEmploymentAndOngoing.submit(request)
      val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
      contentAsString(result) must contain(messagesApi(Mappings.errorRequired))
    }

    "handle gracefully when bad schema number passed to SchemaValidation getRestriction" in new WithApplication {
      val schemaVersion = "BAD-SCHEMA"
      schemaMaxLength(schemaVersion, ongoingPath) mustEqual -1
    }

    "pull maxlength from xml commons OK" in new WithApplication {
      val schemaVersion = getProperty("xml.schema.version", "NOT-SET")
      schemaVersion must not be "NOT-SET"
      schemaMaxLength(schemaVersion, ongoingPath) mustEqual 3000
    }

    "have text maxlength set correctly in present()" in new WithBrowser {
      browser.goTo(GStartedEmploymentAndOngoingPage.url)
      val anythingElse = browser.$("#moreAboutChanges")
      val countdown = browser.$("#moreAboutChanges + .countdown")

      anythingElse.getAttribute("maxlength") mustEqual "3000"
      countdown.getText must contain("3000 char")
      browser.pageSource must contain("maxChars:3000")
    }
  }
  section("unit", models.domain.CircumstancesReportChanges.id)
}
