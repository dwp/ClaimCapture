package utils.pageobjects

import play.api.test.TestBrowser
import utils.pageobjects.circumstances.s1_about_you._
import utils.pageobjects.circumstances.s3_consent_and_declaration.G1DeclarationPage
import utils.pageobjects.circumstances.s2_report_changes.{G1ReportChangesPage, G2SelfEmploymentPage, G3PermanentlyStoppedCaringPage, G4OtherChangeInfoPage}
import controllers.circs.s2_report_changes.{G2SelfEmployment, G1ReportChanges}

object CircumstancesPageFactory extends PageFactory {

  def buildPageFromTitle(title: String,ctx: PageObjectsContext) = {
    // Generic solution using mapping does not work because the objects should register themselves
    // and there is no way to get that registration triggered automatically when test are loaded.

    if (null == title ) XmlPage (ctx)
    else title.toLowerCase match {
      case G1AboutYouPage.title => G1AboutYouPage (ctx)
      case G2YourContactDetailsPage.title => G2YourContactDetailsPage (ctx)
      case G3DetailsOfThePersonYouCareForPage.title => G3DetailsOfThePersonYouCareForPage (ctx)
      case G4CompletedPage.title => G4CompletedPage (ctx)
      case G1ReportChangesPage.title => G1ReportChangesPage (ctx)
      case G2SelfEmploymentPage.title => G2SelfEmploymentPage (ctx)
      case G3PermanentlyStoppedCaringPage.title => G3PermanentlyStoppedCaringPage (ctx)
      case G4OtherChangeInfoPage.title => G4OtherChangeInfoPage (ctx)
      case G1DeclarationPage.title => G1DeclarationPage (ctx)
      case _ => new UnknownPage(title, ctx)
    }
  }
}
