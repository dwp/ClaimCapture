package utils.pageobjects.s_care_you_provide

import utils.WithBrowser
import utils.pageobjects._

final class GBreaksInCarePage(ctx:PageObjectsContext, iteration: Int) extends ClaimPage(ctx, GBreaksInCarePage.url, iteration) {
   declareYesNo("#answer", "AboutTheCareYouProvideHaveYouHadAnyMoreBreaksInCare_" + iteration)
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in PageFactory.scala
 */
object GBreaksInCarePage {
  val url  = "/breaks/breaks-in-care"

  def apply(ctx:PageObjectsContext, iteration:Int=1) = new GBreaksInCarePage(ctx,iteration)
}

/** The context for Specs tests */
trait GBreaksInCarePageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = GBreaksInCarePage apply(PageObjectsContext(browser), iteration = 1)
}