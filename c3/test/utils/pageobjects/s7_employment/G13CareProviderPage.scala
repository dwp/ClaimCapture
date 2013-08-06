package utils.pageobjects.s7_employment

import play.api.test.TestBrowser
import utils.pageobjects.{ClaimScenario, PageContext, Page}

final class G13CareProviderPage(browser: TestBrowser, previousPage: Option[Page] = None, iteration: Int) extends Page(browser, G13CareProviderPage.url.replace(":jobID",iteration.toString), G13CareProviderPage.title, previousPage,iteration) {

    declareAddress("#beenEmployed", "EmploymentAddressCareProvider_"+iteration)
    declareInput("#postcode", "EmploymentPostcodeCareProvider_"+iteration)

}

object G13CareProviderPage {
  val title = "Care provider’s contact Details - Employment"
  val url  = "/employment/careProvider/:jobID"
  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None, iteration: Int) = new G13CareProviderPage(browser,previousPage,iteration)
}

trait G13CareProviderPageContext extends PageContext {
  this: {val browser:TestBrowser}  =>
  val page = G13CareProviderPage buildPageWith(browser,iteration = 1)
}
