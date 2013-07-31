package utils.pageobjects.s7_employment

import play.api.test.TestBrowser
import utils.pageobjects.{ClaimScenario, PageContext, Page}

final class G15CompletedPage(browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G15CompletedPage.url, G15CompletedPage.title, previousPage) {
  def fillPageWith(theClaim: ClaimScenario) {
    fillYesNo("#beenEmployed", theClaim.EmploymentBeenEmployed)
  }
}

object G15CompletedPage {
  val title = "Your employment history - Employment"
  val url  = "/employment/beenEmployed"
  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G15CompletedPage(browser,previousPage)
}

trait G15CompletedPageContext extends PageContext {
  this: {val browser:TestBrowser}  =>
  val page = G15CompletedPage buildPageWith browser
}
