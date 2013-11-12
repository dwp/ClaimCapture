package utils.pageobjects

import play.api.test.TestBrowser

/**
 * Base class of all the PageFactories, used by utils.pageobjects.Page to determine thanks to a page title which Page Object to build.
 * @author Jorge Migueis
 *         Date: 21/08/2013
 */
trait PageFactory {
  def buildPageFromTitle(browser: TestBrowser, title: String, previousPage: Option[Page], iteration: Int):Page

}
