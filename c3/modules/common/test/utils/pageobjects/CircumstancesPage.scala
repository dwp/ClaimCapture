package utils.pageobjects

import play.api.test.TestBrowser


abstract class CircumstancesPage(browser: TestBrowser, url: String, pageTitle: String, previousPage: Option[Page] = None, iteration: Int = 1)
  extends Page(CircumstancesPageFactory,browser, url,pageTitle,previousPage,iteration) {

}
