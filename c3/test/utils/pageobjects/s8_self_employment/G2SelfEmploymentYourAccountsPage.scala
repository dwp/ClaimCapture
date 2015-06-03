package utils.pageobjects.s8_self_employment

import utils.WithBrowser
import utils.pageobjects._

final class G2SelfEmploymentYourAccountsPage(ctx:PageObjectsContext) extends ClaimPage(ctx, G2SelfEmploymentYourAccountsPage.url) {
  declareDate("#whatWasOrIsYourTradingYearFrom", "SelfEmployedWhatWasIsYourTradingYearfrom")
  declareDate("#whatWasOrIsYourTradingYearTo", "SelfEmployedWhatWasIsYourTradingYearIs")
  declareYesNo("#areIncomeOutgoingsProfitSimilarToTrading", "SelfEmployedAretheIncomeOutgoingSimilartoYourCurrent")
  declareInput("#tellUsWhyAndWhenTheChangeHappened", "SelfEmployedTellUsWhyandWhentheChangeHappened")
}

object G2SelfEmploymentYourAccountsPage {
  val url = "/self-employment/your-accounts"

  def apply(ctx:PageObjectsContext) = new G2SelfEmploymentYourAccountsPage(ctx)
}

trait G2SelfEmploymentYourAccountsPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G2SelfEmploymentYourAccountsPage (PageObjectsContext(browser))
}