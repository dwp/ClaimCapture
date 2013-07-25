package utils.pageobjects.s9_self_employment

import play.api.test.TestBrowser
import utils.pageobjects.{PageContext, ClaimScenario, Page}

final class G8CareProvidersContactDetailsPage(browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G8CareProvidersContactDetailsPage.url, G8CareProvidersContactDetailsPage.title, previousPage) {

  def fillPageWith(theClaim: ClaimScenario) {
    fillAddress("#address", theClaim.SelfEmployedCareProviderAddress)
    fillInput("#postcode", theClaim.SelfEmployedCareProviderPostcode)
  }
}

object G8CareProvidersContactDetailsPage {
  val title = "Care provider's contact Details - Self Employment"
  val url = "/selfEmployment/careProvidersContactDetails"

  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G8CareProvidersContactDetailsPage(browser, previousPage)
}

trait G8CareProvidersContactDetailsPageContext extends PageContext {
  this: {val browser: TestBrowser} =>
  val page = G8CareProvidersContactDetailsPage buildPageWith browser
}
