package utils.pageobjects

import utils.WithBrowser
import utils.pageobjects.xml_validation.XMLBusinessValidation

/**
 * Represents the test page where the XML "Submitted" is dumped into.
 * @author Jorge Migueis
 *         Date: 02/08/2013
 */
class XmlPage (ctx: PageObjectsContext, url: String = XmlPage.url) extends Page(null,ctx, url) {

   pageSource = ctx.browser.pageSource()

  def validateXmlWith(claim: TestData, validator: XMLBusinessValidation) = {

    Tuple2(
      validator.validateXMLClaim(claim, pageSource, throwException = false),
      validator.warnings
    )
  }

  /**
   * Throws a PageObjectException.
   * @param throwException Specify whether should throw an exception if a page displays errors. By default set to false.
   * @return next Page or same page if errors detected and did not ask for exception.
   */
  override def submitPage(throwException: Boolean = false, waitForPage: Boolean = false, waitDuration: Int = Page.WAIT_FOR_DURATION) = throw new PageObjectException(s"Cannot submit the XML test page: $url")

  /**
   * Throws a PageObjectException.
   * @param theClaim   Data to use to fill page
   */
  override def fillPageWith(theClaim: TestData): Page = {
    throw new PageObjectException(s"Cannot fill the XML test page [$url] Previous page was [${ctx.previousPage.getOrElse(this).url}]" )
  }
}


/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in Page.scala
 */
object XmlPage {
  val url = "/submit" // or circumstancesSubmit
  def apply(ctx: PageObjectsContext) = new XmlPage(ctx)
}

/** The context for Specs tests */
trait TestPageContext extends PageContext {
  this: WithBrowser[_] =>
  val page = XmlPage (PageObjectsContext(browser))
}
