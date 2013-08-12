package utils.pageobjects.s1_carers_allowance

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.{PageContext, Page}

final class G4LivesInGBPage(browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G4LivesInGBPage.url, G4LivesInGBPage.title, previousPage) {
  declareYesNo("#answer", "CanYouGetCarersAllowanceDoYouNormallyLiveinGb")
}

object G4LivesInGBPage {
  val title = "Lives in GB - Can you get Carer's Allowance?".toLowerCase

  val url = "/allowance/lives-in-gb"

  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G4LivesInGBPage(browser, previousPage)
}

trait G4LivesInGBPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G4LivesInGBPage buildPageWith browser
}