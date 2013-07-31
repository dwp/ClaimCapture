package utils.pageobjects.s5_time_spent_abroad

import play.api.test.TestBrowser
import utils.pageobjects.{ClaimScenario, PageContext, Page}

/**
 * * Page object for s5_time_spent_abroad g2_abroad_for_more_than_4_weeks.
 * @author Saqib Kayani
 *         Date: 31/07/2013
 */
final class G2AbroadForMoreThan4WeeksPage (browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G2AbroadForMoreThan4WeeksPage.url, G2AbroadForMoreThan4WeeksPage.title, previousPage) {
  /**
   * Reads theClaim and interacts with browser to populate page.
   * @param theClaim   Data to use to fill page
   */
  def fillPageWith(theClaim: ClaimScenario) {
    fillYesNo("#anyTrips", theClaim.TimeSpentAbroadHaveYouBeenOutOfGBWithThePersonYouCareFor_1)
  }
}


/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in PageFactory.scala
 */
object G2AbroadForMoreThan4WeeksPage {
  val title = "Abroad for more than 4 weeks - Time Spent Abroad"
  val url  = "/timeSpentAbroad/abroadForMoreThan4Weeks"
  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G2AbroadForMoreThan4WeeksPage(browser,previousPage)
}

/** The context for Specs tests */
trait G2AbroadForMoreThan4WeeksPageContext extends PageContext {
  this: {val browser:TestBrowser}  =>
  val page = G2AbroadForMoreThan4WeeksPage buildPageWith browser
}