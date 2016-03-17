package controllers.circs.report_changes

import app.ConfigProperties._
import org.specs2.mutable._
import play.api.test.FakeRequest
import models.domain.MockForm
import utils.pageobjects.circumstances.report_changes.GStartedAndFinishedEmploymentPage
import utils.pageobjects.circumstances.start_of_process.GCircsYourDetailsPage
import utils.{WithBrowser, LightFakeApplication, WithApplication}
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
  val didYouPayIntoPensionText = "pension text"
  val didYouPayForThingsText = "Some things neeeded to do the job"
  val didCareCostsForThisWorkText = "care text"
  val moreInfo = "more information"
  val startedAndFinishedPath = "DWPCAChangeOfCircumstances//EmploymentChange//StartedEmploymentAndFinished//MoreAboutChanges//Answer"
  val nextPageUrl = GCircsYourDetailsPage.url

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
    "didYouPayIntoPension.answer" -> no,
    "didYouPayForThings.answer" -> no,
    "didCareCostsForThisWork.answer" -> no
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
    "didYouPayIntoPension.answer" -> no,
    "didYouPayForThings.answer" -> no,
    "didCareCostsForThisWork.answer" -> no
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
    "didYouPayIntoPension.answer" -> yes,
    "didYouPayIntoPension.whatFor" -> didYouPayIntoPensionText,
    "didYouPayForThings.answer" -> yes,
    "didYouPayForThings.whatFor" -> didYouPayForThingsText,
    "didCareCostsForThisWork.answer" -> yes,
    "didCareCostsForThisWork.whatCosts" -> didCareCostsForThisWorkText,
    "moreAboutChanges" -> moreInfo
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

    "handle gracefully when bad schema number passed to SchemaValidation getRestriction" in new WithApplication {
      val schemaVersion = "BAD-SCHEMA"
      schemaMaxLength(schemaVersion, startedAndFinishedPath) mustEqual -1
    }

    "pull maxlength from xml commons OK" in new WithApplication {
      val schemaVersion = getProperty("xml.schema.version", "NOT-SET")
      schemaVersion must not be "NOT-SET"
      schemaMaxLength(schemaVersion, startedAndFinishedPath) mustEqual 3000
    }

    "have text maxlength set correctly in present()" in new WithBrowser {
      browser.goTo(GStartedAndFinishedEmploymentPage.url)
      val anythingElse = browser.$("#moreAboutChanges")
      val countdown = browser.$("#moreAboutChanges + .countdown")

      anythingElse.getAttribute("maxlength") mustEqual "3000"
      countdown.getText must contain("3000 char")
      browser.pageSource must contain("maxChars:3000")
    }
  }
  section("unit", models.domain.CircumstancesReportChanges.id)
}
