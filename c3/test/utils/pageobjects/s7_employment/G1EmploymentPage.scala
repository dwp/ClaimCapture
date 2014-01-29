package utils.pageobjects.s7_employment

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.{PageObjectsContext, ClaimPage, Page, PageContext}

final class G1EmploymentPage(ctx:PageObjectsContext, iteration: Int) extends ClaimPage(ctx, G1EmploymentPage.url.replace(":jobID", iteration.toString), G1EmploymentPage.title, iteration) {
  declareYesNo("#beenEmployedSince6MonthsBeforeClaim", "EmploymentHaveYouBeenEmployedAtAnyTime")
  declareYesNo("#beenSelfEmployedSince1WeekBeforeClaim", "EmploymentHaveYouBeenSelfEmployedAtAnyTime")
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in Page.scala
 */
object G1EmploymentPage {
  val title = "Employment Employment History".toLowerCase

  val url  = "/employment/employment"

  def apply(ctx:PageObjectsContext, iteration: Int = 1) = new G1EmploymentPage(ctx, iteration)
}

/** The context for Specs tests */
trait G1EmploymentPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G1EmploymentPage (PageObjectsContext(browser), iteration = 1)
}