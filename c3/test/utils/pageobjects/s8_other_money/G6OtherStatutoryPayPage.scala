package utils.pageobjects.s8_other_money

import play.api.test.TestBrowser
import utils.pageobjects.{ClaimScenario, PageContext, Page}

final class G6OtherStatutoryPayPage(browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G6OtherStatutoryPayPage.url, G6OtherStatutoryPayPage.title, previousPage) {

  def fillPageWith(theClaim: ClaimScenario) {

  }
}

object G6OtherStatutoryPayPage {

  val title = "Other Statutory Pay - Other Money"
  val url = "/otherMoney/otherStatutoryPay"

  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G6OtherStatutoryPayPage(browser, previousPage)
}

trait G6OtherStatutoryPayPageContext extends PageContext {
  this: {val browser: TestBrowser} =>
  val page = G6OtherStatutoryPayPage buildPageWith browser
}


