package utils.pageobjects.s_eligibility

import utils.WithBrowser
import utils.pageobjects.{PageObjectsContext, ClaimPage, PageContext}

final class GBenefitsPage(ctx:PageObjectsContext) extends ClaimPage(ctx, GBenefitsPage.url) {
  declareRadioList("#benefitsAnswer", "CanYouGetCarersAllowanceWhatBenefitDoesThePersonYouCareForGet")
}

object GBenefitsPage {
  val url = "/allowance/benefits"

  def apply(ctx:PageObjectsContext) = new GBenefitsPage(ctx)
}

trait GBenefitsPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = GBenefitsPage (PageObjectsContext(browser))
}