package utils.pageobjects.your_income

import utils.WithBrowser
import utils.pageobjects.{PageContext, ClaimPage, PageObjectsContext}

/**
  * Created by peterwhitehead on 29/03/2016.
  */
class GFosteringAllowancePage(ctx:PageObjectsContext) extends ClaimPage(ctx, GFosteringAllowancePage.url) {
  declareRadioList("#fosteringAllowancePay", "PaymentTypesForThisPay")
  declareInput("#fosteringAllowancePayOther", "PaymentTypesForThisPayOther")
  declareYesNo("#stillBeingPaidThisPay_fosteringAllowance", "StillBeingPaidThisPay")
  declareDate("#whenDidYouLastGetPaid", "WhenDidYouLastGetPaid")
  declareInput("#whoPaidYouThisPay_fosteringAllowance", "WhoPaidYouThisPay")
  declareInput("#amountOfThisPay", "AmountOfThisPay")
  declareRadioList("#howOftenPaidThisPay", "HowOftenPaidThisPay")
  declareInput("#howOftenPaidThisPayOther", "HowOftenPaidThisPayOther")
}

object GFosteringAllowancePage {
  val url  = "/your-income/fostering-allowance"

  def apply(ctx:PageObjectsContext) = new GFosteringAllowancePage(ctx)
}

trait GFosteringAllowancePageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = GFosteringAllowancePage (PageObjectsContext(browser))
}

