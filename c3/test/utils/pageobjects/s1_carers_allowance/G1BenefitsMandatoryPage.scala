package utils.pageobjects.s1_carers_allowance

import play.api.test.TestBrowser
import utils.pageobjects.{PageContext, ClaimScenario, Page}

final class G1BenefitsPage(browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G1BenefitsPage.url, G1BenefitsPage.title, previousPage) {
    declareYesNo("#answer", "CanYouGetCarersAllowanceDoesthePersonYouCareforGetOneofTheseBenefits")
}

object G1BenefitsPage {
  val title = "Benefits - Carer's Allowance"
  val url = "/allowance/benefits"

  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G1BenefitsPage(browser, previousPage)
}

trait G1BenefitsPageContext extends PageContext {
  this: {val browser: TestBrowser} =>
  val page = G1BenefitsPage buildPageWith browser
}
