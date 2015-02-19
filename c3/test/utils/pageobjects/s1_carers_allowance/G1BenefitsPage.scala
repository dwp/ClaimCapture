package utils.pageobjects.s1_carers_allowance

import play.api.test.WithBrowser
import utils.pageobjects.{PageObjectsContext, ClaimPage, PageContext}

final class G1BenefitsPage(ctx:PageObjectsContext) extends ClaimPage(ctx, G1BenefitsPage.url, G1BenefitsPage.title) {
  declareRadioList("#benefitsAnswer", "CanYouGetCarersAllowanceWhatBenefitDoesThePersonYouCareForGet")
}

object G1BenefitsPage {
  val title = "What benefit does the person you care for get? - Can you get Carer's Allowance?".toLowerCase

  val url = "/allowance/benefits"

  def apply(ctx:PageObjectsContext) = new G1BenefitsPage(ctx)
}

trait G1BenefitsPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G1BenefitsPage (PageObjectsContext(browser))
}