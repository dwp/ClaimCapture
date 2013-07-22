package utils.pageobjects.s3_your_partner

import play.api.test.TestBrowser
import utils.pageobjects.{ClaimScenario, PageContext, Page}

/**
 * Page Object for s3_yourPartnerDetails g2_yourPartnerContactDetails
 * @author Saqib Kayani
 *         Date: 22/07/2013
 */
final class YourPartnerContactDetailsPage (browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, YourPartnerContactDetailsPage.url, YourPartnerContactDetailsPage.title, previousPage){
  /**
   * Reads theClaim and interacts with browser to populate page.
   * @param theClaim   Data to use to fill page
   */
  def fillPageWith(theClaim: ClaimScenario) {

    fillAddress("#address", theClaim.AboutYourPartnerAddress)
    fillInput("#postcode", theClaim.AboutYourPartnerPostcode)

  }
}


/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in PageFactory.scala
 */
object YourPartnerContactDetailsPage {
  val title = "Contact Details - Your Partner"
  val url  = "/yourPartner/contactDetails"
  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new YourPartnerContactDetailsPage(browser,previousPage)
}

/** The context for Specs tests */
trait YourPartnerContactDetailsPageContext extends PageContext {
  this: {val browser:TestBrowser}  =>
  val page = YourPartnerContactDetailsPage buildPageWith browser
}