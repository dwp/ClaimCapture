package utils.pageobjects.s7_employment

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.{PageContext, Page}

final class G6MoneyOwedByEmployerPage(browser: TestBrowser, previousPage: Option[Page] = None, iteration: Int) extends Page(browser, G6MoneyOwedByEmployerPage.url.replace(":jobID", iteration.toString), G6MoneyOwedByEmployerPage.title, previousPage, iteration) {
  declareInput("#howMuchOwed", "EmploymentWhatPeriodIsItForFrom_" + iteration)
  declareDateFromTo("#owedPeriod", "EmploymentWhatPeriodIsItForFrom_" + iteration, "EmploymentWhatPeriodIsItForTo_" + iteration)
  declareInput("#owedFor", "EmploymentWhatIsTheMoneyOwedFor_" + iteration)
  declareDate("#shouldBeenPaidBy", "EmploymentWhenShouldTheMoneyOwedHaveBeenPaid_" + iteration)
  declareInput("#whenWillGetIt", "EmploymentWhenWillYouGetMoneyOwed_" + iteration)
}

object G6MoneyOwedByEmployerPage {
  val title = "Money owed to you by your employer - Employment History".toLowerCase

  val url  = "/employment/money-owed-by-employer/:jobID"

  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None, iteration: Int) = new G6MoneyOwedByEmployerPage(browser,previousPage,iteration)
}

trait G6MoneyOwedByEmployerPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G6MoneyOwedByEmployerPage buildPageWith(browser,iteration = 1)
}