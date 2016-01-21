package utils.pageobjects.third_party

import utils.WithBrowser
import utils.pageobjects._

final class GThirdPartyPage(ctx:PageObjectsContext) extends ClaimPage(ctx, GThirdPartyPage.url) {
  declareRadioList("#thirdParty", "ThirdPartyAreYouApplying")
  declareInput("#thirdParty_nameAndOrganisation", "ThirdPartyNameAndOrganisation")
}

object GThirdPartyPage {
  val url = "/third-party/third-party"

  def apply(ctx:PageObjectsContext) = new GThirdPartyPage(ctx)
}

/** The context for Specs tests */
trait GThirdPartyPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = GThirdPartyPage (PageObjectsContext(browser))
}
