package utils.pageobjects.s9_self_employment

import play.api.test.TestBrowser
import utils.pageobjects.{PageContext, ClaimScenario, Page}


final class G2SelfEmploymentYourAccountsPage(browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G2SelfEmploymentYourAccountsPage.url, G2SelfEmploymentYourAccountsPage.title, previousPage) {

  def fillPageWith(theClaim: ClaimScenario) {
    fillDate("#whatWasOrIsYourTradingYearFrom", theClaim.SelfEmployedWhatWasIsYourTradingYearfrom)
    fillDate("#whatWasOrIsYourTradingYearTo", theClaim.SelfEmployedWhatWasIsYourTradingYearIs)
    fillYesNo("#areAccountsPreparedOnCashFlowBasis", theClaim.SelfEmployedAreTheseAccountsPreparedonaCashFlowBasis)
    fillYesNo("#areIncomeOutgoingsProfitSimilarToTrading", theClaim.SelfEmployedAretheIncomeOutgoingSimilartoYourCurrent)
    fillInput("#tellUsWhyAndWhenTheChangeHappened", theClaim.SelfEmployedTellUsWhyandWhentheChangeHappened)
    fillYesNo("#doYouHaveAnAccountant", theClaim.SelfEmployedDoYouHaveAnAccountant)
    fillYesNo("#canWeContactYourAccountant", theClaim.SelfEmployedCanWeContactYourAccountant)
  }
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
