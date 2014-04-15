package utils.pageobjects.s1_carers_allowance

import play.api.test.WithBrowser
import utils.pageobjects._

final class G2HoursPage(ctx:PageObjectsContext) extends ClaimPage(ctx, G2HoursPage.url, G2HoursPage.title) {
  declareYesNo("#hours_answer", "CanYouGetCarersAllowanceDoYouSpend35HoursorMoreEachWeekCaring")
}

object G2HoursPage {
  val title = "Do you spend 35 hours or more each week caring for the person you care for? - Can you get Carer's Allowance?".toLowerCase

  val url = "/allowance/hours"

  def apply(ctx:PageObjectsContext) = new G2HoursPage(ctx)
}

trait G2HoursPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G2HoursPage (PageObjectsContext(browser))
}