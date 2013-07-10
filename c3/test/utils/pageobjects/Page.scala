package utils.pageobjects

import play.api.test.TestBrowser
import org.specs2.specification.Scope
import java.util.concurrent.TimeUnit


/**
 * Super-class of all the PageObject pattern compliant classes representing an application page.
 * @author Jorge Migueis
 *         Date: 08/07/2013
 */
abstract case class Page(browser: TestBrowser, url: String, pageTitle: String) {

  // ========================================
  // Page Management
  // ========================================

  def goToThePage() = goToUrl(url)

  def goToPage(page: Page) = goToUrl(page.url)

  /**
   * Sub-class reads theClaim and interacts with browser to populate page.
   * @param theClaim   Data to use to fill page
   */
  def fillPageWith(theClaim: ClaimScenario)

  /**
   * Reads theClaim, interacts with browser to populate the page, submit the page and
   * asks next page to run the claim.
   * @param theClaim  Data to use to populate all the pages relevant to the scenario tested.
   */
  def runClaimWith(theClaim: ClaimScenario): Unit = {
    fillPageWith(theClaim)
    val nextPage = submitPage
    nextPage runClaimWith(theClaim)
  }

  def submitPage() = {
    val nextPageTile = browser.submit("button[type='submit']").title()
    if (hasErrors()) throw new PageObjectException( """Page """" + nextPageTile + """" has errors. Submit failed.""")
    createPageWithTitle(nextPageTile)
  }

  def goBack() = {
    val backPageTile = browser.click(".form-steps a").title()
    createPageWithTitle(backPageTile)
  }

  def waitForPage() = browser.waitUntil[Boolean](30, TimeUnit.SECONDS) {
    browser.title == pageTitle
  }

  protected def titleMatch(): Boolean = browser.title == this.pageTitle

  protected def hasErrors() = !browser.find("div[class=validation-summary] ol li").isEmpty

  private def createPageWithTitle(title: String) = {
    val newPage = PageFactory createPageFromTitle(browser, title)
    newPage.waitForPage()
    newPage
  }

  private def goToUrl(nextUrl: String) = {
    browser.goTo(nextUrl)
    waitForPage()
  }

  // ========================================
  // Component Management
  // ========================================
  protected def isCompletedYesNo(location: String, index: Integer, name: String, value: String) = {
    val completed = browser.find(location).get(index).getText()
    completed.contains(name) && completed.contains(value)
  }

  protected def valueOfYesNo(location: String): Option[Boolean] = {
    browser.find(location).getAttribute("value") match {
      case "true" => Some(true)
      case "false" => Some(false)
      case _ => None
    }
  }
}

/**
 * A page object that represents an unknown html page, i.e. a page that is not covered by the framework.
 * A developer should create a new sub-class of Page to handle this "unknown" page.
 * @param browser  webDriver browser
 * @param pageTitle  Title of the unknown page
 */
final class UnknownPage(browser: TestBrowser, pageTitle: String) extends Page(browser, null, pageTitle) {
  protected def createNextPage(): Page = this

  override def submitPage() = throw new PageObjectException("Cannot submit unknown page " + pageTitle)

  /**
   * Sub-class reads theClaim and interact with browser to populate page.
   * @param theClaim   Data to use to fill page
   */
  def fillPageWith(theClaim: ClaimScenario) {}
}


trait PageContext extends Scope {

}