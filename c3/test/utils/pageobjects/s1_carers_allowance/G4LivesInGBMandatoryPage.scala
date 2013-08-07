package utils.pageobjects.s1_carers_allowance

import play.api.test.TestBrowser
import utils.pageobjects.{PageContext, ClaimScenario, Page}

final class G4LivesInGBMandatoryPage(browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G4LivesInGBMandatoryPage.url, G4LivesInGBMandatoryPage.title, previousPage) {
    declareYesNo("#answer", "CanYouGetCarersAllowanceDoYouNormallyLiveinGb")
}

object G4LivesInGBMandatoryPage {
  val title = "Lives in GB - Carer's Allowance"
  val url = "/allowance/livesInGBMandatory"

  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G4LivesInGBMandatoryPage(browser, previousPage)
}

trait G4LivesInGBMandatoryPageContext extends PageContext {
  this: {val browser: TestBrowser} =>
  val page = G4LivesInGBMandatoryPage buildPageWith browser
}
