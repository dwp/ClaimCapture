package utils.pageobjects.s4_care_you_provide

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.{ClaimPage,Page, PageContext}

/**
 * Page object for s4_care_you_provide g1_their_personal_details.
 * @author Saqib Kayani
 *         Date: 25/07/2013
 */
final class G1TheirPersonalDetailsPage (browser: TestBrowser, previousPage: Option[Page] = None) extends ClaimPage(browser, G1TheirPersonalDetailsPage.url, G1TheirPersonalDetailsPage.title, previousPage) {
  declareSelect("#title", "AboutTheCareYouProvideTitlePersonCareFor")
  declareInput("#firstName","AboutTheCareYouProvideFirstNamePersonCareFor")
  declareInput("#middleName", "AboutTheCareYouProvideMiddleNamePersonCareFor")
  declareInput("#surname", "AboutTheCareYouProvideSurnamePersonCareFor")
  declareNino("#nationalInsuranceNumber", "AboutTheCareYouProvideNINOPersonCareFor")
  declareDate("#dateOfBirth", "AboutTheCareYouProvideDateofBirthPersonYouCareFor")
  declareYesNo("#liveAtSameAddressCareYouProvide", "AboutTheCareYouProvideDoTheyLiveAtTheSameAddressAsYou")
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in PageFactory.scala
 */
object G1TheirPersonalDetailsPage {
  val title = "Details of the person you care for - About the care you provide".toLowerCase

  val url  = "/care-you-provide/their-personal-details"

  def apply(browser: TestBrowser, previousPage: Option[Page] = None) = new G1TheirPersonalDetailsPage(browser,previousPage)
}

/** The context for Specs tests */
trait G1TheirPersonalDetailsPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G1TheirPersonalDetailsPage (browser)
}