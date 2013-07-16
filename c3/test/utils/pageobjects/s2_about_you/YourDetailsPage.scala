package utils.pageobjects.s2_about_you

import play.api.test.TestBrowser
import utils.pageobjects.{PageFactory, ClaimScenario, PageContext, Page}

/**
 * PageObject for page s2_about_you g1_yourDetails.
 * @author Jorge Migueis
 *         Date: 09/07/2013
 */
class YourDetailsPage(browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, YourDetailsPage.url, YourDetailsPage.title, previousPage) {
  /**
   * Reads theClaim and interacts with browser to populate page.
   * @param theClaim   Data to use to fill page
   */
  def fillPageWith(theClaim: ClaimScenario) {
    fillSelect("#title", theClaim.AboutYouTitle)
    fillInput("#firstName",theClaim.AboutYouFirstName)
    fillInput("#middleName",theClaim.AboutYouMiddleName)
    fillInput("#surname",theClaim.AboutYouSurname)
    fillInput("#otherNames", theClaim.AboutYouOtherNames)
    fillNino("#nationalInsuranceNumber",theClaim.AboutYouNINO)
    fillDate("#dateOfBirth", theClaim.AboutYouDateOfBirth)
    fillInput("#nationality", theClaim.AboutYouNationality)
    fillSelect("#maritalStatus", theClaim.AboutYouWhatIsYourMaritalOrCivilPartnershipStatus)
    fillYesNo("#alwaysLivedUK", theClaim.AboutYouHaveYouAlwaysLivedInTheUK)
  }
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in Page.scala
 */
object YourDetailsPage {
  val title = "Your Details - About You"
  val url  = "/aboutyou/yourDetails"
  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new YourDetailsPage(browser,previousPage)
//  PageFactory.registerPageBuilder[YourDetailsPage](title, buildPageWith)
}

/** The context for Specs tests */
trait YourDetailsPageContext extends PageContext {
  this: {val browser:TestBrowser}  =>
  val page = YourDetailsPage buildPageWith browser
}
