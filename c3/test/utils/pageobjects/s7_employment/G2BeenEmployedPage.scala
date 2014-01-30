package utils.pageobjects.s7_employment

import play.api.test.WithBrowser
import utils.pageobjects._

final class G2BeenEmployedPage(ctx:PageObjectsContext, iteration:Int) extends ClaimPage(ctx, G2BeenEmployedPage.url, G2BeenEmployedPage.title, iteration) {
  declareYesNo("#beenEmployed", "EmploymentHaveYouBeenEmployedAtAnyTime_"+iteration)
  override def getNewIterationNumber: Int = {
    import IterationManager._
    ctx.iterationManager.increment(Employment)
  }
}

object G2BeenEmployedPage {
  val title = "Your employment history - Employment History".toLowerCase

  val url  = "/employment/been-employed"

  def apply(ctx:PageObjectsContext, iteration:Int=1) = new G2BeenEmployedPage(ctx,iteration)
}

trait G2BeenEmployedPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G2BeenEmployedPage (PageObjectsContext(browser),iteration=1)
}