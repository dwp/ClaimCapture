package utils.pageobjects.s_about_you

import utils.WithBrowser
import utils.pageobjects._

/**
 * PageObject pattern associated to S7 about you EEA pension and insurance.
 */
final class GOtherEEAStateOrSwitzerlandPage (ctx:PageObjectsContext) extends ClaimPage(ctx, GOtherEEAStateOrSwitzerlandPage.url) {
  declareYesNo("#eeaGuardQuestion_answer","OtherMoneyOtherEEAGuardQuestion")
  declareYesNo("#eeaGuardQuestion_benefitsFromEEADetails_answer","OtherMoneyOtherAreYouReceivingPensionFromAnotherEEA")
  declareInput("#eeaGuardQuestion_benefitsFromEEADetails_field", "OtherMoneyOtherAreYouReceivingPensionFromAnotherEEADetails")
  declareYesNo("#eeaGuardQuestion_workingForEEADetails_answer","OtherMoneyOtherAreYouPayingInsuranceToAnotherEEA")
  declareInput("#eeaGuardQuestion_workingForEEADetails_field", "OtherMoneyOtherAreYouPayingInsuranceToAnotherEEADetails")
}

object GOtherEEAStateOrSwitzerlandPage {
  val url = "/nationality/payments-from-abroad"

  def apply(ctx:PageObjectsContext) = new GOtherEEAStateOrSwitzerlandPage(ctx)

}

trait GOtherEEAStateOrSwitzerlandPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = GOtherEEAStateOrSwitzerlandPage (PageObjectsContext(browser))
}
