package utils.pageobjects.circumstances.s1_about_you

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.{CircumstancesPage, PageContext, Page}

final class G4CompletedPage(browser: TestBrowser, previousPage: Option[Page] = None) extends CircumstancesPage(browser, G4CompletedPage.url, G4CompletedPage.title, previousPage)

object G4CompletedPage {
  val title = "Completion - About you - the carer".toLowerCase

  val url  = "circumstances/identification/completed"

  def apply(browser: TestBrowser, previousPage: Option[Page] = None) = new G4CompletedPage(browser, previousPage)
}

trait G4CompletedPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G4CompletedPage(browser)
}


