package utils.pageobjects.s9_other_money

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.{PageContext, Page}

final class G2MoneyPaidToSomeoneElseForYouPage(browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G2MoneyPaidToSomeoneElseForYouPage.url, G2MoneyPaidToSomeoneElseForYouPage.title, previousPage) {

    declareYesNo("#moneyAddedToBenefitSinceClaimDate", "OtherMoneyHasAnyoneHadMoneyForBenefitYouClaim")

}

object G2MoneyPaidToSomeoneElseForYouPage {
  val title = "Money Paid - Other Money"
  val url  = "/otherMoney/moneyPaidToSomeoneElseForYou"
  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G2MoneyPaidToSomeoneElseForYouPage(browser,previousPage)
}

trait G2MoneyPaidToSomeoneElseForYouPageContext extends PageContext {
  this: WithBrowser[_] =>
  val page = G2MoneyPaidToSomeoneElseForYouPage buildPageWith browser
}