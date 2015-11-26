package utils.pageobjects.s_about_you

import utils.WithBrowser
import utils.pageobjects._

final class GMaritalStatusPage(ctx:PageObjectsContext) extends ClaimPage(ctx, GMaritalStatusPage.url) {
  declareRadioList("#maritalStatus", "AboutYouWhatIsYourMaritalOrCivilPartnershipStatus")
}

object GMaritalStatusPage {
  val url = "/about-you/marital-status"

  def apply(ctx:PageObjectsContext) = new GMaritalStatusPage(ctx)
}

/** The context for Specs tests */
trait GMaritalStatusPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = GMaritalStatusPage (PageObjectsContext(browser))
}
