package controllers.circs.s2_report_changes

import org.specs2.mutable._
import play.api.test.FakeRequest
import models.domain.MockForm
import utils.{LightFakeApplication, WithApplication}
import models.view.CachedChangeOfCircs
import play.api.test.Helpers._

class G11StartedAndFinishedEmploymentSpec extends Specification {
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
    "present 'CoC Finished Employment Change'" in new WithApplication(app = LightFakeApplication(additionalConfiguration = Map("circs.employment.active" -> "true"))) with MockForm {

      val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey)

      val result = G11StartedAndFinishedEmployment.present(request)
      status(result) mustEqual OK
    }

    "redirect to the next page after valid submission of weekly on going employment" in new WithApplication(app = LightFakeApplication(additionalConfiguration = Map("circs.employment.active" -> "true"))) with MockForm {
      val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey)
        .withFormUrlEncodedBody(validFinishedWeeklyPaymentEmployment: _*)

      val result = G11StartedAndFinishedEmployment.submit(request)
      redirectLocation(result) must beSome("/circumstances/consent-and-declaration/declaration")
    }

    "redirect to the next page after valid submission of monthly on going employment" in new WithApplication(app = LightFakeApplication(additionalConfiguration = Map("circs.employment.active" -> "true"))) with MockForm {
      pending

      val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey)
        .withFormUrlEncodedBody(validFinishedMonthlyPaymentEmployment: _*)

      val result = G11StartedAndFinishedEmployment.submit(request)
      redirectLocation(result) must beSome("/circumstances/consent-and-declaration/declaration")
    }

    "redirect to the next page after valid submission of other on going employment" in new WithApplication(app = LightFakeApplication(additionalConfiguration = Map("circs.employment.active" -> "true"))) with MockForm {
      pending

      val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey)
        .withFormUrlEncodedBody(validFinishedOtherPaymentEmployment: _*)

      val result = G11StartedAndFinishedEmployment.submit(request)
      redirectLocation(result) must beSome("/circumstances/consent-and-declaration/declaration")
    }
  }
  section("unit", models.domain.CircumstancesReportChanges.id)
}
