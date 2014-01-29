package utils.pageobjects.s2_about_you

import play.api.test.WithBrowser
import utils.pageobjects._

class G5AbroadForMoreThan52WeeksPage (ctx:PageObjectsContext, iteration:Int) extends ClaimPage(ctx, G5AbroadForMoreThan52WeeksPage.url, G5AbroadForMoreThan52WeeksPage.title,iteration) {
  declareYesNo("#anyTrips", "AboutYouMoreTripsOutOfGBforMoreThan52WeeksAtATime_" + iteration)
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in PageFactory.scala
 */
object G5AbroadForMoreThan52WeeksPage {
  val title = "Time outside of England, Scotland or Wales - About you - the carer".toLowerCase

  val url  = "/about-you/abroad-for-more-than-52-weeks"

  def apply(ctx:PageObjectsContext, iteration:Int=1) = new G5AbroadForMoreThan52WeeksPage(ctx,iteration)
}

/** The context for Specs tests */
trait G5AbroadForMoreThan52WeeksPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G5AbroadForMoreThan52WeeksPage (PageObjectsContext(browser) , iteration = 1)
}