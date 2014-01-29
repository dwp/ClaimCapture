package utils.pageobjects.s7_employment

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.{ClaimPage,Page, PageContext}

final class G1EmploymentPage (browser: TestBrowser, previousPage: Option[Page] = None) extends ClaimPage(browser, G1EmploymentPage.url, G1EmploymentPage.title, previousPage) {
  declareYesNo("#beenEmployedSince6MonthsBeforeClaim", "EmploymentHaveYouBeenEmployedAtAnyTime")
  declareYesNo("#beenSelfEmployedSince1WeekBeforeClaim", "EmploymentHaveYouBeenSelfEmployedAtAnyTime")
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in Page.scala
 */
object G1EmploymentPage {
  val title = "Employment Employment History".toLowerCase

  val url  = "/employment/employment"

  def apply(browser: TestBrowser, previousPage: Option[Page] = None) = new G1EmploymentPage(browser,previousPage)
}

/** The context for Specs tests */
trait G1EmploymentPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G1EmploymentPage (browser)
}