package utils.pageobjects.s4_care_you_provide

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.{PageContext, Page}

/**
 * Page object for s4_care_you_provide g4_previous_carer_personal_details.
 * @author Saqib Kayani
 *         Date: 25/07/2013
 */
final class G4PreviousCarerPersonalDetailsPage (browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G4PreviousCarerPersonalDetailsPage.url, G4PreviousCarerPersonalDetailsPage.title, previousPage) {
  declareInput("#firstName", "AboutTheCareYouProvideFirstNamePreviousCarer")
  declareInput("#middleName", "AboutTheCareYouProvideMiddleNamePreviousCarer")
  declareInput("#surname", "AboutTheCareYouProvideSurnamePreviousCarer")
  declareNino("#nationalInsuranceNumber", "AboutTheCareYouProvideNINOPreviousCarer")
  declareDate("#dateOfBirth", "AboutTheCareYouProvideDateofBirthPreviousCarer")
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in PageFactory.scala
 */
object G4PreviousCarerPersonalDetailsPage {
  val title = "Details of Previous or Existing Carer - About the care you provide"

  val url  = "/care-you-provide/previous-carer-personal-details"

  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G4PreviousCarerPersonalDetailsPage(browser,previousPage)
}

/** The context for Specs tests */
trait G4PreviousCarerPersonalDetailsPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G4PreviousCarerPersonalDetailsPage buildPageWith browser
}