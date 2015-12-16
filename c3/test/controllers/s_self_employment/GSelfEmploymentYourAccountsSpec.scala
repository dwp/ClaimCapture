package controllers.s_self_employment

import models.DayMonthYear
import models.domain._
import org.specs2.mutable._
import play.api.test.Helpers._
import play.api.test.FakeRequest
import utils.WithApplication

class GSelfEmploymentYourAccountsSpec extends Specification{
  section("unit", models.domain.SelfEmployment.id)
  "Self Employment - Your Accounts - Controller" should {
    val fromDay = 11
    val fromMonth = 11
    val fromYear = 2011
    val toDay = 11
    val toMonth = 11
    val toYear = 2030
    val areIncomeOutgoingsProfitSimilarToTrading = "yes"
    val tellUsWhyAndWhenTheChangeHappened = "A Year Ago"
    val doYouKnowYourTradingYear = "yes"

    val selfEmploymentYourAccountsInput = Seq(
      "doYouKnowYourTradingYear" -> doYouKnowYourTradingYear,
      "whatWasOrIsYourTradingYearFrom.day" -> fromDay.toString,
      "whatWasOrIsYourTradingYearFrom.month" -> fromMonth.toString,
      "whatWasOrIsYourTradingYearFrom.year" -> fromYear.toString,
      "whatWasOrIsYourTradingYearTo.day" -> toDay.toString,
      "whatWasOrIsYourTradingYearTo.month" -> toMonth.toString,
      "whatWasOrIsYourTradingYearTo.year" -> toYear.toString,
      "areIncomeOutgoingsProfitSimilarToTrading" -> areIncomeOutgoingsProfitSimilarToTrading,
      "tellUsWhyAndWhenTheChangeHappened" -> tellUsWhyAndWhenTheChangeHappened
    )

    "present 'Self Employment Your Accounts' " in new WithApplication with Claiming {
      val request = FakeRequest()

      val result = controllers.s_self_employment.GSelfEmploymentYourAccounts.present(request)
      status(result) mustEqual OK
    }

    "add submitted form to the cached claim" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody(selfEmploymentYourAccountsInput: _*)

      val result = controllers.s_self_employment.GSelfEmploymentYourAccounts.submit(request)
      val claim = getClaimFromCache(result)
      val section: Section = claim.section(models.domain.SelfEmployment)

      section.questionGroup(SelfEmploymentYourAccounts) must beLike {
        case Some(f: SelfEmploymentYourAccounts) => {
          f.doYouKnowYourTradingYear must equalTo(doYouKnowYourTradingYear)
          f.whatWasOrIsYourTradingYearFrom must equalTo(Some(DayMonthYear(Some(fromDay), Some(fromMonth), Some(fromYear), None, None)))
          f.whatWasOrIsYourTradingYearTo must equalTo(Some(DayMonthYear(Some(toDay), Some(toMonth), Some(toYear), None, None)))
          f.areIncomeOutgoingsProfitSimilarToTrading must equalTo(Some(areIncomeOutgoingsProfitSimilarToTrading))
          f.tellUsWhyAndWhenTheChangeHappened must equalTo(Some(tellUsWhyAndWhenTheChangeHappened))
        }
      }
    }

    "redirect to the next page after a valid submission" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody(selfEmploymentYourAccountsInput: _*)

      val result = controllers.s_self_employment.GSelfEmploymentYourAccounts.submit(request)
      status(result) mustEqual SEE_OTHER
    }
  }
  section("unit", models.domain.SelfEmployment.id)
}
