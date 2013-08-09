package utils.pageobjects.s7_employment

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.{PageContext, Page}

final class G11ChildcareProviderPage(browser: TestBrowser, previousPage: Option[Page] = None, iteration: Int) extends Page(browser, G11ChildcareProviderPage.url.replace(":jobID",iteration.toString), G11ChildcareProviderPage.title, previousPage,iteration) {

    declareAddress("#address", "EmploymentAddressChildcareProvider_"+iteration)
    declareInput("#postcode", "EmploymentPostcodeChildcareProvider_"+iteration)
  
}

object G11ChildcareProviderPage {
  val title = "Childcare provider's contact details - Employment"
  val url  = "/employment/childcareProvider/:jobID"
  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None, iteration: Int) = new G11ChildcareProviderPage(browser,previousPage,iteration)
}

trait G11ChildcareProviderPageContext extends PageContext {
  this: WithBrowser[_] =>
  val page = G11ChildcareProviderPage buildPageWith(browser,iteration = 1)
}