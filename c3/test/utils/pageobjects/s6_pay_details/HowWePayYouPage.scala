package utils.pageobjects.s6_pay_details

import play.api.test.TestBrowser
import utils.pageobjects.{ClaimScenario, PageContext, Page}

/**
 * To change this template use Preferences | File and Code Templates.
 * @author Jorge Migueis
 *         Date: 18/07/2013
 */
class HowWePayYouPage(browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, HowWePayYouPage.url, HowWePayYouPage.title, previousPage) {
  /**
   * Sub-class reads theClaim and interacts with browser to populate page.
   * @param theClaim   Data to use to fill page
   */
  def fillPageWith(theClaim: ClaimScenario) {
    fillRadioList("likeToPay", theClaim.HowWePayYouHowWouldYouLikeToGetPaid)
    fillSelect("#paymentFrequency", theClaim.HowWePayYouHowOftenDoYouWantToGetPaid)
  }
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in Page.scala
 */
object HowWePayYouPage {
  val title = "How We Pay You - Pay Details"
  val url  = "/payDetails/howWePayYou"
  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new HowWePayYouPage(browser,previousPage)
}

/** The context for Specs tests */
trait HowWePayYouPageContext extends PageContext {
  this: {val browser:TestBrowser}  =>
  val page = HowWePayYouPage buildPageWith browser
}
