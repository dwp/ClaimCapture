package utils.pageobjects.s0_carers_allowance

import play.api.test.WithBrowser
import utils.pageobjects._

final class G2EligibilityPage(ctx:PageObjectsContext) extends ClaimPage(ctx, G2EligibilityPage.url) {
  declareYesNo("#hours_answer", "CanYouGetCarersAllowanceDoYouSpend35HoursorMoreEachWeekCaring")
  declareYesNo("#over16_answer", "CanYouGetCarersAllowanceAreYouAged16OrOver")
  declareYesNo("#livesInGB_answer", "CanYouGetCarersAllowanceDoYouNormallyLiveinGb")
}

object G2EligibilityPage {
  val url = "/allowance/eligibility"

  def apply(ctx:PageObjectsContext) = new G2EligibilityPage(ctx)
}

trait G2EligibilityPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G2EligibilityPage(PageObjectsContext(browser))
}