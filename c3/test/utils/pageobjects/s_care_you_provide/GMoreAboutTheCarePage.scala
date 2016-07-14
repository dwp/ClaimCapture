package utils.pageobjects.s_care_you_provide

import utils.WithBrowser
import utils.pageobjects._

/**
 * * Page object for s_care_you_provide g7_more_about_the_care.
 * @author Saqib Kayani
 *         Date: 29/07/2013
 */
final class GMoreAboutTheCarePage (ctx:PageObjectsContext) extends ClaimPage(ctx, GMoreAboutTheCarePage.url) {
  declareYesNo("#spent35HoursCaring", "AboutTheCareYouProvideDoYouSpend35HoursorMoreEachWeek")
  declareYesNo("#otherCarer", "AboutTheCareYouProvideOtherCarer")
  declareYesNo("#otherCarerUc", "AboutTheCareYouProvideOtherCarerUc")
  declareInput("#otherCarerUcDetails", "AboutTheCareYouProvideOtherCarerUcDetails")
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in PageFactory.scala
 */
object GMoreAboutTheCarePage {
  val url  = "/care-you-provide/more-about-the-care"

  def apply(ctx:PageObjectsContext) = new GMoreAboutTheCarePage(ctx)
}

/** The context for Specs tests */
trait GMoreAboutTheCarePageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = GMoreAboutTheCarePage (PageObjectsContext(browser))
}
