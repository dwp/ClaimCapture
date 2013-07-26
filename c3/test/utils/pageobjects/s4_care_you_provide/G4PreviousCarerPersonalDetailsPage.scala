package utils.pageobjects.s4_care_you_provide

import play.api.test.TestBrowser
import utils.pageobjects.{ClaimScenario, PageContext, Page}

/**
 * Page object for s4_care_you_provide g4_previous_carer_personal_details.
 * @author Saqib Kayani
 *         Date: 25/07/2013
 */
final class G4PreviousCarerPersonalDetailsPage (browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G4PreviousCarerPersonalDetailsPage.url, G4PreviousCarerPersonalDetailsPage.title, previousPage) {
  /**
   * Reads theClaim and interacts with browser to populate page.
   * @param theClaim   Data to use to fill page
   */
  def fillPageWith(theClaim: ClaimScenario) {
    fillInput("#firstName", theClaim.AboutTheCareYouProvideFirstNamePreviousCarer)
    fillInput("#middleName", theClaim.AboutTheCareYouProvideMiddleNamePreviousCarer)
    fillInput("#surname", theClaim.AboutTheCareYouProvideSurnamePreviousCarer)
    fillNino("#nationalInsuranceNumber", theClaim.AboutTheCareYouProvideNINOPreviousCarer)
    fillDate("#dateOfBirth", theClaim.AboutTheCareYouProvideDateofBirthPreviousCarer)
  }
}


/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in PageFactory.scala
 */
object G4PreviousCarerPersonalDetailsPage {
  val title = "Details Of The Person Who Claimed Before - Care You Provide"
  val url  = "/careYouProvide/previousCarerPersonalDetails"
  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G4PreviousCarerPersonalDetailsPage(browser,previousPage)
}

/** The context for Specs tests */
trait G4PreviousCarerPersonalDetailsPageContext extends PageContext {
  this: {val browser:TestBrowser}  =>
  val page = G4PreviousCarerPersonalDetailsPage buildPageWith browser
}
