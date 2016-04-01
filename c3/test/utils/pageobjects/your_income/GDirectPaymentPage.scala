package utils.pageobjects.your_income

import utils.WithBrowser
import utils.pageobjects.{PageContext, ClaimPage, PageObjectsContext}

/**
  * Created by peterwhitehead on 29/03/2016.
  */
class GDirectPaymentPage(ctx:PageObjectsContext) extends ClaimPage(ctx, GDirectPaymentPage.url) {
  declareYesNo("#stillBeingPaidThisPay_directPayment", "StillBeingPaidThisPay")
  declareDate("#whenDidYouLastGetPaid", "WhenDidYouLastGetPaid")
  declareInput("#whoPaidYouThisPay_directPayment", "WhoPaidYouThisPay")
  declareInput("#amountOfThisPay", "AmountOfThisPay")
  declareRadioList("#howOftenPaidThisPay", "HowOftenPaidThisPay")
  declareInput("#howOftenPaidThisPayOther", "HowOftenPaidThisPayOther")
}

object GDirectPaymentPage {
  val url  = "/your-income/direct-payment"

  def apply(ctx:PageObjectsContext) = new GDirectPaymentPage(ctx)
}

trait GDirectPaymentPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = GDirectPaymentPage (PageObjectsContext(browser))
}

