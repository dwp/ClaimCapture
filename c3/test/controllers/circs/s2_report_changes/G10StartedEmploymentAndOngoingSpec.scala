package controllers.circs.s2_report_changes

import controllers.mappings.Mappings
import org.specs2.mutable.{Tags, Specification}
import play.api.i18n.Messages
import play.api.test.{FakeRequest}
import models.domain.MockForm
import models.view.CachedChangeOfCircs
import play.api.test.Helpers._
import play.api.test.FakeApplication
import utils.pageobjects.circumstances.s3_consent_and_declaration.G1DeclarationPage
import utils.{LightFakeApplication, WithApplication}

class G10StartedEmploymentAndOngoingSpec extends Specification with Tags {
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

  "Report an Employment change in your circumstances where the employment is ongoing - Employment - Controller" should {
    "present 'CoC Ongoing Employment Change'" in new WithApplication(app = LightFakeApplication(additionalConfiguration = Map("circs.employment.active" -> "true"))) with MockForm {
      val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey)

      val result = G10StartedEmploymentAndOngoing.present(request)
      status(result) mustEqual OK
    }

    "redirect to the next page after valid submission of weekly on going employment" in new WithApplication(app = FakeApplication(additionalConfiguration = Map("circs.employment.active" -> "true"))) with MockForm {
      val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey)
        .withFormUrlEncodedBody(validOngoingWeeklyPaymentEmployment: _*)

      val result = G10StartedEmploymentAndOngoing.submit(request)
      redirectLocation(result) must beSome(G1DeclarationPage.url)
    }

    "redirect to the next page after valid submission of monthly on going employment" in new WithApplication(app = LightFakeApplication(additionalConfiguration = Map("circs.employment.active" -> "true"))) with MockForm {
      val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey)
        .withFormUrlEncodedBody(validOngoingMonthlyPaymentEmployment: _*)

      val result = G10StartedEmploymentAndOngoing.submit(request)
      redirectLocation(result) must beSome(G1DeclarationPage.url)
    }

    "redirect to the next page after valid submission of other on going employment" in new WithApplication(app = LightFakeApplication(additionalConfiguration = Map("circs.employment.active" -> "true"))) with MockForm {
      val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey)
        .withFormUrlEncodedBody(validOngoingOtherPaymentEmployment: _*)

      val result = G10StartedEmploymentAndOngoing.submit(request)
      redirectLocation(result) must beSome(G1DeclarationPage.url)
    }

    "raise errors and stay same page if mandatory fields missing" in new WithApplication(app = LightFakeApplication(additionalConfiguration = Map("circs.employment.active" -> "true"))) with MockForm {
      val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey)
        .withFormUrlEncodedBody(invalidOngoingOtherPaymentEmployment: _*)

      val result = G10StartedEmploymentAndOngoing.submit(request)
      contentAsString(result) must contain(Messages(Mappings.errorRequired))
    }
  } section("unit", models.domain.CircumstancesReportChanges.id)
}
