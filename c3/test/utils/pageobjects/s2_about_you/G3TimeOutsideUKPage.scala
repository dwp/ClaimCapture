package utils.pageobjects.s2_about_you

import play.api.test.TestBrowser
import utils.pageobjects.{PageContext, ClaimScenario, Page}

/**
 * To change this template use Preferences | File and Code Templates.
 * @author Jorge Migueis
 *         Date: 16/07/2013
 */
final class G3TimeOutsideUKPage(browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G3TimeOutsideUKPage.url, G3TimeOutsideUKPage.title, previousPage) {
  /**
   * Reads theClaim and interacts with browser to populate page.
   * @param theClaim   Data to use to fill page
   */
  def fillPageWith(theClaim: ClaimScenario) {
    fillYesNo("#livingInUK_answer", theClaim.AboutYouAreYouCurrentlyLivingintheUk)
    fillDate("#livingInUK_arrivalDate", theClaim.AboutYouWhenDidYouArriveInYheUK)
    fillInput("#livingInUK_originCountry", theClaim.AboutYouWhatCountryDidYouComeFrom)
    fillYesNo("#livingInUK_goBack_answer", theClaim.AboutYouDoYouPlantoGoBacktoThatCountry)
    fillDate("#livingInUK_goBack_date", theClaim.AboutYouWhenDoYouPlantoGoBack)
    fillInput("#visaReference", theClaim.AboutYouWhatIsYourVisaNumber)
  }
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in Page.scala
 */
object G3TimeOutsideUKPage {
  val title = "Time Outside UK - About You"
  val url  = "/aboutyou/timeOutsideUK"
  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G3TimeOutsideUKPage(browser,previousPage)
}

/** The context for Specs tests */
trait G3TimeOutsideUKPageContext extends PageContext {
  this: {val browser:TestBrowser}  =>
  val page = G3TimeOutsideUKPage buildPageWith browser
}