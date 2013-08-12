package utils.pageobjects.s10_pay_details

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.{PageContext, Page}

/**
 * To change this template use Preferences | File and Code Templates.
 * @author Jorge Migueis
 *         Date: 18/07/2013
 */
class G1HowWePayYouPage(browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G1HowWePayYouPage.url, G1HowWePayYouPage.title, previousPage) {
  declareRadioList("#likeToPay", "HowWePayYouHowWouldYouLikeToGetPaid")
  declareSelect("#paymentFrequency", "HowWePayYouHowOftenDoYouWantToGetPaid")
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object d
 * efined in Page.scala
 */
object G1HowWePayYouPage {
  val title = "How We Pay You - Pay Details".toLowerCase

  val url  = "/pay-details/how-we-pay-you"

  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G1HowWePayYouPage(browser,previousPage)
}

/** The context for Specs tests */
trait G1HowWePayYouPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G1HowWePayYouPage buildPageWith browser
}
