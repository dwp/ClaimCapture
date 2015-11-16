package utils.pageobjects.s_employment

import utils.WithBrowser
import utils.pageobjects._

final class GBeenEmployedPage(ctx:PageObjectsContext, iteration:Int) extends ClaimPage(ctx, GBeenEmployedPage.url, iteration) {
  declareYesNo("#beenEmployed", "EmploymentHaveYouBeenEmployedAtAnyTime_"+iteration)
  override def getNewIterationNumber: Int = {
    import IterationManager._
    ctx.iterationManager.increment(Employment)
  }
}

object GBeenEmployedPage {
  val url  = "/employment/been-employed"

  def apply(ctx:PageObjectsContext, iteration:Int=1) = new GBeenEmployedPage(ctx,iteration)
}

trait GBeenEmployedPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = GBeenEmployedPage (PageObjectsContext(browser),iteration=1)
}
