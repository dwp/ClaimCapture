package utils.pageobjects.s_eligibility

import utils.WithBrowser
import utils.pageobjects._

final class GEligibilityPage(ctx:PageObjectsContext) extends ClaimPage(ctx, GEligibilityPage.url) {
  declareYesNo("#hours_answer", "CanYouGetCarersAllowanceDoYouSpend35HoursorMoreEachWeekCaring")
  declareYesNo("#over16_answer", "CanYouGetCarersAllowanceAreYouAged16OrOver")
  declareRadioList("#origin", "CanYouGetCarersAllowanceWhichCountryLivein")
}

object GEligibilityPage {
  val url = "/allowance/eligibility"

  def apply(ctx:PageObjectsContext) = new GEligibilityPage(ctx)
}

trait GEligibilityPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = GEligibilityPage(PageObjectsContext(browser))
}
