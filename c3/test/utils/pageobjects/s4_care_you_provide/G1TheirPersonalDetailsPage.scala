package utils.pageobjects.s4_care_you_provide

import play.api.test.TestBrowser
import utils.pageobjects.{ClaimScenario, PageContext, Page}

/**
 * Page object for s4_care_you_provide g1_their_personal_details.
 * @author Saqib Kayani
 *         Date: 25/07/2013
 */
final class G1TheirPersonalDetailsPage (browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G1TheirPersonalDetailsPage.url, G1TheirPersonalDetailsPage.title, previousPage) {

    declareSelect("#title", "AboutTheCareYouProvideTitlePersonCareFor")
    declareInput("#firstName","AboutTheCareYouProvideFirstNamePersonCareFor")
    declareInput("#middleName", "AboutTheCareYouProvideMiddleNamePersonCareFor")
    declareInput("#surname", "AboutTheCareYouProvideSurnamePersonCareFor")
    declareNino("#nationalInsuranceNumber", "AboutTheCareYouProvideNINOPersonCareFor")
    declareDate("#dateOfBirth", "AboutTheCareYouProvideDateofBirthPersonYouCareFor")
    declareYesNo("#liveAtSameAddress", "AboutTheCareYouProvideDoTheyLiveAtTheSameAddressAsYou")

}


/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in PageFactory.scala
 */
object G1TheirPersonalDetailsPage {
  val title = "Details of the person you care for - About the care you provide"
  val url  = "/careYouProvide/theirPersonalDetails"
  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G1TheirPersonalDetailsPage(browser,previousPage)
}

/** The context for Specs tests */
trait G1TheirPersonalDetailsPageContext extends PageContext {
  this: {val browser:TestBrowser}  =>
  val page = G1TheirPersonalDetailsPage buildPageWith browser
}