package utils.pageobjects.s7_employment

import play.api.test.TestBrowser
import utils.pageobjects.{ClaimScenario, PageContext, Page}

final class G4LastWagePage(browser: TestBrowser, previousPage: Option[Page] = None, iteration:Int) extends Page(browser, G4LastWagePage.url, G4LastWagePage.title, previousPage) {
  override val url = super.getUrl.replace(":jobID",iteration.toString)

  def fillPageWith(theClaim: ClaimScenario) {
    fillDate("#lastPaidDate", theClaim.selectDynamic("EmploymentWhenWereYouLastPaid_"+iteration))
    fillDateFromTo("#periodCovered",theClaim.selectDynamic("EmploymentWhatPeriodDidThisCoverFrom_"+iteration),theClaim.selectDynamic("EmploymentWhatPeriodDidThisCoverTo_"+iteration))
    fillInput("#grossPay",theClaim.selectDynamic("EmploymentWhatWasTheGrossPayForTheLastPayPeriod_"+iteration))
    fillInput("#payInclusions",theClaim.selectDynamic("EmploymentWhatWasIncludedInYourLastPay_"+iteration))
    fillYesNo("#sameAmountEachTime",theClaim.selectDynamic("EmploymentDoYouGettheSameAmountEachTime_"+iteration))
  }

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
