package utils.pageobjects.circumstances.s2_report_changes

import utils.WithBrowser
import utils.pageobjects._

final class G3PermanentlyStoppedCaringPage(ctx:PageObjectsContext) extends CircumstancesPage(ctx, G3PermanentlyStoppedCaringPage.url) {
  declareInput("#moreAboutChanges", "CircumstancesPermanentlyStoppedCaringMoreAboutChanges")
  declareDate("#stoppedCaringDate", "CircumstancesPermanentlyStoppedCaringStoppedCaringDate")
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in PageFactory.scala
 */
object G3PermanentlyStoppedCaringPage {
  val url  = "/circumstances/report-changes/stopped-caring"

  def apply(ctx:PageObjectsContext) = new G3PermanentlyStoppedCaringPage(ctx)
}

/** The context for Specs tests */
trait G3PermanentlyStoppedCaringPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G3PermanentlyStoppedCaringPage(PageObjectsContext(browser))
}
