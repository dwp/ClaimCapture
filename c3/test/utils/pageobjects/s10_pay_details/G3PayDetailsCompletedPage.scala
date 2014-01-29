package utils.pageobjects.s10_pay_details

import play.api.test.WithBrowser
import utils.pageobjects._

final class G3PayDetailsCompletedPage(ctx:PageObjectsContext) extends ClaimPage(ctx, G3PayDetailsCompletedPage.url, G3PayDetailsCompletedPage.title)

object G3PayDetailsCompletedPage {
  val title = "Completion - How we pay you".toLowerCase

  val url = "/pay-details/completed"

  def apply(ctx:PageObjectsContext) = new G3PayDetailsCompletedPage(ctx)
}

trait G3PayDetailsCompletedPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G3PayDetailsCompletedPage (PageObjectsContext(browser))
}