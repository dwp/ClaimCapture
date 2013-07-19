package utils.pageobjects.s8_other_money

import play.api.test.TestBrowser
import utils.pageobjects.{ClaimScenario, PageContext, Page}


final class G3PersonWhoGetsThisMoneyPage(browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G2MoneyPaidToSomeoneElseForYouPage.url, G2MoneyPaidToSomeoneElseForYouPage.title, previousPage) {
  def fillPageWith(theClaim: ClaimScenario) {
    fillInput("#fullName", theClaim.OtherMoneyG3FullName)
    fillInput("#nameOfBenefit", theClaim.OtherMoneyG3NameOfBenefit)
  }
}

object G3PersonWhoGetsThisMoneyPage {
  val title = "Person Who Gets This Money - Other Money"
  val url  = "/otherMoney/personWhoGetsThisMoney"
  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G2MoneyPaidToSomeoneElseForYouPage(browser,previousPage)
}

trait G3PersonWhoGetsThisMoneyPageContext extends PageContext {
  this: {val browser:TestBrowser}  =>
  val page = G3PersonWhoGetsThisMoneyPage buildPageWith browser
}

