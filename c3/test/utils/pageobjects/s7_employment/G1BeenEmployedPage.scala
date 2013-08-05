package utils.pageobjects.s7_employment

import play.api.test.TestBrowser
import utils.pageobjects.{ClaimScenario, PageContext, Page}

final class G1BeenEmployedPage(browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G1BeenEmployedPage.url, G1BeenEmployedPage.title, previousPage) {
    
  declareYesNo("#beenEmployed", "EmploymentBeenEmployed")
}

object G1BeenEmployedPage {
  val title = "Your employment history - Employment"
  val url  = "/employment/beenEmployed"
  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G1BeenEmployedPage(browser,previousPage)
}

trait G1BeenEmployedPageContext extends PageContext {
  this: {val browser:TestBrowser}  =>
  val page = G1BeenEmployedPage buildPageWith browser
}
