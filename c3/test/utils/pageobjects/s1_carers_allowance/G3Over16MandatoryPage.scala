package utils.pageobjects.s1_carers_allowance

import play.api.test.TestBrowser
import utils.pageobjects.{PageContext, ClaimScenario, Page}

final class G3Over16MandatoryPage(browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G3Over16MandatoryPage.url, G3Over16MandatoryPage.title, previousPage) {
    declareYesNo("#answer", "CanYouGetCarersAllowanceAreYouAged16OrOver")
}

object G3Over16MandatoryPage {
  val title = "Over 16 - Carer's Allowance"
  val url = "/allowance/over16Mandatory"

  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G3Over16MandatoryPage(browser, previousPage)
}

trait G3Over16MandatoryPageContext extends PageContext {
  this: {val browser: TestBrowser} =>
  val page = G3Over16MandatoryPage buildPageWith browser
}
