package utils.pageobjects.s5_time_spent_abroad

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.{PageContext, Page}

/**
 * * Page object for s5_time_spent_abroad g4_trip.
 * @author Saqib Kayani
 *         Date: 31/07/2013
 */
class G4TripPage(browser: TestBrowser, previousPage: Option[Page] = None, iteration: Int) extends Page(browser, G4TripPage.url, G4TripPage.title, previousPage, iteration) {
  if (previousPage == None || previousPage.get.isInstanceOf[G2AbroadForMoreThan4WeeksPage]) {
    declareDate("#start", "TimeSpentAbroadDateYouLeftGB_" + iteration)
    declareDate("#end", "TimeSpentAbroadDateYouReturnedToGB_" + iteration)
    declareInput("#where", "TimeSpentAbroadWhereDidYouGoWithPersonCareFor_" + iteration)
    declareInput("#why", "TimeSpentAbroadWhyDidYouGoWithPersonCareFor_" + iteration)
  } else if (previousPage.get.isInstanceOf[G3AbroadForMoreThan52WeeksPage]) {
    declareDate("#start", "TimeSpentAbroadDateYouLeftGBTripForMoreThan52Weeks_" + iteration)
    declareDate("#end", "TimeSpentAbroadDateYouReturnedToGBTripForMoreThan52Weeks_" + iteration)
    declareInput("#where", "TimeSpentAbroadWhereDidYouGoForMoreThan52Weeks_" + iteration)
    declareInput("#why", "TimeSpentAbroadWhyDidYouGoForMoreThan52Weeks_" + iteration)
  }

  protected override def updateIterationNumber = iteration + 1
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in PageFactory.scala
 */
object G4TripPage {
  val title = "Trips - Time Spent Abroad".toLowerCase

  val url = "/time-spent-abroad/trip/4-weeks"

  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None, iteration: Int) = new G4TripPage(browser, previousPage, iteration)
}

/** The context for Specs tests */
trait G4TripPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G4TripPage buildPageWith(browser = browser, iteration = 1)
}