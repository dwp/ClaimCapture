package utils.pageobjects.s8_other_money

import play.api.test.TestBrowser
import utils.pageobjects.{ClaimScenario, PageContext, Page}

final class G1AboutOtherMoneyPage(browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G1AboutOtherMoneyPage.url, G1AboutOtherMoneyPage.title, previousPage) {

    declareYesNo("#yourBenefits_answer", "OtherMoneyHaveYouClaimedOtherBenefits")
    declareInput("#yourBenefits_text1","OtherMoneyTellUsNamesBenefitsYouReceived")
    declareInput("#yourBenefits_text2", "OtherMoneyTellUsNamesBenefitsYourPartnerReceived")

}

object G1AboutOtherMoneyPage {
  val title = "About Other Money - Other Money"
  val url  = "/otherMoney/aboutOtherMoney"
  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G1AboutOtherMoneyPage(browser,previousPage)
}

trait G1AboutOtherMoneyPageContext extends PageContext {
  this: {val browser:TestBrowser}  =>
  val page = G1AboutOtherMoneyPage buildPageWith browser
}
