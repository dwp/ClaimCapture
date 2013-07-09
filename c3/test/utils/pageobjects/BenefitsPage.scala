package utils.pageobjects

import play.api.test.TestBrowser
import org.specs2.specification.Scope

/**
 * To change this template use Preferences | File and Code Templates.
 * @author Jorge Migueis
 *         Date: 08/07/2013
 */
class BenefitsPage(browser: TestBrowser) extends Page(browser, "/", "Benefits - Carer's Allowance") {

  def clickPersonGetsBenefits() {
    browser.click("#q3-yes")
  }

  def clickPersonDoesNotGetBenefits() {
    browser.click("#q3-no")
  }

  def doesPersonGetBenefit(): Boolean = {
    browser.find("#q3-yes").getAttribute("value") match {
      case "true" => true
      case "false" => false
      case _ => throw new IllegalStateException("Value not set")
    }
  }

  def isInBenefitsPage(): Boolean = titleMatch()

  def isInQ1Page() = browser.find("div[class=carers-allowance]").getText contains "Q1"

  def submitPage() = {
    clickSubmit()
    val nextPage = new HoursPage(browser)
    nextPage.waitForPage()
    nextPage
  }

}

//
///** the context */
//trait BenefitsPageContext extends Scope {
//  val testBrowser = (new play.api.test.WithBrowser {}) browser
//  val page = new BenefitsPage(testBrowser)
//  page.goToPage
//}