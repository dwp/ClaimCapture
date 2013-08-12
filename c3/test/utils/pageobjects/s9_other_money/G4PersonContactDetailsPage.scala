package utils.pageobjects.s9_other_money

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.{PageContext, Page}

final class G4PersonContactDetailsPage(browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G4PersonContactDetailsPage.url, G4PersonContactDetailsPage.title, previousPage) {
  declareAddress("#address", "OtherMoneyOtherPersonAddress")
  declareInput("#postcode", "OtherMoneyOtherPersonPostcode")
}

object G4PersonContactDetailsPage {
  val title = "Contact Details - Other Money".toLowerCase

  val url = "/other-money/person-contact-details"

  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G4PersonContactDetailsPage(browser, previousPage)
}

trait G4PersonContactDetailsPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G4PersonContactDetailsPage buildPageWith browser
}