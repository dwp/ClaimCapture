package utils.pageobjects.your_income

import utils.WithBrowser
import utils.pageobjects.{PageContext, ClaimPage, PageObjectsContext}

/**
  * Created by peterwhitehead on 29/03/2016.
  */
class GStatutorySickPayPage(ctx:PageObjectsContext) extends ClaimPage(ctx, GStatutorySickPayPage.url) {
  declareYesNo("#stillBeingPaidStatutorySickPay", "StatutorySickPayStillBeingPaidStatutorySickPay")
  declareDate("#whenDidYouLastGetPaid", "StatutorySickPayWhenDidYouLastGetPaid")
  declareInput("#whoPaidYouStatutorySickPay", "StatutorySickPayWhoPaidYouStatutorySickPay")
  declareInput("#amountOfStatutorySickPay", "StatutorySickPayAmountOfStatutorySickPay")
  declareRadioList("#howOftenPaidStatutorySickPay", "StatutorySickPayHowOftenPaidStatutorySickPay")
  declareInput("#howOftenPaidStatutorySickPayOther", "StatutorySickPayHowOftenPaidStatutorySickPayOther")
}

object GStatutorySickPayPage {
  val url  = "/your-income/statutory-sick-pay"

  def apply(ctx:PageObjectsContext) = new GStatutorySickPayPage(ctx)
}

trait GStatutorySickPayPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = GStatutorySickPayPage (PageObjectsContext(browser))
}

