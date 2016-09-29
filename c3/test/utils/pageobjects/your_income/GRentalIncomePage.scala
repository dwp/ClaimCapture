package utils.pageobjects.your_income

import utils.WithBrowser
import utils.pageobjects.{PageContext, ClaimPage, PageObjectsContext}

class GRentalIncomePage(ctx:PageObjectsContext) extends ClaimPage(ctx, GRentalIncomePage.url) {
  declareInput("#rentalIncomeInfo", "RentalIncomeInfo")
}

object GRentalIncomePage {
  val url  = "/your-income/rental-income"

  def apply(ctx:PageObjectsContext) = new GRentalIncomePage(ctx)
}

trait GRentalIncomePageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = GRentalIncomePage (PageObjectsContext(browser))
}

