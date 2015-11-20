package utils.pageobjects.tests

import utils.pageobjects._
import org.specs2.mock.Mockito
import org.fluentlenium.core.domain.{FluentWebElement, FluentList}
import java.util
import java.util.concurrent.TimeUnit


/**
 * Companion object that integrates factory method.
 */
object MockPage {
  val url = "/mockpage"
  def buildPage(ctx:PageObjectsContext) = new MockPage(ctx)
}

/**
 * To change this template use Preferences | File and Code Templates.
 * @author Jorge Migueis
 *         Date: 10/07/2013
 */
class MockPage (ctx:PageObjectsContext) extends ClaimPage(ctx, MockPage.url){
  /**
   * Sub-class reads theClaim and interacts with browser to populate page.
   * @param theClaim   Data to use to fill page
   */
  override def fillPageWith(theClaim: TestData): Page = this
}


/** The context for Specs tests */
trait MockPageContext extends PageContext with Mockito {
  val browser = {
    val mockedBrowser = mock[play.api.test.TestBrowser]
    mockedBrowser.url returns MockPage.url
    mockedBrowser.submit("button[type='submit']") returns mockedBrowser
    mockedBrowser.click(".form-steps a") returns mockedBrowser
    mockedBrowser.find("div[class=validation-summary] ol li") returns new FluentList[FluentWebElement](new util.ArrayList[FluentWebElement]())
    mockedBrowser.goTo(anyString) returns mockedBrowser
    val test = (x:Boolean) => true
    mockedBrowser.waitUntil[Boolean](30, TimeUnit.SECONDS)(_:Boolean) returns true
    mockedBrowser
  }

  val page = MockPage buildPage PageObjectsContext(browser)
}

/** The context for Specs tests */
trait MockPageWrongTitleContext extends PageContext with Mockito {
  val browser = {
    val mockedBrowser = mock[play.api.test.TestBrowser]
//    mockedBrowser.title returns  "Wrong Title"
    mockedBrowser.url returns "/wrongurl"
    val test = (x:Boolean) => true
    mockedBrowser.waitUntil[Boolean](30, TimeUnit.SECONDS)(_:Boolean) returns true
    mockedBrowser.waitUntil[Boolean](20, TimeUnit.SECONDS)(_:Boolean) returns false
    mockedBrowser
  }

  val page = MockPage buildPage PageObjectsContext(browser)
}
