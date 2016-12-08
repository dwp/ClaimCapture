package utils.pageobjects

import org.specs2.specification.Scope
import utils.WithBrowsers
import utils.pageobjects.circumstances.consent_and_declaration.GCircsDeclarationPage
import utils.pageobjects.circumstances.start_of_process.{GCircsYourDetailsPage, GGoToCircsPage}

trait PageObjects extends Scope {
  this: WithBrowsers[_] =>

  val context = PageObjectsContext(browser)

  val pageAfterReasonSelectionUrl=GGoToCircsPage.url
  val pageBeforeFunctionsUrl=GCircsYourDetailsPage.url
  val pageAfterFunctionsUrl=GCircsDeclarationPage.url
}
