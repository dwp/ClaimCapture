package utils.pageobjects.s_about_you

import utils.WithBrowser
import utils.pageobjects._

class GAbroadForMoreThan52WeeksPage (ctx:PageObjectsContext, iteration:Int) extends ClaimPage(ctx, GAbroadForMoreThan52WeeksPage.url,iteration) {
  declareYesNo("#anyTrips", "AboutYouMoreTripsOutOfGBforMoreThan52WeeksAtATime_" + iteration)
  declareInput("#tripDetails", "AboutYouTripDetails_" + iteration)
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in PageFactory.scala
 */
object GAbroadForMoreThan52WeeksPage {
  val url  = "/about-you/abroad-for-more-than-52-weeks"

  def apply(ctx:PageObjectsContext, iteration:Int=1) = new GAbroadForMoreThan52WeeksPage(ctx,iteration)
}

/** The context for Specs tests */
trait GAbroadForMoreThan52WeeksPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = GAbroadForMoreThan52WeeksPage (PageObjectsContext(browser) , iteration = 1)
}
