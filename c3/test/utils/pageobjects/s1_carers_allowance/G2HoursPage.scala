package utils.pageobjects.s1_carers_allowance

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.{ClaimPage,Page, PageContext}

final class G2HoursPage(browser: TestBrowser, previousPage: Option[Page] = None) extends ClaimPage(browser, G2HoursPage.url, G2HoursPage.title, previousPage) {
  declareYesNo("#answer", "CanYouGetCarersAllowanceDoYouSpend35HoursorMoreEachWeekCaring")
}

object G2HoursPage {
  val title = "Do you spend 35 hours or more each week caring for the person you care for? - Can you get Carer's Allowance?".toLowerCase

  val url = "/allowance/hours"

  def apply(browser: TestBrowser, previousPage: Option[Page] = None) = new G2HoursPage(browser, previousPage)
}

trait G2HoursPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G2HoursPage (browser)
}