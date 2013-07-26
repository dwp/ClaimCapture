package utils.pageobjects.s4_care_you_provide

import play.api.test.TestBrowser
import utils.pageobjects.{ClaimScenario, PageContext, Page}

/**
 * Page object for s4_care_you_provide g5_previous_carer_contact_details.
 * @author Saqib Kayani
 *         Date: 25/07/2013
 */
final class G5PreviousCarerContactDetailsPage (browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G5PreviousCarerContactDetailsPage.url, G5PreviousCarerContactDetailsPage.title, previousPage) {
  /**
   * Sub-class reads theClaim and interacts with browser to populate page.
   * @param theClaim   Data to use to fill page
   */
  def fillPageWith(theClaim: ClaimScenario) {
    fillAddress("#address", theClaim.AboutTheCareYouProvideAddressPreviousCarer)
    fillInput("#postcode", theClaim.AboutTheCareYouProvidePostcodePreviousCarer)
    fillInput("#phoneNumber", theClaim.AboutTheCareYouProvideDaytimePhoneNumberPreviousCarer)
    fillInput("#mobileNumber", theClaim.AboutTheCareYouProvideMobileNumberPreviousCarer)
  }
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in PageFactory.scala
 */
object G5PreviousCarerContactDetailsPage {
  val title = "Contact Details Of The Person Who Claimed Before - Care You Provide"
  val url  = "/careYouProvide/previousCarerPersonalDetails"
  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G5PreviousCarerContactDetailsPage(browser,previousPage)
}

/** The context for Specs tests */
trait G5PreviousCarerContactDetailsPageContext extends PageContext {
  this: {val browser:TestBrowser}  =>
  val page = G5PreviousCarerContactDetailsPage buildPageWith browser
}
