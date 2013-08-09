package utils.pageobjects

import java.util.concurrent.TimeUnit
import scala.collection.convert.Wrappers.JListWrapper
import org.specs2.specification.Scope
import play.api.test.TestBrowser
import org.openqa.selenium.TimeoutException


/**
 * Super-class of all the PageObject pattern compliant classes representing an application page.
 * @author Jorge Migueis
 *         Date: 08/07/2013
 */
abstract case class Page(browser: TestBrowser, url: String, pageTitle: String, previousPage: Option[Page] = None, iteration: Int = 1) extends Object with FormFields with WebSearchActions with WebFillActions {

  // Cache of the page source
  protected var pageSource = ""

  protected var resetIteration = false

  /* Has the user successfully left this page? if yes then she should not be able to modify it.
     To modify it, she needs to go back to the page, which will create a new page object.
   */
  private var pageLeft = false

  /**
   * Go to the html page corresponding to the current object.
   * If the landing page is not the expected page then throws an exception, unless asked otherwise.
   * @param throwException Should the page throw an exception if landed on different page? By default yes.
   * @param waitForPage Does the test need add extra time to wait every time it goes a page? By default set to true.
   * @return Page object presenting the page. It could be different from current if landed on different page and specified no exception to be thrown.
   */
  def goToThePage(throwException: Boolean = true, waitForPage: Boolean = true, waitDuration: Int = Page.WAIT_FOR_DURATION) = goToUrl(this, throwException, waitForPage, waitDuration)

  /**
   * Go to the html page corresponding to the page passed as parameter.
   * If the landing page is not the expected page then throws an exception, unless asked otherwise.
   * @param page target page
   * @param throwException Should the page throw an exception if landed on different page? By default yes.
   * @param waitForPage Does the test need add extra time to wait every time it goes a page? By default set to true.
   * @return Page object presenting the page. It could be different from target page if landed on different page and specified no exception to be thrown.
   */
  def goToPage(page: Page, throwException: Boolean = true, waitForPage: Boolean = true, waitDuration: Int = Page.WAIT_FOR_DURATION) = {
    this.pageLeft = true
    goToUrl(page, throwException, waitForPage, waitDuration)
  }

  /**
   * Click on back/previous button of the page (not of the browser)
   * @param waitForPage Does the test need add extra time to wait every time it goes back to a page? By default set to true.
   * @return Page object representing the html page the UI went back to.
   */
  def goBack(waitForPage: Boolean = true, waitDuration: Int = Page.WAIT_FOR_DURATION) = {
    this.pageLeft = true
    val backPageTile = browser.click(".form-steps a").title
    val newPage = createPageWithTitle(backPageTile, iteration)
    if (waitForPage) newPage.waitForPage(waitDuration) else newPage

  }

  /**
   * Sub-class reads theClaim and interacts with browser to populate page.
   * @param theClaim   Data to use to fill page
   */
  def fillPageWith(theClaim: ClaimScenario): Page = {
    try {
      fields foreach {
        case (cssElem: String, fieldType: Symbol, claimAttribute: String) =>
          fieldType match {
            case ADDRESS => fillAddress(cssElem, theClaim.selectDynamic(claimAttribute))
            case CHECK => fillCheck(cssElem, theClaim.selectDynamic(claimAttribute))
            case DATE => fillDate(cssElem, theClaim.selectDynamic(claimAttribute))
            case DATE_FROM => fillDate(cssElem, theClaim.selectDynamic(claimAttribute + "_from"))
            case DATE_TO => fillDate(cssElem, theClaim.selectDynamic(claimAttribute + "_to"))
            case INPUT => fillInput(cssElem, theClaim.selectDynamic(claimAttribute))
            case NINO => fillNino(cssElem, theClaim.selectDynamic(claimAttribute))
            case PAYMENT_FREQUENCY => fillPaymentFrequency(cssElem, theClaim.selectDynamic(claimAttribute))
            case RADIO_LIST => fillRadioList(cssElem, theClaim.selectDynamic(claimAttribute))
            case SELECT => fillSelect(cssElem, theClaim.selectDynamic(claimAttribute))
            case SORTCODE => fillSortCode(cssElem, theClaim.selectDynamic(claimAttribute))
            case TIME => fillTime(cssElem, theClaim.selectDynamic(claimAttribute))
            case WHEREABOUTS => fillWhereabouts(cssElem, theClaim.selectDynamic(claimAttribute))
            case YESNO => fillYesNo(cssElem, theClaim.selectDynamic(claimAttribute))
          }
      }
      return this
    }
    catch {
      case e:Exception => throw new PageObjectException("Error when filling form in page [" + pageTitle + "]",exception = e)
    }
  }

  /**
   * Reads theClaim, interacts with browser to populate the page, submit the page and
   * asks next page to run the claim. By default throws a PageObjectException if a page displays errors.
   * @param theClaim  Data to use to populate all the pages relevant to the scenario tested.
   * @param upToPageWithTitle  Title of the page where the automated completion should stop.
   * @param throwException Specify whether should throw an exception if a page displays errors. By default set to true.
   * @param waitForPage Does the test need add extra time to wait for the next page every time it submits a page? By default set to false.
   * @return Last page
   */
  final def runClaimWith(theClaim: ClaimScenario, upToPageWithTitle: String, throwException: Boolean = true, waitForPage: Boolean = false, waitDuration: Int = Page.WAIT_FOR_DURATION, trace: Boolean = false): Page = {
    if (this.pageLeft) throw PageObjectException("This page was already left or submitted. It cannot be submitted." + this.toString)
    if (pageTitle == upToPageWithTitle) {
      this
    } else {
      if (trace) println(this.pageTitle + " @ " + url + " : Iteration " + iteration)
      this fillPageWith theClaim
      submitPage(throwException, waitForPage, waitDuration) runClaimWith(theClaim, upToPageWithTitle, throwException, waitForPage, waitDuration, trace)
    }
  }

  /**
   * Click the submit/next button of a page. By default does not throw a PageObjectException if a page displays errors.
   * @param throwException Specify whether should throw an exception if a page displays errors. By default set to false.
   * @param waitForPage Does the test need add extra time to wait for the next page every time it submits a page? By default set to false.
   * @return next Page or same page if errors detected and did not ask for exception.
   */
  def submitPage(throwException: Boolean = false, waitForPage: Boolean = false, waitDuration: Int = Page.WAIT_FOR_DURATION) = {
    if (this.pageLeft) throw PageObjectException("This page was already left or submitted. It cannot be submitted." + this.toString)
    try {
      this.pageSource = browser.pageSource()
      val nextPageTile = browser.submit("button[type='submit']").title
      if (this checkNoErrorsForPage(nextPageTile, throwException)) this
      else {
        this.pageLeft = true
        val newPage = this createPageWithTitle(nextPageTile, if (!resetIteration) updateIterationNumber else 1)
        if (waitForPage) newPage.waitForPage(waitDuration) else newPage
      }
    }
    catch {
      case e:Exception => throw new PageObjectException("Could not submit page [" + this.pageTitle + "] because " + e.getMessage,exception = e)
    }
  }


  def goToCompletedSection() = {
    val completedPage = browser.click("div[class=completed] ul li a").title
    createPageWithTitle(completedPage, iteration)
  }

  /**
   * Returns html code of the page.
   * @return source code of the page encapsulated in a String
   */
  def source() = if (this.pageLeft) this.pageSource else browser.pageSource()

  /**
   * Provides the list of errors displayed in a page. If there is no error then return None.
   * @return a Option containing the list of errors displayed by a page.
   */
  def listErrors: List[String] = {
    try {
      val rawErrors = browser.find("div[class=validation-summary] ol li")

      if (!rawErrors.isEmpty) {
        new JListWrapper(rawErrors.getTexts).toList
      } else List[String]()
    }
    catch {
      case e: Exception => List[String]()
    }
  }

  def listCompletedForms = findTarget("div[class=completed] ul li")

  def findTarget(target: String): List[String] = {
    val rawErrors = browser.find(target)
    if (!rawErrors.isEmpty) new JListWrapper(rawErrors.getTexts).toList
    else List()
  }

  def fullPagePath: String = {
    if (previousPage == None) this.pageTitle
    else previousPage.get.fullPagePath + " > " + this.pageTitle
  }


  def getUrl = url

  // ==================================================================
  //  NON PUBLIC FUNCTIONS
  // ==================================================================

  protected def updateIterationNumber: Int = iteration

  protected def waitForPage(waitDuration: Int) = {
    try {
      browser.waitUntil[Boolean](waitDuration, TimeUnit.SECONDS) {
        titleMatch()
      }
    }
    catch {
      case e: TimeoutException => throw new PageObjectException("Time out while awaiting [" + browser.title + "] matches [" + this.pageTitle + "]")
    }
    this
  }

  protected def titleMatch(): Boolean = {
    try {
      if (browser.title != null) browser.title.toLowerCase == this.pageTitle.toLowerCase else true
    }
    catch {
      case e:Exception => throw new PageObjectException("Exception was thrown when comparing browser title with page title [" + this.pageTitle + "]",exception = e)
    }
  }

  protected def checkNoErrorsForPage(nextPageTile: String, throwException: Boolean = false) = {
    if (!this.listErrors.isEmpty) {
      if (throwException) throw new PageObjectException( "Page " + this.getClass + " \"" + nextPageTile + "\" Submit failed with errors: ", this.listErrors)
      true
    }
    else false
  }

  private def createPageWithTitle(title: String, newIterationNumber: Int) = {
    val newPage = PageFactory buildPageFromTitle(browser, title, Some(this), newIterationNumber)
    newPage
  }


  private def goToUrl(page: Page, throwException: Boolean, waitForPage: Boolean, waitDuration: Int) = {
    if (!this.pageLeft) this.pageSource = browser.pageSource()
    browser.goTo(page.url)
    if (!page.titleMatch) {
      if (throwException) throw new PageObjectException("Could not go to page with title: " + page.pageTitle + " - Page loaded with title: " + browser.title)
      else this.createPageWithTitle(browser.title, 1)
    } else if (waitForPage) page.waitForPage(waitDuration) else page

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
  override def goToThePage(throwException: Boolean = true, waitForPage: Boolean = true, waitDuration: Int = Page.WAIT_FOR_DURATION) = throw new PageObjectException("Cannot go to an unknown page: " + pageTitle)

  /**
   * Throws a PageObjectException.
   * @param throwException Specify whether should throw an exception if a page displays errors. By default set to false.
   * @return next Page or same page if errors detected and did not ask for exception.
   */
  override def submitPage(throwException: Boolean = false, waitForPage: Boolean = false, waitDuration: Int = Page.WAIT_FOR_DURATION) = throw new PageObjectException("Cannot submit an unknown page: " + pageTitle)

  /**
   * Throws a PageObjectException.
   * @param theClaim   Data to use to fill page
   */
  override def fillPageWith(theClaim: ClaimScenario): Page = {
    throw new PageObjectException("Cannot fill an unknown page: " + pageTitle)
  }
}

object Page {
  val WAIT_FOR_DURATION: Int = 30
}

trait PageContext extends Scope {
}
