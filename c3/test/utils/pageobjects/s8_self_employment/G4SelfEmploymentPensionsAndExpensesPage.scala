package utils.pageobjects.s8_self_employment

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.{ClaimPage,Page, PageContext}

final class G4SelfEmploymentPensionsAndExpensesPage (browser: TestBrowser, previousPage: Option[Page] = None) extends ClaimPage(browser, G4SelfEmploymentPensionsAndExpensesPage.url, G4SelfEmploymentPensionsAndExpensesPage.title, previousPage) {
  declareYesNo("#doYouPayToPensionScheme", "SelfEmployedDoYouPayTowardsPensionScheme")
  declareInput("#howMuchDidYouPay", "SelfEmployedHowMuchYouPayTowardsPensionScheme")
  declareSelect("#howOften_frequency", "SelfEmployedHowoftenYouPayTowardsPensionScheme")
  declareInput("#howOften_frequency_other","SelfEmployedHowOftenOtherYouPayTowardsPensionScheme")
  declareYesNo("#doYouPayToLookAfterYourChildren", "SelfEmployedDoYouPayAnyonetoLookAfterYourChild")
  declareYesNo("#didYouPayToLookAfterThePersonYouCaredFor", "SelfEmployedDoYouPayAnyonetoLookAfterPersonYouCareFor")
}

object G4SelfEmploymentPensionsAndExpensesPage {
  val title = "Pensions and expenses - About self-employment".toLowerCase
  val url = "/self-employment/pensions-and-expenses"

  def apply(browser: TestBrowser, previousPage: Option[Page] = None) = new G4SelfEmploymentPensionsAndExpensesPage(browser, previousPage)
}

trait G4SelfEmploymentPensionsAndExpensesPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G4SelfEmploymentPensionsAndExpensesPage (browser)
}