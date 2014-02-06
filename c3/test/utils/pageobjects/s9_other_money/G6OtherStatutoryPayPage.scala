package utils.pageobjects.s9_other_money

import play.api.test.WithBrowser
import utils.pageobjects._

final class G6OtherStatutoryPayPage(ctx:PageObjectsContext) extends ClaimPage(ctx, G6OtherStatutoryPayPage.url, G6OtherStatutoryPayPage.title) {
  declareYesNo("#otherPay","OtherMoneyHaveYouSMPSinceClaim")
  declareInput("#howMuch", "OtherMOneySMPHowMuch")
  declareSelect("#howOften_frequency", "OtherMOneySMPHowOften")
  declareInput("#howOften_frequency_other", "OtherMOneySMPHowOftenOther")
  declareInput("#employersName", "OtherMoneySMPEmployerName")
  declareAddress("#employersAddress", "OtherMoneySMPEmployerAddress")
  declareInput("#employersPostcode", "OtherMoneySMPEmployerPostcode")
}

object G6OtherStatutoryPayPage {
  val title = "Other Statutory Pay - About Other Money".toLowerCase

  val url = "/other-money/other-statutory-pay"

  def apply(ctx:PageObjectsContext) = new G6OtherStatutoryPayPage(ctx)
}

trait G6OtherStatutoryPayPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G6OtherStatutoryPayPage (PageObjectsContext(browser))
}