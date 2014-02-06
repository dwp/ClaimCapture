package utils.pageobjects.s9_other_money

import play.api.test.WithBrowser
import utils.pageobjects._

final class G1AboutOtherMoneyPage(ctx:PageObjectsContext) extends ClaimPage(ctx, G1AboutOtherMoneyPage.url, G1AboutOtherMoneyPage.title) {
  declareYesNo("#yourBenefits_answer", "OtherMoneyHaveYouClaimedOtherBenefits")
  declareYesNo("#anyPaymentsSinceClaimDate_answer", "OtherMoneyAnyPaymentsSinceClaimDate")
  declareInput("#whoPaysYou", "OtherMoneyWhoPaysYou")
  declareInput("#howMuch", "OtherMoneyHowMuch")
  declareSelect("#howOften_frequency", "OtherMoneyHowOften")
  declareInput("#howOften_frequency_other", "OtherMoneyHowOftenOther")
}

object G1AboutOtherMoneyPage {
  val title = "Details about other money - About Other Money".toLowerCase

  val url  = "/other-money/about-other-money"

  def apply(ctx:PageObjectsContext) = new G1AboutOtherMoneyPage(ctx)
}

trait G1AboutOtherMoneyPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G1AboutOtherMoneyPage (PageObjectsContext(browser))
}