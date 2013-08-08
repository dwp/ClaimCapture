package utils.pageobjects.s6_education

import play.api.test.TestBrowser
import utils.pageobjects.{PageContext, Page}

/**
 * TODO write description
 * @author Jorge Migueis
 *         Date: 06/08/2013
 */
class G3EducationCompletedPage (browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G3EducationCompletedPage.url, G3EducationCompletedPage.title, previousPage) {

}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in PageFactory.scala
 */
object G3EducationCompletedPage {
  val title = "Completion - About your education"
  val url  = "/education/completed"
  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G3EducationCompletedPage(browser,previousPage)
}

/** The context for Specs tests */
trait G3EducationCompletedPageContext extends PageContext {
  this: {val browser:TestBrowser}  =>
  val page = G3EducationCompletedPage buildPageWith browser
}