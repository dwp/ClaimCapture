package utils.pageobjects.your_income

import utils.WithBrowser
import utils.pageobjects.{PageContext, ClaimPage, PageObjectsContext}

/**
  * Created by peterwhitehead on 29/03/2016.
  */
class GYourIncomePage(ctx:PageObjectsContext) extends ClaimPage(ctx, GYourIncomePage.url) {
  declareYesNo("#beenEmployedSince6MonthsBeforeClaim", "EmploymentHaveYouBeenEmployedAtAnyTime_0")
  declareYesNo("#beenSelfEmployedSince1WeekBeforeClaim", "EmploymentHaveYouBeenSelfEmployedAtAnyTime")
  declareCheck("#yourIncome_sickpay", "YourIncomeStatutorySickPay")
  declareCheck("#yourIncome_patmatadoppay", "YourIncomePatMatAdopPay")
  declareCheck("#yourIncome_fostering", "YourIncomeFosteringAllowance")
  declareCheck("#yourIncome_directpay", "YourIncomeDirectPay")
  declareCheck("#yourIncome_anyother", "YourIncomeAnyOtherPay")
  declareCheck("#yourIncome_none", "YourIncomeNone")
}

object GYourIncomePage {
  val url  = "/your-income/your-income"

  def apply(ctx:PageObjectsContext) = new GYourIncomePage(ctx)
}

trait GYourIncomePageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = GYourIncomePage (PageObjectsContext(browser))
}

