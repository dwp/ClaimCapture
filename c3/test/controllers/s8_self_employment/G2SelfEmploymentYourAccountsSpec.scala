package controllers.s8_self_employment

import org.specs2.mutable.{Tags, Specification}
import play.api.test.{FakeRequest, WithApplication}
import models.domain._
import play.api.test.Helpers._
import play.api.cache.Cache
import models.DayMonthYear
import scala.Some


class G2SelfEmploymentYourAccountsSpec extends Specification with Tags{

  "Self Employment - Your Accounts - Controller" should {
    val areAccountsPreparedOnCashFlowBasis = "yes"
    val fromDay = 11
    val fromMonth = 11
    val fromYear = 2011
    val toDay = 11
    val toMonth = 11
    val toYear = 2030
    val areIncomeOutgoingsProfitSimilarToTrading = "yes"
    val tellUsWhyAndWhenTheChangeHappened = "A Year Ago"
    val doYouHaveAnAccountant = "yes"
    val canWeContactYourAccountant = "yes"


    val selfEmploymentYourAccountsInput = Seq("areAccountsPreparedOnCashFlowBasis" -> areAccountsPreparedOnCashFlowBasis,
      "whatWasOrIsYourTradingYearFrom.day" -> fromDay.toString,
      "whatWasOrIsYourTradingYearFrom.month" -> fromMonth.toString,
      "whatWasOrIsYourTradingYearFrom.year" -> fromYear.toString,
      "whatWasOrIsYourTradingYearTo.day" -> toDay.toString,
      "whatWasOrIsYourTradingYearTo.month" -> toMonth.toString,
      "whatWasOrIsYourTradingYearTo.year" -> toYear.toString,
      "areIncomeOutgoingsProfitSimilarToTrading" -> areIncomeOutgoingsProfitSimilarToTrading,
      "tellUsWhyAndWhenTheChangeHappened" -> tellUsWhyAndWhenTheChangeHappened,
      "doYouHaveAnAccountant" -> doYouHaveAnAccountant,
      "canWeContactYourAccountant" -> canWeContactYourAccountant
    )

    "present 'Self Employment Your Accounts' " in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)

      val result = controllers.s8_self_employment.G2SelfEmploymentYourAccounts.present(request)
      status(result) mustEqual OK
    }

    "add submitted form to the cached claim" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody(selfEmploymentYourAccountsInput: _*)

      val result = controllers.s8_self_employment.G2SelfEmploymentYourAccounts.submit(request)
      val claim = Cache.getAs[Claim](claimKey).get
      val section: Section = claim.section(models.domain.SelfEmployment)
      section.questionGroup(SelfEmploymentYourAccounts) must beLike {
        case Some(f: SelfEmploymentYourAccounts) => {
          f.areAccountsPreparedOnCashFlowBasis must equalTo(areAccountsPreparedOnCashFlowBasis)
          f.whatWasOrIsYourTradingYearFrom must equalTo(Some(DayMonthYear(Some(fromDay), Some(fromMonth), Some(fromYear), None, None)))
          f.whatWasOrIsYourTradingYearTo must equalTo(Some(DayMonthYear(Some(toDay), Some(toMonth), Some(toYear), None, None)))
          f.areIncomeOutgoingsProfitSimilarToTrading must equalTo(Some(areIncomeOutgoingsProfitSimilarToTrading))
          f.tellUsWhyAndWhenTheChangeHappened must equalTo(Some(tellUsWhyAndWhenTheChangeHappened))
          f.doYouHaveAnAccountant must equalTo(Some(doYouHaveAnAccountant))
          f.canWeContactYourAccountant must equalTo(Some(canWeContactYourAccountant))
        }
      }
    }

    "missing mandatory field" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody("areAccountsPreparedOnCashFlowBasis" -> "")

      val result = controllers.s8_self_employment.G2SelfEmploymentYourAccounts.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "redirect to the next page after a valid submission" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody(selfEmploymentYourAccountsInput: _*)

      val result = controllers.s8_self_employment.G2SelfEmploymentYourAccounts.submit(request)
      status(result) mustEqual SEE_OTHER
    }

  }
}
