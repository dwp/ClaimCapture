package utils.pageobjects.your_income

import utils.WithBrowser
import utils.pageobjects.{PageContext, ClaimPage, PageObjectsContext}

/**
  * Created by peterwhitehead on 29/03/2016.
  */
class GStatutorySickPayPage(ctx:PageObjectsContext) extends ClaimPage(ctx, GStatutorySickPayPage.url) {
  declareYesNo("#stillBeingPaidThisPay", "StillBeingPaidThisPay")
  declareDate("#whenDidYouLastGetPaid", "WhenDidYouLastGetPaid")
  declareInput("#whoPaidYouThisPay", "WhoPaidYouThisPay")
  declareInput("#amountOfThisPay", "AmountOfThisPay")
  declareRadioList("#howOftenPaidThisPay", "HowOftenPaidThisPay")
  declareInput("#howOftenPaidThisPayOther", "HowOftenPaidThisPayOther")
}

object GStatutorySickPayPage {
  val url  = "/your-income/statutory-sick-pay"

  def apply(ctx:PageObjectsContext) = new GStatutorySickPayPage(ctx)
}

trait GStatutorySickPayPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = GStatutorySickPayPage (PageObjectsContext(browser))
}

