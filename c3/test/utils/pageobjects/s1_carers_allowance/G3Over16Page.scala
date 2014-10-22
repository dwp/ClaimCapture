package utils.pageobjects.s1_carers_allowance

import play.api.test.WithBrowser
import utils.pageobjects._

final class G3Over16Page(ctx:PageObjectsContext) extends ClaimPage(ctx, G3Over16Page.url, G3Over16Page.title) {
  declareYesNo("#over16_answer", "CanYouGetCarersAllowanceAreYouAged16OrOver")
}

object G3Over16Page {
  val title = "Are you aged 16 or over? - Can you get Carer's Allowance?".toLowerCase

  val url = "/allowance/over-16"

  def apply(ctx:PageObjectsContext) = new G3Over16Page(ctx)
}

trait G3Over16PageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G3Over16Page (PageObjectsContext(browser))
}