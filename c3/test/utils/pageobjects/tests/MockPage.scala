package utils.pageobjects.tests

import play.api.test.TestBrowser
import utils.pageobjects.{PageContext, ClaimScenario, Page}
import org.specs2.mock.Mockito
import org.fluentlenium.core.domain.{FluentWebElement, FluentList}
import java.util
import java.util.concurrent.TimeUnit

/**
 * To change this template use Preferences | File and Code Templates.
 * @author Jorge Migueis
 *         Date: 10/07/2013
 */
class MockPage (browser: TestBrowser, title: String) extends Page(browser, "/", title){
  /**
   * Sub-class reads theClaim and interacts with browser to populate page.
   * @param theClaim   Data to use to fill page
   */
  def fillPageWith(theClaim: ClaimScenario) {}
}


/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in Page.scala
 */
object MockPage {
  val title = "Mock Page"
  def buildPage(browser: TestBrowser) = new MockPage(browser, title)
}

/** The context for Specs tests */
trait MockPageContext extends PageContext with Mockito {
  val browser = {
    val mockedBrowser = mock[play.api.test.TestBrowser]
    mockedBrowser.title returns  MockPage.title
    mockedBrowser.submit("button[type='submit']") returns mockedBrowser
    mockedBrowser.click(".form-steps a") returns mockedBrowser
    mockedBrowser.find("div[class=validation-summary] ol li")  returns new FluentList[FluentWebElement](new util.ArrayList[FluentWebElement]())
    mockedBrowser.goTo(anyString) returns mockedBrowser
//    mockedBrowser.waitUntil[Boolean](30, TimeUnit.SECONDS) {true} returns true
    mockedBrowser
  }
  val page = MockPage buildPage(browser)
}

