package utils.pageobjects.s8_self_employment

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.{ClaimPage,Page, PageContext}

final class G8CareProvidersContactDetailsPage(browser: TestBrowser, previousPage: Option[Page] = None) extends ClaimPage(browser, G8CareProvidersContactDetailsPage.url, G8CareProvidersContactDetailsPage.title, previousPage) {
  declareAddress("#address", "SelfEmployedCareProviderAddress")
  declareInput("#postcode", "SelfEmployedCareProviderPostcode")
}

object G8CareProvidersContactDetailsPage {
  val title = "Care provider's contact details - About self-employment".toLowerCase
  val url = "/self-employment/care-providers-contact-details"

  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G8CareProvidersContactDetailsPage(browser, previousPage)
}

trait G8CareProvidersContactDetailsPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G8CareProvidersContactDetailsPage buildPageWith browser
}