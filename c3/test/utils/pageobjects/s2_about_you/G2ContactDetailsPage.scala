package utils.pageobjects.s2_about_you

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.{PageContext, Page}

/**
 * To change this template use Preferences | File and Code Templates.
 * @author Jorge Migueis
 *         Date: 16/07/2013
 */
final class G2ContactDetailsPage(browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G2ContactDetailsPage.url, G2ContactDetailsPage.title, previousPage) {
  declareAddress("#address", "AboutYouAddress")
  declareInput("#postcode", "AboutYouPostcode")
  declareInput("#phoneNumber", "AboutYouDaytimePhoneNumber")
  declareInput("#mobileNumber", "AboutYouMobileNumber")
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in Page.scala
 */
object G2ContactDetailsPage {
  val title = "Your contact details - About you - the carer".toLowerCase

  val url = "/about-you/contact-details"

  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G2ContactDetailsPage(browser, previousPage)
}

/** The context for Specs tests */
trait ContactDetailsPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G2ContactDetailsPage buildPageWith browser
}