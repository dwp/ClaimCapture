package utils.pageobjects.s8_self_employment

import play.api.test.TestBrowser
import utils.pageobjects.{PageContext, ClaimScenario, Page}

final class G6ChildcareProvidersContactDetailsPage(browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G6ChildcareProvidersContactDetailsPage.url, G6ChildcareProvidersContactDetailsPage.title, previousPage) {


    declareAddress("#address", "SelfEmployedChildcareProviderAddress")
    declareInput("#postcode", "SelfEmployedChildcareProviderPostcode")

}

object G6ChildcareProvidersContactDetailsPage {
  val title = "Childcare provider's contact details - Self Employment"
  val url = "/selfEmployment/childcareProvidersContactDetails"

  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G6ChildcareProvidersContactDetailsPage(browser, previousPage)
}

trait G6ChildcareProvidersContactDetailsPageContext extends PageContext {
  this: {val browser: TestBrowser} =>
  val page = G6ChildcareProvidersContactDetailsPage buildPageWith browser
}
