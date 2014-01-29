package utils.pageobjects.s7_employment

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.{ClaimPage,Page, PageContext}

final class G5LastWagePage(browser: TestBrowser, previousPage: Option[Page] = None, iteration:Int) extends ClaimPage(browser, G5LastWagePage.url.replace(":jobID",iteration.toString), G5LastWagePage.title, previousPage,iteration) {
  declareDate("#lastPaidDate", "EmploymentWhenWereYouLastPaid_" + iteration)
  declareInput("#grossPay", "EmploymentWhatWasTheGrossPayForTheLastPayPeriod_" + iteration)
  declareInput("#payInclusions", "EmploymentWhatWasIncludedInYourLastPay_" + iteration)
  declareYesNo("#sameAmountEachTime", "EmploymentDoYouGettheSameAmountEachTime_" + iteration)
}

object G5LastWagePage {
  val title = "Your last wage - Employment History".toLowerCase

  val url  = "/employment/last-wage/:jobID"

  def apply(browser: TestBrowser, previousPage: Option[Page] = None, iteration:Int) = new G5LastWagePage(browser,previousPage,iteration)
}

trait G5LastWagePageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G5LastWagePage (browser,iteration = 1)
}