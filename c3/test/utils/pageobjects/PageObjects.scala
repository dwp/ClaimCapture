package utils.pageobjects

import org.specs2.specification.Scope
import utils.WithBrowsers
import utils.pageobjects.circumstances.start_of_process.{GReportChangesPage, GGoToCircsPage, GCircsYourDetailsPage}

trait PageObjects extends Scope {
  this: WithBrowsers[_] =>

  val context = PageObjectsContext(browser)

  val pageAfterReasonSelectionUrl=GGoToCircsPage.url
  val pageBeforeFunctionsUrl=GReportChangesPage.url
  val pageAfterFunctionsUrl=GCircsYourDetailsPage.url
}
