package utils.pageobjects.circumstances.report_changes

import utils.WithBrowser
import utils.pageobjects._

final class GPermanentlyStoppedCaringPage(ctx:PageObjectsContext) extends CircumstancesPage(ctx, GPermanentlyStoppedCaringPage.url) {
  declareInput("#moreAboutChanges", "CircumstancesPermanentlyStoppedCaringMoreAboutChanges")
  declareDate("#stoppedCaringDate", "CircumstancesPermanentlyStoppedCaringStoppedCaringDate")
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in PageFactory.scala
 */
object GPermanentlyStoppedCaringPage {
  val url  = "/circumstances/report-changes/stopped-caring"

  def apply(ctx:PageObjectsContext) = new GPermanentlyStoppedCaringPage(ctx)
}

/** The context for Specs tests */
trait GPermanentlyStoppedCaringPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = GPermanentlyStoppedCaringPage(PageObjectsContext(browser))
}
