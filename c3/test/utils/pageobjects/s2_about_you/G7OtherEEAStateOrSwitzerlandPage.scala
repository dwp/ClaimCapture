package utils.pageobjects.s2_about_you

import play.api.test.WithBrowser
import utils.pageobjects._

/**
 * PageObject pattern associated to S7 about you EEA pension and insurance.
 */
final class G7OtherEEAStateOrSwitzerlandPage (ctx:PageObjectsContext) extends ClaimPage(ctx, G7OtherEEAStateOrSwitzerlandPage.url, G7OtherEEAStateOrSwitzerlandPage.title) {
  declareYesNo("#benefitsFromEEA","OtherMoneyOtherAreYouReceivingPensionFromAnotherEEA")
  declareYesNo("#workingForEEA","OtherMoneyOtherAreYouPayingInsuranceToAnotherEEA")
}

object G7OtherEEAStateOrSwitzerlandPage {
  val title = "Payments from abroad and working abroad - About you - the carer".toLowerCase

  val url = "/about-you/other-eea-state-or-switzerland"

  def apply(ctx:PageObjectsContext) = new G7OtherEEAStateOrSwitzerlandPage(ctx)

}

trait G7OtherEEAStateOrSwitzerlandPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G7OtherEEAStateOrSwitzerlandPage (PageObjectsContext(browser))
}