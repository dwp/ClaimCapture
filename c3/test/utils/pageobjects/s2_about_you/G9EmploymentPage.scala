package utils.pageobjects.s2_about_you

import play.api.test.WithBrowser
import utils.pageobjects._

/**
 * PageObject for page s2_about_you g9_employment.
 * @author Jorge Migueis
 *         Date: 17/07/2013
 */
final class G9EmploymentPage (ctx:PageObjectsContext) extends ClaimPage(ctx, G9EmploymentPage.url, G9EmploymentPage.title) {
  declareYesNo("#beenEmployedSince6MonthsBeforeClaim", "AboutYouHaveYouBeenEmployedAtAnyTime_1")
  declareYesNo("#beenSelfEmployedSince1WeekBeforeClaim", "AboutYouHaveYouBeenSelfEmployedAtAnyTime")
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in Page.scala
 */
object G9EmploymentPage {
  val title = "Employment - About you - the carer".toLowerCase

  val url  = "/about-you/employment"

  def apply(ctx:PageObjectsContext) = new G9EmploymentPage(ctx)
}

/** The context for Specs tests */
trait G9EmploymentPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G9EmploymentPage (PageObjectsContext(browser))
}