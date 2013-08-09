package utils.pageobjects.s9_other_money

import play.api.test.TestBrowser
import utils.pageobjects.{ClaimScenario, PageContext, Page}

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

  val title = "Other Statutory Pay - Other Money"
  val url = "/otherMoney/otherStatutoryPay"

  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G6OtherStatutoryPayPage(browser, previousPage)
}

trait G6OtherStatutoryPayPageContext extends PageContext {
  this: {val browser: TestBrowser} =>
  val page = G6OtherStatutoryPayPage buildPageWith browser
}


