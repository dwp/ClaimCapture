package utils.pageobjects

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.xml_validation.XMLBusinessValidation

/**
 * Represents the test page where the XML "Submitted" is dumped into.
 * @author Jorge Migueis
 *         Date: 02/08/2013
 */
class XmlPage (browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, XmlPage.url, XmlPage.title, previousPage) {

   pageSource = browser.pageSource()

  def validateXmlWith(claim: ClaimScenario) = {
    val validator = new XMLBusinessValidation("/ClaimScenarioXmlMapping.csv")
    validator.validateXMLClaim(claim, pageSource, throwException = false)
  }
}


/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in Page.scala
 */
object XmlPage {
  val title = null
  val url = "/submit"
  def buildPageWith(browser: TestBrowser,previousPage: Option[Page] = None) = new XmlPage(browser, previousPage)
}

/** The context for Specs tests */
trait TestPageContext extends PageContext {
  this: WithBrowser[_] =>
  val page = XmlPage buildPageWith browser
}