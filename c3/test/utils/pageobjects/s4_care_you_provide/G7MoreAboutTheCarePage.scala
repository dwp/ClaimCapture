package utils.pageobjects.s4_care_you_provide

import play.api.test.TestBrowser
import utils.pageobjects.{ClaimScenario, PageContext, Page}

/**
 * * Page object for s4_care_you_provide g7_more_about_the_care.
 * @author Saqib Kayani
 *         Date: 29/07/2013
 */
final class G7MoreAboutTheCarePage (browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G7MoreAboutTheCarePage.url, G7MoreAboutTheCarePage.title, previousPage) {
  /**
   * Reads theClaim and interacts with browser to populate page.
   * @param theClaim   Data to use to fill page
   */
  def fillPageWith(theClaim: ClaimScenario) {
    fillYesNo("#spent35HoursCaring", theClaim.AboutTheCareYouProvideDoYouSpend35HoursorMoreEachWeek)
    fillYesNo("#beforeClaimCaring_answer", theClaim.AboutTheCareYouProvideDidYouCareForThisPersonfor35Hours)
    fillDate("#beforeClaimCaring_date", theClaim.AboutTheCareYouProvideWhenDidYouStarttoCareForThisPerson)
    fillYesNo("#hasSomeonePaidYou", theClaim.AboutTheCareYouProvideHasSomeonePaidYoutoCare)
  }
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in PageFactory.scala
 */
object G7MoreAboutTheCarePage {
  val title = "More about the care you provide - Care You Provide"
  val url  = "/careYouProvide/moreAboutTheCare"
  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G7MoreAboutTheCarePage(browser,previousPage)
}

/** The context for Specs tests */
trait G7MoreAboutTheCarePageContext extends PageContext {
  this: {val browser:TestBrowser}  =>
  val page = G7MoreAboutTheCarePage buildPageWith browser
}