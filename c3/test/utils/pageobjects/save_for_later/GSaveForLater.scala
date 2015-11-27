package utils.pageobjects.save_for_later

import utils.WithJsBrowser
import utils.pageobjects._

final class GSaveForLaterPage(ctx:PageObjectsContext) extends ClaimPage(ctx, GSaveForLaterPage.url) {

}

object GSaveForLaterPage {
  val url = "/save"

  def apply(ctx:PageObjectsContext) = new GSaveForLaterPage(ctx)
}

/** The context for Specs tests */
trait GSaveForLaterPageContext extends PageContext {
  this: WithJsBrowser[_] =>

  val page = GSaveForLaterPage (PageObjectsContext(browser))
}
