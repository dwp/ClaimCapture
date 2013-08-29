package utils.pageobjects.s7_employment

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.{ClaimPage,Page, PageContext}

final class G4LastWagePage(browser: TestBrowser, previousPage: Option[Page] = None, iteration:Int) extends ClaimPage(browser, G4LastWagePage.url.replace(":jobID",iteration.toString), G4LastWagePage.title, previousPage,iteration) {
  declareDate("#lastPaidDate", "EmploymentWhenWereYouLastPaid_" + iteration)
  declareInput("#grossPay", "EmploymentWhatWasTheGrossPayForTheLastPayPeriod_" + iteration)
  declareInput("#payInclusions", "EmploymentWhatWasIncludedInYourLastPay_" + iteration)
  declareYesNo("#sameAmountEachTime", "EmploymentDoYouGettheSameAmountEachTime_" + iteration)
}

object G4LastWagePage {
  val title = "Your last wage - Employment History".toLowerCase

  val url  = "/employment/last-wage/:jobID"

  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None, iteration:Int) = new G4LastWagePage(browser,previousPage,iteration)
}

trait G4LastWagePageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G4LastWagePage buildPageWith (browser,iteration = 1)
}