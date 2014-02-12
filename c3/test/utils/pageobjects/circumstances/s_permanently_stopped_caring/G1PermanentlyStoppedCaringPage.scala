package utils.pageobjects.circumstances.s_permanently_stopped_caring

import play.api.test.WithBrowser
import utils.pageobjects._

final class G1PermanentlyStoppedCaringPage(ctx:PageObjectsContext) extends CircumstancesPage(ctx, G1PermanentlyStoppedCaringPage.url, G1PermanentlyStoppedCaringPage.title) {
  declareSelect("#moreAboutChanges", "CircumstancesPermanentlyStoppedCaringMoreAboutChanges")
  declareDate("#stoppedCaringDate", "CircumstancesPermanentlyStoppedCaringStoppedCaringDate")
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in PageFactory.scala
 */
object G1PermanentlyStoppedCaringPage {
  val title = "permanently stopped caring".toLowerCase

  val url  = "/circumstances/report-change/stopped-caring"

  def apply(ctx:PageObjectsContext) = new G1PermanentlyStoppedCaringPage(ctx)
}

/** The context for Specs tests */
trait G1PermanentlyStoppedCaringPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G1PermanentlyStoppedCaringPage(PageObjectsContext(browser))
}
