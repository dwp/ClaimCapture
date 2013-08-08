package utils.pageobjects.s8_self_employment

import play.api.test.TestBrowser
import utils.pageobjects.{PageContext, Page}


final class G4SelfEmploymentPensionsAndExpensesPage (browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G4SelfEmploymentPensionsAndExpensesPage.url, G4SelfEmploymentPensionsAndExpensesPage.title, previousPage) {

    declareYesNo("#doYouPayToPensionScheme_answer", "SelfEmployedDoYouPayTowardsPensionScheme")
    declareInput("#doYouPayToPensionScheme_howMuchDidYouPay", "SelfEmployedHowMuchPayPensionExpenses")
    declareYesNo("#doYouPayToLookAfterYourChildren", "SelfEmployedDoYouPayAnyonetoLookAfterYourChild")
    declareYesNo("#didYouPayToLookAfterThePersonYouCaredFor", "SelfEmployedDoYouPayAnyonetoLookAfterPersonYouCareFor")
}

object G4SelfEmploymentPensionsAndExpensesPage {
  val title = "Self Employment - Pensions and Expenses"
  val url = "/selfEmployment/pensionsAndExpenses"

  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None) = new G4SelfEmploymentPensionsAndExpensesPage(browser, previousPage)
}

trait G4SelfEmploymentPensionsAndExpensesPageContext extends PageContext {
  this: {val browser: TestBrowser} =>
  val page = G4SelfEmploymentPensionsAndExpensesPage buildPageWith browser
}

