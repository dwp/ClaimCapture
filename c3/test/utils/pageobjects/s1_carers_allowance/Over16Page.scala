package utils.pageobjects.s1_carers_allowance

import play.api.test.TestBrowser
import utils.pageobjects.{PageContext, ClaimScenario, Page}

/**
 * To change this template use Preferences | File and Code Templates.
 * @author Jorge Migueis
 *         Date: 13/07/2013
 */
class Over16Page(browser: TestBrowser) extends Page(browser, "/allowance/over16", Over16Page.title) {

  /* temporary, until tested class is refactored and use new common components. */
  private val separator  = "-"

  /**
   * Reads theClaim and interacts with browser to populate page.
   * @param theClaim   Data to use to fill page
   */
  def fillPageWith(theClaim: ClaimScenario) {}

  def isQ2Yes(): Boolean = isCompletedYesNo(1, "Q2", "Yes")

  def isQ2No(): Boolean = isCompletedYesNo(1, "Q2", "No")
}


/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in Page.scala
 */
object Over16Page {
  val title = "Over 16 - Carer's Allowance"
  def buildPageWith(browser: TestBrowser) = new Over16Page(browser)
}

/** The context for Specs tests */
trait Over16PageContext extends PageContext {
  this: {val browser:TestBrowser}  =>
  val page = Over16Page buildPageWith browser
}