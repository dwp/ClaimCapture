package utils.pageobjects.s1_carers_allowance

import play.api.test.TestBrowser
import utils.pageobjects.{PageContext, ClaimScenario, Page}

/**
 * PageObject pattern associated to S1 carers allowance G3 Over 16 page.
 * @author Jorge Migueis
 *         Date: 13/07/2013
 */
final class G3Over16Page(browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G3Over16Page.url, G3Over16Page.title, previousPage) {

  /* temporary, until tested class is refactored and use new common components. */
  private val separator  = "-"

  /**
   * Reads theClaim and interacts with browser to populate page.
   * @param theClaim   Data to use to fill page
   */
  def fillPageWith(theClaim: ClaimScenario) {
    fillYesNo("#q3",theClaim.CanYouGetCarersAllowanceAreYouAged16OrOver, separator)
  }

  def isQ2Yes: Boolean = isSpecifiedSectionCompleted(1, "Q2", "Yes")
  def isQ2No: Boolean = isSpecifiedSectionCompleted(1, "Q2", "No")
}


/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in Page.scala
 */
object G3Over16Page {
  val title = "Over 16 - Carer's Allowance"
  val url = "/allowance/over16"
  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G3Over16Page(browser, previousPage)
}

/** The context for Specs tests */
trait G3Over16PageContext extends PageContext {
  this: {val browser:TestBrowser}  =>
  val page = G3Over16Page buildPageWith browser
}