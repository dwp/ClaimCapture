package utils.pageobjects.s1_carers_allowance

import play.api.test.TestBrowser
import utils.pageobjects.{PageContext, ClaimScenario, Page}

final class G2HoursPage(browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G2HoursPage.url, G2HoursPage.title, previousPage){

  declareYesNo("#answer", "CanYouGetCarersAllowanceDoYouSpend35HoursorMoreEachWeekCaring")
}

object G2HoursPage {
  val title = "Hours - Can you get Carer's Allowance?"
  val url = "/allowance/hours"

  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G2HoursPage(browser, previousPage)
}

trait G2HoursPageContext extends PageContext {
  this: {val browser: TestBrowser} =>
  val page = G2HoursPage buildPageWith browser
}
