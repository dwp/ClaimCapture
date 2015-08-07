package utils.pageobjects.s_about_you

import utils.WithBrowser
import utils.pageobjects._

/**
 * PageObject pattern associated to S7 about you EEA pension and insurance.
 */
final class GOtherEEAStateOrSwitzerlandPage (ctx:PageObjectsContext) extends ClaimPage(ctx, GOtherEEAStateOrSwitzerlandPage.url) {
  declareYesNo("#benefitsFromEEA","OtherMoneyOtherAreYouReceivingPensionFromAnotherEEA")
  declareInput("#benefitsFromEEADetails", "OtherMoneyOtherAreYouReceivingPensionFromAnotherEEADetails")
  declareYesNo("#workingForEEA","OtherMoneyOtherAreYouPayingInsuranceToAnotherEEA")
  declareInput("#workingForEEADetails", "OtherMoneyOtherAreYouPayingInsuranceToAnotherEEADetails")
}

object GOtherEEAStateOrSwitzerlandPage {
  val url = "/about-you/other-eea-state-or-switzerland"

  def apply(ctx:PageObjectsContext) = new GOtherEEAStateOrSwitzerlandPage(ctx)

}

trait GOtherEEAStateOrSwitzerlandPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = GOtherEEAStateOrSwitzerlandPage (PageObjectsContext(browser))
}