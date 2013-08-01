package utils.pageobjects.s7_employment

import play.api.test.TestBrowser
import utils.pageobjects.{ClaimScenario, PageContext, Page}

final class G11ChildcareProviderPage(browser: TestBrowser, previousPage: Option[Page] = None, iteration: Int) extends Page(browser, G11ChildcareProviderPage.url, G11ChildcareProviderPage.title, previousPage) {
  override val url = super.getUrl.replace(":jobID",iteration.toString)
  def fillPageWith(theClaim: ClaimScenario) {
    fillAddress("#address", theClaim.selectDynamic("EmploymentAddressChildcareProvider_"+iteration))
    fillInput("#postcode", theClaim.selectDynamic("EmploymentPostcodeChildcareProvider_"+iteration))
  }
}

object G11ChildcareProviderPage {
  val title = "Childcare provider's contact details - Employment"
  val url  = "/employment/childcareProvider/:jobID"
  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None, iteration: Int) = new G11ChildcareProviderPage(browser,previousPage,iteration)
}

trait G11ChildcareProviderPageContext extends PageContext {
  this: {val browser:TestBrowser}  =>
  val page = G11ChildcareProviderPage buildPageWith(browser,iteration = 1)
}
