package utils.pageobjects.s8_self_employment

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.{PageContext, Page}

final class G3SelfEmploymentAccountantContactDetailsPage (browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G3SelfEmploymentAccountantContactDetailsPage.url, G3SelfEmploymentAccountantContactDetailsPage.title, previousPage) {

    declareInput("#accountantsName", "SelfEmployedAccountantName")
    declareAddress("#address", "SelfEmployedAccountantAddress")
    declareInput("#postCode", "SelfEmployedAccountantPostcode")
    declareInput("#telephoneNumber", "SelfEmployedAccountantTelephoneNumber")
    declareInput("#faxNumber", "SelfEmployedAccountantFaxNumber")
}

object G3SelfEmploymentAccountantContactDetailsPage {
  val title = "Self Employment - Accountant Contact Details"
  val url = "/selfEmployment/accountantContactDetails"

  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G3SelfEmploymentAccountantContactDetailsPage(browser, previousPage)
}

trait G3SelfEmploymentAccountantContactDetailsPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G3SelfEmploymentAccountantContactDetailsPage buildPageWith browser
}