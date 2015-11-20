package utils.pageobjects.s_self_employment

import utils.WithBrowser
import utils.pageobjects._

final class GSelfEmploymentYourAccountsPage(ctx:PageObjectsContext) extends ClaimPage(ctx, GSelfEmploymentYourAccountsPage.url) {
  declareYesNo("#doYouKnowYourTradingYear", "SelfEmployedDoYouKnowYourTradingYear")
  declareDate("#whatWasOrIsYourTradingYearFrom", "SelfEmployedWhatWasIsYourTradingYearfrom")
  declareDate("#whatWasOrIsYourTradingYearTo", "SelfEmployedWhatWasIsYourTradingYearIs")
  declareYesNo("#areIncomeOutgoingsProfitSimilarToTrading", "SelfEmployedAretheIncomeOutgoingSimilartoYourCurrent")
  declareInput("#tellUsWhyAndWhenTheChangeHappened", "SelfEmployedTellUsWhyandWhentheChangeHappened")
}

object GSelfEmploymentYourAccountsPage {
  val url = "/self-employment/your-accounts"

  def apply(ctx:PageObjectsContext) = new GSelfEmploymentYourAccountsPage(ctx)
}

trait GSelfEmploymentYourAccountsPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = GSelfEmploymentYourAccountsPage (PageObjectsContext(browser))
}
