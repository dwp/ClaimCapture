package utils.pageobjects.s2_about_you

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.{PageContext, Page}

/**
 * PageObject for page s2_about_you g7_propertyAndRent.
 * @author Jorge Migueis
 *         Date: 17/07/2013
 */
final class G7PropertyAndRentPage (browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G7PropertyAndRentPage.url, G7PropertyAndRentPage.title, previousPage) {
  
    declareYesNo("#ownProperty", "AboutYouDoYouOrYourPartnerSpouseOwnPropertyorLand")
    declareYesNo("#hasSublet", "AboutYouHaveYouOrYourPartnerSubletYourHome")
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in Page.scala
 */
object G7PropertyAndRentPage {
  val title = "Property and Rent - About You"
  val url  = "/aboutyou/propertyAndRent"
  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G7PropertyAndRentPage(browser,previousPage)
}

/** The context for Specs tests */
trait G7PropertyAndRentPageContext extends PageContext {
  this: WithBrowser[_] =>
  val page = G7PropertyAndRentPage buildPageWith browser
}