package utils.pageobjects.s2_about_you

import play.api.test.TestBrowser
import utils.pageobjects.{ClaimScenario, PageContext, Page}

/**
 * PageObject for page s2_about_you g7_propertyAndRent.
 * @author Jorge Migueis
 *         Date: 17/07/2013
 */
final class PropertyAndRentPage (browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, PropertyAndRentPage.url, PropertyAndRentPage.title, previousPage) {
  /**
   * Reads theClaim and interacts with browser to populate page.
   * @param theClaim   Data to use to fill page
   */
  def fillPageWith(theClaim: ClaimScenario) {
    fillYesNo("#ownProperty", theClaim.AboutYouDoYouOrYourPartnerSpouseOwnPropertyorLand)
    fillYesNo("#hasSublet", theClaim.AboutYouHaveYouOrYourPartnerSubletYourHome)
  }
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in Page.scala
 */
object PropertyAndRentPage {
  val title = "Property and Rent - About You"
  val url  = "/aboutyou/propertyAndRent"
  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new PropertyAndRentPage(browser,previousPage)
}

/** The context for Specs tests */
trait PropertyAndRentPageContext extends PageContext {
  this: {val browser:TestBrowser}  =>
  val page = PropertyAndRentPage buildPageWith browser
}