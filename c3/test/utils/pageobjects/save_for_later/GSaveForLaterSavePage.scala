package utils.pageobjects.save_for_later

import utils.WithJsBrowser
import utils.pageobjects._

final class GSaveForLaterSavePage(ctx:PageObjectsContext) extends ClaimPage(ctx, GSaveForLaterSavePage.url) {

}

object GSaveForLaterSavePage {
  val url = "/save"

  def apply(ctx:PageObjectsContext) = new GSaveForLaterSavePage(ctx)
}

/** The context for Specs tests */
trait GSaveForLaterSavePageContext extends PageContext {
  this: WithJsBrowser[_] =>

  val page = GSaveForLaterSavePage (PageObjectsContext(browser))
}
