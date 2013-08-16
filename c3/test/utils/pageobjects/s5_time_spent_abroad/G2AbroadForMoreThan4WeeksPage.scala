package utils.pageobjects.s5_time_spent_abroad

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.{ClaimScenario, PageContext, Page}

/**
 * * Page object for s5_time_spent_abroad g2_abroad_for_more_than_4_weeks.
 * @author Saqib Kayani
 *         Date: 31/07/2013
 */
final class G2AbroadForMoreThan4WeeksPage (browser: TestBrowser, previousPage: Option[Page] = None, iteration:Int) extends Page(browser, G2AbroadForMoreThan4WeeksPage.url, G2AbroadForMoreThan4WeeksPage.title, previousPage,iteration) {
  declareYesNo("#anyTrips", "TimeSpentAbroadHaveYouBeenOutOfGBWithThePersonYouCareFor_" + iteration)

  override def fillPageWith(theClaim: ClaimScenario): Page = {
    super.fillPageWith(theClaim)

    if (theClaim.selectDynamic("TimeSpentAbroadHaveYouBeenOutOfGBWithThePersonYouCareFor_" + iteration).toLowerCase == "no") resetIteration = true

    this
  }
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in PageFactory.scala
 */
object G2AbroadForMoreThan4WeeksPage {
  val title = "Abroad for more than 4 weeks - Time Spent Abroad".toLowerCase
  
  val url  = "/time-spent-abroad/abroad-for-more-than-4-weeks"

  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None, iteration: Int) = new G2AbroadForMoreThan4WeeksPage(browser,previousPage,iteration)
}

/** The context for Specs tests */
trait G2AbroadForMoreThan4WeeksPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G2AbroadForMoreThan4WeeksPage buildPageWith (browser,iteration = 1)
}