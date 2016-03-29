package utils.pageobjects.s_self_employment

import utils.WithBrowser
import utils.pageobjects._

/*
                                stillSelfEmployed: String = "",
                                moreThanYearAgo: Option[String] = None,
                                startThisWork: Option[DayMonthYear] = None,
                                haveAccounts: Option[String] = None,
                                knowTradingYear: Option[String] = None,
                                tradingYearStart: Option[DayMonthYear] = None,
                                paidMoney: Option[String] = None,
                                paidMoneyDate: Option[DayMonthYear] = None,
                                finishThisWork: Option[DayMonthYear] = None
 */
final class GSelfEmploymentDatesPage(ctx:PageObjectsContext) extends ClaimPage(ctx, GSelfEmploymentDatesPage.url) {
  declareYesNo("#stillSelfEmployed", "SelfEmployedAreYouSelfEmployedNow")
  declareDate("#startThisWork", "SelfEmployedWhenDidYouStartThisJob")
  declareDate("#finishThisWork", "SelfEmployedWhenDidTheJobFinish")
}

object GSelfEmploymentDatesPage {
  val url = "/self-employment/self-employment-dates"

  def apply(ctx:PageObjectsContext) = new GSelfEmploymentDatesPage(ctx)
}

trait GSelfEmploymentDatesPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = GSelfEmploymentDatesPage (PageObjectsContext(browser))
}
