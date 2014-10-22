package utils.pageobjects.s2_about_you

import play.api.test.WithBrowser
import utils.pageobjects._

/**
 * * Page object for s2_about_you_time_spent_abroad g6_trip.
 * @author Saqib Kayani
 *         Date: 31/07/2013
 */
class G6TripPage(ctx:PageObjectsContext, iteration: Int) extends ClaimPage(ctx, G6TripPage.url, G6TripPage.title, iteration) {

  declareDate("#start", "DateYouLeftGB_" + iteration)
  declareDate("#end", "DateYouReturnedToGB_" + iteration)
  declareInput("#where", "WhereDidYouGo_" + iteration)
  declareSelect("#why_reason", "WhyDidYou_" + iteration)
  declareInput("#why_reason_other", "WhyDidYouOther_" + iteration)
  declareYesNo("#personWithYou", "PersonWithYou_" + iteration)

  protected override def getNewIterationNumber = {
    import IterationManager._
    ctx.iterationManager.increment(Abroad)
  }
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in PageFactory.scala
 */
object G6TripPage {
  val title = "Trips - About you - the carer".toLowerCase

  val url = "/about-you/trip/52-weeks"

  def apply(ctx:PageObjectsContext, iteration: Int=1) = new G6TripPage(ctx, iteration)
}

/** The context for Specs tests */
trait G6TripPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G6TripPage(PageObjectsContext(browser), iteration = 1)
}