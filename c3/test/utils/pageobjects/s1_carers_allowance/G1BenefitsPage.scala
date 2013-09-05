package utils.pageobjects.s1_carers_allowance

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.{ClaimPage,Page, PageContext}

final class G1BenefitsPage(browser: TestBrowser, previousPage: Option[Page] = None) extends ClaimPage(browser, G1BenefitsPage.url, G1BenefitsPage.title, previousPage) {
  declareYesNo("#answer", "CanYouGetCarersAllowanceDoesthePersonYouCareforGetOneofTheseBenefits")
}

object G1BenefitsPage {
  val title = "Does the person you care for get one of these benefits? - Can you get Carer's Allowance?".toLowerCase

  val url = "/allowance/benefits"

  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G1BenefitsPage(browser, previousPage)
}

trait G1BenefitsPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G1BenefitsPage buildPageWith browser
}