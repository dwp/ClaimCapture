package utils.pageobjects.s1_carers_allowance

import play.api.test.TestBrowser
import utils.pageobjects.{PageContext, ClaimScenario, Page}

final class G4LivesInGBPage(browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G4LivesInGBPage.url, G4LivesInGBPage.title, previousPage) {
    declareYesNo("#answer", "CanYouGetCarersAllowanceDoYouNormallyLiveinGb")
}

object G4LivesInGBPage {
  val title = "Lives in GB - Carer's Allowance"
  val url = "/allowance/livesInGB"

  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G4LivesInGBPage(browser, previousPage)
}

trait G4LivesInGBPageContext extends PageContext {
  this: {val browser: TestBrowser} =>
  val page = G4LivesInGBPage buildPageWith browser
}
