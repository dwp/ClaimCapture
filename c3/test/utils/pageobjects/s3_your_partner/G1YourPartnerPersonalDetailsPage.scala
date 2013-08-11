package utils.pageobjects.s3_your_partner

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.{PageContext, Page}

/**
 * PageObject for page s3_your_partner g1_yourPartnerPersonalDetails.
 * @author Jorge Migueis
 *         Date: 19/07/2013
 */
final class G1YourPartnerPersonalDetailsPage (browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G1YourPartnerPersonalDetailsPage.url, G1YourPartnerPersonalDetailsPage.title, previousPage) {
  declareSelect("#title", "AboutYourPartnerTitle")
  declareInput("#firstName", "AboutYourPartnerFirstName")
  declareInput("#middleName", "AboutYourPartnerMiddleName")
  declareInput("#surname", "AboutYourPartnerSurname")
  declareInput("#otherNames", "AboutYourPartnerOtherNames")
  declareNino("#nationalInsuranceNumber", "AboutYourPartnerNINO")
  declareDate("#dateOfBirth", "AboutYourPartnerDateofBirth")
  declareInput("#nationality", "AboutYourPartnerNationality")
  declareYesNo("#liveAtSameAddress", "AboutYourPartnerDoesYourPartnerLiveAtTheSameAddressAsYou")
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in PageFactory.scala
 */
object G1YourPartnerPersonalDetailsPage {
  val title = "Partner/Spouse Details - About Your Partner/Spouse"

  val url  = "/your-partner/personal-details"

  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G1YourPartnerPersonalDetailsPage(browser,previousPage)
}

/** The context for Specs tests */
trait G1YourPartnerPersonalDetailsPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G1YourPartnerPersonalDetailsPage buildPageWith browser
}