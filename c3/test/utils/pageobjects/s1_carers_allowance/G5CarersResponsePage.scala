package utils.pageobjects.s1_carers_allowance

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.{ClaimScenario, PageContext, Page}

final class G5CarersResponsePage(browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G5CarersResponsePage.url, G5CarersResponsePage.title, previousPage) {
  override def fillPageWith(theClaim: ClaimScenario): Page = this

  def isApproved =  browser.find(".prompt").size != 0 && browser.find(".prompt.error]").size == 0

  def isNotApproved =  browser.find(".prompt.error]").size != 0
}

object G5CarersResponsePage {
  val title = "Carers response - Carer's Allowance"

  val url = "/allowance/carers-response"

  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G5CarersResponsePage(browser, previousPage)
}

trait G5CarersResponsePageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G5CarersResponsePage buildPageWith browser
}