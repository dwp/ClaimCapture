package utils.pageobjects.s9_other_money

import play.api.test.WithBrowser
import utils.pageobjects._

final class G1AboutOtherMoneyPage(ctx:PageObjectsContext) extends ClaimPage(ctx, G1AboutOtherMoneyPage.url) {
  declareYesNo("#anyPaymentsSinceClaimDate_answer", "OtherMoneyAnyPaymentsSinceClaimDate")
  declareInput("#whoPaysYou", "OtherMoneyWhoPaysYou")
  declareInput("#howMuch", "OtherMoneyHowMuch")
  declareSelect("#howOften_frequency", "OtherMoneyHowOften")
  declareInput("#howOften_frequency_other", "OtherMoneyHowOftenOther")
  declareYesNo("#statutorySickPay_answer", "OtherMoneyHaveYouSSPSinceClaim")
  declareInput("#statutorySickPay_howMuch", "OtherMoneySSPHowMuch")
  declareSelect("#statutorySickPay_howOften_frequency", "OtherMoneySSPHowOften")
  declareInput("#statutorySickPay_howOften_frequency_other", "OtherMoneySSPHowOftenOther")
  declareInput("#statutorySickPay_employersName", "OtherMoneySSPEmployerName")
  declareAddress("#statutorySickPay_employersAddress", "OtherMoneySSPEmployerAddress")
  declareInput("#statutorySickPay_employersPostcode", "OtherMoneyEmployerPostcode")
  declareYesNo("#otherStatutoryPay_answer","OtherMoneyHaveYouSMPSinceClaim")
  declareInput("#otherStatutoryPay_howMuch", "OtherMOneySMPHowMuch")
  declareSelect("#otherStatutoryPay_howOften_frequency", "OtherMOneySMPHowOften")
  declareInput("#otherStatutoryPay_howOften_frequency_other", "OtherMOneySMPHowOftenOther")
  declareInput("#otherStatutoryPay_employersName", "OtherMoneySMPEmployerName")
  declareAddress("#otherStatutoryPay_employersAddress", "OtherMoneySMPEmployerAddress")
  declareInput("#otherStatutoryPay_employersPostcode", "OtherMoneySMPEmployerPostcode")
}

object G1AboutOtherMoneyPage {
  val url  = "/other-money/about-other-money"

  def apply(ctx:PageObjectsContext) = new G1AboutOtherMoneyPage(ctx)
}

trait G1AboutOtherMoneyPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G1AboutOtherMoneyPage (PageObjectsContext(browser))
}