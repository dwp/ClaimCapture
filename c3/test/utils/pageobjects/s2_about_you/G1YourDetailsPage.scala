package utils.pageobjects.s2_about_you

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.{PageContext, Page}

/**
 * PageObject for page s2_about_you g1_yourDetails.
 * @author Jorge Migueis
 *         Date: 09/07/2013
 */
final class G1YourDetailsPage(browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G1YourDetailsPage.url, G1YourDetailsPage.title, previousPage) {
  declareSelect("#title", "AboutYouTitle")
  declareInput("#firstName","AboutYouFirstName")
  declareInput("#middleName","AboutYouMiddleName")
  declareInput("#surname","AboutYouSurname")
  declareInput("#otherNames", "AboutYouOtherNames")
  declareNino("#nationalInsuranceNumber","AboutYouNINO")
  declareDate("#dateOfBirth", "AboutYouDateOfBirth")
  declareInput("#nationality", "AboutYouNationality")
  declareSelect("#maritalStatus", "AboutYouWhatIsYourMaritalOrCivilPartnershipStatus")
  declareYesNo("#alwaysLivedUK", "AboutYouHaveYouAlwaysLivedInTheUK")
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in PageFactory.scala
 */
object G1YourDetailsPage {
  val title = "Your details - About you - the carer".toLowerCase

  val url  = "/about-you/your-details"

  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G1YourDetailsPage(browser, previousPage)
}

/** The context for Specs tests */
trait G1YourDetailsPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G1YourDetailsPage buildPageWith browser
}