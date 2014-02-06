package utils.pageobjects.circumstances.s1_about_you

import play.api.test.WithBrowser
import utils.pageobjects._

final class G4CompletedPage(ctx:PageObjectsContext) extends CircumstancesPage(ctx, G4CompletedPage.url, G4CompletedPage.title)

object G4CompletedPage {
  val title = "Completion - About you - the carer".toLowerCase

  val url  = "/circumstances/identification/completed"

  def apply(ctx:PageObjectsContext) = new G4CompletedPage(ctx)
}

trait G4CompletedPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G4CompletedPage(PageObjectsContext(browser))
}


