package utils.pageobjects.s4_care_you_provide

import play.api.test.TestBrowser
import utils.pageobjects.{ClaimScenario, PageContext, Page}

/**
 * Page object for s4_care_you_provide g2_their_contact_details.
 * @author Saqib Kayani
 *         Date: 25/07/2013
 */
class G2TheirContactDetailsPage (browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G2TheirContactDetailsPage.url, G2TheirContactDetailsPage.title, previousPage) {
  /**
   * Reads theClaim and interacts with browser to populate page.
   * @param theClaim   Data to use to fill page
   */
  def fillPageWith(theClaim: ClaimScenario) {
    fillAddress("#address", theClaim.AboutTheCareYouProvideAddressPersonCareFor)
    fillInput("#postcode", theClaim.AboutTheCareYouProvidePostcodePersonCareFor)
    fillInput("#phoneNumber", theClaim.AboutTheCareYouProvideDaytimePhoneNumberPersonYouCare)
  }
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in PageFactory.scala
 */
object G2TheirContactDetailsPage {
  val title = "Their Contact Details - Care You Provide"
  val url  = "/careYouProvide/theirContactDetails"
  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G2TheirContactDetailsPage(browser,previousPage)
}

/** The context for Specs tests */
trait G2TheirContactDetailsPageContext extends PageContext {
  this: {val browser:TestBrowser}  =>
  val page = G2TheirContactDetailsPage buildPageWith browser
}
