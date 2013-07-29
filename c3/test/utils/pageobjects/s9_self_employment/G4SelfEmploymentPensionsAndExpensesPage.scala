package utils.pageobjects.s9_self_employment

import play.api.test.TestBrowser
import utils.pageobjects.{PageContext, ClaimScenario, Page}


final class G4SelfEmploymentPensionsAndExpensesPage (browser: TestBrowser, previousPage: Option[Page] = None) extends Page(browser, G4SelfEmploymentPensionsAndExpensesPage.url, G4SelfEmploymentPensionsAndExpensesPage.title, previousPage) {

  def fillPageWith(theClaim: ClaimScenario) {
    fillYesNo("#doYouPayToPensionScheme", theClaim.SelfEmployedDoYouPayTowardsPensionScheme)
    fillInput("#howMuchDidYouPay", theClaim.SelfEmployedHowMuchPayPensionExpenses)
    fillYesNo("#doYouPayToLookAfterYourChildren", theClaim.SelfEmployedDoYouPayAnyonetoLookAfterYourChild)
    fillYesNo("#isItTheSameExpenseWhileAtWorkForChildren", theClaim.SelfEmployedChildCareIsThistheSameAsDuringYourEmployedWork)
    fillYesNo("#didYouPayToLookAfterThePersonYouCaredFor", theClaim.SelfEmployedDoYouPayAnyonetoLookAfterPersonYouCareFor)
    fillYesNo("#isItTheSameExpenseDuringWorkForThePersonYouCaredFor", theClaim.SelfEmployedCareIsThistheSameAsDuringYourEmployedWork)
  }
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

