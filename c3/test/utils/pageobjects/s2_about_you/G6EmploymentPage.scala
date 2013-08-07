package utils.pageobjects.s2_about_you

import play.api.test.TestBrowser
import utils.pageobjects.{PageContext, ClaimScenario, Page}

/**
 * PageObject for page s2_about_you g6_employment.
 * @author Jorge Migueis
 *         Date: 17/07/2013
 */
final class G6EmploymentPage (browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G6EmploymentPage.url, G6EmploymentPage.title, previousPage) {
  
    declareYesNo("#beenEmployedSince6MonthsBeforeClaim", "AboutYouHaveYouBeenEmployedAtAnyTime_1")
    declareYesNo("#beenSelfEmployedSince1WeekBeforeClaim", "AboutYouHaveYouBeenSelfEmployedAtAnyTime")
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in Page.scala
 */
object G6EmploymentPage {
  val title = "Employment - About You"
  val url  = "/aboutyou/employment"
  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G6EmploymentPage(browser,previousPage)
}

/** The context for Specs tests */
trait G6EmploymentPageContext extends PageContext {
  this: {val browser:TestBrowser}  =>
  val page = G6EmploymentPage buildPageWith browser
}
