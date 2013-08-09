package utils.pageobjects.s2_about_you

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.{PageContext, Page}

/**
 * To change this template use Preferences | File and Code Templates.
 * @author Jorge Migueis
 *         Date: 16/07/2013
 */
final class G3TimeOutsideUKPage(browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G3TimeOutsideUKPage.url, G3TimeOutsideUKPage.title, previousPage) {

    declareYesNo("#livingInUK_answer", "AboutYouAreYouCurrentlyLivingintheUk")
    declareDate("#livingInUK_arrivalDate", "AboutYouWhenDidYouArriveInYheUK")
    declareInput("#livingInUK_originCountry", "AboutYouWhatCountryDidYouComeFrom")
    declareYesNo("#livingInUK_goBack_answer", "AboutYouDoYouPlantoGoBacktoThatCountry")
    declareDate("#livingInUK_goBack_date", "AboutYouWhenDoYouPlantoGoBack")
    declareInput("#visaReference", "AboutYouWhatIsYourVisaNumber")
  
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in Page.scala
 */
object G3TimeOutsideUKPage {
  val title = "About your time outside the UK - About You"
  val url  = "/aboutyou/timeOutsideUK"
  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G3TimeOutsideUKPage(browser,previousPage)
}

/** The context for Specs tests */
trait G3TimeOutsideUKPageContext extends PageContext {
  this: WithBrowser[_] =>
  val page = G3TimeOutsideUKPage buildPageWith browser
}