package utils.pageobjects.your_income

import utils.WithBrowser
import utils.pageobjects.{PageContext, ClaimPage, PageObjectsContext}

/**
  * Created by peterwhitehead on 29/03/2016.
  */
class GStatutoryMaternityPaternityAdoptionPayPage(ctx:PageObjectsContext) extends ClaimPage(ctx, GStatutoryMaternityPaternityAdoptionPayPage.url) {
  declareRadioList("#paymentTypesForThisPay", "PaymentTypesForThisPay")
  declareYesNo("#stillBeingPaidThisPay_paternityMaternityAdoption", "StillBeingPaidThisPay")
  declareDate("#whenDidYouLastGetPaid", "WhenDidYouLastGetPaid")
  declareInput("#whoPaidYouThisPay_paternityMaternityAdoption", "WhoPaidYouThisPay")
  declareInput("#amountOfThisPay", "AmountOfThisPay")
  declareRadioList("#howOftenPaidThisPay", "HowOftenPaidThisPay")
  declareInput("#howOftenPaidThisPayOther", "HowOftenPaidThisPayOther")
}

object GStatutoryMaternityPaternityAdoptionPayPage {
  val url  = "/your-income/smp-spa-sap"

  def apply(ctx:PageObjectsContext) = new GStatutoryMaternityPaternityAdoptionPayPage(ctx)
}

trait GStatutoryMaternityPaternityAdoptionPayPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = GStatutoryMaternityPaternityAdoptionPayPage (PageObjectsContext(browser))
}

