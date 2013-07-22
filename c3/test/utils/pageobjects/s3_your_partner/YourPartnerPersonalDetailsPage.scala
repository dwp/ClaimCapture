package utils.pageobjects.s3_your_partner

import play.api.test.TestBrowser
import utils.pageobjects.{ClaimScenario, PageContext, Page}

/**
 * PageObject for page s3_your_partner g1_yourPartnerPersonalDetails.
 * @author Jorge Migueis
 *         Date: 19/07/2013
 */
final class YourPartnerPersonalDetailsPage (browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, YourPartnerPersonalDetailsPage.url, YourPartnerPersonalDetailsPage.title, previousPage) {
  /**
   * Reads theClaim and interacts with browser to populate page.
   * @param theClaim   Data to use to fill page
   */
  def fillPageWith(theClaim: ClaimScenario) {
    fillSelect("#title", theClaim.AboutYourPartnerTitle)
    fillInput("#firstName", theClaim.AboutYourPartnerFirstName)
    fillInput("#middleName", theClaim.AboutYourPartnerMiddleName)
    fillInput("#surname", theClaim.AboutYourPartnerSurname)
    fillInput("#otherNames", theClaim.AboutYourPartnerOtherNames)
    fillNino("#nationalInsuranceNumber", theClaim.AboutYourPartnerNINO)
    fillDate("#dateOfBirth", theClaim.AboutYourPartnerDateofBirth)
    fillInput("#nationality", theClaim.AboutYourPartnerNationality)
    fillYesNo("#liveAtSameAddress", theClaim.AboutYourPartnerDoesYourPartnerLiveAtTheSameAddressAsYou)
  }
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in PageFactory.scala
 */
object YourPartnerPersonalDetailsPage {
  val title = "Personal Details - Your Partner"
  val url  = "/yourPartner/personalDetails"
  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new YourPartnerPersonalDetailsPage(browser,previousPage)
}

/** The context for Specs tests */
trait YourPartnerPersonalDetailsPageContext extends PageContext {
  this: {val browser:TestBrowser}  =>
  val page = YourPartnerPersonalDetailsPage buildPageWith browser
}
