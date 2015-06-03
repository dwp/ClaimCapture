package utils.pageobjects.s7_employment

import utils.WithBrowser
import utils.pageobjects.{PageObjectsContext, ClaimPage, PageContext}

final class G1EmploymentPage(ctx:PageObjectsContext) extends ClaimPage(ctx, G1EmploymentPage.url) {
  declareYesNo("#beenEmployedSince6MonthsBeforeClaim", "EmploymentHaveYouBeenEmployedAtAnyTime_0")
  declareYesNo("#beenSelfEmployedSince1WeekBeforeClaim", "EmploymentHaveYouBeenSelfEmployedAtAnyTime")
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in Page.scala
 */
object G1EmploymentPage {
  val url  = "/employment/employment"

  def apply(ctx:PageObjectsContext) = new G1EmploymentPage(ctx)
}

/** The context for Specs tests */
trait G1EmploymentPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G1EmploymentPage (PageObjectsContext(browser))
}