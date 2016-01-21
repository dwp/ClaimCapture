package controllers.circs.report_changes

import org.specs2.mutable._
import play.api.test.FakeRequest
import models.domain.MockForm
import utils.{LightFakeApplication, WithApplication}
import models.view.CachedChangeOfCircs
import play.api.test.Helpers._

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
      "present 'CoC Future Employment Change'" in new WithApplication(app = LightFakeApplication(additionalConfiguration = Map("circs.employment.active" -> "true"))) with MockForm {

        val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey)

        val result = GEmploymentNotStarted.present(request)
        status(result) mustEqual OK
      }

      "redirect to the next page after valid submission of employment with expected weekly payment" in new WithApplication(app = LightFakeApplication(additionalConfiguration = Map("circs.employment.active" -> "true"))) with MockForm {
        val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey)
          .withFormUrlEncodedBody(validWeeklyPaymentEmployment: _*)

        val result = GEmploymentNotStarted.submit(request)
        redirectLocation(result) must beSome("/circumstances/consent-and-declaration/declaration")
      }

      "redirect to the next page after valid submission of employment with expected monthly payment" in new WithApplication(app = LightFakeApplication(additionalConfiguration = Map("circs.employment.active" -> "true"))) with MockForm {
        val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey)
          .withFormUrlEncodedBody(validMonthlyPaymentEmployment: _*)

        val result = GEmploymentNotStarted.submit(request)
        redirectLocation(result) must beSome("/circumstances/consent-and-declaration/declaration")
      }

      "redirect to the next page after valid submission of employment with expected other payment" in new WithApplication(app = LightFakeApplication(additionalConfiguration = Map("circs.employment.active" -> "true"))) with MockForm {
        val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey)
          .withFormUrlEncodedBody(validOtherPaymentEmployment: _*)

        val result = GEmploymentNotStarted.submit(request)
        redirectLocation(result) must beSome("/circumstances/consent-and-declaration/declaration")
      }
    }
  }
  section("unit", models.domain.CircumstancesSelfEmployment.id)
}
