package utils.pageobjects.s8_other_money

import play.api.test.TestBrowser
import utils.pageobjects.{ClaimScenario, PageContext, Page}

final class G1AboutOtherMoneyPage(browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G1AboutOtherMoneyPage.url, G1AboutOtherMoneyPage.title, previousPage) { 
  def fillPageWith(theClaim: ClaimScenario) {
    fillYesNo("#yourBenefits_answer", theClaim.OtherMoneyHaveYouClaimedOtherBenefits)
    fillInput("#yourBenefits_text1",theClaim.OtherMoneyTellUsNamesBenefitsYouReceived)
    fillInput("#yourBenefits_text2", theClaim.OtherMoneyTellUsNamesBenefitsYourPartnerReceived)
  }
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
