package utils.pageobjects.s7_employment

import play.api.test.TestBrowser
import utils.pageobjects.{ClaimScenario, PageContext, Page}

final class G9NecessaryExpensesPage(browser: TestBrowser, previousPage: Option[Page] = None, iteration: Int) extends Page(browser, G9NecessaryExpensesPage.url, G9NecessaryExpensesPage.title, previousPage) {
  override val url = super.getUrl.replace(":jobID",iteration.toString)
  def fillPageWith(theClaim: ClaimScenario) {
    fillInput("#whatAreThose", theClaim.selectDynamic("EmploymentWhatAreNecessaryJobExpenses_"+iteration))
    fillInput("#howMuchCostEachWeek", theClaim.selectDynamic("EmploymentWhyYouNeedTheseExpensesToDoYourJob_"+iteration))
    fillInput("#whyDoYouNeedThose", theClaim.selectDynamic("EmploymentHowMuchDidTheseExpensesCostYouEachWeek_"+iteration))

  }
}

object G9NecessaryExpensesPage {
  val title = "Necessary expenses to do your job - Employment"
  val url  = "/employment/necessaryExpenses/:jobID"
  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None, iteration: Int) = new G9NecessaryExpensesPage(browser,previousPage,iteration)
}

trait G9NecessaryExpensesPageContext extends PageContext {
  this: {val browser:TestBrowser}  =>
  val page = G9NecessaryExpensesPage buildPageWith(browser,iteration = 1)
}
