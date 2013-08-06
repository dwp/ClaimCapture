package utils.pageobjects.s7_employment

import play.api.test.TestBrowser
import utils.pageobjects.{ClaimScenario, PageContext, Page}

final class G3EmployerContactDetailsPage(browser: TestBrowser, previousPage: Option[Page] = None, iteration:Int) extends Page(browser, G3EmployerContactDetailsPage.url.replace(":jobID",iteration.toString), G3EmployerContactDetailsPage.title, previousPage, iteration) {

    declareAddress("#address", "EmploymentEmployerAddress_"+iteration)
    declareInput("#postcode","EmploymentEmployerPostcode_"+iteration)
    declareInput("#phoneNumber","EmploymentEmployerPhoneNumber_"+iteration)

}

object G3EmployerContactDetailsPage {
  val title = "Employer contact details - Employment"
  val url  = "/employment/employersContactDetails/:jobID"
  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None, iteration:Int) = new G3EmployerContactDetailsPage(browser,previousPage,iteration)
}

trait G3EmployerContactDetailsPageContext extends PageContext {
  this: {val browser:TestBrowser}  =>
  val page = G3EmployerContactDetailsPage buildPageWith (browser,iteration = 1)
}
