package utils.pageobjects.s_employment

import utils.WithBrowser
import utils.pageobjects._

final class GLastWagePage(ctx:PageObjectsContext, iteration:Int) extends ClaimPage(ctx, s"${GLastWagePage.url}/${iteration.toString}",iteration) {
  declareSelect("#oftenGetPaid_frequency","EmploymentAddtionalWageHowOftenAreYouPaid_" + iteration)
  declareInput("#oftenGetPaid_frequency_other","EmploymentAddtionalWageOther_" + iteration)
  declareInput("#whenGetPaid","EmploymentAddtionalWageWhenDoYouGetPaid_" + iteration)
  declareDate("#lastPaidDate", "EmploymentWhenWereYouLastPaid_" + iteration)
  declareInput("#grossPay", "EmploymentWhatWasTheGrossPayForTheLastPayPeriod_" + iteration)
  declareInput("#payInclusions", "EmploymentWhatWasIncludedInYourLastPay_" + iteration)
  declareYesNo("#sameAmountEachTime", "EmploymentDoYouGettheSameAmountEachTime_" + iteration)
  declareYesNo("#employerOwesYouMoney","EmploymentAdditionalWageDoesYourEmployerOweYouAnyMoney_" + iteration)
}

object GLastWagePage {
  val url  = "/your-income/employment/last-wage"

  def apply(ctx:PageObjectsContext, iteration:Int=1) = new GLastWagePage(ctx,iteration)
}

trait GLastWagePageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = GLastWagePage (PageObjectsContext(browser),iteration = 1)
}
