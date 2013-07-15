package utils.pageobjects

import play.api.test.{WithBrowser, TestBrowser}
import org.specs2.specification.Scope
import java.util.concurrent.TimeUnit
import scala.collection.convert.Wrappers._
import org.openqa.selenium._

/**
 * Super-class of all the PageObject pattern compliant classes representing an application page.
 * @author Jorge Migueis
 *         Date: 08/07/2013
 */
abstract case class Page(browser: TestBrowser, url: String, pageTitle: String, previousPage: Option[Page] = None) extends Object with WebSearchActions with WebFillActions {

  def goToThePage() = goToUrl(this)

  def goToPage(page: Page) = goToUrl(page)

  /**
   * Sub-class reads theClaim and interacts with browser to populate page.
   * @param theClaim   Data to use to fill page
   */
  def fillPageWith(theClaim: ClaimScenario)

  /**
   * Reads theClaim, interacts with browser to populate the page, submit the page and
   * asks next page to run the claim.
   * @param theClaim  Data to use to populate all the pages relevant to the scenario tested.
   * @param upToPageWithTitle  Title of the page where the automated completion should stop.
   */
  def runClaimWith(theClaim: ClaimScenario, upToPageWithTitle: String): Page = {
    if (pageTitle == upToPageWithTitle) {
      this
    }else {
      this fillPageWith theClaim
      submitPage runClaimWith (theClaim,upToPageWithTitle)
    }
  }

  def submitPage() = {
    val nextPageTile = browser.submit("button[type='submit']").title()
    this checkNoErrorsForPage nextPageTile
    this createPageWithTitle nextPageTile
  }

  def goBack() = {
    val backPageTile = browser.click(".form-steps a").title()
    createPageWithTitle(backPageTile)
  }

  def pageSource() = browser.pageSource()

  protected def waitForPage() = browser.waitUntil[Boolean](30, TimeUnit.SECONDS) {
    browser.title == pageTitle
  }

  // ==================================================================
  //  NON PUBLIC FUNCTIONS
  // ==================================================================
  protected def titleMatch(): Boolean = browser.title == this.pageTitle

  protected def checkNoErrorsForPage(nextPageTile: String) = {
    val rawErrors = browser.find("div[class=validation-summary] ol li")
    if (!rawErrors.isEmpty) {
      throw new PageObjectException( """Page """" + nextPageTile + """" has errors. Submit failed""", (new JListWrapper(rawErrors.getTexts)).toList)
    }
  }

  private def createPageWithTitle(title: String) = {
    val newPage = PageFactory buildPageFromTitle(browser, title, Some(this))
    newPage.waitForPage()
    newPage
  }


  private def goToUrl(page: Page) = {
    browser.goTo(page.url)
    page.waitForPage()
    if (!page.titleMatch) throw new PageObjectException("Could not go to page " + page.pageTitle + " - Page loaded " + browser.title)
  }
}

/**
 * A page object that represents an unknown html page, i.e. a page that is not covered by the framework.
 * A developer should create a new sub-class of Page to handle this "unknown" page.
 * @param browser  webDriver browser
 * @param pageTitle  Title of the unknown page
 */
final class UnknownPage(browser: TestBrowser, pageTitle: String, previousPage: Option[Page] = None) extends Page(browser, null, pageTitle, previousPage) {
  protected def createNextPage(): Page = this

  override def submitPage() = throw new PageObjectException("Cannot submit an unknown page: " + pageTitle)

  /**
   * Sub-class reads theClaim and interact with browser to populate page.
   * @param theClaim   Data to use to fill page
   */
  def fillPageWith(theClaim: ClaimScenario) {}
}


trait PageContext extends Scope {
}