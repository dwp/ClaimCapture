package controllers.circs.report_changes

import app.ConfigProperties._
import org.specs2.mutable._
import play.api.test.FakeRequest
import models.domain.MockForm
import utils.pageobjects.circumstances.start_of_process.GCircsYourDetailsPage
import utils.{WithBrowser, LightFakeApplication, WithApplication}
import models.view.CachedChangeOfCircs
import play.api.test.Helpers._
import utils.pageobjects.circumstances.report_changes.GEmploymentNotStartedPage

class GEmploymentNotStartedSpec extends Specification {
  val yes = "yes"
  val no = "no"
  val amountPaid = "£199.98"
  val whenExpectedToBePaidDateDay = 10
  val whenExpectedToBePaidDateMonth = 11
  val whenExpectedToBePaidDateYear = 2012
  val weekly = "weekly"
  val monthly = "monthly"
  val monthlyPayDay = "2nd Thursday every month"
  val other = "other"
  val otherText = "some other text"
  val willYouPayIntoPensionText = "pension text"
  val willYouPayForThingsText = "Some things needed to do the job"
  val willCareCostsForThisWorkText = "care text"
  val moreInfo = "more information"
  val employmentNoStartedPath = "DWPCAChangeOfCircumstances//EmploymentChange//NotStartedEmployment//MoreAboutChanges//Answer"
  val nextPageUrl = GCircsYourDetailsPage.url

  "Report an Employment change in your circumstances where employment has not started - Employment Controller" should {
    val validWeeklyPaymentEmployment = Seq(
      "beenPaidYet" -> yes,
      "howMuchPaid" -> amountPaid,
      "whenExpectedToBePaidDate.day" -> whenExpectedToBePaidDateDay.toString,
      "whenExpectedToBePaidDate.month" -> whenExpectedToBePaidDateMonth.toString,
      "whenExpectedToBePaidDate.year" -> whenExpectedToBePaidDateYear.toString,
      "howOften.frequency" -> weekly,
      "usuallyPaidSameAmount" -> no,
      "willYouPayIntoPension.answer" -> no,
      "willYouPayForThings.answer" -> no,
      "willCareCostsForThisWork.answer" -> no
    )

    val validMonthlyPaymentEmployment = Seq(
      "beenPaidYet" -> yes,
      "howMuchPaid" -> amountPaid,
      "whenExpectedToBePaidDate.day" -> whenExpectedToBePaidDateDay.toString,
      "whenExpectedToBePaidDate.month" -> whenExpectedToBePaidDateMonth.toString,
      "whenExpectedToBePaidDate.year" -> whenExpectedToBePaidDateYear.toString,
      "howOften.frequency" -> monthly,
      "monthlyPayDay" -> monthlyPayDay,
      "usuallyPaidSameAmount" -> no,
      "willYouPayIntoPension.answer" -> no,
      "willYouPayForThings.answer" -> no,
      "willCareCostsForThisWork.answer" -> no
    )

    val validOtherPaymentEmployment = Seq(
      "beenPaidYet" -> no,
      "howMuchPaid" -> amountPaid,
      "whenExpectedToBePaidDate.day" -> whenExpectedToBePaidDateDay.toString,
      "whenExpectedToBePaidDate.month" -> whenExpectedToBePaidDateMonth.toString,
      "whenExpectedToBePaidDate.year" -> whenExpectedToBePaidDateYear.toString,
      "howOften.frequency" -> other,
      "howOften.frequency.other" -> otherText,
      "usuallyPaidSameAmount" -> yes,
      "willYouPayIntoPension.answer" -> yes,
      "willYouPayIntoPension.whatFor" -> willYouPayIntoPensionText,
      "willYouPayForThings.answer" -> yes,
      "willYouPayForThings.whatFor" -> willYouPayForThingsText,
      "willCareCostsForThisWork.answer" -> yes,
      "willCareCostsForThisWork.whatCosts" -> willCareCostsForThisWorkText,
      "moreAboutChanges" -> moreInfo
    )

    section("unit", models.domain.CircumstancesSelfEmployment.id)
    "Report an Employment change in your circumstances where the employment is ongoing - Employment - Controller" should {
      "present 'CoC Future Employment Change'" in new WithApplication with MockForm {

        val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey)

        val result = GEmploymentNotStarted.present(request)
        status(result) mustEqual OK
      }

      "redirect to the next page after valid submission of employment with expected weekly payment" in new WithApplication with MockForm {
        val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey)
          .withFormUrlEncodedBody(validWeeklyPaymentEmployment: _*)

        val result = GEmploymentNotStarted.submit(request)
        redirectLocation(result) must beSome(nextPageUrl)
      }

      "redirect to the next page after valid submission of employment with expected monthly payment" in new WithApplication with MockForm {
        val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey)
          .withFormUrlEncodedBody(validMonthlyPaymentEmployment: _*)

        val result = GEmploymentNotStarted.submit(request)
        redirectLocation(result) must beSome(nextPageUrl)
      }

      "redirect to the next page after valid submission of employment with expected other payment" in new WithApplication with MockForm {
        val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey)
          .withFormUrlEncodedBody(validOtherPaymentEmployment: _*)

        val result = GEmploymentNotStarted.submit(request)
        redirectLocation(result) must beSome(nextPageUrl)
      }
    }

    "handle gracefully when bad schema number passed to SchemaValidation getRestriction" in new WithApplication {
      val schemaVersion = "BAD-SCHEMA"
      schemaMaxLength(schemaVersion, employmentNoStartedPath) mustEqual -1
    }

    "pull maxlength from xml commons OK" in new WithApplication {
      val schemaVersion = getProperty("xml.schema.version", "NOT-SET")
      schemaVersion must not be "NOT-SET"
      schemaMaxLength(schemaVersion, employmentNoStartedPath) mustEqual 3000
    }

    "have text maxlength set correctly in present()" in new WithBrowser {
      browser.goTo(GEmploymentNotStartedPage.url)
      val anythingElse = browser.$("#moreAboutChanges")
      val countdown = browser.$("#moreAboutChanges + .countdown")

      anythingElse.getAttribute("maxlength") mustEqual "3000"
      countdown.getText must contain( "3000 char")
      browser.pageSource must contain("maxChars:3000")
    }
  }
  section("unit", models.domain.CircumstancesSelfEmployment.id)
}
