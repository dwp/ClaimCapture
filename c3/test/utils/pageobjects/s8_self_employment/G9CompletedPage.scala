package utils.pageobjects.s8_self_employment

import play.api.test.WithBrowser
import utils.pageobjects._

final class G9CompletedPage(ctx:PageObjectsContext) extends ClaimPage(ctx, G9CompletedPage.url, G9CompletedPage.title)

object G9CompletedPage {
  val title = "Completion - Self Employment".toLowerCase

  val url = "/self-employment/completed"

  def apply(ctx:PageObjectsContext) = new G9CompletedPage(ctx)
}

trait G9CompletedPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G9CompletedPage (PageObjectsContext(browser))
}