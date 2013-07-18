package utils.pageobjects.s8_other_money

import play.api.test.TestBrowser
import utils.pageobjects.{ClaimScenario, PageContext, Page}

final class PersonContactDetailsPage(browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, PersonContactDetailsPage.url, PersonContactDetailsPage.title, previousPage) {

  def fillPageWith(theClaim: ClaimScenario) {
    fillAddress("#address", theClaim.PersonAddress)
    fillInput("#postcode", theClaim.PersonPostcode)
  }
}

object PersonContactDetailsPage {
  val title = "Contact Details - Other Money"
  val url = "/otherMoney/personContactDetails"

  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new PersonContactDetailsPage(browser, previousPage)
}

trait PersonContactDetailsPageContext extends PageContext {
  this: {val browser: TestBrowser} =>
  val page = PersonContactDetailsPage buildPageWith browser
}
