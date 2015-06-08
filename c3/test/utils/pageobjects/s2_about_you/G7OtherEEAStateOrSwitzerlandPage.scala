package utils.pageobjects.s2_about_you

import utils.WithBrowser
import utils.pageobjects._

/**
 * PageObject pattern associated to S7 about you EEA pension and insurance.
 */
final class G7OtherEEAStateOrSwitzerlandPage (ctx:PageObjectsContext) extends ClaimPage(ctx, G7OtherEEAStateOrSwitzerlandPage.url) {
  declareYesNo("#benefitsFromEEA","OtherMoneyOtherAreYouReceivingPensionFromAnotherEEA")
  declareInput("#benefitsFromEEADetails", "OtherMoneyOtherAreYouReceivingPensionFromAnotherEEADetails")
  declareYesNo("#workingForEEA","OtherMoneyOtherAreYouPayingInsuranceToAnotherEEA")
  declareInput("#workingForEEADetails", "OtherMoneyOtherAreYouPayingInsuranceToAnotherEEADetails")
}

object G7OtherEEAStateOrSwitzerlandPage {
  val url = "/about-you/other-eea-state-or-switzerland"

  def apply(ctx:PageObjectsContext) = new G7OtherEEAStateOrSwitzerlandPage(ctx)

}

trait G7OtherEEAStateOrSwitzerlandPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G7OtherEEAStateOrSwitzerlandPage (PageObjectsContext(browser))
}