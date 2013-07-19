package utils.pageobjects

import play.api.test.TestBrowser
import org.specs2.specification.Scope
import java.util.concurrent.TimeUnit
import scala.collection.convert.Wrappers._

/**
 * Super-class of all the PageObject pattern compliant classes representing an application page.
 * @author Jorge Migueis
 *         Date: 08/07/2013
 */
abstract case class Page(browser: TestBrowser, url: String, pageTitle: String, previousPage: Option[Page] = None, iteration: Int = 1) extends Object with WebSearchActions with WebFillActions {

  /**
   * Go to the html page corresponding to the current object.
   * If the landing page is not the expected page then throws an exception, unless asked otherwise.
   * @param throwException Should the page throw an exception if landed on different page? By default yes.
   * @return Page object presenting the page. It could be different from current if landed on different page and specified no exception to be thrown.
   */
  def goToThePage(throwException: Boolean = true) = goToUrl(this, throwException)

  /**
   * Go to the html page corresponding to the page passed as parameter.
   * If the landing page is not the expected page then throws an exception, unless asked otherwise.
   * @param page target page
   * @param throwException Should the page throw an exception if landed on different page? By default yes.
   * @return Page object presenting the page. It could be different from target page if landed on different page and specified no exception to be thrown.
   */
  def goToPage(page: Page, throwException: Boolean = true) = goToUrl(page, throwException)

  /**
   * Click on back/previous button of the page (not of the browser)
   * @return Page object representing the html page the UI went back to.
   */
  def goBack() = {
    val backPageTile = browser.click(".form-steps a").title
    createPageWithTitle(backPageTile)
  }

  /**
   * Sub-class reads theClaim and interacts with browser to populate page.
   * @param theClaim   Data to use to fill page
   */
  def fillPageWith(theClaim: ClaimScenario)

  /**
   * Reads theClaim, interacts with browser to populate the page, submit the page and
   * asks next page to run the claim. By default throws a PageObjectException if a page displays errors.
   * @param theClaim  Data to use to populate all the pages relevant to the scenario tested.
   * @param upToPageWithTitle  Title of the page where the automated completion should stop.
   * @param throwException Specify whether should throw an exception if a page displays errors. By default set to true.
   * @return Last page
   */
  def runClaimWith(theClaim: ClaimScenario, upToPageWithTitle: String, throwException: Boolean = true): Page = {
    if (pageTitle == upToPageWithTitle) {
      this
    } else {
      this fillPageWith theClaim
      submitPage(throwException) runClaimWith(theClaim, upToPageWithTitle)
    }
  }

  /**
   * Click the submit/next button of a page. By default does not throw a PageObjectException if a page displays errors.
   * @param throwException Specify whether should throw an exception if a page displays errors. By default set to false.
   * @return next Page or same page if errors detected and did not ask for exception.
   */
  def submitPage(throwException: Boolean = false) = {
    val nextPageTile = browser.submit("button[type='submit']").title
    if (this checkNoErrorsForPage(nextPageTile, throwException)) this
    else this createPageWithTitle nextPageTile
  }


  def goToCompletedSection() = {
    val completedPage = browser.click("div[class=completed] ul li a").title
    createPageWithTitle(completedPage)
  }

  /**
   * Returns html code of the page.
   * @return source code of the page encapsulated in a String
   */
  def source() = browser.pageSource()

  /**
   * Provides the list of errors displayed in a page. If there is no error then return None.
   * @return a Option containing the list of errors displayed by a page.
   */
  def listErrors() : Option[List[String]] = {
    val rawErrors = browser.find("div[class=validation-summary] ol li")
    if (!rawErrors.isEmpty) {
      Some(new JListWrapper(rawErrors.getTexts).toList)
    } else None
  }

  def listCompletedForms = findTarget("div[class=completed] ul li")
  
  def findTarget(target: String) : List[String] = {
    val rawErrors = browser.find(target)
    if (!rawErrors.isEmpty) new JListWrapper(rawErrors.getTexts).toList
    else List()
  }

  // ==================================================================
  //  NON PUBLIC FUNCTIONS
  // ==================================================================

  protected def waitForPage() =  {
    browser.waitUntil[Boolean](30, TimeUnit.SECONDS) { titleMatch() }
    this
  }

  protected def titleMatch(): Boolean = browser.title == this.pageTitle

  protected def checkNoErrorsForPage(nextPageTile: String, throwException: Boolean = false) = {
    if (this.listErrors() != None) {
      if (throwException)  throw new PageObjectException( """Page """" + nextPageTile + """" has errors. Submit failed""", this.listErrors().get)
      true
    }
    else false
  }

  private def createPageWithTitle(title: String) = {
    val newPage = PageFactory buildPageFromTitle(browser, title, Some(this), iteration + 1)
    newPage
  }


  private def goToUrl(page: Page, throwException: Boolean) = {
    browser.goTo(page.url)
    if (!page.titleMatch) {
      if (throwException) throw new PageObjectException("Could not go to page " + page.pageTitle + " - Page loaded " + browser.title)
      else this.createPageWithTitle(browser.title)
    } else page.waitForPage()
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

  /**
   * Throws a PageObjectException.
   * @param throwException Should the page throw an exception if landed on different page? By default yes.
   * @return Page object presenting the page. It could be different from current if landed on different page and specified no exception to be thrown.
   */
  override def goToThePage(throwException: Boolean = true) = throw new PageObjectException("Cannot go to an unknown page")

  /**
   * Throws a PageObjectException.
   * @param throwException Specify whether should throw an exception if a page displays errors. By default set to false.
   * @return next Page or same page if errors detected and did not ask for exception.
   */
  override def submitPage(throwException: Boolean = false) = throw new PageObjectException("Cannot submit an unknown page: " + pageTitle)

  /**
   * Throws a PageObjectException.
   * @param theClaim   Data to use to fill page
   */
  def fillPageWith(theClaim: ClaimScenario) { throw new PageObjectException("Cannot fill an unknown page: " + pageTitle)}
}


trait PageContext extends Scope {
}