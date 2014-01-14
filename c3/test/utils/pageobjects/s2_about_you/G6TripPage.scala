package utils.pageobjects.s2_about_you

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.{ClaimPage, Page, PageContext}

/**
 * * Page object for s2_about_you_time_spent_abroad g6_trip.
 * @author Saqib Kayani
 *         Date: 31/07/2013
 */
class G6TripPage(browser: TestBrowser, previousPage: Option[Page] = None, iteration: Int) extends ClaimPage(browser, G6TripPage.url, G6TripPage.title, previousPage, iteration) {

  declareDate("#start", "AboutYouDateYouLeftGBTripForMoreThan52Weeks_" + iteration)
  declareDate("#end", "AboutYouDateYouReturnedToGBTripForMoreThan52Weeks_" + iteration)
  declareInput("#where", "AboutYouWhereDidYouGoForMoreThan52Weeks_" + iteration)
  declareSelect("#why", "AboutYouWhyDidYouGoForMoreThan52Weeks_" + iteration)

  protected override def getNewIterationNumber = iteration + 1
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in PageFactory.scala
 */
object G6TripPage {
  val title = "Trips - About you - the carer".toLowerCase

  val url = "/about-you/trip/52-weeks"

  def apply(browser: TestBrowser, previousPage: Option[Page] = None, iteration: Int) = new G6TripPage(browser, previousPage, iteration)
}

/** The context for Specs tests */
trait G6TripPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G6TripPage(browser = browser, iteration = 1)
}