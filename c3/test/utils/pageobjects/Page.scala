package utils.pageobjects

import play.api.test.TestBrowser
import java.util.concurrent.TimeUnit
import org.specs2.specification.Scope
import utils.pageobjects.s1_carers_allowance.{HoursPage, BenefitsPage}


/**
 * Super-class of all the PageObject pattern compliant classes representing an application page.
 * @author Jorge Migueis
 *         Date: 08/07/2013
 */
abstract case class Page(browser: TestBrowser, url: String, pageTitle: String) {

  // ========================================
  // Page Management
  // ========================================

  def goToThePage() = {
    browser.goTo(url)
    waitForPage()
  }

  def goToPage(page: Page) = browser.goTo(page.url)

  def submitPage() = {
    val nextPageTile = browser.submit("button[type='submit']").title()
    createPageWithTitle(nextPageTile)
  }

  def goBack() = {
    val backPageTile = browser.click(".form-steps a").title()
    createPageWithTitle(backPageTile)

  }
  def waitForPage() = browser.waitUntil[Boolean](30, TimeUnit.SECONDS) {
    browser.title == pageTitle
  }

  protected def titleMatch(): Boolean = browser.title == this.pageTitle

  private def createPageWithTitle(title: String) = {
    val newPage = PageBuilder createPageFromTitle(browser, title)
    newPage.waitForPage()
    newPage
  }

  // ========================================
  // Component Management
  // ========================================
  protected def isCompletedYesNo(location: String, index: Integer, name: String, value: String) = {
    val completed = browser.find(location).get(index).getText()
    completed.contains(name) && completed.contains(value)
  }

  protected def valueOfYesNo(location: String): Option[Boolean] = {
    browser.find(location).getAttribute("value") match {
      case "true" => Some(true)
      case "false" => Some(false)
      case _ => None
    }
  }
}


final class UnknownPage(browser: TestBrowser,pageTitle: String) extends Page(browser, null, pageTitle) {
  protected def createNextPage(): Page = this
}

object PageBuilder {

  def createPageFromTitle(browser: TestBrowser, title: String) = {
    title match {
      case HoursPage.title => HoursPage buildPage (browser)
      case BenefitsPage.title => BenefitsPage buildPage (browser)
      case _ => new UnknownPage(browser, title)
    }

  }
}

trait PageContext extends Scope {

}