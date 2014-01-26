package utils.pageobjects.s2_about_you

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.{TestData, ClaimPage, Page, PageContext}

/**
 * PageObject for page s2_about_you g9_employment.
 * @author Jorge Migueis
 *         Date: 17/07/2013
 */
final class G9EmploymentPage (browser: TestBrowser, previousPage: Option[Page] = None) extends ClaimPage(browser, G9EmploymentPage.url, G9EmploymentPage.title, previousPage) {
  declareYesNo("#beenEmployedSince6MonthsBeforeClaim", "AboutYouHaveYouBeenEmployedAtAnyTime_1")
  declareYesNo("#beenSelfEmployedSince1WeekBeforeClaim", "AboutYouHaveYouBeenSelfEmployedAtAnyTime")
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in Page.scala
 */
object G9EmploymentPage {
  val title = "Employment - About you - the carer".toLowerCase

  val url  = "/about-you/employment"

  def apply(browser: TestBrowser, previousPage: Option[Page] = None) = new G9EmploymentPage(browser,previousPage)
}

/** The context for Specs tests */
trait G9EmploymentPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G9EmploymentPage (browser)
}