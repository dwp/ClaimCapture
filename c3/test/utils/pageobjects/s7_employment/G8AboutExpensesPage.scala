package utils.pageobjects.s7_employment

import play.api.test.TestBrowser
import utils.pageobjects.{ClaimScenario, PageContext, Page}

final class G8AboutExpensesPage(browser: TestBrowser, previousPage: Option[Page] = None, iteration: Int) extends Page(browser, G8AboutExpensesPage.url, G8AboutExpensesPage.title, previousPage) {
  override val url = super.getUrl.replace(":jobID",iteration.toString)
  def fillPageWith(theClaim: ClaimScenario) {
    fillYesNo("#payForAnythingNecessary", theClaim.selectDynamic("EmploymentDoYouPayforAnythingNecessaryToDoYourJob_"+iteration))
    fillYesNo("#payAnyoneToLookAfterChildren", theClaim.selectDynamic("EmploymentDoYouPayAnyoneLookAfterYourChild_"+iteration))
    fillYesNo("#payAnyoneToLookAfterPerson", theClaim.selectDynamic("EmploymentDoYouPayAnyonetoLookAfterPersonYouCareFor_"+iteration))
  }
}

object G8AboutExpensesPage {
  val title = "About expenses to do with your employment - Employment"
  val url  = "/employment/aboutExpenses/:jobID"
  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None, iteration: Int) = new G8AboutExpensesPage(browser,previousPage,iteration)
}

trait G8AboutExpensesPageContext extends PageContext {
  this: {val browser:TestBrowser}  =>
  val page = G8AboutExpensesPage buildPageWith(browser,iteration = 1)
}
