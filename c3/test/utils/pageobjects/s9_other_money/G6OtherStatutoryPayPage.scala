package utils.pageobjects.s9_other_money

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.{PageContext, Page}

final class G6OtherStatutoryPayPage(browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G6OtherStatutoryPayPage.url, G6OtherStatutoryPayPage.title, previousPage) {
  declareYesNo("#otherPay","OtherMoneyHaveYouSMPSinceClaim")
  declareInput("#howMuch", "OtherMOneySMPHowMuch")
  declareSelect("#howOften_frequency", "OtherMOneySMPHowOften")
  declareInput("#howOften_other", "OtherMOneySMPHowOften")
  declareInput("#employersName", "OtherMoneySMPEmployerName")
  declareAddress("#employersAddress", "OtherMoneySMPEmployerAddress")
  declareInput("#employersPostcode", "OtherMoneySMPEmployerPostcode")
}

object G6OtherStatutoryPayPage {
  val title = "Other Statutory Pay - About Other Money".toLowerCase

  val url = "/other-money/other-statutory-pay"

  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G6OtherStatutoryPayPage(browser, previousPage)
}

trait G6OtherStatutoryPayPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G6OtherStatutoryPayPage buildPageWith browser
}