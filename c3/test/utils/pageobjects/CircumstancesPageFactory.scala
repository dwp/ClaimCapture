package utils.pageobjects

import utils.pageobjects.circumstances.s1_start_of_process._
import utils.pageobjects.circumstances.s3_consent_and_declaration.G1DeclarationPage
import utils.pageobjects.circumstances.s2_report_changes._

object CircumstancesPageFactory extends PageFactory {

  def buildPageFromUrl(url: String,ctx: PageObjectsContext) = {
    // Generic solution using mapping does not work because the objects should register themselves
    // and there is no way to get that registration triggered automatically when test are loaded.
    url.replaceFirst("\\?.*$","") match {
      case G2ReportAChangeInYourCircumstancesPage.url => G2ReportAChangeInYourCircumstancesPage (ctx)
      case G1ReportChangesPage.url => G1ReportChangesPage (ctx)
      case G2SelfEmploymentPage.url => G2SelfEmploymentPage (ctx)
      case G3PermanentlyStoppedCaringPage.url => G3PermanentlyStoppedCaringPage (ctx)
      case G4OtherChangeInfoPage.url => G4OtherChangeInfoPage (ctx)
      case G5PaymentChangePage.url => G5PaymentChangePage (ctx)
      case G6AddressChangePage.url => G6AddressChangePage (ctx)
      case G7BreaksInCarePage.url => G7BreaksInCarePage (ctx)
      case G8BreaksInCareSummaryPage.url => G8BreaksInCareSummaryPage (ctx)
      case G9EmploymentChangePage.url => G9EmploymentChangePage (ctx)
      case G10StartedEmploymentAndOngoingPage.url => G10StartedEmploymentAndOngoingPage (ctx)
      case G11StartedAndFinishedEmploymentPage.url => G11StartedAndFinishedEmploymentPage (ctx)
      case G12EmploymentNotStartedPage.url => G12EmploymentNotStartedPage (ctx)
      case G1DeclarationPage.url =>
        if (ctx.browser.pageSource() contains "DWPBody") XmlPage(ctx)
        else G1DeclarationPage (ctx)
      case _ => new UnknownPage(url, ctx)
    }
  }
}
