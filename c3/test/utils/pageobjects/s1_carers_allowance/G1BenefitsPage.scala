package utils.pageobjects.s1_carers_allowance

import play.api.test.TestBrowser
import utils.pageobjects._
import utils.pageobjects.Page

/**
 * PageObject pattern associated to S1 carers allowance G1 Benefits page.
 * @author Jorge Migueis
 *         Date: 08/07/2013
 */
final class G1BenefitsPage(browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G1BenefitsPage.url, G1BenefitsPage.title, previousPage) {

  /* temporary, until tested class is refactored and use new common components. */
  private val separator  = "-"

  def clickPersonGetsBenefits() = fillYesNo("#q3", "Yes",separator)

  def clickPersonDoesNotGetBenefits() = fillYesNo("#q3", "No",separator)

  def doesPersonGetBenefit() = valueOfYesNoElement("#q3",separator)

  /* Normally not necessary since framework automatically checks pageobject on right html page. */
  def isInBenefitsPage: Boolean = titleMatch()

  def hasQ1 = browser.find("div[class=carers-allowance]").getText contains "Q1"

  /**
   * Sub-class reads theClaim and interact with browser to populate page.
   * @param theClaim   Data to use to fill page
   */
  override def fillPageWith(theClaim: ClaimScenario) {
    fillYesNo("#q3",theClaim.CanYouGetCarersAllowanceDoesthePersonYouCareforGetOneofTheseBenefits, separator)
  }
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in Page.scala
 */
object G1BenefitsPage {
  val title = "Benefits - Carer's Allowance"
  val url = "/"
  def buildPageWith(browser: TestBrowser,previousPage: Option[Page] = None) = new G1BenefitsPage(browser, previousPage)
//  PageFactory.registerPageBuilder[G1BenefitsPage](title, buildPageWith)
}

/** The context for Specs tests */
trait G1BenefitsPageContext extends PageContext {
  this: {val browser:TestBrowser}  =>
  val page = G1BenefitsPage buildPageWith browser
}