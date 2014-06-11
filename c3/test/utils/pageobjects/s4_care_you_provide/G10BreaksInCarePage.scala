package utils.pageobjects.s4_care_you_provide

import play.api.test.WithBrowser
import utils.pageobjects._

final class G10BreaksInCarePage(ctx:PageObjectsContext, iteration: Int) extends ClaimPage(ctx, G10BreaksInCarePage.url, G10BreaksInCarePage.title, iteration) {
   declareYesNo("#answer", "AboutTheCareYouProvideHaveYouHadAnyMoreBreaksInCare_" + iteration)
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in PageFactory.scala
 */
object G10BreaksInCarePage {
  val title = "Breaks from care - About the care you provide".toLowerCase

  val url  = "/care-you-provide/breaks-in-care"

  def apply(ctx:PageObjectsContext, iteration:Int=1) = new G10BreaksInCarePage(ctx,iteration)
}

/** The context for Specs tests */
trait G10BreaksInCarePageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G10BreaksInCarePage apply(PageObjectsContext(browser), iteration = 1)
}