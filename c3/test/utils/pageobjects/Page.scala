package utils.pageobjects

import java.util.concurrent.TimeUnit
import org.specs2.specification.Scope
import org.openqa.selenium.TimeoutException
import org.fluentlenium.core.Fluent
import scala.util.Try

/**
 * Super-class of all the PageObject pattern compliant classes representing an application page.
 * @author Jorge Migueis
 *         Date: 08/07/2013
 */
abstract case class Page(pageFactory: PageFactory, ctx:PageObjectsContext, url: String, pageTitle: String, iteration: Int = 1) extends Object with FormFields with WebSearchActions with WebFillActions {

  // Cache of the page source
  protected var pageSource = ""

  // Indicates whether we need to reset iteration number because we are leaving an iterative section.
  protected var resetIteration = false

  /* Has the user successfully left this page? if yes then she should not be able to modify it.
     To modify it, she needs to go back to the page, which will create a new page object.
   */
  private var pageLeftOrSubmitted = false

  /**
   * Go to the html page corresponding to the current object.
   * If the landing page is not the expected page then throws an exception, unless asked otherwise.
   * @param throwException Should the page throw an exception if landed on different page? By default yes.
   * @param waitForPage Does the test need add extra time to wait every time it goes a page? By default set to true.
   * @return Page object presenting the page. It could be different from current if landed on different page and specified no exception to be thrown.
   */
  def goToThePage(throwException: Boolean = true, waitForPage: Boolean = true, waitDuration: Int = Page.WAIT_FOR_DURATION) =
    goToPage(this, throwException, waitForPage, waitDuration)

  /**
   * Go to the html page corresponding to the page passed as parameter.
   * If the landing page is not the expected page then throws an exception, unless asked otherwise.
   * @param page target page
   * @param throwException Should the page throw an exception if landed on different page? By default yes.
   * @param waitForPage Does the test need add extra time to wait every time it goes a page? By default set to true.
   * @return Page object presenting the page. It could be different from target page if landed on different page and specified no exception to be thrown.
   */
  def goToPage(page: Page, throwException: Boolean = true, waitForPage: Boolean = true, waitDuration: Int = Page.WAIT_FOR_DURATION) = {
    val newPage = goToUrl(page, throwException, waitForPage, waitDuration)
    if (page != this) this.pageLeftOrSubmitted = true
    newPage
  }

  /**
   * Click on back/previous button of the page (not of the browser)
   * @param waitForPage Does the test need add extra time to wait every time it goes back to a page? By default set to true.
   * @return Page object representing the html page the UI went back to.
   */
  def goBack(waitForPage: Boolean = true, waitDuration: Int = Page.WAIT_FOR_DURATION) = {
    val fluent = ctx.browser.click("#backButton")
    val title = getPageTitle(fluent, waitForPage, waitDuration)
    createPageWithTitle(title, iteration)
  }

  /**
   * Read a claim and interacts with browser to populate page.
   * @param theClaim   Data to use to fill page
   */
  def fillPageWith(theClaim: TestData): Page = {
    try {
      fields foreach {
        case (cssElem: String, fieldType: Symbol, claimAttribute: String) =>
          fieldType match {
            case ADDRESS => fillAddress(cssElem, theClaim.selectDynamic(claimAttribute))
            case CHECK => fillCheck(cssElem, theClaim.selectDynamic(claimAttribute))
            case DATE => fillDate(cssElem, theClaim.selectDynamic(claimAttribute))
            case DATE_FROM => fillDate(cssElem + "_from", theClaim.selectDynamic(claimAttribute))
            case DATE_TO => fillDate(cssElem + "_to", theClaim.selectDynamic(claimAttribute))
            case INPUT => fillInput(cssElem, theClaim.selectDynamic(claimAttribute))
            case JSINPUT => fillJSInput(cssElem, theClaim.selectDynamic(claimAttribute))
            case NINO => fillNino(cssElem, theClaim.selectDynamic(claimAttribute))
            case RADIO_LIST => fillRadioList(cssElem, theClaim.selectDynamic(claimAttribute))
            case SELECT => fillSelect(cssElem, theClaim.selectDynamic(claimAttribute))
            case SORTCODE => fillSortCode(cssElem, theClaim.selectDynamic(claimAttribute))
            case TIME => fillTime(cssElem, theClaim.selectDynamic(claimAttribute))
            case YESNO => fillYesNo(cssElem, theClaim.selectDynamic(claimAttribute))
            case YESNODONTKNOW => fillYesNoDontknow(cssElem, theClaim.selectDynamic(claimAttribute))
            case _ => throw new PageObjectException("Framework error in Page.fillPageWith. Unhandled fieldType: " + fieldType.name)
          }
      }
      this
    }
    catch {
      case e: Exception => throw new PageObjectException("Error when filling form in page [" + pageTitle + "]", exception = e)
    }
  }

  /**
   * Read a claim and interacts with browser to populate page.
   * @param theClaim   Claim to populate
   */
  def populateClaim(theClaim: TestData): Page = {
    try {
      fields foreach {
        case (cssElem: String, fieldType: Symbol, claimAttribute: String) =>

          def assignToClaimAttribute(value: Option[String]) =
            if (value.isDefined) theClaim.updateDynamic(claimAttribute)(value.get)

          fieldType match {
            case ADDRESS => assignToClaimAttribute(readAddress(cssElem))
            case CHECK => assignToClaimAttribute(readCheck(cssElem))
            case DATE => assignToClaimAttribute(readDate(cssElem))
            case DATE_FROM => assignToClaimAttribute(readDate(cssElem + "_from"))
            case DATE_TO => assignToClaimAttribute(readDate(cssElem + "_to"))
            case INPUT | JSINPUT => assignToClaimAttribute(readInput(cssElem))
            case NINO => assignToClaimAttribute(readNino(cssElem))
            // TODO            case RADIO_LIST => fillRadioList(cssElem, theClaim.selectDynamic(claimAttribute))
            case SELECT => assignToClaimAttribute(readSelect(cssElem))
            case SORTCODE => assignToClaimAttribute(readSortCode(cssElem))
            case TIME => assignToClaimAttribute(readTime(cssElem))
            case YESNO => assignToClaimAttribute(readYesNo(cssElem))
            case YESNODONTKNOW => assignToClaimAttribute(readYesNoDontknow(cssElem))
            case _ => throw new PageObjectException("Framework error in Page.populateClaim. Unhandled fieldType: " + fieldType.name)
          }
      }
      this
    }
    catch {
      case e: Exception => throw new PageObjectException("Error when reading form in page [" + pageTitle + "]", exception = e)
    }
  }


  /**
   * Reads theClaim, interacts with browser to populate the page, submit the page and
   * asks next page to run the claim. By default throws a PageObjectException if a page displays errors.
   * @param theClaim  Data to use to populate all the pages relevant to the scenario tested.
   * @param upToPageWithTitle  Title of the page where the automated completion should stop.
   * @param upToIteration Iteration number of the page the automated completion should stop. By default set to 1.
   * @param throwException Specify whether should throw an exception if a page displays errors. By default set to true.
   * @param waitForPage Does the test need add extra time to wait for the next page every time it submits a page? By default set to true.
   * @param trace If set to yes, then print to console the titles, iterations and url of pages traversed.
   * @return Last page
   */
  final def runClaimWith(theClaim: TestData, upToPageWithTitle: String, upToIteration: Int = 1, throwException: Boolean = true, waitForPage: Boolean = true, waitDuration: Int = Page.WAIT_FOR_DURATION, trace: Boolean = false): Page = {
    if (this.pageLeftOrSubmitted) throw PageObjectException("This page was already left or submitted. It cannot be (re)submitted." + this.toString)
    if (pageTitle == upToPageWithTitle && iteration == upToIteration) this
    else {
      if (trace) println(this.pageTitle + " @ " + url + " : Iteration " + iteration)
      this fillPageWith theClaim
      submitPage(throwException, waitForPage, waitDuration) runClaimWith(theClaim, upToPageWithTitle, upToIteration, throwException, waitForPage, waitDuration, trace)
    }
  }


  /**
   * Click the submit/next button of a page. By default does not throw a PageObjectException if a page displays errors.
   * @param throwException Specify whether should throw an exception if a page displays errors. By default set to false.
   * @param waitForPage Does the test need add extra time to wait for the next page every time it submits a page? By default set to true.
   * @param waitDuration Duration of the wait in seconds.
   * @return next Page or same page if errors detected and did not ask for exception.
   */
  def submitPage(throwException: Boolean = false, waitForPage: Boolean = true, waitDuration: Int = Page.WAIT_FOR_DURATION) = {
    if (this.pageLeftOrSubmitted) throw PageObjectException("This page was already left or submitted. It cannot be submitted. " + this.toString)
    try {
      this.pageSource = getPageSource() // cache page source so can always look back at html as it was when submitting.
      val fluent = ctx.browser.submit("button[type='submit']")
      if (errorsInPage(throwException)) this
      else {
        val title = getPageTitle(fluent, waitForPage, waitDuration)
        createPageWithTitle(title, if (!resetIteration) getNewIterationNumber else 1)
      }
    }
    catch {
      case e: Exception => throw new PageObjectException("Could not submit page [" + this.pageTitle + "] because " + e.getMessage, exception = e)
    }
  }

  /**
   * Go to a completed section.
   * @param waitForPage Does the test need add extra time to wait for the next page every time it submits a page? By default set to true.
   * @param waitDuration Duration of the wait in seconds.
   * @return
   */
  def goToCompletedSection(waitForPage: Boolean = false, waitDuration: Int = Page.WAIT_FOR_DURATION) = {
    val fluent = ctx.browser.click("div[class=completed] ul li a")
    val title = getPageTitle(fluent, waitForPage, waitDuration)
    createPageWithTitle(title, iteration)
  }

  def goToPageFromIterationsTableAtIndex(index: Int, location: String = "input[value='Change']", waitForPage: Boolean = true, waitDuration: Int = Page.WAIT_FOR_DURATION) = {
    ctx.browser.find(location, index).click
    val title = getPageTitle(null, waitForPage, waitDuration)
    createPageWithTitle(title, iteration)
  }


  /**
   * Returns html code of the page.
   * @return source code of the page encapsulated in a String
   */
  def source() = if (this.pageLeftOrSubmitted) this.pageSource else getPageSource()


  def jsCheckEnabled() = source.contains("jsEnabled") && source.contains("#js")

  /**
   * Provides the full list of pages traversed to get to the current page.
   * @return String that lists the titles of the pages traversed, separated by '<'.
   */
  def fullPagePath: String = {
    if (ctx.previousPage == None) this.pageTitle
    else this.pageTitle + " < " + ctx.previousPage.get.fullPagePath
  }

  def getUrl = url

  // ==================================================================
  //  NON PUBLIC FUNCTIONS
  // ==================================================================

  protected def getNewIterationNumber: Int = iteration

  protected def waitForPage(waitDuration: Int) = {
    try {
      ctx.browser.waitUntil[Boolean](waitDuration, TimeUnit.SECONDS) {
        titleMatch()._1
      }
    }
    catch {
      case e: TimeoutException => throw new PageObjectException("Time out. Framework in page [" + getTitleFromBrowser() + "] while expected to reach page [" + this.pageTitle + "]")
    }
    this
  }

  protected def waitForNewTitle(waitDuration: Int) = {
    try {
      var matchResult = (false, "")
      ctx.browser.waitUntil[Boolean](waitDuration, TimeUnit.SECONDS) {
        matchResult = titleDoesNotMatch("")
        matchResult._1
      }
      matchResult._2
    }
    catch {
      case e: TimeoutException => throw new PageObjectException("Time out while awaiting for a page with title different from [" + this.pageTitle + "]")
    }
  }

  protected def titleMatch(expectedTitle: String = this.pageTitle): (Boolean, String) = {
    try {
      val titleRead = getTitleFromBrowser()
      (if (titleRead != null) titleRead.toLowerCase == expectedTitle.toLowerCase else false, titleRead)
    }
    catch {
      case _: Exception => (false, "")
    }
  }

  protected def titleDoesNotMatch(expectedTitle: String = this.pageTitle): (Boolean, String) = {
    val matching = titleMatch(expectedTitle)
    (!matching._1, matching._2)
  }

  protected def getTitleFromBrowser(index: Int = 0): String = {
    if (index < 5) {
      try {
        val htmlTitle = ctx.browser.title()
        if (htmlTitle != null && htmlTitle.isEmpty) getTitleFromBrowser(index + 1)
        else htmlTitle
      }
      catch {
        case _: Exception => getTitleFromBrowser(index + 1)
      }
    } else ""
  }


  protected def getPageTitle(fluent: Fluent, waitForPage: Boolean, waitDuration: Int): String = {
    def htmlTitle = try {
      if (fluent != null) fluent.title else getTitleFromBrowser()
    } catch {
      case _: Exception => getTitleFromBrowser()
    }

    if (waitForPage && htmlTitle != null && htmlTitle.isEmpty) waitForNewTitle(waitDuration) else htmlTitle
  }

  private def createPageWithTitle(title: String, newIterationNumber: Int) = {
    this.pageLeftOrSubmitted = true
    val context = PageObjectsContext(ctx.browser,ctx.iterationManager,Some(this))
    pageFactory buildPageFromTitle(title, context)
  }

  private def goToUrl(page: Page, throwException: Boolean, waitForPage: Boolean, waitDuration: Int) = {
    if (!this.pageLeftOrSubmitted) this.pageSource = getPageSource()
    val fluent = ctx.browser.goTo(page.url)
    val title = getPageTitle(fluent, waitForPage, waitDuration)
    if (title.toLowerCase != page.pageTitle.toLowerCase) {
      if (throwException) throw new PageObjectException("Could not go to page with title: " + page.pageTitle + " Iteration(" + iteration + ") - Page loaded with title: " + title)
      else this.createPageWithTitle(title, 1)
    } else page
  }

  protected def errorsInPage(throwException: Boolean) = {
    if (!this.listErrors.isEmpty) {
      if (throwException) throw new PageObjectException("Page " + this.getClass + " \"" + pageTitle + "\" Submit failed with errors: ", this.listErrors)
      true
    }
    else false
  }

  private def getPageSource() = try {
    ctx.browser.pageSource()
  }
  catch {
    case _: Exception => "Error: Page source not available."
  }
}

/**
 * A page object that represents an unknown html page, i.e. a page that is not covered by the framework.
 * A developer should create a new sub-class of Page to handle this "unknown" page.
 * @param pageTitle  Title of the unknown page
 * @param context global context for the pageobjects
 */
final class UnknownPage(pageTitle: String, context:PageObjectsContext) extends Page(null, context, null, pageTitle) {
  protected def createNextPage(): Page = this

  /**
   * Throws a PageObjectException.
   * @param throwException Should the page throw an exception if landed on different page? By default yes.
   * @return Page object presenting the page. It could be different from current if landed on different page and specified no exception to be thrown.
   */
  override def goToThePage(throwException: Boolean = true, waitForPage: Boolean = true, waitDuration: Int = Page.WAIT_FOR_DURATION) =
    throw new PageObjectException("Cannot leave an unknown page: " + pageTitle)

  /**
   * Throws a PageObjectException.
   * @param throwException Specify whether should throw an exception if a page displays errors. By default set to false.
   * @return next Page or same page if errors detected and did not ask for exception.
   */
  override def submitPage(throwException: Boolean = false, waitForPage: Boolean = false, waitDuration: Int = Page.WAIT_FOR_DURATION) =
    throw new PageObjectException("Cannot submit an unknown page: " + pageTitle)

  /**
   * Throws a PageObjectException.
   * @param theClaim   Data to use to fill page
   */
  override def fillPageWith(theClaim: TestData): Page =
    throw new PageObjectException("Cannot fill an unknown page [" + pageTitle + "] Previous page was [" + ctx.previousPage.getOrElse(this).pageTitle + "]. Page content is" + source())

  /**
   * Throws a PageObjectException.
   * @param theClaim   Claim to populate.
   */
  override def populateClaim(theClaim: TestData): Page =
    throw new PageObjectException("Cannot populate a claim from an unknown page [" + pageTitle + "] Previous page was [" + ctx.previousPage.getOrElse(this).pageTitle + "]. Page content is" + source())
}

object Page {
  val WAIT_FOR_DURATION: Int = Try(System.getProperty("waitSeconds", "30").toInt).getOrElse(30)
}

trait PageContext extends Scope {
}
