package utils.pageobjects.s9_other_money

import play.api.test.WithBrowser
import utils.pageobjects._

final class G5StatutorySickPayPage(ctx:PageObjectsContext) extends ClaimPage(ctx, G5StatutorySickPayPage.url, G5StatutorySickPayPage.title) {
  declareYesNo("#haveYouHadAnyStatutorySickPay", "OtherMoneyHaveYouSSPSinceClaim")
  declareInput("#howMuch", "OtherMoneySSPHowMuch")
  declareSelect("#howOften_frequency", "OtherMoneySSPHowOften")
  declareInput("#howOften_frequency_other", "OtherMoneySSPHowOftenOther")
  declareInput("#employersName", "OtherMoneySSPEmployerName")
  declareAddress("#employersAddress", "OtherMoneySSPEmployerAddress")
  declareInput("#employersPostcode", "OtherMoneyEmployerPostcode")
}

object G5StatutorySickPayPage {
  val title = "Statutory Sick Pay - About Other Money".toLowerCase

  val url = "/other-money/statutory-sick-pay"

  def apply(ctx:PageObjectsContext) = new G5StatutorySickPayPage(ctx)
}

trait G5StatutorySickPayPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G5StatutorySickPayPage (PageObjectsContext(browser))
}