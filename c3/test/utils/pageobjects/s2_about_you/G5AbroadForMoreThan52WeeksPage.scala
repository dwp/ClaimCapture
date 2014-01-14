package utils.pageobjects.s2_about_you

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.{ClaimPage,Page, PageContext}

class G5AbroadForMoreThan52WeeksPage (browser: TestBrowser, previousPage: Option[Page] = None, iteration:Int) extends ClaimPage(browser, G5AbroadForMoreThan52WeeksPage.url, G5AbroadForMoreThan52WeeksPage.title, previousPage,iteration) {
  declareYesNo("#anyTrips", "AboutYouMoreTripsOutOfGBforMoreThan52WeeksAtATime_" + iteration)
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in PageFactory.scala
 */
object G5AbroadForMoreThan52WeeksPage {
  val title = "Time outside of England, Scotland or Wales - About you - the carer".toLowerCase

  val url  = "/about-you/abroad-for-more-than-52-weeks"

  def apply(browser: TestBrowser, previousPage: Option[Page] = None, iteration:Int) = new G5AbroadForMoreThan52WeeksPage(browser,previousPage,iteration)
}

/** The context for Specs tests */
trait G5AbroadForMoreThan52WeeksPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G5AbroadForMoreThan52WeeksPage (browser , iteration = 1)
}