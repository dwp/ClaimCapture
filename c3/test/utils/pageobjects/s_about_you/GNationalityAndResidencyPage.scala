package utils.pageobjects.s_about_you

import utils.WithBrowser
import utils.pageobjects._

final class GNationalityAndResidencyPage(ctx: PageObjectsContext) extends ClaimPage(ctx, GNationalityAndResidencyPage.url) {
  declareRadioList("#nationality", "AboutYouNationalityAndResidencyNationality")
  declareInput("#actualnationality", "AboutYouNationalityAndResidencyActualNationality")
  declareYesNo("#alwaysLivedInUK", "AboutYouNationalityAndResidencyAlwaysLivedInUK")
  declareYesNo("#liveInUKNow", "AboutYouNationalityAndResidencyLiveInUKNow")
  declareRadioList("#arrivedInUK", "AboutYouNationalityAndResidencyArrivedInUK")
  declareDate("#arrivedInUKDate", "AboutYouNationalityAndResidencyArrivedInUKDate")
  declareInput("#arrivedInUKFrom", "AboutYouNationalityAndResidencyArrivedInUKFrom")
  declareYesNo("#trip52weeks", "AboutYouNationalityAndResidencyTrip52Weeks")
  declareInput("#tripDetails", "AboutYouNationalityAndResidencyTripDetails")
}

object GNationalityAndResidencyPage {
  val url = "/about-you/nationality-and-residency"

  def apply(ctx: PageObjectsContext) = new GNationalityAndResidencyPage(ctx)
}

/** The context for Specs tests */
trait GNationalityAndResidencyPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = GNationalityAndResidencyPage(PageObjectsContext(browser))
}
