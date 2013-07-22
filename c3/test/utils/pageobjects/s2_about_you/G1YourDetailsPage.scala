package utils.pageobjects.s2_about_you

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.{ClaimScenario, PageContext, Page}

/**
 * PageObject for page s2_about_you g1_yourDetails.
 * @author Jorge Migueis
 *         Date: 09/07/2013
 */
final class G1YourDetailsPage(browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G1YourDetailsPage.url, G1YourDetailsPage.title, previousPage) {
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
 * It is used by PageFactory object defined in PageFactory.scala
 */
object G1YourDetailsPage {
  val title = "Your Details - About You"
  val url  = "/aboutyou/yourDetails"
  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G1YourDetailsPage(browser, previousPage)
}

/** The context for Specs tests */
trait G1YourDetailsPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G1YourDetailsPage buildPageWith browser
}
