package utils.pageobjects

import play.api.test.TestBrowser

/**
 * PageObject pattern associated to S1 carers allowance G1 Benefits page.
 * @author Jorge Migueis
 *         Date: 08/07/2013
 */
class BenefitsPage(browser: TestBrowser, title: String) extends Page(browser, "/", title) {

  def clickPersonGetsBenefits() = browser.click("#q3-yes")

  def clickPersonDoesNotGetBenefits() = browser.click("#q3-no")

  def doesPersonGetBenefit() = valueOfYesNo("#q3-yes")

  def isInBenefitsPage(): Boolean = titleMatch()

  def hasQ1() = browser.find("div[class=carers-allowance]").getText contains "Q1"
}

/**
 * Companion object that integrates factory method.
 * It is used by PageBuilder object defined in Page.scala
 */
object BenefitsPage {
  val title = "Benefits - Carer's Allowance"
  def buildPage(browser: TestBrowser) = new BenefitsPage(browser, title)
}

/** The context for Specs tests */
trait BenefitsPageContext extends PageContext {
  this: {val browser: TestBrowser} =>
  val page = BenefitsPage buildPage (browser)
}