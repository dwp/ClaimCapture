package utils.pageobjects.s9_other_money

import play.api.test.TestBrowser
import utils.pageobjects.{ClaimScenario, PageContext, Page}

final class G5StatutorySickPayPage(browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G5StatutorySickPayPage.url, G5StatutorySickPayPage.title, previousPage) {

    declareYesNo("#haveYouHadAnyStatutorySickPay", "OtherMoneyHaveYouSSPSinceClaim")
    declareInput("#howMuch", "OtherMoneySSPHowMuch")
    declareInput("#howOften_frequency", "OtherMoneySSPHowOften")
    declareInput("#employersName", "OtherMoneySSPEmployerName")
    declareAddress("#employersAddress", "OtherMoneySSPEmployerAddress")
    declareInput("#employersPostcode", "OtherMoneyEmployerPostcode")

}

object G5StatutorySickPayPage {
  val title = "Statutory Sick Pay - Other Money"
  val url = "/otherMoney/statutorySickPay"

  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G5StatutorySickPayPage(browser, previousPage)
}

trait G5StatutorySickPayPageContext extends PageContext {
  this: {val browser: TestBrowser} =>
  val page = G5StatutorySickPayPage buildPageWith browser
}
