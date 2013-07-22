package utils.pageobjects.s1_carers_allowance

import play.api.test.TestBrowser
import utils.pageobjects.{PageContext, ClaimScenario, Page}

/**
 * PageObject pattern associated to S1 carers allowance G4 living in GB page.
 * @author Jorge Migueis
 *         Date: 15/07/2013
 */
final class G4LivingInGBPage (browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G4LivingInGBPage.url, G4LivingInGBPage.title, previousPage) {
  /* temporary, until tested class is refactored and use new common components. */
  private val separator  = "-"

  /**
   * Reads theClaim and interacts with browser to populate page.
   * @param theClaim   Data to use to fill page
   */
  def fillPageWith(theClaim: ClaimScenario) {
    fillYesNo("#q3",theClaim.CanYouGetCarersAllowanceDoYouNormallyLiveinGb, separator)
  }
}


/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in Page.scala
 */
object G4LivingInGBPage {
  val title = "Lives in GB - Carer's Allowance"
  val url = "/allowance/livesInGB"
  def buildPageWith(browser: TestBrowser,previousPage: Option[Page] = None) = new G4LivingInGBPage(browser, previousPage)
}

/** The context for Specs tests */
trait G4LivingInGBPageContext extends PageContext {
  this: {val browser:TestBrowser}  =>
  val page = G4LivingInGBPage buildPageWith browser
}
