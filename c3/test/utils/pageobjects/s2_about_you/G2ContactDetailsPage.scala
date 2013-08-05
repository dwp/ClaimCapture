package utils.pageobjects.s2_about_you

import play.api.test.TestBrowser
import utils.pageobjects.{PageContext, Page}

/**
 * To change this template use Preferences | File and Code Templates.
 * @author Jorge Migueis
 *         Date: 16/07/2013
 */
final class G2ContactDetailsPage(browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G2ContactDetailsPage.url, G2ContactDetailsPage.title, previousPage) {

  declareAddress("#address", "AboutYouAddress")
  declareInput("#postcode", "AboutYouPostcode")
  declareInput("#phoneNumber", "AboutYouDaytimePhoneNumber")
  declareInput("#mobileNumber", "AboutYouMobileNumber")
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in Page.scala
 */
object G2ContactDetailsPage {
  val title = "Contact Details - About You"
  val url = "/aboutyou/contactDetails"

  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G2ContactDetailsPage(browser, previousPage)
}

/** The context for Specs tests */
trait ContactDetailsPageContext extends PageContext {
  this: {val browser: TestBrowser} =>
  val page = G2ContactDetailsPage buildPageWith browser
}
