package utils.pageobjects

import play.api.test.TestBrowser
import utils.pageobjects.circumstances.s1_about_you.G1AboutYouPage


object CircumstancesPageFactory extends PageFactory {

  def buildPageFromTitle(browser: TestBrowser, title: String, previousPage: Option[Page], iteration: Int) = {
    // Generic solution using mapping does not work because the objects should register themselves
    // and there is no way to get that registration triggered automatically when test are loaded.
    if (null == title ) XmlPage buildPageWith(browser, previousPage)
    else title.toLowerCase match {
      case G1AboutYouPage.title => G1AboutYouPage buildPageWith(browser, previousPage)
      case _ => new UnknownPage(browser, title, previousPage)
    }
  }
}
