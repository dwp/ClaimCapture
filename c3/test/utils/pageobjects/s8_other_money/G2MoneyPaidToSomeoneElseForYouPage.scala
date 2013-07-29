package utils.pageobjects.s8_other_money

import play.api.test.TestBrowser
import utils.pageobjects.{ClaimScenario, PageContext, Page}

final class G2MoneyPaidToSomeoneElseForYouPage(browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G2MoneyPaidToSomeoneElseForYouPage.url, G2MoneyPaidToSomeoneElseForYouPage.title, previousPage) { 
  def fillPageWith(theClaim: ClaimScenario) {
    fillYesNo("#moneyAddedToBenefitSinceClaimDate", theClaim.OtherMoneyHasAnyoneHadMoneyForBenefitYouClaim)
  }
}

object G2MoneyPaidToSomeoneElseForYouPage {
  val title = "Money Paid - Other Money"
  val url  = "/otherMoney/moneyPaidToSomeoneElseForYou"
  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G2MoneyPaidToSomeoneElseForYouPage(browser,previousPage)
}

trait G2MoneyPaidToSomeoneElseForYouPageContext extends PageContext {
  this: {val browser:TestBrowser}  =>
  val page = G2MoneyPaidToSomeoneElseForYouPage buildPageWith browser
}
