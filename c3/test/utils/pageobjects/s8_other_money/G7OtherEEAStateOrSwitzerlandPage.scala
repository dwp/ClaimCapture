package utils.pageobjects.s8_other_money

import play.api.test.TestBrowser
import utils.pageobjects.{ClaimScenario, PageContext, Page}

/**
 * PageObject pattern associated to S8 other money EEA pension and insurance.
 * @author Jorge Migueis
 *         Date: 02/08/2013
 */
class G7OtherEEAStateOrSwitzerlandPage (browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G7OtherEEAStateOrSwitzerlandPage.url, G7OtherEEAStateOrSwitzerlandPage.title, previousPage) {
 
  
    declareYesNo("#benefitsFromOtherEEAStateOrSwitzerland","OtherMoneyOtherAreYouReceivingPensionFromAnotherEEA")
    declareYesNo("#workingForOtherEEAStateOrSwitzerland", "OtherMoneyOtherAreYouPayingInsuranceToAnotherEEA")
  
  
}

object G7OtherEEAStateOrSwitzerlandPage {

  val title = "Other EEA State or Switzerland - Other Money"
  val url = "/otherMoney/otherEEAStateOrSwitzerland"

  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G7OtherEEAStateOrSwitzerlandPage(browser, previousPage)

}

trait G7OtherEEAStateOrSwitzerlandPageContext extends PageContext {
  this: {val browser: TestBrowser} =>
  val page = G7OtherEEAStateOrSwitzerlandPage buildPageWith browser
}
