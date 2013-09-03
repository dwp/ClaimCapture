package utils.pageobjects

import play.api.test.TestBrowser
import utils.pageobjects.circumstances.s1_about_you._
import utils.pageobjects.circumstances.s2_additional_info.G1OtherChangeInfoPage
import utils.pageobjects.circumstances.s3_consent_and_declaration.G1DeclarationPage


object CircumstancesPageFactory extends PageFactory {

  def buildPageFromTitle(browser: TestBrowser, title: String, previousPage: Option[Page], iteration: Int) = {
    // Generic solution using mapping does not work because the objects should register themselves
    // and there is no way to get that registration triggered automatically when test are loaded.

    if (null == title ) XmlPage buildPageWith(browser, previousPage)
    else title.toLowerCase match {
      case G1AboutYouPage.title => G1AboutYouPage buildPageWith(browser, previousPage)
      case G2YourContactDetailsPage.title => G2YourContactDetailsPage buildPageWith(browser, previousPage)
      case G3DetailsOfThePersonYouCareForPage.title => G3DetailsOfThePersonYouCareForPage buildPageWith(browser, previousPage)
      case G4CompletedPage.title => G4CompletedPage buildPageWith(browser, previousPage)
      case G1OtherChangeInfoPage.title => G1OtherChangeInfoPage buildPageWith(browser, previousPage)
      case G1DeclarationPage.title => G1DeclarationPage buildPageWith(browser, previousPage)
      case _ => new UnknownPage(browser, title, previousPage)
    }
  }
}
