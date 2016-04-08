package utils.pageobjects.s_self_employment

import utils.WithBrowser
import utils.pageobjects._

final class GSelfEmploymentDatesPage(ctx:PageObjectsContext) extends ClaimPage(ctx, GSelfEmploymentDatesPage.url) {
  declareYesNo("#stillSelfEmployed", "SelfEmployedAreYouSelfEmployedNow")
  declareDate("#startThisWork", "SelfEmployedWhenDidYouStartThisJob")
  declareDate("#finishThisWork", "SelfEmployedWhenDidTheJobFinish")
  declareYesNo("#moreThanYearAgo", "SelfEmployedMoreThanYearAgo")
  declareYesNo("#haveAccounts", "SelfEmployedHaveAccounts")
  declareYesNo("#knowTradingYear", "SelfEmployedKnowTradingYear")
  declareDate("#tradingYearStart", "SelfEmployedTradingYearStart")
  declareYesNo("#paidMoney", "SelfEmployedPaidMoneyYet")
  declareDate("#paidMoneyDate", "SelfEmployedPaidMoneyDate")
}

object GSelfEmploymentDatesPage {
  val url = "/your-income/self-employment/self-employment-dates"

  def apply(ctx:PageObjectsContext) = new GSelfEmploymentDatesPage(ctx)
}

trait GSelfEmploymentDatesPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = GSelfEmploymentDatesPage (PageObjectsContext(browser))
}
