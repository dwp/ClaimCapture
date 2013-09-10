package utils.pageobjects.s9_other_money

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.{ClaimPage,Page, PageContext}

final class G5StatutorySickPayPage(browser: TestBrowser, previousPage: Option[Page] = None) extends ClaimPage(browser, G5StatutorySickPayPage.url, G5StatutorySickPayPage.title, previousPage) {
  declareYesNo("#haveYouHadAnyStatutorySickPay", "OtherMoneyHaveYouSSPSinceClaim")
  declareInput("#howMuch", "OtherMoneySSPHowMuch")
  declareSelect("#howOften_frequency", "OtherMoneySSPHowOften")
  declareInput("#howOften_frequency_other", "OtherMoneySSPHowOftenOther")
  declareInput("#employersName", "OtherMoneySSPEmployerName")
  declareAddress("#employersAddress", "OtherMoneySSPEmployerAddress")
  declareInput("#employersPostcode", "OtherMoneyEmployerPostcode")
}

object G5StatutorySickPayPage {
  val title = "Statutory Sick Pay - About Other Money".toLowerCase

  val url = "/other-money/statutory-sick-pay"

  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G5StatutorySickPayPage(browser, previousPage)
}

trait G5StatutorySickPayPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G5StatutorySickPayPage buildPageWith browser
}