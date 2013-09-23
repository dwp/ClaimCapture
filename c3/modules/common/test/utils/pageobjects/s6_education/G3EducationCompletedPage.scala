package utils.pageobjects.s6_education

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.{ClaimPage,Page, PageContext}

/**
 * TODO write description
 * @author Jorge Migueis
 *         Date: 06/08/2013
 */
class G3EducationCompletedPage (browser: TestBrowser, previousPage: Option[Page] = None) extends ClaimPage(browser, G3EducationCompletedPage.url, G3EducationCompletedPage.title, previousPage) {

}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in PageFactory.scala
 */
object G3EducationCompletedPage {
  val title = "Completion - About your education".toLowerCase
  val url  = "/education/completed"
  def apply(browser: TestBrowser, previousPage: Option[Page] = None) = new G3EducationCompletedPage(browser,previousPage)
}

/** The context for Specs tests */
trait G3EducationCompletedPageContext extends PageContext {
  this: WithBrowser[_] =>
  val page = G3EducationCompletedPage (browser)
}