package utils.pageobjects

import play.api.test.TestBrowser
import utils.pageobjects.circumstances.s1_about_you._
import utils.pageobjects.circumstances.s3_consent_and_declaration.G1DeclarationPage
import utils.pageobjects.circumstances.s2_report_changes._
import controllers.circs.s2_report_changes.{G9EmploymentChange, G2SelfEmployment, G1ReportChanges}

object CircumstancesPageFactory extends PageFactory {

  def buildPageFromTitle(title: String,ctx: PageObjectsContext) = {
    // Generic solution using mapping does not work because the objects should register themselves
    // and there is no way to get that registration triggered automatically when test are loaded.

    if (null == title ) XmlPage (ctx)
    else title.toLowerCase match {
      case G1ReportAChangeInYourCircumstancesPage.title => G1ReportAChangeInYourCircumstancesPage (ctx)
      case G1ReportChangesPage.title => G1ReportChangesPage (ctx)
      case G2SelfEmploymentPage.title => G2SelfEmploymentPage (ctx)
      case G3PermanentlyStoppedCaringPage.title => G3PermanentlyStoppedCaringPage (ctx)
      case G4OtherChangeInfoPage.title => G4OtherChangeInfoPage (ctx)
      case G5PaymentChangePage.title => G5PaymentChangePage (ctx)
      case G6AddressChangePage.title => G6AddressChangePage (ctx)
      case G7BreaksInCarePage.title => G7BreaksInCarePage (ctx)
      case G8BreaksInCareSummaryPage.title => G8BreaksInCareSummaryPage (ctx)
      case G9EmploymentChangePage.title => G9EmploymentChangePage (ctx)
      case G1DeclarationPage.title => G1DeclarationPage (ctx)
      case _ => new UnknownPage(title, ctx)
    }
  }
}
