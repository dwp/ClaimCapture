package utils.pageobjects.s1_carers_allowance

import play.api.test.TestBrowser
import utils.pageobjects.{ClaimScenario, PageContext, Page}


final class G5CarersResponsePage(browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G5CarersResponsePage.url, G5CarersResponsePage.title, previousPage) {

  def fillPageWith(theClaim: ClaimScenario) {}

  def isApproved =  browser.find(".prompt").size != 0 && browser.find(".prompt.error]").size == 0
  def isNotApproved =  browser.find(".prompt.error]").size != 0

}


object G5CarersResponsePage {
  val title = "Carers response - Carer's Allowance"
  val url = "/allowance/carersResponse"
  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G5CarersResponsePage(browser, previousPage)
}

trait G5CarersResponsePageContext extends PageContext {
  this: {val browser:TestBrowser}  =>
  val page = G5CarersResponsePage buildPageWith browser
}
