package utils.pageobjects

import play.api.test.TestBrowser

/**
 * TODO write description
 * @author Jorge Migueis
 *         Date: 21/08/2013
 */
abstract class ClaimPage(browser: TestBrowser, url: String, pageTitle: String, previousPage: Option[Page] = None, iteration: Int = 1)
  extends Page(ClaimPageFactory,browser, url,pageTitle,previousPage,iteration) {

}
