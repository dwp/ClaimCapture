package utils.pageobjects.s7_employment

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.{PageContext, Page}

final class G9NecessaryExpensesPage(browser: TestBrowser, previousPage: Option[Page] = None, iteration: Int) extends Page(browser, G9NecessaryExpensesPage.url.replace(":jobID",iteration.toString), G9NecessaryExpensesPage.title, previousPage,iteration) {

    declareInput("#whatAreThose", "EmploymentWhatAreNecessaryJobExpenses_"+iteration)
    declareInput("#howMuchCostEachWeek", "EmploymentWhyYouNeedTheseExpensesToDoYourJob_"+iteration)
    declareInput("#whyDoYouNeedThose", "EmploymentHowMuchDidTheseExpensesCostYouEachWeek_"+iteration)

}

object G9NecessaryExpensesPage {
  val title = "Necessary expenses to do your job - Employment"
  val url  = "/employment/necessaryExpenses/:jobID"
  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None, iteration: Int) = new G9NecessaryExpensesPage(browser,previousPage,iteration)
}

trait G9NecessaryExpensesPageContext extends PageContext {
  this: WithBrowser[_] =>
  val page = G9NecessaryExpensesPage buildPageWith(browser,iteration = 1)
}