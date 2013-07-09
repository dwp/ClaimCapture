package utils.pageobjects

import play.api.test.TestBrowser

/**
 * To change this template use Preferences | File and Code Templates.
 * @author Jorge Migueis
 *         Date: 08/07/2013
 */
class HoursPage(browser: TestBrowser) extends Page(browser, "/allowance/hours", "Hours - Carer's Allowance") {

  def submitPage(): Page = this

  def clickChangeBenefitsDetails() = browser.click("div[class=completed] a")

  def isInHoursPage(): Boolean = titleMatch()

  def isQ1Yes(): Boolean = {
    val completed = browser.find("div[class=completed] ul li").get(0).getText()
    completed.contains("Q1") && completed.contains("Yes")
  }

  def isQ1No(): Boolean = {
    browser.find("div[class=completed] ul li").get(0).getText().contains("Q1") &&
      browser.find("div[class=completed] ul li").get(0).getText().contains("No")
  }
}
