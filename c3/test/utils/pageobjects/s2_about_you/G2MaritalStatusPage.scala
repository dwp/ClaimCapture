package utils.pageobjects.s2_about_you

import utils.WithBrowser
import utils.pageobjects._

final class G2MaritalStatusPage(ctx:PageObjectsContext) extends ClaimPage(ctx, G2MaritalStatusPage.url) {
  declareRadioList("#maritalStatus", "AboutYouWhatIsYourMaritalOrCivilPartnershipStatus")
}

object G2MaritalStatusPage {
  val url = "/about-you/marital-status"

  def apply(ctx:PageObjectsContext) = new G2MaritalStatusPage(ctx)
}

/** The context for Specs tests */
trait G2MaritalStatusPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G2MaritalStatusPage (PageObjectsContext(browser))
}