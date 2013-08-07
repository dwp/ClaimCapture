package utils.pageobjects.s1_carers_allowance

import play.api.test.TestBrowser
import utils.pageobjects.{PageContext, ClaimScenario, Page}

final class G1BenefitsMandatoryPage(browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G1BenefitsMandatoryPage.url, G1BenefitsMandatoryPage.title, previousPage) {
    declareYesNo("#answer", "CanYouGetCarersAllowanceDoesthePersonYouCareforGetOneofTheseBenefits")
}

object G1BenefitsMandatoryPage {
  val title = "Benefits - Carer's Allowance"
  val url = "/allowance/benefitsMandatory"

  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G1BenefitsMandatoryPage(browser, previousPage)
}

trait G1BenefitsMandatoryPageContext extends PageContext {
  this: {val browser: TestBrowser} =>
  val page = G1BenefitsMandatoryPage buildPageWith browser
}
