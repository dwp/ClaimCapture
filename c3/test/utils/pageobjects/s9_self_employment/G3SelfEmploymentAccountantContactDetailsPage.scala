package utils.pageobjects.s9_self_employment

import play.api.test.TestBrowser
import utils.pageobjects.{PageContext, ClaimScenario, Page}


final class G3SelfEmploymentAccountantContactDetailsPage (browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G3SelfEmploymentAccountantContactDetailsPage.url, G3SelfEmploymentAccountantContactDetailsPage.title, previousPage) {

  def fillPageWith(theClaim: ClaimScenario) {
    fillInput("#accountantsName", theClaim.SelfEmployedAccountantName)
    fillInput("#address", theClaim.SelfEmployedAccountantAddress)
    fillInput("#postCode", theClaim.SelfEmployedAccountantPostcode)
    fillInput("#telephoneNumber", theClaim.SelfEmployedAccountantTelephoneNumber)
    fillInput("#faxNumber", theClaim.SelfEmployedAccountantFaxNumber)
  }
}

object G3SelfEmploymentAccountantContactDetailsPage {
  val title = "Self Employment - Accountant Contact Details"
  val url = "/selfEmployment/accountantContactDetails"

  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G3SelfEmploymentAccountantContactDetailsPage(browser, previousPage)
}

trait G3SelfEmploymentAccountantContactDetailsPageContext extends PageContext {
  this: {val browser: TestBrowser} =>
  val page = G3SelfEmploymentAccountantContactDetailsPage buildPageWith browser
}

