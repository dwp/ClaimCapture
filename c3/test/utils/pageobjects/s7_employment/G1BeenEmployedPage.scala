package utils.pageobjects.s7_employment

import play.api.test.WithBrowser
import utils.pageobjects._

final class G1BeenEmployedPage(ctx:PageObjectsContext, iteration:Int) extends ClaimPage(ctx, G1BeenEmployedPage.url, G1BeenEmployedPage.title, iteration) {
  declareYesNo("#beenEmployed", "AboutYouHaveYouBeenEmployedAtAnyTime_"+iteration)
}

object G1BeenEmployedPage {
  val title = "Your employment history - Employment History".toLowerCase

  val url  = "/employment/been-employed"

  def apply(ctx:PageObjectsContext, iteration:Int=1) = new G1BeenEmployedPage(ctx,iteration)
}

trait G1BeenEmployedPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G1BeenEmployedPage (PageObjectsContext(browser),iteration=1)
}