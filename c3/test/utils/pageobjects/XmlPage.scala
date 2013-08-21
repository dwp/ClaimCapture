package utils.pageobjects

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.xml_validation.XMLBusinessValidation

/**
 * Represents the test page where the XML "Submitted" is dumped into.
 * @author Jorge Migueis
 *         Date: 02/08/2013
 */
class XmlPage (browser: TestBrowser, previousPage: Option[Page] = None) extends Page(null,browser, XmlPage.url, XmlPage.title, previousPage) {

   pageSource = browser.pageSource()

  def validateXmlWith(claim: ClaimScenario) = {
    val validator = new XMLBusinessValidation("/ClaimScenarioXmlMapping.csv")
    Tuple2(validator.validateXMLClaim(claim, pageSource, throwException = false),validator.warnings)
  }

  /**
   * Throws a PageObjectException.
   * @param throwException Specify whether should throw an exception if a page displays errors. By default set to false.
   * @return next Page or same page if errors detected and did not ask for exception.
   */
  override def submitPage(throwException: Boolean = false, waitForPage: Boolean = false, waitDuration: Int = Page.WAIT_FOR_DURATION) = throw new PageObjectException("Cannot submit the XML test page: " + pageTitle)

  /**
   * Throws a PageObjectException.
   * @param theClaim   Data to use to fill page
   */
  override def fillPageWith(theClaim: ClaimScenario): Page = {
    throw new PageObjectException("Cannot fill the XML test page [" + pageTitle +"] Previous page was [" + previousPage.getOrElse(this).pageTitle + "]" )
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