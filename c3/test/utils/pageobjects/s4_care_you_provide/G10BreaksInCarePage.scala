package utils.pageobjects.s4_care_you_provide

import play.api.test.TestBrowser
import utils.pageobjects.{PageContext, ClaimScenario, Page}

/**
 * Created with IntelliJ IDEA.
 * User: jmi
 * Date: 30/07/2013
 * Time: 09:40
 * To change this template use File | Settings | File Templates.
 */
class G10BreaksInCarePage(browser: TestBrowser, previousPage: Option[Page] = None, iteration: Int) extends Page(browser, G10BreaksInCarePage.url, G10BreaksInCarePage.title, previousPage, iteration) {
  /**
   * Sub-class reads theClaim and interacts with browser to populate page.
   * @param theClaim   Data to use to fill page
   */
  def fillPageWith(theClaim: ClaimScenario) {
     fillYesNo("#answer", theClaim.selectDynamic("AboutTheCareYouProvideHaveYouHadAnyMoreBreaksInCare_" + iteration))
  }
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in PageFactory.scala
 */
object G10BreaksInCarePage {
  val title = "Breaks in care - Care You Provide"
  val url  = "/careYouProvide/breaksInCare"
  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None, iteration:Int) = new G10BreaksInCarePage(browser,previousPage,iteration)
}

/** The context for Specs tests */
trait G10BreaksInCarePageContext extends PageContext {
  this: {val browser:TestBrowser}  =>
  val page = G10BreaksInCarePage buildPageWith (browser = browser, iteration = 1)
}

