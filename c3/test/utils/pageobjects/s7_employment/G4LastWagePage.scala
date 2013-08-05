package utils.pageobjects.s7_employment

import play.api.test.TestBrowser
import utils.pageobjects.{ClaimScenario, PageContext, Page}

final class G4LastWagePage(browser: TestBrowser, previousPage: Option[Page] = None, iteration:Int) extends Page(browser, G4LastWagePage.url, G4LastWagePage.title, previousPage) {
  override val url = super.getUrl.replace(":jobID",iteration.toString)

    declareDate("#lastPaidDate", "EmploymentWhenWereYouLastPaid_"+iteration)
    declareDateFromTo("#periodCovered","EmploymentWhatPeriodDidThisCoverFrom_"+iteration,"EmploymentWhatPeriodDidThisCoverTo_"+iteration)
    declareInput("#grossPay","EmploymentWhatWasTheGrossPayForTheLastPayPeriod_"+iteration)
    declareInput("#payInclusions","EmploymentWhatWasIncludedInYourLastPay_"+iteration)
    declareYesNo("#sameAmountEachTime","EmploymentDoYouGettheSameAmountEachTime_"+iteration)
  

}

object G4LastWagePage {
  val title = "Last wage - Employment"
  val url  = "/employment/lastWage/:jobID"
  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None, iteration:Int) = new G4LastWagePage(browser,previousPage,iteration)
}

trait G4LastWagePageContext extends PageContext {
  this: {val browser:TestBrowser}  =>
  val page = G4LastWagePage buildPageWith (browser,iteration = 1)
}
