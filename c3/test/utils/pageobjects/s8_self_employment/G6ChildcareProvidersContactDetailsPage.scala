package utils.pageobjects.s8_self_employment

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.{ClaimPage,Page, PageContext}

final class G6ChildcareProvidersContactDetailsPage(browser: TestBrowser, previousPage: Option[Page] = None) extends ClaimPage(browser, G6ChildcareProvidersContactDetailsPage.url, G6ChildcareProvidersContactDetailsPage.title, previousPage) {
  declareAddress("#address", "SelfEmployedChildcareProviderAddress")
  declareInput("#postcode", "SelfEmployedChildcareProviderPostcode")
}

object G6ChildcareProvidersContactDetailsPage {
  val title = "Childcare provider's contact details - About self-employment".toLowerCase
  val url = "/self-employment/childcare-providers-contact-details"

  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G6ChildcareProvidersContactDetailsPage(browser, previousPage)
}

trait G6ChildcareProvidersContactDetailsPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G6ChildcareProvidersContactDetailsPage buildPageWith browser
}