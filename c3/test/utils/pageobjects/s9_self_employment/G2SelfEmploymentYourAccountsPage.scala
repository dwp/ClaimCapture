package utils.pageobjects.s9_self_employment

import play.api.test.TestBrowser
import utils.pageobjects.{PageContext, ClaimScenario, Page}


final class G2SelfEmploymentYourAccountsPage(browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G2SelfEmploymentYourAccountsPage.url, G2SelfEmploymentYourAccountsPage.title, previousPage) {

    declareDate("#whatWasOrIsYourTradingYearFrom", "SelfEmployedWhatWasIsYourTradingYearfrom")
    declareDate("#whatWasOrIsYourTradingYearTo", "SelfEmployedWhatWasIsYourTradingYearIs")
    declareYesNo("#areAccountsPreparedOnCashFlowBasis", "SelfEmployedAreTheseAccountsPreparedonaCashFlowBasis")
    declareYesNo("#areIncomeOutgoingsProfitSimilarToTrading", "SelfEmployedAretheIncomeOutgoingSimilartoYourCurrent")
    declareInput("#tellUsWhyAndWhenTheChangeHappened", "SelfEmployedTellUsWhyandWhentheChangeHappened")
    declareYesNo("#doYouHaveAnAccountant", "SelfEmployedDoYouHaveAnAccountant")             //SelfEmployedDoYouHaveAnAccountant
    declareYesNo("#canWeContactYourAccountant", "SelfEmployedCanWeContactYourAccountant")

}

object G2SelfEmploymentYourAccountsPage {
  val title = "Self Employment - Your Accounts"
  val url = "/selfEmployment/yourAccounts"

  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G2SelfEmploymentYourAccountsPage(browser, previousPage)
}

trait G2SelfEmploymentYourAccountsPageContext extends PageContext {
  this: {val browser: TestBrowser} =>
  val page = G2SelfEmploymentYourAccountsPage buildPageWith browser
}
