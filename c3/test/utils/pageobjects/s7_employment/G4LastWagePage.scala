package utils.pageobjects.s7_employment

import play.api.test.WithBrowser
import utils.pageobjects._

final class G4LastWagePage(ctx:PageObjectsContext, iteration:Int) extends ClaimPage(ctx, G4LastWagePage.url.replace(":jobID",iteration.toString), G4LastWagePage.title,iteration) {
  declareDate("#lastPaidDate", "EmploymentWhenWereYouLastPaid_" + iteration)
  declareInput("#grossPay", "EmploymentWhatWasTheGrossPayForTheLastPayPeriod_" + iteration)
  declareInput("#payInclusions", "EmploymentWhatWasIncludedInYourLastPay_" + iteration)
  declareYesNo("#sameAmountEachTime", "EmploymentDoYouGettheSameAmountEachTime_" + iteration)
}

object G4LastWagePage {
  val title = "Your last wage - Employment History".toLowerCase

  val url  = "/employment/last-wage/:jobID"

  def apply(ctx:PageObjectsContext, iteration:Int) = new G4LastWagePage(ctx,iteration)
}

trait G4LastWagePageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G4LastWagePage (PageObjectsContext(browser),iteration = 1)
}