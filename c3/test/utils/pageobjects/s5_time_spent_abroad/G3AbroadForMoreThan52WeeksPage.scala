package utils.pageobjects.s5_time_spent_abroad

import play.api.test.TestBrowser
import utils.pageobjects.{ClaimScenario, PageContext, Page}

/**
 * * Page object for s5_time_spent_abroad g3_abroad_for_more_than_52_weeks.
 * @author Saqib Kayani
 *         Date: 31/07/2013
 */
final class G3AbroadForMoreThan52WeeksPage (browser: TestBrowser, previousPage: Option[Page] = None, iteration:Int) extends Page(browser, G3AbroadForMoreThan52WeeksPage.url, G3AbroadForMoreThan52WeeksPage.title, previousPage) {
 
    declareYesNo("#anyTrips", "TimeSpentAbroadMoreTripsOutOfGBforMoreThan52WeeksAtATime_" + iteration)
  
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in PageFactory.scala
 */
object G3AbroadForMoreThan52WeeksPage {
  val title = "Abroad for more than 52 weeks - Time Spent Abroad"
  val url  = "/timeSpentAbroad/abroadForMoreThan52Weeks"
  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None, iteration:Int) = new G3AbroadForMoreThan52WeeksPage(browser,previousPage,iteration)
}

/** The context for Specs tests */
trait G3AbroadForMoreThan52WeeksPageContext extends PageContext {
  this: {val browser:TestBrowser}  =>
  val page = G3AbroadForMoreThan52WeeksPage buildPageWith (browser , iteration = 1)
}