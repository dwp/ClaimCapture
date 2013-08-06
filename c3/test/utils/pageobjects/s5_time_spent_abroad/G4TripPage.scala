package utils.pageobjects.s5_time_spent_abroad

import play.api.test.TestBrowser
import utils.pageobjects.{ClaimScenario, PageContext, Page}

/**
 * * Page object for s5_time_spent_abroad g4_trip.
 * @author Saqib Kayani
 *         Date: 31/07/2013
 */
class G4TripPage(browser: TestBrowser, previousPage: Option[Page] = None, iteration: Int) extends Page(browser, G4TripPage.url, G4TripPage.title, previousPage, iteration) {

  previousPage match {
    case p:Some[G3AbroadForMoreThan52WeeksPage] => {
      declareDate("#start", "TimeSpentAbroadDateYouLeftGBTripForMoreThan52Weeks_" + iteration)
      declareDate("#end", "TimeSpentAbroadDateYouReturnedToGBTripForMoreThan52Weeks_" + iteration)
      declareInput("#where", "TimeSpentAbroadWhereDidYouGoForMoreThan52Weeks_" + iteration)
      declareInput("#why", "TimeSpentAbroadWhyDidYouGoForMoreThan52Weeks_" + iteration)
    }
    case _ => {
      declareDate("#start", "TimeSpentAbroadDateYouLeftGB_" + iteration)
      declareDate("#end", "TimeSpentAbroadDateYouReturnedToGB_" + iteration)
      declareInput("#where", "TimeSpentAbroadWhereDidYouGoWithPersonCareFor_" + iteration)
      declareInput("#why", "TimeSpentAbroadWhyDidYouGoWithPersonCareFor_" + iteration)
    }
  }

  protected override def updateIterationNumber = iteration + 1
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in PageFactory.scala
 */
object G4TripPage {
  val title = "Trip - Time Spent Abroad"
  val url = "/timeSpentAbroad/trip/4Weeks"

  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None, iteration: Int) = new G4TripPage(browser, previousPage, iteration)
}

/** The context for Specs tests */
trait G4TripPageContext extends PageContext {
  this: {val browser: TestBrowser} =>
  val page = G4TripPage buildPageWith(browser = browser, iteration = 1)
}