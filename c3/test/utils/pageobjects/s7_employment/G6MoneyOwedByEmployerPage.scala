package utils.pageobjects.s7_employment

import play.api.test.TestBrowser
import utils.pageobjects.{ClaimScenario, PageContext, Page}

final class G6MoneyOwedByEmployerPage(browser: TestBrowser, previousPage: Option[Page] = None, iteration: Int) extends Page(browser, G6MoneyOwedByEmployerPage.url, G6MoneyOwedByEmployerPage.title, previousPage) {
  override val url = super.getUrl.replace(":jobID",iteration.toString)
  def fillPageWith(theClaim: ClaimScenario) {

    fillInput("#howMuchOwed",theClaim.selectDynamic("EmploymentWhatPeriodIsItForFrom_"+iteration))
    fillDateFromTo("#owedPeriod",theClaim.selectDynamic("EmploymentWhatPeriodIsItForFrom_"+iteration),theClaim.selectDynamic("EmploymentWhatPeriodIsItForTo_"+iteration))
    fillInput("#owedFor",theClaim.selectDynamic("EmploymentWhatIsTheMoneyOwedFor_"+iteration))
    fillDate("#shouldBeenPaidBy",theClaim.selectDynamic("EmploymentWhenShouldTheMoneyOwedHaveBeenPaid_"+iteration))
    fillInput("#whenWillGetIt",theClaim.selectDynamic("EmploymentWhenWillYouGetMoneyOwed_"+iteration))

  }
}

object G6MoneyOwedByEmployerPage {
  val title = "Money owed to you by your employer"
  val url  = "/employment/moneyOwedByEmployer/:jobID"
  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None, iteration: Int) = new G6MoneyOwedByEmployerPage(browser,previousPage,iteration)
}

trait G6MoneyOwedByEmployerPageContext extends PageContext {
  this: {val browser:TestBrowser}  =>
  val page = G6MoneyOwedByEmployerPage buildPageWith(browser,iteration = 1)
}
