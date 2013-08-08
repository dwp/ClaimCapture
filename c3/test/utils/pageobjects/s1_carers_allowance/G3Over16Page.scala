package utils.pageobjects.s1_carers_allowance

import play.api.test.TestBrowser
import utils.pageobjects.{PageContext, ClaimScenario, Page}

final class G3Over16Page(browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G3Over16Page.url, G3Over16Page.title, previousPage) {
    declareYesNo("#answer", "CanYouGetCarersAllowanceAreYouAged16OrOver")
}

object G3Over16Page {
  val title = "Over 16 - Can you get Carer's Allowance?"
  val url = "/allowance/over16"

  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G3Over16Page(browser, previousPage)
}

trait G3Over16PageContext extends PageContext {
  this: {val browser: TestBrowser} =>
  val page = G3Over16Page buildPageWith browser
}
