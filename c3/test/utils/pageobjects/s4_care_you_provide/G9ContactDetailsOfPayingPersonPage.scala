package utils.pageobjects.s4_care_you_provide

import play.api.test.TestBrowser
import utils.pageobjects.{ClaimScenario, PageContext, Page}

/**
 * * Page object for s4_care_you_provide g9_contact_details_of_paying_person.
 * @author Saqib Kayani
 *         Date: 29/07/2013
 */
final class G9ContactDetailsOfPayingPersonPage (browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G9ContactDetailsOfPayingPersonPage.url, G9ContactDetailsOfPayingPersonPage.title, previousPage) {
  /**
   * Reads theClaim and interacts with browser to populate page.
   * @param theClaim   Data to use to fill page
   */
  def fillPageWith(theClaim: ClaimScenario) {
    fillAddress("#address", theClaim.AboutTheCareYouProvideAddressPersonPaysYou)
    fillInput("#postcode", theClaim.AboutTheCareYouProvidePostcodePersonPaysYou)
  }
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in PageFactory.scala
 */
object G9ContactDetailsOfPayingPersonPage {
  val title = "Contact Details of Paying Person - Care You Provide"
  val url  = "/careYouProvide/contactDetailsOfPayingPerson"
  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G9ContactDetailsOfPayingPersonPage(browser,previousPage)
}

/** The context for Specs tests */
trait G9ContactDetailsOfPayingPersonPageContext extends PageContext {
  this: {val browser:TestBrowser}  =>
  val page = G9ContactDetailsOfPayingPersonPage buildPageWith browser
}
