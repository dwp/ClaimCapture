package utils.pageobjects.s7_employment

import play.api.test.WithBrowser
import utils.pageobjects._

final class G15CompletedPage(ctx:PageObjectsContext) extends ClaimPage(ctx, G15CompletedPage.url, G15CompletedPage.title) {

}

object G15CompletedPage {
  val title = "Completion - Employment History".toLowerCase
  val url  = "/employment/completed"
  def apply(ctx:PageObjectsContext) = new G15CompletedPage(ctx)
}

trait G15CompletedPageContext extends PageContext {
  this: WithBrowser[_] =>
  val page = G15CompletedPage (PageObjectsContext(browser))
}