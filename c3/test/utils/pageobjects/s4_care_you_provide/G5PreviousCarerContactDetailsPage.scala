package utils.pageobjects.s4_care_you_provide

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.{PageContext, Page}

/**
 * Page object for s4_care_you_provide g5_previous_carer_contact_details.
 * @author Saqib Kayani
 *         Date: 25/07/2013
 */
final class G5PreviousCarerContactDetailsPage (browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G5PreviousCarerContactDetailsPage.url, G5PreviousCarerContactDetailsPage.title, previousPage) {
  declareAddress("#address", "AboutTheCareYouProvideAddressPreviousCarer")
  declareInput("#postcode", "AboutTheCareYouProvidePostcodePreviousCarer")
  declareInput("#phoneNumber", "AboutTheCareYouProvideDaytimePhoneNumberPreviousCarer")
  declareInput("#mobileNumber", "AboutTheCareYouProvideMobileNumberPreviousCarer")
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in PageFactory.scala
 */
object G5PreviousCarerContactDetailsPage {
  val title = "Contact details of previous or existing carer - About the care you provide".toLowerCase

  val url  = "/care-you-provide/previous-carer-personal-details"

  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G5PreviousCarerContactDetailsPage(browser,previousPage)
}

/** The context for Specs tests */
trait G5PreviousCarerContactDetailsPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G5PreviousCarerContactDetailsPage buildPageWith browser
}