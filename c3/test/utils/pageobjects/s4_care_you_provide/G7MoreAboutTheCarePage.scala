package utils.pageobjects.s4_care_you_provide

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.{PageContext, Page}

/**
 * * Page object for s4_care_you_provide g7_more_about_the_care.
 * @author Saqib Kayani
 *         Date: 29/07/2013
 */
final class G7MoreAboutTheCarePage (browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G7MoreAboutTheCarePage.url, G7MoreAboutTheCarePage.title, previousPage) {
  declareYesNo("#spent35HoursCaring", "AboutTheCareYouProvideDoYouSpend35HoursorMoreEachWeek")
  declareYesNo("#beforeClaimCaring_answer", "AboutTheCareYouProvideDidYouCareForThisPersonfor35Hours")
  declareDate("#beforeClaimCaring_date", "AboutTheCareYouProvideWhenDidYouStarttoCareForThisPerson")
  declareYesNo("#hasSomeonePaidYou", "AboutTheCareYouProvideHasSomeonePaidYoutoCare")
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in PageFactory.scala
 */
object G7MoreAboutTheCarePage {
  val title = "More about the care you provide - About the care you provide".toLowerCase

  val url  = "/care-you-provide/more-about-the-care"

  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G7MoreAboutTheCarePage(browser,previousPage)
}

/** The context for Specs tests */
trait G7MoreAboutTheCarePageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G7MoreAboutTheCarePage buildPageWith browser
}