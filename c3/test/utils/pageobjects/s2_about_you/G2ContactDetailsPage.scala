package utils.pageobjects.s2_about_you

import play.api.test.TestBrowser
import utils.pageobjects.{PageContext, ClaimScenario, Page}

/**
 * To change this template use Preferences | File and Code Templates.
 * @author Jorge Migueis
 *         Date: 16/07/2013
 */
final class G2ContactDetailsPage(browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G1YourDetailsPage.url, G1YourDetailsPage.title, previousPage) {
  /**
   * Reads theClaim and interacts with browser to populate page.
   * @param theClaim   Data to use to fill page
   */
  def fillPageWith(theClaim: ClaimScenario) {
    fillAddress("#address", theClaim.AboutYouAddress)
    fillInput("#postcode", theClaim.AboutYouPostcode)
    fillInput("#phoneNumber", theClaim.AboutYouDaytimePhoneNumber)
    fillInput("#mobileNumber", theClaim.AboutYouMobileNumber)
  }
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
