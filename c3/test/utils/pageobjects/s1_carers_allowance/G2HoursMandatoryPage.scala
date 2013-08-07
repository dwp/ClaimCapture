package utils.pageobjects.s1_carers_allowance

import play.api.test.TestBrowser
import utils.pageobjects.{PageContext, ClaimScenario, Page}

final class G2HoursMandatoryPage(browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G2HoursMandatoryPage.url, G2HoursMandatoryPage.title, previousPage) {
    declareYesNo("#answer", "CanYouGetCarersAllowanceDoYouSpend35HoursorMoreEachWeekCaring")
}

object G2HoursMandatoryPage {
  val title = "Hours - Carer's Allowance"
  val url = "/allowance/hoursMandatory"

  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G2HoursMandatoryPage(browser, previousPage)
}

trait G2HoursMandatoryPageContext extends PageContext {
  this: {val browser: TestBrowser} =>
  val page = G2HoursMandatoryPage buildPageWith browser
}
