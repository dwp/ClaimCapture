package utils.pageobjects.s1_carers_allowance

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.{ClaimPage,Page, PageContext}

final class G3Over16Page(browser: TestBrowser, previousPage: Option[Page] = None) extends ClaimPage(browser, G3Over16Page.url, G3Over16Page.title, previousPage) {
  declareYesNo("#answer", "CanYouGetCarersAllowanceAreYouAged16OrOver")
}

object G3Over16Page {
  val title = "Are you aged 16 or over? - Can you get Carer's Allowance?".toLowerCase

  val url = "/allowance/over-16"

  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G3Over16Page(browser, previousPage)
}

trait G3Over16PageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G3Over16Page buildPageWith browser
}