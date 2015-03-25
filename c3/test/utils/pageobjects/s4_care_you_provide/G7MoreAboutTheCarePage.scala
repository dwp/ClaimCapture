package utils.pageobjects.s4_care_you_provide

import play.api.test.WithBrowser
import utils.pageobjects._

/**
 * * Page object for s4_care_you_provide g7_more_about_the_care.
 * @author Saqib Kayani
 *         Date: 29/07/2013
 */
final class G7MoreAboutTheCarePage (ctx:PageObjectsContext) extends ClaimPage(ctx, G7MoreAboutTheCarePage.url) {
  declareYesNo("#spent35HoursCaring", "AboutTheCareYouProvideDoYouSpend35HoursorMoreEachWeek")
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in PageFactory.scala
 */
object G7MoreAboutTheCarePage {
  val url  = "/care-you-provide/more-about-the-care"

  def apply(ctx:PageObjectsContext) = new G7MoreAboutTheCarePage(ctx)
}

/** The context for Specs tests */
trait G7MoreAboutTheCarePageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G7MoreAboutTheCarePage (PageObjectsContext(browser))
}