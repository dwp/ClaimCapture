package utils.pageobjects.s_employment

import utils.WithBrowser
import utils.pageobjects.{PageObjectsContext, ClaimPage, PageContext}

final class GEmploymentPage(ctx:PageObjectsContext) extends ClaimPage(ctx, GEmploymentPage.url) {
  declareYesNo("#beenEmployedSince6MonthsBeforeClaim", "EmploymentHaveYouBeenEmployedAtAnyTime_0")
  declareYesNo("#beenSelfEmployedSince1WeekBeforeClaim", "EmploymentHaveYouBeenSelfEmployedAtAnyTime")
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in Page.scala
 */
object GEmploymentPage {
  val url  = "/employment/employment"

  def apply(ctx:PageObjectsContext) = new GEmploymentPage(ctx)
}

/** The context for Specs tests */
trait GEmploymentPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = GEmploymentPage (PageObjectsContext(browser))
}
