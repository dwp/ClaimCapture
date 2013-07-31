package utils.pageobjects.s5_time_spent_abroad

import play.api.test.TestBrowser
import utils.pageobjects.{ClaimScenario, PageContext, Page}

/**
 * * Page object for s5_time_spent_abroad g4_trip.
 * @author Saqib Kayani
 *         Date: 31/07/2013
 */
class G4TripPage (browser: TestBrowser, previousPage: Option[Page] = None, iteration: Int) extends Page(browser, G4TripPage.url, G4TripPage.title, previousPage, iteration) {
  /**
   * Reads theClaim and interacts with browser to populate page.
   * @param theClaim   Data to use to fill page
   */
  def fillPageWith(theClaim: ClaimScenario) {
    fillDate("#start", theClaim.selectDynamic("TimeSpentAbroadDateYouLeftGBTripForMoreThan52Weeks_" + iteration))
    fillDate("#end", theClaim.selectDynamic("TimeSpentAbroadDateYouReturnedToGBTripForMoreThan52Weeks_" + iteration))
    fillInput("#where", theClaim.selectDynamic("TimeSpentAbroadWhereDidYouGoForMoreThan52Weeks_" + iteration))
    fillInput("#why", theClaim.selectDynamic("TimeSpentAbroadWhyDidYouGoForMoreThan52Weeks_" + iteration))
  }
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in PageFactory.scala
 */
object G4TripPage {
  val title = "Trip - Time Spent Abroad"
  val url  = "/timeSpentAbroad/trip/4Weeks"
  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None, iteration: Int) = new G4TripPage(browser,previousPage,iteration)
}

/** The context for Specs tests */
trait G4TripPageContext extends PageContext {
  this: {val browser:TestBrowser}  =>
  val page = G4TripPage buildPageWith (browser = browser, iteration = 1)
}