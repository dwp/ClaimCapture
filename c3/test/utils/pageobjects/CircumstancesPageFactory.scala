package utils.pageobjects

import utils.pageobjects.circumstances.start_of_process._
import utils.pageobjects.circumstances.consent_and_declaration.GCircsDeclarationPage
import utils.pageobjects.circumstances.report_changes._

object CircumstancesPageFactory extends PageFactory {

  def buildPageFromUrl(url: String,ctx: PageObjectsContext) = {
    // Generic solution using mapping does not work because the objects should register themselves
    // and there is no way to get that registration triggered automatically when test are loaded.
    url.replaceFirst("\\?.*$","") match {
      case GReportAChangeInYourCircumstancesPage.url => GReportAChangeInYourCircumstancesPage (ctx)
      case GReportChangesPage.url => GReportChangesPage (ctx)
      case GSelfEmploymentPage.url => GSelfEmploymentPage (ctx)
      case GPermanentlyStoppedCaringPage.url => GPermanentlyStoppedCaringPage (ctx)
      case GOtherChangeInfoPage.url => GOtherChangeInfoPage (ctx)
      case GPaymentChangePage.url => GPaymentChangePage (ctx)
      case GAddressChangePage.url => GAddressChangePage (ctx)
      case GBreaksInCarePage.url => GBreaksInCarePage (ctx)
      case GBreaksInCareSummaryPage.url => GBreaksInCareSummaryPage (ctx)
      case GEmploymentChangePage.url => GEmploymentChangePage (ctx)
      case GStartedEmploymentAndOngoingPage.url => GStartedEmploymentAndOngoingPage (ctx)
      case GStartedAndFinishedEmploymentPage.url => GStartedAndFinishedEmploymentPage (ctx)
      case GEmploymentNotStartedPage.url => GEmploymentNotStartedPage (ctx)
      case GCircsDeclarationPage.url =>
        if (ctx.browser.pageSource() contains "DWPBody") XmlPage(ctx)
        else GCircsDeclarationPage (ctx)
      case _ => new UnknownPage(url, ctx)
    }
  }
}
