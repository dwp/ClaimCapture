package utils.pageobjects.s4_care_you_provide

import play.api.test.TestBrowser
import utils.pageobjects.{ClaimScenario, PageContext, Page}

/**
 * Page object for s4_care_you_provide g1_TheirPersonalDetails.
 * @author Saqib Kayani
 *         Date: 25/07/2013
 */
final class G1TheirPersonalDetailsPage (browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G1TheirPersonalDetailsPage.url, G1TheirPersonalDetailsPage.title, previousPage) {
  /**
   * Reads theClaim and interacts with browser to populate page.
   * @param theClaim   Data to use to fill page
   */
  def fillPageWith(theClaim: ClaimScenario) {
    fillSelect("#title", theClaim.AboutTheCareYouProvideTitlePersonCareFor)
    fillInput("#firstName",theClaim.AboutTheCareYouProvideFirstNamePersonCareFor)
    fillInput("#middleName", theClaim.AboutTheCareYouProvideMiddleNamePersonCareFor)
    fillInput("#surname", theClaim.AboutTheCareYouProvideSurnamePersonCareFor)
    fillNino("#nationalInsuranceNumber", theClaim.AboutTheCareYouProvideNINOPersonCareFor)
    fillDate("#dateOfBirth", theClaim.AboutTheCareYouProvideDateofBirthPersonYouCareFor)
    fillYesNo("#liveAtSameAddress", theClaim.AboutTheCareYouProvideDoTheyLiveAtTheSameAddressAsYou)
  }
}


/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in PageFactory.scala
 */
object G1TheirPersonalDetailsPage {
  val title = "Their Personal Details - Care You Provide"
  val url  = "/careYouProvide/theirPersonalDetails"
  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G1TheirPersonalDetailsPage(browser,previousPage)
}

/** The context for Specs tests */
trait G1TheirPersonalDetailsPageContext extends PageContext {
  this: {val browser:TestBrowser}  =>
  val page = G1TheirPersonalDetailsPage buildPageWith browser
}