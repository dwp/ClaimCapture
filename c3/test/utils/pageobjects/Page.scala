package utils.pageobjects

import play.api.test.TestBrowser
import java.util.concurrent.TimeUnit


/**
 * To change this template use Preferences | File and Code Templates.
 * @author Jorge Migueis
 *         Date: 08/07/2013
 */
abstract case class Page(browser: TestBrowser, url: String, pageTitle: String) {

  def goToPage() = {
    browser.goTo(url)
    waitForPage()
  }

  // def goToUrl(url: String) = browser.goTo("/allowance/benefits?changing=true")

  def goToPage(page: Page) = browser.goTo(page.url)

  def submitPage(): Page

  def waitForPage() = browser.waitUntil[Boolean](30, TimeUnit.SECONDS) {
    browser.title == pageTitle
  }

  protected def clickSubmit() = browser.submit("button[type='submit']")

  protected def titleMatch(): Boolean = browser.title == this.pageTitle
}

