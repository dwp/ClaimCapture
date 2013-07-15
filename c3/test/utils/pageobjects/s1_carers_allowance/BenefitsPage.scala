package utils.pageobjects.s1_carers_allowance

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.{PageElements, ClaimScenario, PageContext, Page}

/**
 * PageObject pattern associated to S1 carers allowance G1 Benefits page.
 * @author Jorge Migueis
 *         Date: 08/07/2013
 */
class BenefitsPage(browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, BenefitsPage.url, BenefitsPage.title, previousPage) {

  /* temporary, until tested class is refactored and use new common components. */
  private val separator  = "-"

  def clickPersonGetsBenefits() = fillYesNo("#q3", "Yes",separator)

  def clickPersonDoesNotGetBenefits() = fillYesNo("#q3", "No",separator)

  def doesPersonGetBenefit() = valueOfYesNo("#q3",separator)

  /* Normally not necessary since framework automatically checks pageobject on right html page. */
  def isInBenefitsPage(): Boolean = titleMatch()

  def hasQ1() = browser.find("div[class=carers-allowance]").getText contains "Q1"

  /**
   * Sub-class reads theClaim and interact with browser to populate page.
   * @param theClaim   Data to use to fill page
   */
  def fillPageWith(theClaim: ClaimScenario) {
    fillYesNo("#q3",theClaim.CanYouGetCarersAllowanceDoesthePersonYouCareforGetOneofTheseBenefits, separator)
  }
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in Page.scala
 */
object BenefitsPage {
  val title = "Benefits - Carer's Allowance"
  val url = "/"
  def buildPageWith(browser: TestBrowser,previousPage: Option[Page] = None) = new BenefitsPage(browser, previousPage)
}

/** The context for Specs tests */
trait BenefitsPageContext extends PageContext {
  this: {val browser:TestBrowser}  =>
  val page = BenefitsPage buildPageWith browser
}