package utils.pageobjects.s7_employment

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.{ClaimPage,Page, PageContext}

final class G0EmploymentPage (browser: TestBrowser, previousPage: Option[Page] = None) extends ClaimPage(browser, G0EmploymentPage.url, G0EmploymentPage.title, previousPage) {
  declareYesNo("#beenEmployedSince6MonthsBeforeClaim", "AboutYouHaveYouBeenEmployedAtAnyTime_1")
  declareYesNo("#beenSelfEmployedSince1WeekBeforeClaim", "AboutYouHaveYouBeenSelfEmployedAtAnyTime")
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in Page.scala
 */
object G0EmploymentPage {
  val title = "Employment - Employment History".toLowerCase

    val url  = "/employment/employment"

    def apply(browser: TestBrowser, previousPage: Option[Page] = None) = new G0EmploymentPage(browser,previousPage)
}

/** The context for Specs tests */
trait G0EmploymentPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G0EmploymentPage (browser)
}